/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.delimeat.processor.entity;

import java.net.URI;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.delimeat.torrent.entity.InfoHash;
import io.delimeat.torrent.entity.ScrapeResult;
import io.delimeat.torrent.entity.Torrent;

public class FeedProcessUnitTest {

	private FeedProcessUnit processUnit;
	
	@BeforeEach
	public void setUp(){
		processUnit = new FeedProcessUnit();
	}
	
	@Test
	public void downloadUriTest() throws Exception{
		Assertions.assertNull(processUnit.getDownloadUri());
		URI uri = new URI("udp://test.com");
		processUnit.setDownloadUri(uri);
		Assertions.assertEquals(uri, processUnit.getDownloadUri());
	}
	
	@Test
	public void feedResultRejectionsTest(){
		Assertions.assertNotNull(processUnit.getRejections());
		Assertions.assertEquals(0, processUnit.getRejections().size());
	}
	
	@Test
	public void infoHashTest(){
		Assertions.assertNull(processUnit.getInfoHash());
		InfoHash infoHash = new InfoHash("bytes".getBytes());
		processUnit.setInfoHash(infoHash);
		Assertions.assertEquals(infoHash, processUnit.getInfoHash());
	}
	
	@Test
	public void scrapeTest(){
		Assertions.assertNull(processUnit.getScrape());
		ScrapeResult result = ScrapeResult.builder().seeders(Long.MAX_VALUE).leechers(Long.MIN_VALUE).build();
		processUnit.setScrape(result);
		Assertions.assertEquals(Long.MAX_VALUE, processUnit.getScrape().getSeeders());
		Assertions.assertEquals(Long.MIN_VALUE, processUnit.getScrape().getLeechers());	
	}
	
	@Test
	public void titleTest(){
		Assertions.assertNull(processUnit.getTitle());
		processUnit.setTitle("TITLE");
		Assertions.assertEquals("TITLE", processUnit.getTitle());
	}
	
	@Test
	public void torrentTest(){
		Assertions.assertNull(processUnit.getTorrent());
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		Assertions.assertEquals(torrent, processUnit.getTorrent());
	}
	
	@Test
	public void hashCodeTest() throws Exception{
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setScrape(ScrapeResult.builder().seeders(Long.MAX_VALUE).leechers(Long.MAX_VALUE).build());
		processUnit.setTitle("TITLE");
		processUnit.setTorrent(new Torrent());
		Assertions.assertEquals(-1163967515, processUnit.hashCode());
	}
	
	@Test
	public void toStringTest(){
		Assertions.assertEquals("FeedProcessUnit [source=null, title=null, infoHash=null, downloadUri=null, scrape=null, torrent=null, rejections=[]]", processUnit.toString());
	}
	
	@Test
	public void equalsTest() throws Exception{
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setScrape(ScrapeResult.builder().seeders(Long.MAX_VALUE).leechers(Long.MAX_VALUE).build());
		processUnit.setTitle("TITLE");
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setScrape(ScrapeResult.builder().seeders(Long.MAX_VALUE).leechers(Long.MAX_VALUE).build());
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assertions.assertTrue(processUnit.equals(other));
	}
	
	@Test
	public void equalsSelfTest(){
		Assertions.assertTrue(processUnit.equals(processUnit));
	}
	
	@Test
	public void equalsNullTest(){
		Assertions.assertFalse(processUnit.equals(null));
	}
	
	@Test
	public void equalsObjectTest(){
		Assertions.assertFalse(processUnit.equals(new Object()));
	}

