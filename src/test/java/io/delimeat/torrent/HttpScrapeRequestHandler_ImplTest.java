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

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.hash.Hashing;

import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.TorrentNotFoundException;
import io.delimeat.torrent.exception.TorrentResponseBodyException;
import io.delimeat.torrent.exception.TorrentResponseException;
import io.delimeat.torrent.exception.TorrentTimeoutException;
import io.delimeat.torrent.exception.UnhandledScrapeException;

public class HttpScrapeRequestHandler_ImplTest {

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);
	
	private HttpScrapeRequestHandler_Impl scraper;

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

		byte[] sha1Bytes = Hashing.sha1().hashBytes("INFO_HASH".getBytes()).asBytes();
		InfoHash infoHash = new InfoHash(sha1Bytes);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write("d5:filesd20:".getBytes());
		baos.write(infoHash.getBytes());
		baos.write("d8:completei5e10:downloadedi50e10:incompletei10eeee".getBytes());
		byte[] scrapeResult = baos.toByteArray();
     
		stubFor(get(urlPathEqualTo("/scrape"))
				.withQueryParam("test", equalTo("true"))
				//.withQueryParam("info_hash", equalTo("%60%14%C2%92%C3%A0T%C3%B9T%0E%C2%B0%12%C2%9C5%C3%9E%C2%B3%C2%85%C2%BA%C2%A2%C3%BA%C3%B0%C3%BE"))
				.willReturn(aResponse()
							.withStatus(200)
							.withBody(scrapeResult)
							));

		ScrapeResult result = scraper.doScrape(new URI("http://localhost:8089/announce?test=true"), infoHash);

		Assert.assertEquals(5, result.getSeeders());
		Assert.assertEquals(10, result.getLeechers());
	}
	
	@Test(expected=TorrentTimeoutException.class)
	public void scrapeTimeoutExceptionTest() throws URISyntaxException, Exception{

		byte[] sha1Bytes = Hashing.sha1().hashBytes("INFO_HASH".getBytes()).asBytes();
		InfoHash infoHash = new InfoHash(sha1Bytes);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write("d5:filesd20:".getBytes());
		baos.write(infoHash.getBytes());
		baos.write("d8:completei5e10:downloadedi50e10:incompletei10eeee".getBytes());
     
		stubFor(get(urlPathEqualTo("/scrape"))
				.withQueryParam("test", equalTo("true"))
				//.withQueryParam("info_hash", equalTo("%60%14%C2%92%C3%A0T%C3%B9T%0E%C2%B0%12%C2%9C5%C3%9E%C2%B3%C2%85%C2%BA%C2%A2%C3%BA%C3%B0%C3%BE"))
				.willReturn(aResponse()
							.withStatus(200)
							.withFixedDelay(2000)
							));

		scraper.doScrape(new URI("http://localhost:8089/announce?test=true"), infoHash);
		Assert.fail();
	}
	
	@Test(expected=TorrentNotFoundException.class)
	public void scrapeNotFoundTest() throws Exception{

		byte[] sha1Bytes = Hashing.sha1().hashBytes("INFO_HASH".getBytes()).asBytes();
		InfoHash infoHash = new InfoHash(sha1Bytes);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write("d5:filesd20:".getBytes());
		baos.write(infoHash.getBytes());
		baos.write("d8:completei5e10:downloadedi50e10:incompletei10eeee".getBytes());
		byte[] scrapeResult = baos.toByteArray();
     
		stubFor(get(urlPathEqualTo("/scrape"))
				.withQueryParam("test", equalTo("true"))
				//.withQueryParam("info_hash", equalTo("%60%14%C2%92%C3%A0T%C3%B9T%0E%C2%B0%12%C2%9C5%C3%9E%C2%B3%C2%85%C2%BA%C2%A2%C3%BA%C3%B0%C3%BE"))
				.willReturn(aResponse()
							.withStatus(404)
							.withBody(scrapeResult)
							));

		scraper.doScrape(new URI("http://localhost:8089/announce?test=true"), infoHash);
	}
	
	@Test(expected=TorrentResponseException.class)
	public void scrapeResponseExceptionTest() throws URISyntaxException, Exception{

		byte[] sha1Bytes = Hashing.sha1().hashBytes("INFO_HASH".getBytes()).asBytes();
		InfoHash infoHash = new InfoHash(sha1Bytes);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write("d5:filesd20:".getBytes());
		baos.write(infoHash.getBytes());
		baos.write("d8:completei5e10:downloadedi50e10:incompletei10eeee".getBytes());
     
		stubFor(get(urlPathEqualTo("/scrape"))
				.withQueryParam("test", equalTo("true"))
				//.withQueryParam("info_hash", equalTo("%60%14%C2%92%C3%A0T%C3%B9T%0E%C2%B0%12%C2%9C5%C3%9E%C2%B3%C2%85%C2%BA%C2%A2%C3%BA%C3%B0%C3%BE"))
				.willReturn(aResponse()
							.withStatus(500)));

		scraper.doScrape(new URI("http://localhost:8089/announce?test=true"), infoHash);
		Assert.fail();
	}

	@Test(expected=TorrentResponseBodyException.class)
	public void scrapeResponseBodyExceptionTest() throws URISyntaxException, Exception{
		byte[] sha1Bytes = Hashing.sha1().hashBytes("INFO_HASH".getBytes()).asBytes();
		InfoHash infoHash = new InfoHash(sha1Bytes);

		byte[] scrapeResult = "x".getBytes();

		stubFor(get(urlPathEqualTo("/scrape"))
					.willReturn(aResponse()
						.withStatus(200)
						.withBody(scrapeResult)));

		scraper.doScrape(new URI("http://localhost:8089/announce?test=true"), infoHash);
	}

	@Test(expected=TorrentException.class)
	public void scrapeExceptionTest() throws URISyntaxException, Exception{
		byte[] sha1Bytes = Hashing.sha1().hashBytes("INFO_HASH".getBytes()).asBytes();
		InfoHash infoHash = new InfoHash(sha1Bytes);

		scraper.doScrape(new URI("http://localhost:8089/announce?test=true"), infoHash);
	}

	@Test(expected=TorrentException.class)
	public void scrapeNotHTTPTest() throws URISyntaxException, Exception{
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		scraper.doScrape(new URI("udp://test.com/announce"), infoHash);
	}
}
