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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.config.domain.Config;
import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.torrent.domain.Torrent;
import io.delimeat.torrent.domain.TorrentInfo;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.UnhandledScrapeException;

public class TorrentService_ImplTest {


	private TorrentService_Impl service;

	@Before
	public void setUp() {
		service = new TorrentService_Impl();
	}
	
	@Test
	public void downloadUriTemplateTest(){
		Assert.assertNull(service.getMagnetUriTemplate());
		service.setMagnetUriTemplate("template");
		Assert.assertEquals("template", service.getMagnetUriTemplate());
	}
	
	@Test
	public void readersTest(){
    	Assert.assertEquals(0, service.getReaders().size());

		TorrentReader reader1 = Mockito.mock(TorrentReader.class);
		TorrentReader reader2 = Mockito.mock(TorrentReader.class);
			
		service.setReaders(Arrays.asList(reader1,reader2));
		
     	Assert.assertEquals(2, service.getReaders().size());
		Assert.assertEquals(reader1,	service.getReaders().get(0));
		Assert.assertEquals(reader2,service.getReaders().get(1));
	}

	@Test
	public void writerTest(){
		Assert.assertNull(service.getWriter());
		TorrentWriter writer = Mockito.mock(TorrentWriter.class);
		service.setWriter(writer);
		Assert.assertEquals(writer, service.getWriter());
	}
	
	@Test
	public void scrapeRequestHandlersTest() {
    	Assert.assertEquals(0, service.getScrapeRequestHandlers().size());

		ScrapeRequestHandler mockedScraper1 = Mockito.mock(ScrapeRequestHandler.class);
		ScrapeRequestHandler mockedScraper2 = Mockito.mock(ScrapeRequestHandler.class);
			
		service.setScrapeRequestHandlers(Arrays.asList(mockedScraper1,mockedScraper2));
		
     	Assert.assertEquals(2, service.getScrapeRequestHandlers().size());
		Assert.assertEquals(mockedScraper1,	service.getScrapeRequestHandlers().get(0));
		Assert.assertEquals(mockedScraper2,service.getScrapeRequestHandlers().get(1));
	}
	
	@Test
	public void buildMagnetUriTest() throws Exception{
		service.setMagnetUriTemplate("magnet:?xt=urn:btih:%s");
		
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		URI uri = service.buildMagnetUri(infoHash);
		
		Assert.assertEquals(new URI("magnet:?xt=urn:btih:494e464f5f48415348"), uri);
	}
	
	@Test(expected=TorrentException.class)
	public void buildMagnetUriURISyntaxExceptionTest() throws Exception{
		
		service.setMagnetUriTemplate("\\//");

		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		service.buildMagnetUri(infoHash);	
	}
	
	@Test
	public void readUriTest() throws Exception{
		URI uri = new URI("http://test.com");
		Torrent torrent = new Torrent();
		TorrentReader reader = Mockito.mock(TorrentReader.class);
		Mockito.when(reader.getSupportedProtocols()).thenReturn(Arrays.asList("HTTP"));
		Mockito.when(reader.read(uri)).thenReturn(torrent);

		service.setReaders(Arrays.asList(reader));
		
		Torrent result = service.read(uri);
		Assert.assertEquals(torrent, result);
		
		Mockito.verify(reader).getSupportedProtocols();
		Mockito.verify(reader).read(uri);
		Mockito.verifyNoMoreInteractions(reader);
	}
	
	@Test(expected=TorrentException.class)
	public void readUriUnsupportedSchemeTest() throws Exception{	
		service.read(new URI("udp://test.com"));
	}
	