	@Test
	public void equalsDownloadUriTest() throws Exception{
		processUnit.setDownloadUri(new URI("http://different.io"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setScrape(ScrapeResult.builder().seeders(Long.MAX_VALUE).leechers(Long.MAX_VALUE).build());
		processUnit.setTitle("TITLE");
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setScrape(ScrapeResult.builder().seeders(Long.MAX_VALUE).leechers(Long.MAX_VALUE).build());
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assertions.assertFalse(processUnit.equals(other));
	}
	
	@Test
	public void equalsInfoHashTest() throws Exception{
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("other".getBytes()));
		processUnit.setScrape(ScrapeResult.builder().seeders(Long.MAX_VALUE).leechers(Long.MAX_VALUE).build());
		processUnit.setTitle("TITLE");
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setScrape(ScrapeResult.builder().seeders(Long.MAX_VALUE).leechers(Long.MAX_VALUE).build());
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assertions.assertFalse(processUnit.equals(other));
	}
	
	@Test
	public void equalsScrapeTest() throws Exception{
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setScrape(ScrapeResult.builder().seeders(Long.MIN_VALUE).leechers(Long.MAX_VALUE).build());
		processUnit.setTitle("TITLE");
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setScrape(ScrapeResult.builder().seeders(Long.MAX_VALUE).leechers(Long.MAX_VALUE).build());
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assertions.assertFalse(processUnit.equals(other));
	}
	
	@Test
	public void equalsTitleTest() throws Exception{
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setScrape(ScrapeResult.builder().seeders(Long.MAX_VALUE).leechers(Long.MAX_VALUE).build());
		processUnit.setTitle("OTHER");
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setScrape(ScrapeResult.builder().seeders(Long.MAX_VALUE).leechers(Long.MAX_VALUE).build());
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assertions.assertFalse(processUnit.equals(other));
	}

	@Test
	public void equalsTorrentTest() throws Exception{
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setScrape(ScrapeResult.builder().seeders(Long.MAX_VALUE).leechers(Long.MAX_VALUE).build());
		processUnit.setTitle("TITLE");
		Torrent torrent = new Torrent();
		torrent.setTracker("TRACKER");
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setScrape(ScrapeResult.builder().seeders(Long.MAX_VALUE).leechers(Long.MAX_VALUE).build());
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assertions.assertFalse(processUnit.equals(other));
	}
	
	@Test
	public void equalsDownloadUriNullTest() throws Exception{
		processUnit.setDownloadUri(null);
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setScrape(ScrapeResult.builder().seeders(Long.MAX_VALUE).leechers(Long.MAX_VALUE).build());
		processUnit.setTitle("TITLE");
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setScrape(ScrapeResult.builder().seeders(Long.MAX_VALUE).leechers(Long.MAX_VALUE).build());
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assertions.assertFalse(processUnit.equals(other));
	}
	
	@Test
	public void equalsInfoHashNullTest() throws Exception{
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(null);
		processUnit.setScrape(ScrapeResult.builder().seeders(Long.MAX_VALUE).leechers(Long.MAX_VALUE).build());
		processUnit.setTitle("TITLE");
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setScrape(ScrapeResult.builder().seeders(Long.MAX_VALUE).leechers(Long.MAX_VALUE).build());
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assertions.assertFalse(processUnit.equals(other));
	}	
	
	@Test
	public void equalsTitleNullTest() throws Exception{
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setScrape(ScrapeResult.builder().seeders(Long.MAX_VALUE).leechers(Long.MAX_VALUE).build());
		processUnit.setTitle(null);
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setScrape(ScrapeResult.builder().seeders(Long.MAX_VALUE).leechers(Long.MAX_VALUE).build());
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assertions.assertFalse(processUnit.equals(other));
	}
	
	@Test
	public void equalsTorrentNullTest() throws Exception{
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setScrape(ScrapeResult.builder().seeders(Long.MAX_VALUE).leechers(Long.MAX_VALUE).build());
		processUnit.setTitle("TITLE");
		processUnit.setTorrent(null);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setScrape(ScrapeResult.builder().seeders(Long.MAX_VALUE).leechers(Long.MAX_VALUE).build());
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assertions.assertFalse(processUnit.equals(other));
	}
	
	@Test
	public void equalsScrapeNullTest() throws Exception{
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setScrape(null);
		processUnit.setTitle("TITLE");
		processUnit.setTorrent(null);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setScrape(ScrapeResult.builder().seeders(Long.MAX_VALUE).leechers(Long.MAX_VALUE).build());
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assertions.assertFalse(processUnit.equals(other));
	}
}
