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
import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.delimeat.torrent.entity.InfoHash;
import io.delimeat.torrent.entity.ScrapeResult;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.util.DelimeatUtils;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;

public class MagnetScrapeRequestHandler_ImplTest {

	private static final int PORT = 8089;
	private static MockWebServer mockedServer = new MockWebServer();

	@BeforeAll
	public static void beforeClass() throws IOException {
		mockedServer.start(PORT);
	}

	@AfterAll
	public static void tearDown() throws IOException {
		mockedServer.shutdown();
	}

	private MagnetScrapeRequestHandler_Impl scraper;

	@BeforeEach
	public void setUp() {
		scraper = new MagnetScrapeRequestHandler_Impl();
	}

	@Test
	public void supportedProtocolTest() {
		Assertions.assertEquals(Arrays.asList("MAGNET"), scraper.getSupportedProtocols());
	}

	@Test
	public void defaultTrackerTest() throws Exception {
		Assertions.assertNull(scraper.getDefaultTracker());
		scraper.setDefaultTracker(new URI("TRACKER"));
		Assertions.assertEquals(new URI("TRACKER"), scraper.getDefaultTracker());
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

		scraper.setDefaultTracker(new URI("http://localhost:8089/announce?test=true"));

		ScrapeResult result = scraper.scrape(new URI("magnet:?xt=urn:btih:"), infoHash);

		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE",
				request.getPath());
		Assertions.assertEquals("text/plain", request.getHeader("Accept"));
		Assertions.assertEquals("true", request.getRequestUrl().queryParameter("test"));
		Assertions.assertEquals(5, result.getSeeders());
		Assertions.assertEquals(10, result.getLeechers());

	}

	@Test
	public void scrapeInvalidProtocolTest() throws URISyntaxException, Exception {
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		Exception ex = Assertions.assertThrows(TorrentException.class, () -> {
			scraper.scrape(new URI("udp://test.com/announce"), infoHash);
		});

		Assertions.assertEquals("Unsupported protocol udp", ex.getMessage());
	}
}
