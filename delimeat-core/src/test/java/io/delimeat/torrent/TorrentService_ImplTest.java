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
import java.util.Arrays;
import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.delimeat.config.entity.Config;
import io.delimeat.torrent.entity.InfoHash;
import io.delimeat.torrent.entity.ScrapeResult;
import io.delimeat.torrent.entity.Torrent;
import io.delimeat.torrent.entity.TorrentInfo;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.UnhandledScrapeException;

public class TorrentService_ImplTest {

	private TorrentService_Impl service;

	@BeforeEach
	public void setUp() {
		service = new TorrentService_Impl();
	}

	@Test
	public void readersTest() {
		Assertions.assertNotNull(service.getReaders());
		Assertions.assertEquals(0, service.getReaders().size());

		service.setReaders( new HashMap<>());

		Assertions.assertEquals(new HashMap<>(), service.getReaders());
	}

	@Test
	public void writerTest() {
		Assertions.assertNull(service.getWriter());
		TorrentWriter writer = Mockito.mock(TorrentWriter.class);
		service.setWriter(writer);
		Assertions.assertEquals(writer, service.getWriter());
	}

	@Test
	public void scrapeRequestHandlersTest() {
		Assertions.assertNotNull(service.getScrapeRequestHandlers());
		Assertions.assertEquals(0, service.getScrapeRequestHandlers().size());

		service.setScrapeRequestHandlers(new HashMap<>());

		Assertions.assertEquals(new HashMap<>(), service.getScrapeRequestHandlers());
	}

	@Test
	public void buildMagnetUriTest() throws Exception {

		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		URI uri = service.buildMagnetUri(infoHash);

		Assertions.assertEquals(new URI("magnet:?xt=urn:btih:494e464f5f48415348"), uri);
	}

	@Test
	public void readUriTest() throws Exception {
		URI uri = new URI("http://test.com");
		Torrent torrent = new Torrent();
		TorrentReader reader = Mockito.mock(TorrentReader.class);
		Mockito.when(reader.read(uri)).thenReturn(torrent);

		service.getReaders().put("HTTP", reader);

		Torrent result = service.read(uri);
		Assertions.assertEquals(torrent, result);

		Mockito.verify(reader).read(uri);
		Mockito.verifyNoMoreInteractions(reader);
	}

	@Test
	public void readUriUnsupportedSchemeTest() throws Exception {
		Exception ex = Assertions.assertThrows(TorrentException.class, () -> {
			service.read(new URI("udp://test.com"));
		});

		Assertions.assertEquals("Protocol UDP not supported for read", ex.getMessage());
	}

	@Test
	public void readInfoHashTest() throws Exception {
		Torrent torrent = new Torrent();
		TorrentReader reader = Mockito.mock(TorrentReader.class);
		Mockito.when(reader.read(new URI("magnet:?xt=urn:btih:494e464f5f48415348"))).thenReturn(torrent);

		service.getReaders().put("MAGNET", reader);

		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		Torrent result = service.read(infoHash);

		Assertions.assertEquals(torrent, result);

		Mockito.verify(reader).read(new URI("magnet:?xt=urn:btih:494e464f5f48415348"));
		Mockito.verifyNoMoreInteractions(reader);
	}

	@Test
	public void writeTest() throws Exception {
		String fileName = "fileName";
		Torrent torrent = new Torrent();
		torrent.setBytes("bytes".getBytes());
		Config config = new Config();

		TorrentWriter writer = Mockito.mock(TorrentWriter.class);
		service.setWriter(writer);

		service.write(fileName, torrent, config);

		Mockito.verify(writer).write(fileName, "bytes".getBytes(), config);
		Mockito.verifyNoMoreInteractions(writer);
	}

	@Test
	public void scrapeUnhandledTest() throws Exception {
		ScrapeRequestHandler scraper = Mockito.mock(ScrapeRequestHandler.class);
		service.getScrapeRequestHandlers().put("NOT_UDP", scraper);


		URI scrapeUri = new URI("udp://scrape.me:8080");
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());

		Exception ex = Assertions.assertThrows(UnhandledScrapeException.class, () -> {
			service.doScrape(scrapeUri, infoHash);
		});

		Assertions.assertEquals("Protocol UDP not supported for scrape", ex.getMessage());
		
