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

import java.net.URI;
import java.net.URL;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.delimeat.torrent.entity.InfoHash;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.util.DelimeatUtils;

public class HttpScrapeRequestHandler_ImplTest {

	private HttpScrapeRequestHandler_Impl scraper;

	@BeforeEach
	public void setUp() {
		scraper = new HttpScrapeRequestHandler_Impl();
	}
	
	@Test
	public void validGenerateScrapeURIAnnounceTest() throws Exception {
		URI announceURI = new URI("http://test/announce");
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);
		URL scrapeURL = scraper.generateScrapeURL(announceURI, infoHash);
		Assertions.assertEquals("http://test/scrape?info_hash=%60%14%92%E0T%F9T%0E%B0%12%9C5%DE%B3%85%BA%A2%FA%F0%FE", scrapeURL.toString());
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
	public void invalidGenerateScrapeURLTest() throws Exception {
		URI announceURI = new URI("http://test/fail");
		byte[] sha1Bytes = DelimeatUtils.hashBytes("INFO_HASH".getBytes(), "SHA-1");
		InfoHash infoHash = new InfoHash(sha1Bytes);
		TorrentException ex = Assertions.assertThrows(TorrentException.class, ()->{ 
			scraper.generateScrapeURL(announceURI, infoHash);
		});
		
		Assertions.assertEquals("Unable to scrape URI: http://test/fail", ex.getMessage());
		
	}
}
