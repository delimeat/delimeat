package io.delimeat.torrent;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.net.URI;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
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
		Assert.assertEquals(address, handler.getChannel().getLocalAddress());
		Assert.assertEquals(2*1024, handler.getChannel().getOption(StandardSocketOptions.SO_SNDBUF).intValue());
		Assert.assertEquals(2*1024, handler.getChannel().getOption(StandardSocketOptions.SO_RCVBUF).intValue());
		Assert.assertTrue(handler.getChannel().getOption(StandardSocketOptions.SO_REUSEADDR).booleanValue());
		
		handler.shutdown();
		Assert.assertFalse(handler.isActive());
		Assert.assertFalse(handler.getSelector().isOpen());
		Assert.assertFalse(handler.getChannel().isOpen());
		Assert.assertTrue(handler.getExecutor().isShutdown());
		Assert.assertTrue(handler.getExecutor().isTerminated());
	}	
	
	@Ignore
	@Test
	public void test() throws Exception{	
		UdpScrapeRequestHandler_Impl server = new UdpScrapeRequestHandler_Impl();
		
		server.initialize();
		
		URI uri = new URI("udp://tracker.coppersurfer.tk:6969");
		InfoHash infoHash = new InfoHash(DelimeatUtils.hexToBytes("d73ae09044abf44faf3ade9e43cbc902b319c15b"));
		ScrapeResult result = server.doScrape(uri, infoHash);
		System.out.println(result);
		
		uri = new URI("udp://tracker.leechers-paradise.org:6969");
		result = server.doScrape(uri, infoHash);
		System.out.println(result);

		uri = new URI("udp://eddie4.nl:6969");
		result = server.doScrape(uri, infoHash);
		System.out.println(result);
		
		uri = new URI("udp://tracker.pirateparty.gr:6969");
		result = server.doScrape(uri, infoHash);
		System.out.println(result);

		uri = new URI("udp://tracker.zer0day.to:1337");
		result = server.doScrape(uri, infoHash);
		System.out.println(result);
		
		Thread.sleep(10000);
		server.shutdown();
		System.out.println("FINISHED");
	}
}
