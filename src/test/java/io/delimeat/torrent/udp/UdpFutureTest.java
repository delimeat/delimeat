package io.delimeat.torrent.udp;

import java.net.InetSocketAddress;

import org.junit.Ignore;
import org.junit.Test;

import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.torrent.udp.domain.ConnectionId;
import io.delimeat.util.DelimeatUtils;

public class UdpFutureTest {
	
	@Ignore
	@Test
	public void test() throws Exception{
		UdpServer server = new UdpServer();
		InetSocketAddress address = new InetSocketAddress("tracker.leechers-paradise.org",6969);
		InfoHash infoHash = new InfoHash(DelimeatUtils.hexToBytes("d73ae09044abf44faf3ade9e43cbc902b319c15b"));
		server.initialize();
		ConnectionId connectionId = server.connect(address);
		System.out.println(connectionId);
		ScrapeResult result = server.scrape(infoHash, address);
		System.out.println(result);
		
		address = new InetSocketAddress("tracker.coppersurfer.tk",6969);
		result = server.scrape(infoHash, address);
		System.out.println(result);

		address = new InetSocketAddress("eddie4.nl",6969);
		result = server.scrape(infoHash, address);
		System.out.println(result);
		
		address = new InetSocketAddress("tracker.pirateparty.gr",6969);
		result = server.scrape(infoHash, address);
		System.out.println(result);
		
		address = new InetSocketAddress("tracker.zer0day.to",1337);
		result = server.scrape(infoHash, address);
		System.out.println(result);
		
		Thread.sleep(10000);
		server.shudown();
		System.out.println("FINISHED");
	}
}
