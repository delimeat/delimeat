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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.torrent.entity.InfoHash;
import io.delimeat.torrent.entity.ScrapeResult;
import io.delimeat.torrent.entity.Torrent;

public class FeedProcessUnitTest {

	private FeedProcessUnit processUnit;
	
	@Before
	public void setUp(){
		processUnit = new FeedProcessUnit();
	}
	
	@Test
	public void downloadUriTest() throws Exception{
		Assert.assertNull(processUnit.getDownloadUri());
		URI uri = new URI("udp://test.com");
		processUnit.setDownloadUri(uri);
		Assert.assertEquals(uri, processUnit.getDownloadUri());
	}
	
	@Test
	public void feedResultRejectionsTest(){
		Assert.assertNotNull(processUnit.getRejections());
		Assert.assertEquals(0, processUnit.getRejections().size());
	}
	
	@Test
	public void infoHashTest(){
		Assert.assertNull(processUnit.getInfoHash());
		InfoHash infoHash = new InfoHash("bytes".getBytes());
		processUnit.setInfoHash(infoHash);
		Assert.assertEquals(infoHash, processUnit.getInfoHash());
	}
	
	@Test
	public void scrapeTest(){
		Assert.assertNull(processUnit.getScrape());
		ScrapeResult result = new ScrapeResult(Long.MAX_VALUE, Long.MIN_VALUE);
		processUnit.setScrape(result);
		Assert.assertEquals(Long.MAX_VALUE, processUnit.getScrape().getSeeders());
		Assert.assertEquals(Long.MIN_VALUE, processUnit.getScrape().getLeechers());	
	}
	
	@Test
	public void titleTest(){
		Assert.assertNull(processUnit.getTitle());
		processUnit.setTitle("TITLE");
		Assert.assertEquals("TITLE", processUnit.getTitle());
	}
	
	@Test
	public void torrentTest(){
		Assert.assertNull(processUnit.getTorrent());
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		Assert.assertEquals(torrent, processUnit.getTorrent());
	}
	
	@Test
	public void hashCodeTest() throws Exception{
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setScrape(new ScrapeResult(Long.MAX_VALUE,Long.MAX_VALUE));
		processUnit.setTitle("TITLE");
		processUnit.setTorrent(new Torrent());
		Assert.assertEquals(-1163967515, processUnit.hashCode());
	}
	
	@Test
	public void toStringTest(){
		Assert.assertEquals("FeedProcessUnit [source=null, title=null, infoHash=null, downloadUri=null, scrape=null, torrent=null, rejections=[]]", processUnit.toString());
	}
	
	@Test
	public void equalsTest() throws Exception{
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setScrape(new ScrapeResult(Long.MAX_VALUE,Long.MAX_VALUE));
		processUnit.setTitle("TITLE");
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setScrape(new ScrapeResult(Long.MAX_VALUE,Long.MAX_VALUE));
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assert.assertTrue(processUnit.equals(other));
	}
	
	@Test
	public void equalsSelfTest(){
		Assert.assertTrue(processUnit.equals(processUnit));
	}
	
	@Test
	public void equalsNullTest(){
		Assert.assertFalse(processUnit.equals(null));
	}
	
	@Test
	public void equalsObjectTest(){
		Assert.assertFalse(processUnit.equals(new Object()));
	}

	@Test
	public void equalsDownloadUriTest() throws Exception{
		processUnit.setDownloadUri(new URI("http://different.io"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setScrape(new ScrapeResult(Long.MAX_VALUE,Long.MAX_VALUE));
		processUnit.setTitle("TITLE");
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setScrape(new ScrapeResult(Long.MAX_VALUE,Long.MAX_VALUE));
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assert.assertFalse(processUnit.equals(other));
	}
	
	@Test
	public void equalsInfoHashTest() throws Exception{
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("other".getBytes()));
		processUnit.setScrape(new ScrapeResult(Long.MAX_VALUE,Long.MAX_VALUE));
		processUnit.setTitle("TITLE");
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setScrape(new ScrapeResult(Long.MAX_VALUE,Long.MAX_VALUE));
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assert.assertFalse(processUnit.equals(other));
	}
	
	@Test
	public void equalsScrapeTest() throws Exception{
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setScrape(new ScrapeResult(Long.MIN_VALUE,Long.MAX_VALUE));
		processUnit.setTitle("TITLE");
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setScrape(new ScrapeResult(Long.MAX_VALUE,Long.MAX_VALUE));
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assert.assertFalse(processUnit.equals(other));
	}
	
	@Test
	public void equalsTitleTest() throws Exception{
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setScrape(new ScrapeResult(Long.MAX_VALUE,Long.MAX_VALUE));
		processUnit.setTitle("OTHER");
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setScrape(new ScrapeResult(Long.MAX_VALUE,Long.MAX_VALUE));
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assert.assertFalse(processUnit.equals(other));
	}

	@Test
	public void equalsTorrentTest() throws Exception{
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setScrape(new ScrapeResult(Long.MAX_VALUE,Long.MAX_VALUE));
		processUnit.setTitle("TITLE");
		Torrent torrent = new Torrent();
		torrent.setTracker("TRACKER");
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setScrape(new ScrapeResult(Long.MAX_VALUE,Long.MAX_VALUE));
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assert.assertFalse(processUnit.equals(other));
	}
	
	@Test
	public void equalsDownloadUriNullTest() throws Exception{
		processUnit.setDownloadUri(null);
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setScrape(new ScrapeResult(Long.MAX_VALUE,Long.MAX_VALUE));
		processUnit.setTitle("TITLE");
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setScrape(new ScrapeResult(Long.MAX_VALUE,Long.MAX_VALUE));
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assert.assertFalse(processUnit.equals(other));
	}
	
	@Test
	public void equalsInfoHashNullTest() throws Exception{
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(null);
		processUnit.setScrape(new ScrapeResult(Long.MAX_VALUE,Long.MAX_VALUE));
		processUnit.setTitle("TITLE");
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setScrape(new ScrapeResult(Long.MAX_VALUE,Long.MAX_VALUE));
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assert.assertFalse(processUnit.equals(other));
	}	
	
	@Test
	public void equalsTitleNullTest() throws Exception{
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setScrape(new ScrapeResult(Long.MAX_VALUE,Long.MAX_VALUE));
		processUnit.setTitle(null);
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setScrape(new ScrapeResult(Long.MAX_VALUE,Long.MAX_VALUE));
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assert.assertFalse(processUnit.equals(other));
	}
	
	@Test
	public void equalsTorrentNullTest() throws Exception{
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setScrape(new ScrapeResult(Long.MAX_VALUE,Long.MAX_VALUE));
		processUnit.setTitle("TITLE");
		processUnit.setTorrent(null);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setScrape(new ScrapeResult(Long.MAX_VALUE,Long.MAX_VALUE));
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assert.assertFalse(processUnit.equals(other));
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
		other.setScrape(new ScrapeResult(Long.MAX_VALUE,Long.MAX_VALUE));
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assert.assertFalse(processUnit.equals(other));
	}
}
