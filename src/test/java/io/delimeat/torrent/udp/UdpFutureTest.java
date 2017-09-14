package io.delimeat.torrent.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardProtocolFamily;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.udp.domain.ConnectUdpRequest;
import io.delimeat.torrent.udp.domain.ConnectUdpResponse;
import io.delimeat.torrent.udp.domain.ConnectionId;
import io.delimeat.torrent.udp.domain.ErrorUdpResponse;
import io.delimeat.torrent.udp.domain.ScrapeUdpRequest;
import io.delimeat.torrent.udp.domain.UdpAction;
import io.delimeat.torrent.udp.domain.UdpResponse;
import io.delimeat.torrent.udp.domain.UdpTransaction;
import io.delimeat.util.DelimeatUtils;

public class UdpFutureTest {
	
	private Selector selector;
	private DatagramChannel channel;
	private Executor executor; 
	private boolean readActive = false;
	
	private Map<Integer, UdpTransaction> queue = new ConcurrentHashMap<>();
	private Queue<UdpTransaction> sendPipeline = new ConcurrentLinkedQueue<>();

	@Test
	public void selectorTest() throws Exception{
		
		InetSocketAddress addr1 = new InetSocketAddress("tracker.leechers-paradise.org",6969);
		InetSocketAddress addr2 = new InetSocketAddress("tracker.coppersurfer.tk",6969);
		ConnectUdpRequest req1 = new ConnectUdpRequest(new Random().nextInt());
		ConnectUdpRequest req2 = new ConnectUdpRequest(new Random().nextInt());

		UdpTransaction transaction1 = new UdpTransaction(req1, addr1);
		UdpTransaction transaction2 = new UdpTransaction(req2, addr2);
		sendPipeline.add(transaction1);
		sendPipeline.add(transaction2);
		
		selector = Selector.open();
		channel = DatagramChannel.open(StandardProtocolFamily.INET);
		channel.bind(new InetSocketAddress("0.0.0.0", 4041));
		channel.configureBlocking(false);
		
		channel.setOption(StandardSocketOptions.SO_SNDBUF, 2*1024*1024);
		channel.setOption(StandardSocketOptions.SO_RCVBUF, 2*1024*1024);
		channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		
		channel.register(selector, SelectionKey.OP_READ);
		
		executor = Executors.newScheduledThreadPool(3);
		//executor = Executors.newSingleThreadExecutor();
		Thread thread = new Thread(){
			
			@Override
			public void run(){
				try{
					executor.execute(()->send());

					Instant start = Instant.now();
				while (Instant.now().isBefore(start.plusSeconds(10))) {
		            int count = selector.select();
		            System.out.println("SELECT: "+ count);
		            
		            Set<SelectionKey> selectedKeys = selector.selectedKeys();
		            Iterator<SelectionKey> iter = selectedKeys.iterator();
		            
		            while (iter.hasNext()) {
		            	 
		                SelectionKey key = iter.next();
		                iter.remove();
		                
		                if (key.isReadable()) {
		                	System.out.println("READABLE");
		                	read();
		                }
		            }

		        }	
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		};
		
		executor.execute(thread);
		thread.sleep(10000);
		
		System.out.println("FINISHED");

		
	}
	
	public void send(){
		try{
			UdpTransaction transaction = null;
			while((transaction = sendPipeline.poll()) != null){
				channel.send(transaction.getRequest().toByteBuffer(), transaction.getToAddress());
				queue.put(transaction.getRequest().getTransactionId(), transaction);
				System.out.println(transaction);
			}
		}catch(Exception e){
			System.out.println("SEND EXCEPTION");
			e.printStackTrace();
		}
	}
	
	public void read(){
		System.out.println("Starting read");
		if(readActive == true){
			System.out.println("Already reading, Exiting");
			return;
		}
		readActive = true;
		ByteBuffer readBuffer = ByteBuffer.allocate(1024);
		while(true){
			System.out.println("Start read loop");
			readBuffer.clear();
			InetSocketAddress fromAddress = null;
			try{
				fromAddress = (InetSocketAddress) channel.receive(readBuffer);
			}catch(IOException ex){
				ex.printStackTrace();
				continue;
			}
			
			System.out.println("Read from " + fromAddress);
			
			// if there is no fromAddress there are no responses to read
			if(fromAddress == null){
				System.out.println("NO FROM ADDRESS MEANS NO MORE TO READ");
				break;
			}
			
			readBuffer.flip();
			UdpResponse response = null;
			try{
				response = UdpUtils.buildResponse(readBuffer);
			}catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			System.out.println("response received");
			System.out.println(response);
			
			
			UdpTransaction transaction = queue.remove(response.getTransactionId());
			transaction.setResponse(response);
			System.out.println(transaction);
			
			if(response.getAction() == UdpAction.CONNECT){
				byte[] bytes = DelimeatUtils.hexToBytes("d73ae09044abf44faf3ade9e43cbc902b319c15b");
				InfoHash infoHash = new InfoHash(bytes);
				ScrapeUdpRequest request = new ScrapeUdpRequest(((ConnectUdpResponse)response).getConnectionId(),new Random().nextInt() ,infoHash);
				UdpTransaction scrapeTxn = new UdpTransaction(request, transaction.getToAddress());
				sendPipeline.add(scrapeTxn);
				executor.execute(()->send());
			}else{
				System.out.println("SCRAPE RESPONSE");
				System.out.println(response);
			}
			break;
			
		}
		readActive = false;
	}
	
	public void test() throws Exception{
		UdpServer server = new UdpServer();
		
		ConnectionId connectionId = server.connect(new InetSocketAddress("tracker.leechers-paradise.org",6969));
		System.out.println(connectionId);
		Thread.sleep(10000);
	}
}
