package io.delimeat.torrent.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
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
import io.delimeat.torrent.udp.domain.ScrapeUdpRequest;
import io.delimeat.torrent.udp.domain.UdpRequest;
import io.delimeat.torrent.udp.domain.UdpResponse;
import io.delimeat.torrent.udp.domain.UdpTransaction;
import io.delimeat.torrent.udp.exception.UdpErrorResponseException;
import io.delimeat.torrent.udp.exception.UdpException;

public class UdpServer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UdpServer.class);

	private static final Random RANDOM_GEN = new Random();
	
	private Map<Integer, UdpTransaction> queue = new ConcurrentHashMap<>();
	private Queue<UdpTransaction> sendPipeline = new ConcurrentLinkedQueue<>();
	
	private Map<InetSocketAddress,ConnectionId> connections = new ConcurrentHashMap<>(); 
	
	private DatagramChannel channel;
	private Selector selector;
	private Executor executor;
	
	private boolean writeActive = false;
	private boolean readActive = false;
	
	public void initialize() throws IOException{
		
		selector = Selector.open();
		channel = DatagramChannel.open();
		channel.bind(new InetSocketAddress("localhost", 4041));
		channel.configureBlocking(false);
		
		channel.setOption(StandardSocketOptions.SO_SNDBUF, 2*1024*1024);
		channel.setOption(StandardSocketOptions.SO_RCVBUF, 2*1024*1024);
		channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		
		channel.register(selector, SelectionKey.OP_ACCEPT);
		channel.register(selector, SelectionKey.OP_CONNECT);
		channel.register(selector, SelectionKey.OP_READ);
		channel.register(selector, SelectionKey.OP_WRITE);
		
		executor = Executors.newSingleThreadExecutor();
		
		while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {
 
                SelectionKey key = iter.next();
 
                if(key.isValid() == false){
                	continue;
                }
                
                if (key.isAcceptable()) {
                	System.out.println("ACCEPTABLE");
                	System.out.println(key);
                }else if (key.isWritable()) {
                	System.out.println("WRITEABLE");
                	System.out.println(key);
                }else if (key.isConnectable()) {
                	System.out.println("CONNECTABLE");
                	System.out.println(key);
                }else if ( key.isReadable()) {	
                	System.out.println("READABLE");
                	System.out.println(key);
                	executor.execute(new Runnable(){

						@Override
						public void run() {
							read();	
						}
                		
                	});
                }
                
                iter.remove();
            }
        }
	}
	
	public void setChannel(DatagramChannel channel){
		this.channel = channel;
	}
	
	public DatagramChannel getChannel(){
		return channel;
	}
	
	public void write(){
		if(writeActive == true){
			return;
		}
		
		writeActive = true;
		while(true){
			UdpTransaction transaction = sendPipeline.poll();
			if(transaction == null){
				break;
			}
			
			UdpRequest request = transaction.getRequest();
			try{
				
				channel.send(request.toByteBuffer(), transaction.getToAddress());
			}catch(SocketTimeoutException ex){
				//TODO alert timeouts
			}catch(IOException ex){
				//TODO alert errors
			}
		}
		writeActive = false;
	}
	
	public void read(){
		if(readActive == true){
			return;
		}
		readActive = true;
		ByteBuffer readBuffer = ByteBuffer.allocate(1024);
		while(true){
			readBuffer.clear();
			InetSocketAddress fromAddress = null;
			try{
				fromAddress = (InetSocketAddress) channel.receive(readBuffer);
			}catch(IOException ex){
				//TODO handle exceptions
				LOGGER.warn("Recieved an error reading", ex);
				continue;
			}
			
			// if there is no fromAddress there are no responses to read
			if(fromAddress == null){
				break;
			}
			
			// if the response is less than 8 bytes or no port its invalid
			if(readBuffer.position() < 8 || fromAddress.getPort() == 0){
				continue;
			}
			
			int action = readBuffer.getInt();			
			ByteBuffer messageBytes = ByteBuffer.allocate(readBuffer.limit()).put(readBuffer);
			UdpResponse response;
			try{
				switch(action){
				case UdpUtils.CONNECT_ACTION:
					response = UdpUtils.buildConnectResponse(messageBytes);
					break;
				
				case UdpUtils.SCRAPE_ACTION:
					response = UdpUtils.buildScrapeResponse(messageBytes);

					break;
				case UdpUtils.ERROR_ACTION:
					response = UdpUtils.buildErrorResponse(messageBytes);
					break;
				default:
					LOGGER.warn("Received unsupported udp action {} from {}", action, fromAddress);
					continue;
				}
			}catch(UdpException ex){
				LOGGER.warn("Unable to build response", ex);
				continue;
			}
					
			Integer transactionId = response.getTransactionId();
			UdpTransaction transaction = queue.get(transactionId);
			if(transaction == null){
				LOGGER.warn("Received unsolicited response {}", response);
				continue;
			}else if(transaction.getToAddress() != fromAddress){
				LOGGER.warn("TransactionId {} received from different address it was sent to\n{}\n{}",response.getTransactionId(), transaction, response);
				continue;				
			}else if(action == UdpUtils.ERROR_ACTION){
				transaction.setException(new UdpErrorResponseException((ErrorUdpResponse)response));
				continue;
			} else if(transaction.getRequest() instanceof ConnectUdpRequest && action == UdpUtils.SCRAPE_ACTION){
				LOGGER.warn("Unexpected response for connect request\n{}\n{}", transaction, response);
				continue;	
			}else if(transaction.getRequest() instanceof ScrapeUdpRequest && action == UdpUtils.CONNECT_ACTION){
				LOGGER.warn("Unexpected response for scrape request\n{}\n{}", transaction, response);
				continue;
			}
			transaction.setResponse(response);
		}
		readActive = false;
	}
	
	public ConnectionId connect(InetSocketAddress toAddress) throws Exception{
		ConnectUdpRequest request = new ConnectUdpRequest(generateTransactionId());
		
		UdpTransaction txn = new UdpTransaction(request, toAddress);
		executor.execute(new Runnable(){

			@Override
			public void run() {
				write();	
			}
			
		});
		
		UdpResponse response = txn.getResponse(3000);
		
		if(response instanceof ConnectUdpResponse){
			ConnectUdpResponse connResp = (ConnectUdpResponse)response;
			ConnectionId connectionId = new ConnectionId(connResp.getConnectionId(),txn.getToAddress(), Instant.now().plusSeconds(10*60));
			connections.put(toAddress, connectionId);
			return connectionId;
		}else{
			//TODO
			throw new Exception();
		}
	}
	
	public Integer generateTransactionId(){
		Integer transactionId = RANDOM_GEN.nextInt();
		do{
			transactionId = RANDOM_GEN.nextInt();
		}while(queue.containsKey(transactionId) == true);
		return transactionId;
	}
	
	
}
