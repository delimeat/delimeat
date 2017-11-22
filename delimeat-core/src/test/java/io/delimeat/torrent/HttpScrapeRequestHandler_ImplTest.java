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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.delimeat.torrent.entity.InfoHash;
import io.delimeat.torrent.entity.ScrapeResult;
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

	@BeforeAll
	public static void beforeClass() throws IOException {
		mockedServer.start(PORT);
	}

	@AfterAll
	public static void tearDown() throws IOException {
		mockedServer.shutdown();
	}

	@BeforeEach
	public void setUp() {
		scraper = new HttpScrapeRequestHandler_Impl();
	}

	@Test
	public void supportedProtocolTest() {
		Assertions.assertEquals(Arrays.asList("HTTP", "HTTPS"), scraper.getSupportedProtocols());
	}

	@Test
	public void invalidGenerateScrapeURITest() throws Exception {
		URI announceURI = new URI("http://test.com/a");
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		UnhandledScrapeException ex = Assertions.assertThrows(UnhandledScrapeException.class, () -> {
			scraper.generateScrapeURL(announceURI, infoHash);
		});

		Assertions.assertEquals("Unable to scrape URI: http://test.com/a", ex.getMessage());
	}

	@Test
	public void validGenerateScrapeURIAnnounceTest() throws Exception {
		URI announceURI = new URI("http://test/announce");
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);
		URL scrapeURL = scraper.generateScrapeURL(announceURI, infoHash);
		Assertions.assertEquals("http://test/scrape?info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE",
				scrapeURL.toString());
	}

	@Test
	public void validGenerateScrapeURITest() throws Exception {
		URI announceURI = new URI("http://test/scrape");
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);
		URL scrapeURL = scraper.generateScrapeURL(announceURI, infoHash);
		Assertions.assertEquals("http://test/scrape?info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE",
				scrapeURL.toString());
	}

	@Test
	public void validGenerateScrapeURIIncludesInfoHashTest() throws Exception {
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);
		URI announceURI = new URI(
				"http://test/scrape?info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE");
		URL scrapeURL = scraper.generateScrapeURL(announceURI, infoHash);
		Assertions.assertEquals("http://test/scrape?info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE",
				scrapeURL.toString());
	}

	@Test
	public void validGenerateScrapeURIIncludesQueryTest() throws Exception {
		URI announceURI = new URI("http://test/scrape?test=true");
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);
		URL scrapeURL = scraper.generateScrapeURL(announceURI, infoHash);
		Assertions.assertEquals(
				"http://test/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE",
				scrapeURL.toString());
	}

	@Test
	public void scrapeTest() throws URISyntaxException, Exception {
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);

		Buffer buffer = new Buffer();
		buffer.write("d5:filesd20:".getBytes()).write(infoHash.getBytes())
				.write("d8:completei5e10:downloadedi50e10:incompletei10eeee".getBytes());

		MockResponse mockResponse = new MockResponse().setResponseCode(200).addHeader("Content-Type", "text/plain")
				.setBody(buffer);

		mockedServer.enqueue(mockResponse);

		ScrapeResult result = scraper.scrape(new URI("http://localhost:8089/announce?test=true"), infoHash);

		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE",
				request.getPath());
		Assertions.assertEquals("text/plain", request.getHeader("Accept"));
		Assertions.assertEquals("true", request.getRequestUrl().queryParameter("test"));
		Assertions.assertEquals(5, result.getSeeders());
		Assertions.assertEquals(10, result.getLeechers());

	}

	@Test
	public void scrapeTimeoutExceptionTest() throws URISyntaxException, Exception {
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);

		MockResponse mockResponse = new MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE);

		mockedServer.enqueue(mockResponse);

		TorrentTimeoutException ex = Assertions.assertThrows(TorrentTimeoutException.class, () -> {
			scraper.scrape(new URI("http://localhost:8089/announce?test=true"), infoHash);
		});

		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE",
				request.getPath());
		Assertions.assertEquals("text/plain", request.getHeader("Accept"));

		Assertions.assertEquals("Timeout for http://localhost:8089/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE", ex.getMessage());
	}

	@Test
	public void scrapeNotFoundExceptionTest() throws URISyntaxException, Exception {
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);

		MockResponse mockResponse = new MockResponse().setResponseCode(404).addHeader("Content-Type", "text/plain");

		mockedServer.enqueue(mockResponse);

		TorrentNotFoundException ex = Assertions.assertThrows(TorrentNotFoundException.class, () -> {
			scraper.scrape(new URI("http://localhost:8089/announce?test=true"), infoHash);
		});

		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE",
				request.getPath());
		Assertions.assertEquals("text/plain", request.getHeader("Accept"));

		Assertions.assertEquals("HTTP response code 404 with message \"Client Error\" for url http://localhost:8089/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE", ex.getMessage());
	}

	@Test
	public void scrapeResponseExceptionTest() throws URISyntaxException, Exception {
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);

		MockResponse mockResponse = new MockResponse().setResponseCode(500).addHeader("Content-Type", "text/plain");

		mockedServer.enqueue(mockResponse);

		TorrentResponseException ex = Assertions.assertThrows(TorrentResponseException.class, () -> {
			scraper.scrape(new URI("http://localhost:8089/announce?test=true"), infoHash);
		});

		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE",
				request.getPath());
		Assertions.assertEquals("text/plain", request.getHeader("Accept"));
		Assertions.assertEquals(
				"HTTP response code 500 with message \"Server Error\" for url http://localhost:8089/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE",
				ex.getMessage());

		Assertions.assertEquals("HTTP response code 500 with message \"Server Error\" for url http://localhost:8089/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE", ex.getMessage());
	}

	@Test
	public void scrapeResponseBodyExceptionTest() throws URISyntaxException, Exception {
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);

		MockResponse mockResponse = new MockResponse().setResponseCode(200).addHeader("Content-Type", "text/plain")
				.setBody("JIBBERISH");

		mockedServer.enqueue(mockResponse);

		TorrentResponseBodyException ex = Assertions.assertThrows(TorrentResponseBodyException.class, () -> {
			scraper.scrape(new URI("http://localhost:8089/announce?test=true"), infoHash);
		});

		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE",
				request.getPath());
		Assertions.assertEquals("text/plain", request.getHeader("Accept"));
		Assertions.assertEquals(
				"Unable to parse response for url http://localhost:8089/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE \nJIBBERISH",
				ex.getMessage());

		Assertions.assertEquals("Unable to parse response for url http://localhost:8089/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE \nJIBBERISH", ex.getMessage());
	}

	@Test
	public void scrapeExceptionTest() throws URISyntaxException, Exception {
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);

		TorrentException ex = Assertions.assertThrows(TorrentException.class, () -> {
			scraper.scrape(new URI("http://localhost/announce?test=true"), infoHash);
		});

		Assertions.assertEquals("Unnable to connect to http://localhost/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE", ex.getMessage());
	}

	@Test
	public void scrapeNotHTTPTest() throws URISyntaxException, Exception {
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());

		TorrentException ex = Assertions.assertThrows(TorrentException.class, () -> {
			scraper.scrape(new URI("udp://test.com/announce"), infoHash);
		});

		Assertions.assertEquals("Unsupported protocol udp", ex.getMessage());
	}
}