	@Test
	public void readInfoHashTest() throws Exception {
		Torrent torrent = new Torrent();
		TorrentReader reader = Mockito.mock(TorrentReader.class);
		Mockito.when(reader.getSupportedProtocols()).thenReturn(Arrays.asList("MAGNET"));
		Mockito.when(reader.read(new URI("magnet:?xt=urn:btih:494e464f5f48415348"))).thenReturn(torrent);
		
		service.setReaders(Arrays.asList(reader));
		
		service.setMagnetUriTemplate("magnet:?xt=urn:btih:%s");
		
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		Torrent result = service.read(infoHash);
		
		Assert.assertEquals(torrent, result);
		
		Mockito.verify(reader).getSupportedProtocols();
		Mockito.verify(reader).read(new URI("magnet:?xt=urn:btih:494e464f5f48415348"));
		Mockito.verifyNoMoreInteractions(reader);		
	}

	@Test
	public void writeTest() throws Exception{
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
	

	@Test(expected = UnhandledScrapeException.class)
	public void scrapeUnhandledTest() throws Exception {
		ScrapeRequestHandler mockedScraper = Mockito
				.mock(ScrapeRequestHandler.class);
		Mockito.when(mockedScraper.getSupportedProtocols()).thenReturn(Arrays.asList("NOT_UDP"));
		service.setScrapeRequestHandlers(Arrays.asList(mockedScraper));

		URI scrapeUri = new URI("udp://scrape.me:8080");
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		service.doScrape(scrapeUri, infoHash);
	}


	@Test
	public void scrapeTest() throws Exception {
		ScrapeResult mockedResult = Mockito.mock(ScrapeResult.class);
		ScrapeRequestHandler mockedScraper = Mockito.mock(ScrapeRequestHandler.class);
		Mockito.when(mockedScraper.getSupportedProtocols()).thenReturn(Arrays.asList("HTTP"));
		Mockito.when(mockedScraper.scrape(new URI("http://scrape.me.com"),
				new InfoHash("INFO_HASH".getBytes()))).thenReturn(mockedResult);
		
		service.setScrapeRequestHandlers(Arrays.asList(mockedScraper));

		URI uri = new URI("http://scrape.me.com");
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		Assert.assertEquals(mockedResult,service.doScrape(uri, infoHash));
	}
  	
  	@Test
  	public void scrapeTrackerTest() throws Exception{
  		Torrent torrent = new Torrent();
  		torrent.setTracker("http://tracker");
  		TorrentInfo info = new TorrentInfo();
  		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
  		info.setInfoHash(infoHash);
  		torrent.setInfo(info);
  		
		ScrapeResult scrapeResult = new ScrapeResult(100,200);
		ScrapeRequestHandler scraper = Mockito.mock(ScrapeRequestHandler.class);
		Mockito.when(scraper.getSupportedProtocols()).thenReturn(Arrays.asList("HTTP"));
		Mockito.when(scraper.scrape(new URI("http://tracker"),
				new InfoHash("INFO_HASH".getBytes()))).thenReturn(scrapeResult);
		
		service.setScrapeRequestHandlers(Arrays.asList(scraper));
  		
  		ScrapeResult result = service.scrape(torrent);
  		
  		Assert.assertEquals(scrapeResult, result);
  		
  		Mockito.verify(scraper).getSupportedProtocols();
  		Mockito.verify(scraper).scrape(new URI("http://tracker"),new InfoHash("INFO_HASH".getBytes()));
  		Mockito.verifyNoMoreInteractions(scraper);
  		
  	}
  	
  	@Test
  	public void scrapeTrackersTest() throws Exception{
  		Torrent torrent = new Torrent();
  		torrent.setTrackers(Arrays.asList("http://tracker"));
  		TorrentInfo info = new TorrentInfo();
  		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
  		info.setInfoHash(infoHash);
  		torrent.setInfo(info);
  		
		ScrapeResult scrapeResult = new ScrapeResult(100,200);
		ScrapeRequestHandler scraper = Mockito.mock(ScrapeRequestHandler.class);
		Mockito.when(scraper.getSupportedProtocols()).thenReturn(Arrays.asList("HTTP"));
		Mockito.when(scraper.scrape(new URI("http://tracker"),
				new InfoHash("INFO_HASH".getBytes()))).thenReturn(scrapeResult);
		
		service.setScrapeRequestHandlers(Arrays.asList(scraper));
  		
  		ScrapeResult result = service.scrape(torrent);
  		
  		Assert.assertEquals(scrapeResult, result);
  		
  		Mockito.verify(scraper).getSupportedProtocols();
  		Mockito.verify(scraper).scrape(new URI("http://tracker"),new InfoHash("INFO_HASH".getBytes()));
  		Mockito.verifyNoMoreInteractions(scraper);  	
  	}
  	
  	@Test
  	public void scrapeTrackersExceptionTest() throws Exception{
  		Torrent torrent = new Torrent();
  		torrent.setTrackers(Arrays.asList("http://tracker","http://tracker"));
  		TorrentInfo info = new TorrentInfo();
  		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
  		info.setInfoHash(infoHash);
  		torrent.setInfo(info);
  		
		ScrapeRequestHandler scraper = Mockito.mock(ScrapeRequestHandler.class);
		Mockito.when(scraper.getSupportedProtocols()).thenReturn(Arrays.asList("HTTP"));
		Mockito.when(scraper.scrape(new URI("http://tracker"),
				new InfoHash("INFO_HASH".getBytes())))
					.thenReturn(null)
					.thenThrow(TorrentException.class);
		
		service.setScrapeRequestHandlers(Arrays.asList(scraper));
  		
  		Assert.assertNull(service.scrape(torrent));
  		  		
  		Mockito.verify(scraper,Mockito.times(2)).getSupportedProtocols();
  		Mockito.verify(scraper, Mockito.times(2)).scrape(new URI("http://tracker"),new InfoHash("INFO_HASH".getBytes()));
  		Mockito.verifyNoMoreInteractions(scraper);  	
  	}
  	
  	@Test
  	public void scrapeNoTrackersTest() throws Exception{
  		Torrent torrent = new Torrent();
  		torrent.setTrackers(Arrays.asList(""));
  		torrent.setTracker(null);
  		TorrentInfo info = new TorrentInfo();
  		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
  		info.setInfoHash(infoHash);
  		torrent.setInfo(info);
  		
		ScrapeRequestHandler scraper = Mockito.mock(ScrapeRequestHandler.class);
		Mockito.when(scraper.getSupportedProtocols()).thenReturn(Arrays.asList("MAGNET"));
		
		service.setMagnetUriTemplate("magnet:?xt=urn:btih:%s");
		service.setScrapeRequestHandlers(Arrays.asList(scraper));
  		
  		Assert.assertNull(service.scrape(torrent));
  		
  		Mockito.verify(scraper).getSupportedProtocols();
  		Mockito.verify(scraper).scrape(new URI("magnet:?xt=urn:btih:494e464f5f48415348"), infoHash);
  		Mockito.verifyNoMoreInteractions(scraper);
  	}
  	
  	@Test
  	public void scrapeMagnetUriExceptionTest() throws Exception{
  		Torrent torrent = new Torrent();
  		torrent.setTrackers(Arrays.asList(""));
  		torrent.setTracker(null);
  		TorrentInfo info = new TorrentInfo();
  		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
  		info.setInfoHash(infoHash);
  		torrent.setInfo(info);
  		
		ScrapeRequestHandler scraper = Mockito.mock(ScrapeRequestHandler.class);
		Mockito.when(scraper.getSupportedProtocols()).thenReturn(Arrays.asList("MAGNET"));
		
		service.setMagnetUriTemplate("\\//");
		service.setScrapeRequestHandlers(Arrays.asList(scraper));
  		
  		Assert.assertNull(service.scrape(torrent));
  		
  		//Mockito.verify(scraper).getSupportedProtocols();
  		//Mockito.verify(scraper).scrape(new URI("magnet:?xt=urn:btih:494e464f5f48415348"), infoHash);
  		Mockito.verifyNoMoreInteractions(scraper);
  	}
	
}