		Mockito.verifyZeroInteractions(scraper);
	}

	@Test
	public void scrapeTest() throws Exception {
		ScrapeResult mockedResult = Mockito.mock(ScrapeResult.class);
		ScrapeRequestHandler scraper = Mockito.mock(ScrapeRequestHandler.class);
		Mockito.when(scraper.scrape(new URI("http://scrape.me.com"), new InfoHash("INFO_HASH".getBytes())))
				.thenReturn(mockedResult);

		service.getScrapeRequestHandlers().put("HTTP", scraper);

		URI uri = new URI("http://scrape.me.com");
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		Assertions.assertEquals(mockedResult, service.doScrape(uri, infoHash));
		
		Mockito.verify(scraper).scrape(new URI("http://scrape.me.com"), new InfoHash("INFO_HASH".getBytes()));
	}

	@Test
	public void scrapeTrackerTest() throws Exception {
		Torrent torrent = new Torrent();
		torrent.setTracker("http://tracker");
		TorrentInfo info = new TorrentInfo();
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		info.setInfoHash(infoHash);
		torrent.setInfo(info);

		ScrapeResult scrapeResult = new ScrapeResult(100, 200);
		ScrapeRequestHandler scraper = Mockito.mock(ScrapeRequestHandler.class);
		Mockito.when(scraper.scrape(new URI("http://tracker"), new InfoHash("INFO_HASH".getBytes())))
				.thenReturn(scrapeResult);

		service.getScrapeRequestHandlers().put("HTTP", scraper);

		ScrapeResult result = service.scrape(torrent);

		Assertions.assertEquals(scrapeResult, result);

		Mockito.verify(scraper).scrape(new URI("http://tracker"), new InfoHash("INFO_HASH".getBytes()));
		Mockito.verifyNoMoreInteractions(scraper);

	}

	@Test
	public void scrapeTrackersTest() throws Exception {
		Torrent torrent = new Torrent();
		torrent.setTrackers(Arrays.asList("http://tracker"));
		TorrentInfo info = new TorrentInfo();
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		info.setInfoHash(infoHash);
		torrent.setInfo(info);

		ScrapeResult scrapeResult = new ScrapeResult(100, 200);
		ScrapeRequestHandler scraper = Mockito.mock(ScrapeRequestHandler.class);
		Mockito.when(scraper.scrape(new URI("http://tracker"), new InfoHash("INFO_HASH".getBytes())))
				.thenReturn(scrapeResult);

		service.getScrapeRequestHandlers().put("HTTP", scraper);

		ScrapeResult result = service.scrape(torrent);

		Assertions.assertEquals(scrapeResult, result);

		Mockito.verify(scraper).scrape(new URI("http://tracker"), new InfoHash("INFO_HASH".getBytes()));
		Mockito.verifyNoMoreInteractions(scraper);
	}

	@Test
	public void scrapeTrackersExceptionTest() throws Exception {
		Torrent torrent = new Torrent();
		torrent.setTrackers(Arrays.asList("http://tracker", "http://tracker"));
		TorrentInfo info = new TorrentInfo();
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		info.setInfoHash(infoHash);
		torrent.setInfo(info);

		ScrapeRequestHandler scraper = Mockito.mock(ScrapeRequestHandler.class);
		Mockito.when(scraper.scrape(new URI("http://tracker"), new InfoHash("INFO_HASH".getBytes())))
			.thenReturn(null)
			.thenThrow(TorrentException.class);

		service.getScrapeRequestHandlers().put("HTTP", scraper);

		Assertions.assertNull(service.scrape(torrent));

		Mockito.verify(scraper, Mockito.times(2)).scrape(new URI("http://tracker"), new InfoHash("INFO_HASH".getBytes()));
		Mockito.verifyNoMoreInteractions(scraper);
	}

	@Test
	public void scrapeNoTrackersTest() throws Exception {
		Torrent torrent = new Torrent();
		torrent.setTrackers(Arrays.asList(""));
		torrent.setTracker(null);
		TorrentInfo info = new TorrentInfo();
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		info.setInfoHash(infoHash);
		torrent.setInfo(info);

		ScrapeRequestHandler scraper = Mockito.mock(ScrapeRequestHandler.class);

		service.getScrapeRequestHandlers().put("MAGNET", scraper);

		Assertions.assertNull(service.scrape(torrent));

		Mockito.verify(scraper).scrape(new URI("magnet:?xt=urn:btih:494e464f5f48415348"), infoHash);
		Mockito.verifyNoMoreInteractions(scraper);
	}

}