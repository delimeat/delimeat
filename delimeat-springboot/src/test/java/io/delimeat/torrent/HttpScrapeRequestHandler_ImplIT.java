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
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.WireMockServer;

import io.delimeat.torrent.entity.InfoHash;
import io.delimeat.torrent.entity.ScrapeResult;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.TorrentNotFoundException;
import io.delimeat.util.DelimeatUtils;

public class HttpScrapeRequestHandler_ImplIT {

	private static WireMockServer server = new WireMockServer(8089);

	private HttpScrapeRequestHandler_Impl scraper;

	@BeforeAll
	public static void setUpClass() {
		server.start();
	}
	
	@AfterAll
	public static void tearDownClass() {
		server.stop();
	}
	
	@BeforeEach
	public void setUp() {
		scraper = new HttpScrapeRequestHandler_Impl();
	}

	@Test
	public void scrapeTest() throws URISyntaxException, Exception {
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);

		ByteBuffer buffer = ByteBuffer.allocate(1024)
				.put("d5:filesd20:".getBytes())
				.put(infoHash.getBytes())
				.put("d8:completei5e10:downloadedi50e10:incompletei10eeee".getBytes());
		
		server.stubFor(get(urlEqualTo("/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE"))
				.withHeader("Accept", matching("text/plain"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "text/plain")
						.withBody(buffer.array())));
		
		ScrapeResult result = scraper.scrape(new URI("http://localhost:8089/announce?test=true"), infoHash);

		Assertions.assertEquals(5, result.getSeeders());
		Assertions.assertEquals(10, result.getLeechers());
	}

	@Test
	public void scrapeNotFoundExceptionTest() throws URISyntaxException, Exception {
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);
		
		server.stubFor(get(urlEqualTo("/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE"))
				.withHeader("Accept", matching("text/plain"))
				.willReturn(aResponse()
						.withStatus(404)));
	
		TorrentNotFoundException ex = Assertions.assertThrows(TorrentNotFoundException.class, () -> {
			scraper.scrape(new URI("http://localhost:8089/announce?test=true"), infoHash);
		});

		Assertions.assertEquals("HTTP response code 404 with message \"HTTP 404 Not Found\" for url http://localhost:8089/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE", ex.getMessage());
	}

	@Test
	public void scrapeResponseExceptionTest() throws URISyntaxException, Exception {
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);

		server.stubFor(get(urlEqualTo("/scrape?test=true&info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE"))
				.withHeader("Accept", matching("text/plain"))
				.willReturn(aResponse()
						.withStatus(500)));

		TorrentException ex = Assertions.assertThrows(TorrentException.class, () -> {
			scraper.scrape(new URI("http://localhost:8089/announce?test=true"), infoHash);
		});

		Assertions.assertEquals("javax.ws.rs.InternalServerErrorException: HTTP 500 Server Error", ex.getMessage());
	}
}
