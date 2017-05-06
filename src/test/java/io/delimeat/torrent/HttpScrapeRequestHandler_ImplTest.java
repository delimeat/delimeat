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
package io.delimeat.torrent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.hash.Hashing;

import io.delimeat.torrent.HttpScrapeRequestHandler_Impl;
import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.UnhandledScrapeException;
import io.delimeat.util.UrlHandler;

public class HttpScrapeRequestHandler_ImplTest {

	private HttpScrapeRequestHandler_Impl scraper;
	
	@Before
	public void setUp(){
		scraper = new HttpScrapeRequestHandler_Impl();
	}
	
	@Test
	public void UrlHandlerTest(){
		Assert.assertNull(scraper.getUrlHandler());
		UrlHandler mockedHandler= Mockito.mock(UrlHandler.class);
		scraper.setUrlHandler(mockedHandler);
		Assert.assertEquals(mockedHandler, scraper.getUrlHandler());
	}
	
	@Test
	public void supportedProtocolTest(){
		Assert.assertEquals(Arrays.asList("HTTP","HTTPS"),scraper.getSupportedProtocols());
	}
	
	@Test(expected=UnhandledScrapeException.class)
	public void invalidGenerateScrapeURITest() throws Exception{
		URI announceURI = new URI("http://test.com/a");
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		scraper.generateScrapeURL(announceURI, infoHash);
	}
	
	@Test
	public void validGenerateScrapeURIAnnounceTest() throws Exception{
		URI announceURI = new URI("http://test/announce");
		byte[] sha1Bytes = Hashing.sha1().hashBytes("INFO_HASH".getBytes()).asBytes();
		InfoHash infoHash = new InfoHash(sha1Bytes);
     	URL scrapeURL = scraper.generateScrapeURL(announceURI, infoHash);
		Assert.assertEquals("http://test/scrape?info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE", scrapeURL.toString());
	}
	
	@Test
	public void validGenerateScrapeURITest() throws Exception{
		URI announceURI = new URI("http://test/scrape");
		byte[] sha1Bytes = Hashing.sha1().hashBytes("INFO_HASH".getBytes()).asBytes();
		InfoHash infoHash = new InfoHash(sha1Bytes);
		URL scrapeURL = scraper.generateScrapeURL(announceURI, infoHash);
		Assert.assertEquals("http://test/scrape?info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE", scrapeURL.toString());
	}
	
	@Test
	public void validGenerateScrapeURIIncludesInfoHashTest() throws Exception{
		byte[] sha1Bytes = Hashing.sha1().hashBytes("INFO_HASH".getBytes()).asBytes();
		InfoHash infoHash = new InfoHash(sha1Bytes);
    	URI announceURI = new URI("http://test/scrape?info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE");
		URL scrapeURL = scraper.generateScrapeURL(announceURI, infoHash);
		Assert.assertEquals("http://test/scrape?info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE", scrapeURL.toString());
	}
	
	@Test
	public void validGenerateScrapeURIIncludesQueryTest() throws Exception{
		URI announceURI = new URI("http://test/scrape?test=true");
		byte[] sha1Bytes = Hashing.sha1().hashBytes("INFO_HASH".getBytes()).asBytes();
		InfoHash infoHash = new InfoHash(sha1Bytes);
		URL scrapeURL = scraper.generateScrapeURL(announceURI, infoHash);
		Assert.assertEquals("http://test/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE", scrapeURL.toString());
	}
	
	@Test
	public void scrapeTest() throws URISyntaxException, Exception{
		UrlHandler mockedHandler= Mockito.mock(UrlHandler.class);
		byte[] sha1Bytes = Hashing.sha1().hashBytes("INFO_HASH".getBytes()).asBytes();
		InfoHash infoHash = new InfoHash(sha1Bytes);

     	ByteArrayOutputStream baos = new ByteArrayOutputStream();
     	baos.write("d5:filesd20:".getBytes());
     	baos.write(infoHash.getBytes());
     	baos.write("d8:completei5e10:downloadedi50e10:incompletei10eeee".getBytes());
      byte[] scrapeResult = baos.toByteArray();

      HttpURLConnection mockedConnection = Mockito.mock(HttpURLConnection.class);
      Mockito.when(mockedConnection.getResponseCode()).thenReturn(200);
      Mockito.when(mockedConnection.getInputStream()).thenReturn(new ByteArrayInputStream(scrapeResult));
		Mockito.when(mockedHandler.openUrlConnection(Mockito.any(URL.class))).thenReturn(mockedConnection);
     	Mockito.when(mockedHandler.openInput(Mockito.any(URLConnection.class))).thenReturn(new ByteArrayInputStream(scrapeResult));
		
		scraper.setUrlHandler(mockedHandler);

		ScrapeResult result = scraper.doScrape(new URI("http://test/announce?test=true"), infoHash);
		
		Assert.assertEquals(5, result.getSeeders());
		Assert.assertEquals(10, result.getLeechers());
	}
  
	@Test(expected=TorrentException.class)
	public void scrapeBencodeExceptionTest() throws URISyntaxException, Exception{
		UrlHandler mockedHandler= Mockito.mock(UrlHandler.class);
		byte[] sha1Bytes = Hashing.sha1().hashBytes("INFO_HASH".getBytes()).asBytes();
		InfoHash infoHash = new InfoHash(sha1Bytes);

      byte[] scrapeResult = "x".getBytes();

      HttpURLConnection mockedConnection = Mockito.mock(HttpURLConnection.class);
      Mockito.when(mockedConnection.getResponseCode()).thenReturn(200);
      Mockito.when(mockedConnection.getInputStream()).thenReturn(new ByteArrayInputStream(scrapeResult));
		Mockito.when(mockedHandler.openUrlConnection(Mockito.any(URL.class))).thenReturn(mockedConnection);
     	Mockito.when(mockedHandler.openInput(Mockito.any(URLConnection.class))).thenReturn(new ByteArrayInputStream(scrapeResult));	
		scraper.setUrlHandler(mockedHandler);

		scraper.doScrape(new URI("http://test/announce?test=true"), infoHash);
	}
  
	@Test(expected=TorrentException.class)
	public void scrapeIOExceptionTest() throws URISyntaxException, Exception{
		byte[] sha1Bytes = Hashing.sha1().hashBytes("INFO_HASH".getBytes()).asBytes();
		InfoHash infoHash = new InfoHash(sha1Bytes);

     	UrlHandler mockedHandler= Mockito.mock(UrlHandler.class);
		Mockito.when(mockedHandler.openUrlConnection(Mockito.any(URL.class))).thenThrow(new IOException());
		scraper.setUrlHandler(mockedHandler);

		scraper.doScrape(new URI("http://test/announce?test=true"), infoHash);
	}
  
	@Test(expected=TorrentException.class)
	public void scrapeNotHTTPTest() throws URISyntaxException, Exception{
     InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		scraper.doScrape(new URI("udp://test.com"), infoHash);
	}
  
	@Test(expected=TorrentException.class)
	public void scrapeNotOKTest() throws Exception{
		UrlHandler mockedHandler= Mockito.mock(UrlHandler.class);
      HttpURLConnection mockedConnection = Mockito.mock(HttpURLConnection.class);
      Mockito.when(mockedConnection.getResponseCode()).thenReturn(204);
		Mockito.when(mockedHandler.openUrlConnection(Mockito.any(URL.class))).thenReturn(mockedConnection);
		
		scraper.setUrlHandler(mockedHandler);
      InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());

		scraper.doScrape(new URI("http://test/announce?test=true"), infoHash);
	}
}
