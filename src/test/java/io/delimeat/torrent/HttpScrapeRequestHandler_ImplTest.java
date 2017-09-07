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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.TorrentNotFoundException;
import io.delimeat.torrent.exception.TorrentResponseBodyException;
import io.delimeat.torrent.exception.TorrentResponseException;
import io.delimeat.torrent.exception.TorrentTimeoutException;
import io.delimeat.torrent.exception.UnhandledScrapeException;
import io.delimeat.util.DelimeatUtils;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.mockwebserver.SocketPolicy;
import okio.Buffer;

public class HttpScrapeRequestHandler_ImplTest {
	
	private static final int PORT = 8089;
	private static MockWebServer mockedServer = new MockWebServer();
	
	private HttpScrapeRequestHandler_Impl scraper;

	@BeforeClass
	public static void beforeClass() throws IOException{
		mockedServer.start(PORT);
	}
	
	@AfterClass
	public static void tearDown() throws IOException{
		mockedServer.shutdown();
	}
	
	@Before
	public void setUp(){
		scraper = new HttpScrapeRequestHandler_Impl();
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
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);
		URL scrapeURL = scraper.generateScrapeURL(announceURI, infoHash);
		Assert.assertEquals("http://test/scrape?info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE", scrapeURL.toString());
	}

	@Test
	public void validGenerateScrapeURITest() throws Exception{
		URI announceURI = new URI("http://test/scrape");
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);
		URL scrapeURL = scraper.generateScrapeURL(announceURI, infoHash);
		Assert.assertEquals("http://test/scrape?info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE", scrapeURL.toString());
	}

	@Test
	public void validGenerateScrapeURIIncludesInfoHashTest() throws Exception{
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);
		URI announceURI = new URI("http://test/scrape?info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE");
		URL scrapeURL = scraper.generateScrapeURL(announceURI, infoHash);
		Assert.assertEquals("http://test/scrape?info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE", scrapeURL.toString());
	}

	@Test
	public void validGenerateScrapeURIIncludesQueryTest() throws Exception{
		URI announceURI = new URI("http://test/scrape?test=true");
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);
		URL scrapeURL = scraper.generateScrapeURL(announceURI, infoHash);
		Assert.assertEquals("http://test/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE", scrapeURL.toString());
	}
	
	@Test
	public void scrapeTest() throws URISyntaxException, Exception{
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);
	
		Buffer buffer = new Buffer();
		buffer.write("d5:filesd20:".getBytes())
			.write(infoHash.getBytes())
			.write("d8:completei5e10:downloadedi50e10:incompletei10eeee".getBytes());
		
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(200)
			    .addHeader("Content-Type", "text/plain")
			    .setBody(buffer);
		
		mockedServer.enqueue(mockResponse);
		
		ScrapeResult result = scraper.scrape(new URI("http://localhost:8089/announce?test=true"), infoHash);

		RecordedRequest request = mockedServer.takeRequest();
		Assert.assertEquals("/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE", request.getPath());
		Assert.assertEquals("text/plain", request.getHeader("Accept"));
		Assert.assertEquals("true", request.getRequestUrl().queryParameter("test"));
		Assert.assertEquals(5, result.getSeeders());
		Assert.assertEquals(10, result.getLeechers());

	}
	
	@Test
	public void scrapeTimeoutExceptionTest() throws URISyntaxException, Exception{
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);
		
		MockResponse mockResponse = new MockResponse()
			    .setSocketPolicy(SocketPolicy.NO_RESPONSE);
		
		mockedServer.enqueue(mockResponse);
				
		try{
			scraper.scrape(new URI("http://localhost:8089/announce?test=true"), infoHash);
		}catch(TorrentTimeoutException ex){
			RecordedRequest request = mockedServer.takeRequest();
			Assert.assertEquals("/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE", request.getPath());
			Assert.assertEquals("text/plain", request.getHeader("Accept"));
			return;
		}
		Assert.fail();
	}
	
	@Test
	public void scrapeNotFoundExceptionTest() throws URISyntaxException, Exception{
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);
		
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(404)
			    .addHeader("Content-Type", "text/plain");
		
		mockedServer.enqueue(mockResponse);
		
		try{
			scraper.scrape(new URI("http://localhost:8089/announce?test=true"), infoHash);
		}catch(TorrentNotFoundException ex){
			RecordedRequest request = mockedServer.takeRequest();
			Assert.assertEquals("/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE", request.getPath());
			Assert.assertEquals("text/plain", request.getHeader("Accept"));
			return;
		}
		Assert.fail();
	}
	
	@Test
	public void scrapeResponseExceptionTest() throws URISyntaxException, Exception{
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);
		
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(500)
			    .addHeader("Content-Type", "text/plain");
		
		mockedServer.enqueue(mockResponse);
		
		try{
			scraper.scrape(new URI("http://localhost:8089/announce?test=true"), infoHash);
		}catch(TorrentResponseException ex){
			RecordedRequest request = mockedServer.takeRequest();
			Assert.assertEquals("/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE", request.getPath());
			Assert.assertEquals("text/plain", request.getHeader("Accept"));
			Assert.assertEquals("HTTP response code 500 with message \"Server Error\" for url http://localhost:8089/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE", ex.getMessage());
			return;
		}
		Assert.fail();
	}
	
	@Test
	public void scrapeResponseBodyExceptionTest() throws URISyntaxException, Exception{
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);
		
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(200)
			    .addHeader("Content-Type", "text/plain")
			    .setBody("JIBBERISH");
		
		mockedServer.enqueue(mockResponse);
		
		try{
			scraper.scrape(new URI("http://localhost:8089/announce?test=true"), infoHash);
		}catch(TorrentResponseBodyException ex){
			RecordedRequest request = mockedServer.takeRequest();
			Assert.assertEquals("/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE", request.getPath());
			Assert.assertEquals("text/plain", request.getHeader("Accept"));
			Assert.assertEquals("Unable to parse response for url http://localhost:8089/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE \nJIBBERISH", ex.getMessage());
			return;
		}
		Assert.fail();
	}

	@Test(expected=TorrentException.class)
	public void scrapeExceptionTest() throws URISyntaxException, Exception{
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);

		scraper.scrape(new URI("http://localhost/announce?test=true"), infoHash);
	}

	@Test(expected=TorrentException.class)
	public void scrapeNotHTTPTest() throws URISyntaxException, Exception{
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		scraper.scrape(new URI("udp://test.com/announce"), infoHash);
	}
}
