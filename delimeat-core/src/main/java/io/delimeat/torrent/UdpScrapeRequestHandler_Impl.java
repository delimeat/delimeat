package io.delimeat.torrent;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.net.URI;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.UnresolvedAddressException;
import java.time.Instant;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.delimeat.torrent.entity.InfoHash;
import io.delimeat.torrent.entity.ScrapeResult;
import io.delimeat.torrent.entity.UdpAction;
import io.delimeat.torrent.entity.UdpConnectRequest;
import io.delimeat.torrent.entity.UdpConnectResponse;
import io.delimeat.torrent.entity.UdpConnectionId;
import io.delimeat.torrent.entity.UdpErrorResponse;
import io.delimeat.torrent.entity.UdpRequest;
import io.delimeat.torrent.entity.UdpResponse;
import io.delimeat.torrent.entity.UdpScrapeRequest;
import io.delimeat.torrent.entity.UdpScrapeResponse;
import io.delimeat.torrent.entity.UdpTransaction;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.UdpErrorResponseException;
import io.delimeat.torrent.exception.UdpTimeoutException;
import io.delimeat.torrent.exception.UdpTorrentException;
import io.delimeat.torrent.exception.UnhandledScrapeException;

public class UdpScrapeRequestHandler_Impl implements ScrapeRequestHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UdpScrapeRequestHandler_Impl.class);
	
	private static final Random RANDOM_GEN = new Random();
	
	private final Map<Integer, UdpTransaction> queue = new ConcurrentHashMap<>();
	private final Queue<UdpTransaction> sendPipeline = new ConcurrentLinkedQueue<>();
	
	private final Map<InetSocketAddress,UdpConnectionId> connections = new ConcurrentHashMap<>(); 
	
	private AtomicBoolean sendActive = new AtomicBoolean(false);
	private AtomicBoolean receiveActive = new AtomicBoolean(false);
	private AtomicBoolean active = new AtomicBoolean(false);
	private Instant lastTransaction = Instant.EPOCH;
	
	private DatagramChannel channel;
	private Selector selector;
	private ScheduledExecutorService executor;
	private InetSocketAddress address;

	/**
	 * @return the channel
	 */
	public DatagramChannel getChannel() {
		return channel;
	}

	/**
	 * @return the selector
	 */
	public Selector getSelector() {
		return selector;
	}

	/**
	 * @return the executor
	 */
	public ExecutorService getExecutor() {
		return executor;
	}
	
	/**
	 * @return the address
	 */
	public InetSocketAddress getAddress() {
		return address;
	}

	/**
	 * @return the connections
	 */
	public Map<InetSocketAddress, UdpConnectionId> getConnections() {
		return connections;
	}

	/**
	 * @return the lastTransaction
	 */
	public Instant getLastTransaction() {
		return lastTransaction;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(InetSocketAddress address) {
		this.address = address;
	}

	public void initialize() throws IOException{
		LOGGER.trace("Start initialize");
		
		if(active.compareAndSet(false, true) == false){
			LOGGER.trace("Already initialized");
			return;
		}
		
		selector = Selector.open();
		
		channel = DatagramChannel.open();
		LOGGER.trace("Binding to address: {}", address);
		channel.bind(address);
		channel.configureBlocking(false);
		channel.setOption(StandardSocketOptions.SO_SNDBUF, 2*1024);
		channel.setOption(StandardSocketOptions.SO_RCVBUF, 2*1024);
		channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		channel.register(selector, SelectionKey.OP_READ);
		
		executor = Executors.newScheduledThreadPool(4);		
		executor.execute(this::doSelect);
		executor.scheduleWithFixedDelay(this::purgeInvalidConnectionIds, 60*1000, 60*1000, TimeUnit.MILLISECONDS);
		executor.scheduleWithFixedDelay(this::shutdownDueToInactivity,  5*60*1000, 5*60*1000, TimeUnit.MILLISECONDS);
		
		LOGGER.trace("End initialize");

	}
	
	public void shutdown() throws IOException, InterruptedException{
		LOGGER.trace("Start shutdown");
		if(active.compareAndSet(true, false) == false){
			LOGGER.trace("Already shutdown");
			return;
		}
		
		selector.close();
		channel.close();
		executor.shutdown();
		executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
		LOGGER.trace("End shutdown");
	}
	
	public boolean isActive(){
		return active.get();
	}
	
	public void shutdownDueToInactivity(){
		LOGGER.trace("Start shutdown due to inactivity");
		if(lastTransaction.isBefore(Instant.now().minusMillis(5*60*1000))){
			LOGGER.trace("Going to shutdown");
			try{
				shutdown();
			}catch(IOException | InterruptedException ex){
				LOGGER.warn("Encountered an error shuting down", ex);
			}
		}
		LOGGER.trace("End shutdown due to inactivity");
	}
	
	public void purgeInvalidConnectionIds(){
		LOGGER.trace("Start purge expired connection ids");
		Instant now = Instant.now();
		Iterator<UdpConnectionId> iterator = connections.values().iterator();
		while(iterator.hasNext()){
			UdpConnectionId connectionId = iterator.next();
			if(connectionId.getExpiry().isBefore(now)){
				LOGGER.trace("Expired connection id {} for {}", connectionId.getValue(), connectionId.getFromAddress());
				iterator.remove();
			}	
		}
		LOGGER.trace("End purge expired connection ids");
	}
	
	public void doSelect(){
		LOGGER.trace("Select thread started");
		while (active.get() && Thread.currentThread().isInterrupted() == false) {
			
			try{
				selector.select();
				LOGGER.trace("Selected");
			}catch(IOException ex){
				LOGGER.error("Selector encountered an error",ex);
			}
			
			//allows for closing gracefully
			if(active.get() == false || selector.isOpen() == false){
				LOGGER.trace("Breaking select active: {} open: {}", active, selector.isOpen());
				break;
			}
			
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {
 
                SelectionKey key = iter.next();
                iter.remove();
                
                if (key.isValid() && key.isReadable()) {	
                	LOGGER.trace("Got a valid readable key");
                	doReceive();
                }
            }
        }
		LOGGER.trace("Select thread ended with active {} isInterupted {}", active, Thread.currentThread().isInterrupted());
	}
	
	public void doSend(){
		LOGGER.trace("Starting sending");
		if(sendActive.compareAndSet(false, true) == false){
			LOGGER.warn("Send processing is already underway, exiting");
			return;
		}
		
		lastTransaction = Instant.now();
		UdpTransaction transaction = null;
		while((transaction = sendPipeline.poll()) != null){
			LOGGER.trace("Sending request for {}", transaction);
			try{
				channel.send(transaction.getRequest().toByteBuffer(), transaction.getToAddress());
			}catch(IOException | UnresolvedAddressException e){
				LOGGER.error("Encountered an error sending", e);
				transaction.setException(e);
				continue;
			}catch(Throwable e){
				LOGGER.error("Encountered an error sending", e);
				transaction.setException(new UdpTorrentException(e));
				continue;
			}
			queue.put(transaction.getRequest().getTransactionId(), transaction);
			LOGGER.trace("Sent request for {}", transaction);
			
		}
		sendActive.compareAndSet(true, false);
		LOGGER.trace("Finished sending");
	}
	
	public void doReceive(){
		LOGGER.trace("Processing reads");
		if(receiveActive.compareAndSet(false, true) == false){
			LOGGER.warn("Receiving processing is already underway, exiting");
			return;
		}
		

		lastTransaction = Instant.now();
		ByteBuffer readBuffer = ByteBuffer.allocate(1024);
		while(true && Thread.currentThread().isInterrupted() == false){
			try{
				readBuffer.clear();
				
				InetSocketAddress fromAddress = (InetSocketAddress) channel.receive(readBuffer);
				
				// if there is no fromAddress there are no responses to read
				if(fromAddress == null){
					break;
				}
				
				LOGGER.trace("Received data from {}", fromAddress);
				
				// if the response is less than 8 bytes its invalid and port zero is reserved
				if(readBuffer.position() < 8 || fromAddress.getPort() == 0){
					LOGGER.warn("Invalid response received from {}", fromAddress);
					continue;
				}
				
				// copy the buffer because its processed in a separate thread
				readBuffer.flip();		
				ByteBuffer messageBuffer = ByteBuffer.allocate(readBuffer.limit()).put(readBuffer);
				messageBuffer.flip();
				
				//process the response on a separate thread
				executor.execute(()->{
						processResponse(messageBuffer,fromAddress);
					});
			
			}catch(IOException ex){
				LOGGER.error("Encountered an error receiving", ex);
				continue;
			}
		}
		receiveActive.compareAndSet(true, false);
	}
	
	public void processResponse(ByteBuffer buffer, InetSocketAddress fromAddress){
		LOGGER.trace("Processing response from {}", fromAddress);
		int actionVal = buffer.getInt();
		UdpAction action = UdpAction.get(actionVal);
		
		UdpResponse response;
		try{
			switch(action){
			case CONNECT:
				LOGGER.trace("Unmarshalling a connect response");
				response = unmarshallConnectResponse(buffer);
				break;	
			case SCRAPE:
				LOGGER.trace("Unmarshalling a scrape response");
				response = unmarshallScrapeResponse(buffer);
				break;
			case ERROR:
				LOGGER.trace("Unmarshalling an error response");
				response = unmarshallErrorResponse(buffer);
				break;
			default:
				LOGGER.warn("Received unsupported udp action {} from {}", actionVal, fromAddress);
				return;
			}
		}catch(BufferUnderflowException ex){
			LOGGER.error(String.format("Unable to unmarshall response from %s", fromAddress), ex);
			return;
		}
				
		Integer transactionId = response.getTransactionId();
		UdpTransaction transaction = queue.remove(transactionId);
		if(transaction == null){
			LOGGER.warn("Received unsolicited response {} from {}", response, fromAddress);
			return;
		}else if(fromAddress.equals(transaction.getToAddress()) == false){
			LOGGER.warn("TransactionId {} received from different address it was sent to\n{}\n{}",response.getTransactionId(), transaction, response);
			return;			
		}else if(response.getAction() == UdpAction.ERROR){
			transaction.setException(new UdpErrorResponseException((UdpErrorResponse)response));
			LOGGER.trace("Successfully processed error response {}", transaction);
			return;
		} else if(transaction.getRequest().getAction() != response.getAction()){
			LOGGER.error("Unexpected response for request\n{}\n{}", transaction, response);
			return;	
		}
		transaction.setResponse(response);
		LOGGER.trace("Successfully processed {} ", transaction);
	}
	
	public UdpResponse enqueueRequest(UdpRequest request, InetSocketAddress address) throws Exception{
		UdpTransaction txn = new UdpTransaction(request, address);
		int count = 0;
		
		do{
			LOGGER.trace("Adding {} to send queue, attempt {}", txn, count+1);
			sendPipeline.add(txn);
			executor.execute(this::doSend);
			try{
				//txn.awaitResponse((15*2^count)*1000);
				txn.awaitResponse(1000*(count+1));
				break;
			}catch(UdpTimeoutException ex){
				LOGGER.trace("Transaction {} timed out, attempt {}", txn, count+1);
				if(count >= 2) {
					throw ex;
				}
			}
			count++;
		}while(true);
		
		return txn.getResponse();
	}
	
	public UdpConnectionId requestConnection(InetSocketAddress toAddress) throws Exception {
		LOGGER.trace("Received request for connectionId for {}", toAddress);

		UdpConnectionId connectionId = connections.get(toAddress);
		if(connectionId == null || connectionId.getExpiry().isBefore(Instant.now())){
			LOGGER.trace("Requesting new connectionId for {}", toAddress);
			
			// remove expired connectionId if it exists
			connections.remove(toAddress);
			
			// put the send request in another thread
			UdpConnectRequest request = new UdpConnectRequest(generateTransactionId());
			
			// wait for response
			UdpConnectResponse response;
			try{
				response = (UdpConnectResponse)enqueueRequest(request, toAddress);
			}catch(Exception ex){
				LOGGER.error("Received an error fetching the connection id", ex);
				throw ex;
			}
			
			// assume txn will throw exception if no response
			connectionId = new UdpConnectionId(response.getConnectionId(), toAddress, Instant.now().plusSeconds(60));
			connections.put(toAddress, connectionId);
			LOGGER.trace("Receieve new connection id {} for {}", connectionId, toAddress);

		}
		LOGGER.trace("Returning valid connectionId {}", connectionId);
		return connectionId;

	}
	
	public Integer generateTransactionId(){
		Integer transactionId;
		
		do{
			transactionId = RANDOM_GEN.nextInt();
		}while(queue.containsKey(transactionId) == true);
		
		return transactionId;
	}
	
	public UdpConnectResponse unmarshallConnectResponse(ByteBuffer buffer) throws BufferUnderflowException{
		return new UdpConnectResponse(buffer.getInt(), buffer.getLong());
	}
	
	public UdpScrapeResponse unmarshallScrapeResponse(ByteBuffer buffer) throws BufferUnderflowException{
		return new UdpScrapeResponse(buffer.getInt(), buffer.getInt(), buffer.getInt());

	}
	
	public UdpErrorResponse unmarshallErrorResponse(ByteBuffer buffer) throws BufferUnderflowException{
		int transactionId = buffer.getInt();
		byte[] msgBytes = new byte[buffer.remaining()];
		buffer.get(msgBytes);
		String message = new String(msgBytes).trim();
		return new UdpErrorResponse(transactionId, message);
	}

	@Override
	public ScrapeResult scrape(URI uri, InfoHash infoHash)
			throws IOException, UnhandledScrapeException, TorrentException {
		LOGGER.trace("Received request for scrape of {} from {}", infoHash, uri);

		initialize();
		
		String host = uri.getHost();
		int port = uri.getPort() != -1 ? uri.getPort() : 6969; // if no port is provided use default of 6969
		InetSocketAddress address = new InetSocketAddress(host,port);
		
		UdpConnectionId connId;
		try{
			connId = requestConnection(address);
		}catch(Exception ex){
			throw new TorrentException(ex);
		}
		UdpScrapeRequest request = new UdpScrapeRequest(connId.getValue(), generateTransactionId(), infoHash);

		UdpScrapeResponse response;
		try{
			response = (UdpScrapeResponse)enqueueRequest(request, address);
		}catch(Exception ex){
			LOGGER.error("Received an error scraping", ex);
			throw new TorrentException(ex);
		}
		
		
		ScrapeResult result = new ScrapeResult(response.getSeeders(), response.getLeechers());
		LOGGER.trace("Returning scrape {} for {} from {}", result, infoHash, address);
		return result;
	}

}
