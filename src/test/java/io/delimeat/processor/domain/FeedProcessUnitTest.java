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
package io.delimeat.processor.domain;

import java.net.URI;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.Torrent;

public class FeedProcessUnitTest {

	private FeedProcessUnit processUnit;
	
	@Before
	public void setUp(){
		processUnit = new FeedProcessUnit();
	}
	
	@Test
	public void contentLengthTest(){
		Assert.assertEquals(0, processUnit.getContentLength());
		processUnit.setContentLength(Long.MAX_VALUE);
		Assert.assertEquals(Long.MAX_VALUE, processUnit.getContentLength());
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
	public void leechersTest(){
		Assert.assertEquals(0, processUnit.getLeechers());
		processUnit.setLeechers(Long.MAX_VALUE);
		Assert.assertEquals(Long.MAX_VALUE, processUnit.getLeechers());
	}
	
	@Test
	public void seedersTest(){
		Assert.assertEquals(0, processUnit.getSeeders());
		processUnit.setSeeders(Long.MAX_VALUE);
		Assert.assertEquals(Long.MAX_VALUE, processUnit.getSeeders());
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
		processUnit.setContentLength(Long.MAX_VALUE);
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setLeechers(Long.MAX_VALUE);
		processUnit.setSeeders(Long.MAX_VALUE);
		processUnit.setTitle("TITLE");
		processUnit.setTorrent(new Torrent());
		Assert.assertEquals(545030552, processUnit.hashCode());
	}
	
	@Test
	public void toStringTest(){
		Assert.assertEquals("FeedProcessUnit [contentLength=0, rejections=[], leechers=0, seeders=0, ]", processUnit.toString());
	}
	
	@Test
	public void equalsTest() throws Exception{
		processUnit.setContentLength(Long.MAX_VALUE);
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setLeechers(Long.MAX_VALUE);
		processUnit.setSeeders(Long.MAX_VALUE);
		processUnit.setTitle("TITLE");
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setContentLength(Long.MAX_VALUE);
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setLeechers(Long.MAX_VALUE);
		other.setSeeders(Long.MAX_VALUE);
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
	public void equalsContentLengthTest() throws Exception{
		processUnit.setContentLength(Long.MIN_VALUE);
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setLeechers(Long.MAX_VALUE);
		processUnit.setSeeders(Long.MAX_VALUE);
		processUnit.setTitle("TITLE");
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setContentLength(Long.MAX_VALUE);
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setLeechers(Long.MAX_VALUE);
		other.setSeeders(Long.MAX_VALUE);
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assert.assertFalse(processUnit.equals(other));
	}

	@Test
	public void equalsDownloadUriTest() throws Exception{
		processUnit.setContentLength(Long.MAX_VALUE);
		processUnit.setDownloadUri(new URI("http://different.io"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setLeechers(Long.MAX_VALUE);
		processUnit.setSeeders(Long.MAX_VALUE);
		processUnit.setTitle("TITLE");
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setContentLength(Long.MAX_VALUE);
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setLeechers(Long.MAX_VALUE);
		other.setSeeders(Long.MAX_VALUE);
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assert.assertFalse(processUnit.equals(other));
	}
	
	@Test
	public void equalsInfoHashTest() throws Exception{
		processUnit.setContentLength(Long.MAX_VALUE);
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("other".getBytes()));
		processUnit.setLeechers(Long.MAX_VALUE);
		processUnit.setSeeders(Long.MAX_VALUE);
		processUnit.setTitle("TITLE");
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setContentLength(Long.MAX_VALUE);
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setLeechers(Long.MAX_VALUE);
		other.setSeeders(Long.MAX_VALUE);
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assert.assertFalse(processUnit.equals(other));
	}
	
	@Test
	public void equalsLeechersTest() throws Exception{
		processUnit.setContentLength(Long.MAX_VALUE);
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setLeechers(Long.MIN_VALUE);
		processUnit.setSeeders(Long.MAX_VALUE);
		processUnit.setTitle("TITLE");
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setContentLength(Long.MAX_VALUE);
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setLeechers(Long.MAX_VALUE);
		other.setSeeders(Long.MAX_VALUE);
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assert.assertFalse(processUnit.equals(other));
	}
	
	@Test
	public void equalsSeedersTest() throws Exception{
		processUnit.setContentLength(Long.MAX_VALUE);
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setLeechers(Long.MAX_VALUE);
		processUnit.setSeeders(Long.MIN_VALUE);
		processUnit.setTitle("TITLE");
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setContentLength(Long.MAX_VALUE);
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setLeechers(Long.MAX_VALUE);
		other.setSeeders(Long.MAX_VALUE);
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assert.assertFalse(processUnit.equals(other));
	}
	
	@Test
	public void equalsTitleTest() throws Exception{
		processUnit.setContentLength(Long.MAX_VALUE);
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setLeechers(Long.MAX_VALUE);
		processUnit.setSeeders(Long.MAX_VALUE);
		processUnit.setTitle("OTHER");
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setContentLength(Long.MAX_VALUE);
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setLeechers(Long.MAX_VALUE);
		other.setSeeders(Long.MAX_VALUE);
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assert.assertFalse(processUnit.equals(other));
	}

	@Test
	public void equalsTorrentTest() throws Exception{
		processUnit.setContentLength(Long.MAX_VALUE);
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setLeechers(Long.MAX_VALUE);
		processUnit.setSeeders(Long.MAX_VALUE);
		processUnit.setTitle("TITLE");
		Torrent torrent = new Torrent();
		torrent.setTracker("TRACKER");
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setContentLength(Long.MAX_VALUE);
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setLeechers(Long.MAX_VALUE);
		other.setSeeders(Long.MAX_VALUE);
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assert.assertFalse(processUnit.equals(other));
	}
	
	@Test
	public void equalsDownloadUriNullTest() throws Exception{
		processUnit.setContentLength(Long.MAX_VALUE);
		processUnit.setDownloadUri(null);
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setLeechers(Long.MAX_VALUE);
		processUnit.setSeeders(Long.MAX_VALUE);
		processUnit.setTitle("TITLE");
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setContentLength(Long.MAX_VALUE);
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setLeechers(Long.MAX_VALUE);
		other.setSeeders(Long.MAX_VALUE);
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assert.assertFalse(processUnit.equals(other));
	}
	
	@Test
	public void equalsInfoHashNullTest() throws Exception{
		processUnit.setContentLength(Long.MAX_VALUE);
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(null);
		processUnit.setLeechers(Long.MAX_VALUE);
		processUnit.setSeeders(Long.MAX_VALUE);
		processUnit.setTitle("TITLE");
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setContentLength(Long.MAX_VALUE);
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setLeechers(Long.MAX_VALUE);
		other.setSeeders(Long.MAX_VALUE);
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assert.assertFalse(processUnit.equals(other));
	}	
	
	@Test
	public void equalsTitleNullTest() throws Exception{
		processUnit.setContentLength(Long.MAX_VALUE);
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setLeechers(Long.MAX_VALUE);
		processUnit.setSeeders(Long.MAX_VALUE);
		processUnit.setTitle(null);
		Torrent torrent = new Torrent();
		processUnit.setTorrent(torrent);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setContentLength(Long.MAX_VALUE);
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setLeechers(Long.MAX_VALUE);
		other.setSeeders(Long.MAX_VALUE);
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assert.assertFalse(processUnit.equals(other));
	}
	
	@Test
	public void equalsTorrentNullTest() throws Exception{
		processUnit.setContentLength(Long.MAX_VALUE);
		processUnit.setDownloadUri(new URI("udp:test.com"));
		processUnit.setInfoHash(new InfoHash("bytes".getBytes()));
		processUnit.setLeechers(Long.MAX_VALUE);
		processUnit.setSeeders(Long.MAX_VALUE);
		processUnit.setTitle("TITLE");
		processUnit.setTorrent(null);
		
		FeedProcessUnit other = new FeedProcessUnit();
		other.setContentLength(Long.MAX_VALUE);
		other.setDownloadUri(new URI("udp:test.com"));
		other.setInfoHash(new InfoHash("bytes".getBytes()));
		other.setLeechers(Long.MAX_VALUE);
		other.setSeeders(Long.MAX_VALUE);
		other.setTitle("TITLE");
		Torrent otherTorrent = new Torrent();
		other.setTorrent(otherTorrent);
		
		Assert.assertFalse(processUnit.equals(other));
	}
}
