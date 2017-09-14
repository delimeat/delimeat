package io.delimeat.torrent.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.time.Instant;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.delimeat.torrent.udp.domain.ConnectUdpRequest;
import io.delimeat.torrent.udp.domain.ConnectUdpResponse;
import io.delimeat.torrent.udp.domain.ConnectionId;
import io.delimeat.torrent.udp.domain.ErrorUdpResponse;
import io.delimeat.torrent.udp.domain.UdpResponse;
import io.delimeat.torrent.udp.domain.UdpTransaction;
import io.delimeat.torrent.udp.exception.UdpErrorResponseException;
import io.delimeat.torrent.udp.exception.UdpException;

public class UdpServer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UdpServer.class);

	private static final Random RANDOM_GEN = new Random();
	
	private final Map<Integer, UdpTransaction> queue = new ConcurrentHashMap<>();
	private final Queue<UdpTransaction> sendPipeline = new ConcurrentLinkedQueue<>();
	
	private final Map<InetSocketAddress,ConnectionId> connections = new ConcurrentHashMap<>(); 
	
	private boolean sendActive = false;
	private boolean receiveActive = false;
	private boolean active = false;
	
	private DatagramChannel channel;
	private Selector selector;
	private Executor executor;
	
	/**
	 * @return the channel
	 */
	public DatagramChannel getChannel() {
		return channel;
	}



	/**
	 * @param channel the channel to set
	 */
	public void setChannel(DatagramChannel channel) {
		this.channel = channel;
	}



	/**
	 * @return the selector
	 */
	public Selector getSelector() {
		return selector;
	}



	/**
	 * @param selector the selector to set
	 */
	public void setSelector(Selector selector) {
		this.selector = selector;
	}



	/**
	 * @return the executor
	 */
	public Executor getExecutor() {
		return executor;
	}


	/**
	 * @param executor the executor to set
	 */
	public void setExecutor(Executor executor) {
		this.executor = executor;
	}
	
	public void initialize() throws IOException{
		
		selector = Selector.open();
		channel = DatagramChannel.open();
		channel.bind(new InetSocketAddress("0.0.0.0", 4041));
		channel.configureBlocking(false);
		
		channel.setOption(StandardSocketOptions.SO_SNDBUF, 2*1024*1024);
		channel.setOption(StandardSocketOptions.SO_RCVBUF, 2*1024*1024);
		channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		
		channel.register(selector, SelectionKey.OP_READ);
		
		executor = Executors.newFixedThreadPool(2);
		
		active = true;
		executor.execute(this::select);

	}
	
	public void select(){
		while (active) {
			try{
				selector.select();
			}catch(IOException | ClosedSelectorException ex){
				LOGGER.error("Selector encountered an error",ex);
			}
			//allows for closing gracefully
			if(selector.isOpen() == false){
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
	
	public void shudown() throws IOException{
		active = false;
		selector.close();
		channel.close();
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
	
	public ConnectionId connect(InetSocketAddress toAddress) throws Exception {
		LOGGER.trace("Received request for connectionId for {}", toAddress);

		ConnectionId connectionId = connections.get(toAddress);
		if(connectionId == null || connectionId.getExpiry().isBefore(Instant.now().minusSeconds(60))){
			LOGGER.trace("Requesting new connectionId for {}", toAddress);
			
			// remove expired connectionId if it exists
			connections.remove(toAddress);
			
			// put the send request in another thread
			ConnectUdpRequest request = new ConnectUdpRequest(generateTransactionId());
			UdpTransaction txn = new UdpTransaction(request, toAddress);
			sendPipeline.add(txn);
			executor.execute(this::send);
			
			// wait for response
			UdpResponse response;
			try{
				response = txn.getResponse(3000);
			}catch(Exception ex){
				LOGGER.error("Received an error fetching the connection id", ex);
				throw ex;
			}
			
			// assume txn will throw exception if no response
			ConnectUdpResponse connResp = (ConnectUdpResponse)response;
			connectionId = new ConnectionId(connResp.getConnectionId(), txn.getToAddress(), Instant.now().plusSeconds(10*60));
			connections.put(toAddress, connectionId);
			LOGGER.trace("Receieve new connection id {} for {}", connectionId, toAddress);

		}
		LOGGER.trace("Returning valid connectionId {}", connectionId);
		return connectionId;

	}
	
	public Integer generateTransactionId(){
		Integer transactionId = RANDOM_GEN.nextInt();
		do{
			transactionId = RANDOM_GEN.nextInt();
		}while(queue.containsKey(transactionId) == true);
		return transactionId;
	}
	
	
}
