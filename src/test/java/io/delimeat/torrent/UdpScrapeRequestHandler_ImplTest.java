package io.delimeat.torrent;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.net.URI;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.torrent.udp.domain.ConnectionId;
import io.delimeat.util.DelimeatUtils;

public class UdpScrapeRequestHandler_ImplTest {
	
	private UdpScrapeRequestHandler_Impl handler;
	
	@Before
	public void setUp(){
		handler = new UdpScrapeRequestHandler_Impl();
	}
	
	@Test
	public void supportedProtocolTest(){
		Assert.assertEquals(Arrays.asList("UDP"),handler.getSupportedProtocols());
	}
	
	@Test
	public void addressTest(){
		Assert.assertNull(handler.getAddress());
		
		InetSocketAddress address = new InetSocketAddress("localhost", 4040);
		handler.setAddress(address);
		
		Assert.assertEquals(address, handler.getAddress());
	}
	
	@Test
	public void initializeAndShutdownTest() throws Exception{
		InetSocketAddress address = new InetSocketAddress("localhost", 4040);
		handler.setAddress(address);
		
		handler.initialize();

		Assert.assertTrue(handler.isActive());
		Assert.assertTrue(handler.getSelector().isOpen());
		
		Assert.assertFalse(handler.getExecutor().isShutdown());
		Assert.assertFalse(handler.getExecutor().isTerminated());
		
		Assert.assertTrue(handler.getChannel().isOpen());
		Assert.assertFalse(handler.getChannel().isBlocking());
		// this is causing an error when building on travis-ci disabling for now
		//Assert.assertEquals("2048", handler.getChannel().getOption(StandardSocketOptions.SO_SNDBUF).toString());
		//Assert.assertEquals("2048", handler.getChannel().getOption(StandardSocketOptions.SO_RCVBUF).toString());
		Assert.assertTrue(handler.getChannel().getOption(StandardSocketOptions.SO_REUSEADDR).booleanValue());
		
		handler.shutdown();
		Assert.assertFalse(handler.isActive());
		Assert.assertFalse(handler.getSelector().isOpen());
		Assert.assertFalse(handler.getChannel().isOpen());
		Assert.assertTrue(handler.getExecutor().isShutdown());
		Assert.assertTrue(handler.getExecutor().isTerminated());
	}	
	
	@Test
	public void shutdownDueToInactivityTest() throws Exception{
		InetSocketAddress address = new InetSocketAddress("localhost", 4040);
		handler.setAddress(address);
		
		handler.initialize();
		Assert.assertTrue(handler.isActive());
		
		handler.shutdownDueToInactivity();
		Assert.assertFalse(handler.isActive());
		Assert.assertFalse(handler.getSelector().isOpen());
		Assert.assertFalse(handler.getChannel().isOpen());
		Assert.assertTrue(handler.getExecutor().isShutdown());
		Assert.assertTrue(handler.getExecutor().isTerminated());
	}
	
	@Test
	public void purgeInvalidConnectionIdsTest(){
		ConnectionId connIdRemove = new ConnectionId(Long.MAX_VALUE,new InetSocketAddress("localhost",9004),Instant.EPOCH);
		ConnectionId connIdKeep = new ConnectionId(Long.MIN_VALUE,new InetSocketAddress("0.0.0.0",9004),Instant.MAX);

		handler.getConnections().put(connIdRemove.getFromAddress(), connIdRemove);
		handler.getConnections().put(connIdKeep.getFromAddress(), connIdKeep);
		
		handler.purgeInvalidConnectionIds();
		
		Assert.assertEquals(1, handler.getConnections().size());
		Assert.assertEquals(connIdKeep, handler.getConnections().get(connIdKeep.getFromAddress()));

	}
	
	@Ignore
	@Test
	public void test() throws Exception{	
		InetSocketAddress address = new InetSocketAddress("0.0.0.0",9004);
		
		UdpScrapeRequestHandler_Impl server = new UdpScrapeRequestHandler_Impl();
		server.setAddress(address);
		
		//server.initialize();
		
		InfoHash infoHash = new InfoHash(DelimeatUtils.hexToBytes("d73ae09044abf44faf3ade9e43cbc902b319c15b"));

		List<URI> uris = Arrays.asList(new URI("udp://tracker.coppersurfer.tk:6969"),
				new URI("udp://tracker.leechers-paradise.org:6969"),
				new URI("udp://eddie4.nl:6969"),
				new URI("udp://tracker.pirateparty.gr:6969"),
				new URI("udp://tracker.zer0day.to:1337")
				);
		ScrapeResult result = null;
		for(URI uri : uris){
			try{
			result = server.scrape(uri, infoHash);
			System.out.println(result);
			}catch(Exception ex){
				System.out.println("Exception");
			}
		}
		
		Thread.sleep(10000);
		server.shutdown();
		System.out.println("FINISHED");
	}
	
	@Ignore
	@Test
	public void test2() throws Exception{
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
		
		scheduler.execute(new Runnable(){

			@Override
			public void run() {
				int count = 0;
				while(count < 10){
					System.out.println("Sleeping");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					count++;
					System.out.println("Woke up");
				}
				
			}
			
		});
		
		scheduler.scheduleWithFixedDelay(this::doSchedule, 2000, 3000, TimeUnit.MILLISECONDS);

		
		Thread.sleep(11000);
	}
	
	public void doSchedule(){
		System.out.println("Scheduled run");
	}
}
