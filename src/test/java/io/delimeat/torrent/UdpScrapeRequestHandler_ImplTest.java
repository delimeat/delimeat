package io.delimeat.torrent;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.net.URI;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Selector;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

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
	public void channelTest(){
		Assert.assertNull(handler.getChannel());
		
		DatagramChannel channel = Mockito.mock(DatagramChannel.class);
		handler.setChannel(channel);
		
		Assert.assertEquals(channel, handler.getChannel());
	}
	
	@Test
	public void selectorTest(){
		Assert.assertNull(handler.getSelector());
		
		Selector selector = Mockito.mock(Selector.class);
		handler.setSelector(selector);
		
		Assert.assertEquals(selector, handler.getSelector());
	}
	
	@Test
	public void executorTest(){
		Assert.assertNull(handler.getExecutor());
		
		Executor executor = Mockito.mock(Executor.class);
		handler.setExecutor(executor);
		
		Assert.assertEquals(executor, handler.getExecutor());
	}
	
	@Ignore
	@Test
	public void test() throws Exception{
		Selector selector = Selector.open();
		DatagramChannel channel = DatagramChannel.open();
		channel.bind(new InetSocketAddress("0.0.0.0", 4041));
		channel.configureBlocking(false);

		channel.setOption(StandardSocketOptions.SO_SNDBUF, 2*1024*1024);
		channel.setOption(StandardSocketOptions.SO_RCVBUF, 2*1024*1024);
		channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		
		Executor executor = Executors.newScheduledThreadPool(2);
		
		UdpScrapeRequestHandler_Impl server = new UdpScrapeRequestHandler_Impl();
		server.setChannel(channel);
		server.setSelector(selector);
		server.setExecutor(executor);
		
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
		server.shudown();
		System.out.println("FINISHED");
	}
}
