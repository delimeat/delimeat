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
import io.delimeat.torrent.exception.UnhandledScrapeException;

public class TorrentService_ImplTest {


	private TorrentService_Impl service;

	@Before
	public void setUp() {
		service = new TorrentService_Impl();
	}
	
	@Test
	public void setDaoTest(){
		Assert.assertNull(service.getDao());
		TorrentDao dao = Mockito.mock(TorrentDao.class);
		service.setDao(dao);
		Assert.assertEquals(dao, service.getDao());
	}

	@Test
	public void setWriterTest(){
		Assert.assertNull(service.getWriter());
		TorrentWriter writer = Mockito.mock(TorrentWriter.class);
		service.setWriter(writer);
		Assert.assertEquals(writer, service.getWriter());
	}
	

	@Test
	public void setScrapeRequestHandlersTest() {
    	Assert.assertEquals(0, service.getScrapeRequestHandlers().size());

		ScrapeRequestHandler mockedScraper1 = Mockito.mock(ScrapeRequestHandler.class);
		ScrapeRequestHandler mockedScraper2 = Mockito.mock(ScrapeRequestHandler.class);
			
		service.setScrapeRequestHandlers(Arrays.asList(mockedScraper1,mockedScraper2));
		
     	Assert.assertEquals(2, service.getScrapeRequestHandlers().size());
		Assert.assertEquals(mockedScraper1,	service.getScrapeRequestHandlers().get(0));
		Assert.assertEquals(mockedScraper2,service.getScrapeRequestHandlers().get(1));
	}
	
	@Test
	public void readTest() throws Exception{
		URI uri = new URI("http://test.com");
		TorrentDao dao = Mockito.mock(TorrentDao.class);
		Torrent torrent = new Torrent();
		Mockito.when(dao.read(uri)).thenReturn(torrent);
		service.setDao(dao);
		
		Torrent result = service.read(uri);
		Assert.assertEquals(torrent, result);
		
		Mockito.verify(dao).read(uri);
		Mockito.verifyNoMoreInteractions(dao);
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
		Mockito.when(mockedScraper.doScrape(new URI("http://scrape.me.com"),
				new InfoHash("INFO_HASH".getBytes()))).thenReturn(mockedResult);
		
		service.setScrapeRequestHandlers(Arrays.asList(mockedScraper));

		URI uri = new URI("http://scrape.me.com");
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		Assert.assertEquals(mockedResult,service.doScrape(uri, infoHash));
	}
	
  	@Test
  	public void buildInfoHashFromMagnetTest() throws Exception{
  		InfoHash infoHash = service.infoHashFromMagnet(new URI("magnet:?xt=urn:btih:df706cf16f45e8c0fd226223509c7e97b4ffec13&tr=udp://tracker.coppersurfer.tk:6969/announce"));
  		Assert.assertEquals("df706cf16f45e8c0fd226223509c7e97b4ffec13", infoHash.getHex());
  	}
  	
  	@Test
  	public void buildInfoHashFromMagnetNoMatchTest() throws Exception{
  		Assert.assertNull(service.infoHashFromMagnet(new URI("magnet:?xt=urn:btih:")));
  	}
	
}
