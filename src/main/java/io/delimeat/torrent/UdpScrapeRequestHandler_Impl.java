package io.delimeat.torrent;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.time.Instant;
import java.util.Arrays;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.UnhandledScrapeException;
import io.delimeat.torrent.udp.UdpUtils;
import io.delimeat.torrent.udp.domain.ConnectUdpRequest;
import io.delimeat.torrent.udp.domain.ConnectUdpResponse;
import io.delimeat.torrent.udp.domain.ConnectionId;
import io.delimeat.torrent.udp.domain.ErrorUdpResponse;
import io.delimeat.torrent.udp.domain.ScrapeUdpRequest;
import io.delimeat.torrent.udp.domain.ScrapeUdpResponse;
import io.delimeat.torrent.udp.domain.UdpRequest;
import io.delimeat.torrent.udp.domain.UdpResponse;
import io.delimeat.torrent.udp.domain.UdpTransaction;
import io.delimeat.torrent.udp.exception.UdpErrorResponseException;
import io.delimeat.torrent.udp.exception.UdpException;

public class UdpScrapeRequestHandler_Impl extends AbstractScrapeRequestHandler implements ScrapeRequestHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UdpScrapeRequestHandler_Impl.class);

	private static final Random RANDOM_GEN = new Random();
	
	private final Map<Integer, UdpTransaction> queue = new ConcurrentHashMap<>();
	private final Queue<UdpTransaction> sendPipeline = new ConcurrentLinkedQueue<>();
	
	private final Map<InetSocketAddress,ConnectionId> connections = new ConcurrentHashMap<>(); 
	
	private boolean sendActive = false;
	private boolean receiveActive = false;
	private boolean active = false;
	private Instant lastTransaction = Instant.EPOCH;
	
	private DatagramChannel channel;
	private Selector selector;
	private ScheduledExecutorService executor;
	
	@Autowired
	@Qualifier("io.delimeat.torrent.udpAddress")
	private InetSocketAddress address;
	
	public UdpScrapeRequestHandler_Impl(){
		super(Arrays.asList("UDP"));	
	}
	
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
	public Map<InetSocketAddress, ConnectionId> getConnections() {
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
		if(active == false){
			selector = Selector.open();
			
			channel = DatagramChannel.open();
			channel.bind(address);
			channel.configureBlocking(false);
			channel.setOption(StandardSocketOptions.SO_SNDBUF, 2*1024);
			channel.setOption(StandardSocketOptions.SO_RCVBUF, 2*1024);
			channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			
			executor = Executors.newScheduledThreadPool(2);
			executor.execute(this::select);
			executor.scheduleWithFixedDelay(this::purgeInvalidConnectionIds, 5*60*1000, 5*60*1000, TimeUnit.MILLISECONDS);
			executor.scheduleWithFixedDelay(this::shutdownDueToInactivity,  5*60*1000, 5*60*1000, TimeUnit.MILLISECONDS);
			active = true;
		}

	}
	
	public void shutdown() throws IOException, InterruptedException{
		if(active == true){
			active = false;
			selector.close();
			channel.close();
			executor.shutdown();
			executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
		}
	}
	
	public boolean isActive(){
		return active;
	}
	
	public void shutdownDueToInactivity(){
		if(lastTransaction.isBefore(Instant.now().minusMillis(5*60*1000))){
			try{
				shutdown();
			}catch(IOException | InterruptedException ex){
				LOGGER.warn("Encountered an error shuting down", ex);
			}
		}
	}
	
	public void purgeInvalidConnectionIds(){
		Instant now = Instant.now();
		for(InetSocketAddress address: connections.keySet()){
			ConnectionId connectionId = connections.get(address);
			if(connectionId.getExpiry().isBefore(now)){
				connections.remove(address);
			}
		}
	}
	
	public void select(){
		while (active && Thread.currentThread().isInterrupted() == false) {
			try{
				selector.select();
			}catch(IOException ex){
				LOGGER.error("Selector encountered an error",ex);
			}
			
			//allows for closing gracefully
			if(active == false || selector.isOpen() == false){
				break;
			}
			
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {
 
                SelectionKey key = iter.next();
                iter.remove();
                
                if (key.isValid() && key.isReadable()) {	
                	receive();
                }
            }
        }	
	}
	
	public void send(){
		LOGGER.trace("Starting sending");
		if(sendActive == true){
			LOGGER.warn("Send processing is already underway, exiting");
			return;
		}
		
		sendActive = true;
		UdpTransaction transaction = null;
		while((transaction = sendPipeline.poll()) != null){
			LOGGER.trace("Sending request for {}", transaction);
			try{
				channel.send(transaction.getRequest().toByteBuffer(), transaction.getToAddress());
			}catch(IOException e){
				LOGGER.error("Encountered an error sending", e);
				transaction.setException(e);
				continue;
			}
			queue.put(transaction.getRequest().getTransactionId(), transaction);
			LOGGER.trace("Sent request for {}", transaction);
		}
		sendActive = false;
		LOGGER.trace("Finished sending");
	}
	
	public void receive(){
		LOGGER.trace("Processing reads");
		if(receiveActive == true){
			LOGGER.warn("Receiving processing is already underway, exiting");
			return;
		}
		
		receiveActive = true;
		ByteBuffer readBuffer = ByteBuffer.allocate(1024);
		while(true){
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
				executor.execute(()->{processResponse(messageBuffer,fromAddress);});
			
			}catch(IOException ex){
				LOGGER.error("Encountered an error receiving", ex);
				continue;
			}
		}
		receiveActive = false;
	}
	
	public void processResponse(ByteBuffer buffer, InetSocketAddress fromAddress){
		LOGGER.trace("Processing response from", fromAddress);
		int action = buffer.getInt();	
		UdpResponse response;
		try{
			switch(action){
			case UdpUtils.CONNECT_ACTION:
				LOGGER.trace("Unmarshalling a connect response");
				response = UdpUtils.unmarshallConnectResponse(buffer);
				break;
			
			case UdpUtils.SCRAPE_ACTION:
				LOGGER.trace("Unmarshalling a scrape response");
				response = UdpUtils.unmarshallScrapeResponse(buffer);
				break;
			case UdpUtils.ERROR_ACTION:
				LOGGER.trace("Unmarshalling an error response");
				response = UdpUtils.unmarshallErrorResponse(buffer);
				break;
			default:
				LOGGER.warn("Received unsupported udp action {} from {}", action, fromAddress);
				return;
			}
		}catch(UdpException ex){
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
		}else if(action == UdpUtils.ERROR_ACTION){
			transaction.setException(new UdpErrorResponseException((ErrorUdpResponse)response));
			return;
		} else if(transaction.getRequest().getAction() != response.getAction()){
			LOGGER.error("Unexpected response for request\n{}\n{}", transaction, response);
			//TODO new exception type
			transaction.setException(new Exception(String.format("Received unexpected response type\n%s\n%s", transaction, response)));
			return;	
		}
		transaction.setResponse(response);
		LOGGER.trace("Successfully processed {} ", transaction);
	}
	
	public UdpResponse enqueue(UdpRequest request, InetSocketAddress address) throws Exception{
		UdpTransaction txn = new UdpTransaction(request, address);
		LOGGER.trace("Adding {} to send queue", txn);
		sendPipeline.add(txn);
		executor.execute(this::send);	
		return txn.getResponse(3000);
	}
	
	public ConnectionId connect(InetSocketAddress toAddress) throws Exception {
		LOGGER.trace("Received request for connectionId for {}", toAddress);

		ConnectionId connectionId = connections.get(toAddress);
		if(connectionId == null || connectionId.getExpiry().isBefore(Instant.now().minusSeconds(60))){
			LOGGER.trace("Requesting new connectionId for {}", toAddress);
			
			// remove expired connectionId if it exists
			connections.remove(toAddress);
			
			// put the send request in another thread
			ConnectUdpRequest request = new ConnectUdpRequest(generateTransactionId());
			
			// wait for response
			ConnectUdpResponse response;
			try{
				response = (ConnectUdpResponse)enqueue(request, toAddress);
			}catch(Exception ex){
				LOGGER.error("Received an error fetching the connection id", ex);
				throw ex;
			}
			
			// assume txn will throw exception if no response
			connectionId = new ConnectionId(response.getConnectionId(), toAddress, Instant.now().plusSeconds(10*60));
			connections.put(toAddress, connectionId);
			LOGGER.trace("Receieve new connection id {} for {}", connectionId, toAddress);

		}
		LOGGER.trace("Returning valid connectionId {}", connectionId);
		return connectionId;

	}
	
	@Override
	public ScrapeResult doScrape(URI uri, InfoHash infoHash) throws IOException, UnhandledScrapeException, TorrentException {
		LOGGER.trace("Received request for scrape of {} from {}", infoHash, uri);

		String host = uri.getHost();
		int port = uri.getPort() != -1 ? uri.getPort() : 6969; // if no port is provided use default of 6969
		InetSocketAddress address = new InetSocketAddress(host,port);
		
		ConnectionId connId;
		try{
			connId = connect(address);
		}catch(Exception ex){
			throw new TorrentException(ex);
		}
		ScrapeUdpRequest request = new ScrapeUdpRequest(connId.getValue(), generateTransactionId(), infoHash);

		ScrapeUdpResponse response;
		try{
			response = (ScrapeUdpResponse)enqueue(request, address);
		}catch(Exception ex){
			LOGGER.error("Received an error scraping", ex);
			throw new TorrentException(ex);
		}
		ScrapeResult result = new ScrapeResult(response.getSeeders(), response.getLeechers());
		LOGGER.trace("Returning scrape {} for {} from {}", result, infoHash, address);
		return result;
	}
	
	public Integer generateTransactionId(){
		Integer transactionId = RANDOM_GEN.nextInt();
		do{
			transactionId = RANDOM_GEN.nextInt();
		}while(queue.containsKey(transactionId) == true);
		return transactionId;
	}

}