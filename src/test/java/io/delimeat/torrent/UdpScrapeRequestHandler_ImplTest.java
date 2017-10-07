package io.delimeat.torrent;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.net.URI;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
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
import io.delimeat.torrent.domain.UdpConnectResponse;
import io.delimeat.torrent.domain.UdpConnectionId;
import io.delimeat.torrent.domain.UdpErrorResponse;
import io.delimeat.torrent.domain.UdpScrapeResponse;
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
		UdpConnectionId connIdRemove = new UdpConnectionId(Long.MAX_VALUE,new InetSocketAddress("localhost",9004),Instant.EPOCH);
		UdpConnectionId connIdKeep = new UdpConnectionId(Long.MIN_VALUE,new InetSocketAddress("0.0.0.0",9004),Instant.MAX);

		handler.getConnections().put(connIdRemove.getFromAddress(), connIdRemove);
		handler.getConnections().put(connIdKeep.getFromAddress(), connIdKeep);
		
		handler.purgeInvalidConnectionIds();
		
		Assert.assertEquals(1, handler.getConnections().size());
		Assert.assertEquals(connIdKeep, handler.getConnections().get(connIdKeep.getFromAddress()));
	}
	
	
	@Test
	public void buildErrorResponseTest() throws Exception{
		ByteBuffer buf = ByteBuffer.allocate(1024)
				.putInt(Integer.MAX_VALUE)
				.put("MESSAGE".getBytes());
		buf.clear();

		UdpErrorResponse result = handler.unmarshallErrorResponse(buf);
		
		
		Assert.assertEquals(Integer.MAX_VALUE, result.getTransactionId());
		Assert.assertEquals("MESSAGE", result.getMessage());
	}
	
	@Test(expected=BufferUnderflowException.class)
	public void buildErrorResponseExceptionTest() throws Exception{
		ByteBuffer buf = ByteBuffer.allocate(0);
		handler.unmarshallErrorResponse( buf);
	}
	
	@Test
	public void buildConnectResponseTest() throws Exception{
		ByteBuffer buf = ByteBuffer.allocate(12)
				.putInt(Integer.MAX_VALUE)
				.putLong(0x41727101980L);
		buf.clear();
		
		UdpConnectResponse result = handler.unmarshallConnectResponse(buf);
		
		Assert.assertEquals(Integer.MAX_VALUE, result.getTransactionId());
		Assert.assertEquals(0x41727101980L, result.getConnectionId());
	}
	
	@Test(expected=BufferUnderflowException.class)
	public void buildConnectResponseExceptionTest() throws Exception{
		ByteBuffer buf = ByteBuffer.wrap("MESSAGE".getBytes());
		handler.unmarshallConnectResponse(buf);
	}
	
	@Test
	public void buildScrapeResponseTest() throws Exception{
		ByteBuffer buf = ByteBuffer.allocate(12)
				.putInt(Integer.MAX_VALUE)
				.putInt(Integer.MAX_VALUE)
				.putInt(Integer.MIN_VALUE);
		
		ByteBuffer buff2 = ByteBuffer.wrap(buf.array());
		UdpScrapeResponse result = handler.unmarshallScrapeResponse(buff2);
		
		Assert.assertEquals(Integer.MAX_VALUE, result.getTransactionId());
		Assert.assertEquals(Integer.MAX_VALUE, result.getSeeders());
		Assert.assertEquals(Integer.MIN_VALUE, result.getLeechers());
	}
	
	@Test(expected=BufferUnderflowException.class)
	public void buildScrapeResponseExceptionTest() throws Exception{
		ByteBuffer buf = ByteBuffer.wrap("MESSAGE".getBytes());
		handler.unmarshallScrapeResponse(buf);
	}
	
	@Ignore
	@Test
	public void test() throws Exception{	
		InetSocketAddress address = new InetSocketAddress("0.0.0.0",9004);
		
		UdpScrapeRequestHandler_Impl server = new UdpScrapeRequestHandler_Impl();
		server.setAddress(address);
				
		InfoHash infoHash = new InfoHash(DelimeatUtils.hexToBytes("d73ae09044abf44faf3ade9e43cbc902b319c15b"));

		List<URI> uris = Arrays.asList(
				new URI("udp://tracker.coppersurfer.tk:6969")
				,new URI("udp://tracker.leechers-paradise.org:6969")
				,new URI("udp://eddie4.nl:6969")
				,new URI("udp://tracker.pirateparty.gr:6969")
				,new URI("udp://tracker.zer0day.to:1337")
				,new URI("udp://tracker.coppersurfer.tk:6969")
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
		
		scheduler.execute(this::doThread);
		
		scheduler.scheduleWithFixedDelay(this::doSchedule, 200, 3000, TimeUnit.MILLISECONDS);
		
		Thread.sleep(11000);
	}
	
	public void doThread(){
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
	
	public void doSchedule(){
		System.out.println("Scheduled run");
	}
}
