package io.delimeat.torrent.udp;

import java.net.URI;

import org.junit.Ignore;
import org.junit.Test;

import io.delimeat.torrent.UdpNIOScrapeRequestHandler_Impl;
import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.util.DelimeatUtils;

public class UdpFutureTest {
	
	@Ignore
	@Test
	public void test() throws Exception{
		UdpNIOScrapeRequestHandler_Impl server = new UdpNIOScrapeRequestHandler_Impl();
		URI uri = new URI("udp://tracker.leechers-paradise.org:6969");
		InfoHash infoHash = new InfoHash(DelimeatUtils.hexToBytes("d73ae09044abf44faf3ade9e43cbc902b319c15b"));
		ScrapeResult result = server.doScrape(uri, infoHash);
		System.out.println(result);
		
		uri = new URI("udp://tracker.coppersurfer.tk:6969");
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
