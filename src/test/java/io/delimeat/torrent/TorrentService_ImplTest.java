package io.delimeat.torrent;

import java.net.URI;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.config.domain.Config;
import io.delimeat.torrent.TorrentDao;
import io.delimeat.torrent.TorrentService_Impl;
import io.delimeat.torrent.TorrentWriter;
import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.torrent.domain.Torrent;

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
	public void scrapeTest() throws Exception{
		URI uri = new URI("http://test.com");
		InfoHash infoHash = new InfoHash("infohash".getBytes());
		TorrentDao dao = Mockito.mock(TorrentDao.class);
		ScrapeResult scrapeResult = new ScrapeResult(Integer.MAX_VALUE,Integer.MIN_VALUE);
		Mockito.when(dao.scrape(uri,infoHash)).thenReturn(scrapeResult);
		service.setDao(dao);
		
		ScrapeResult result = service.scrape(uri, infoHash);
		Assert.assertEquals(scrapeResult, result);
		
		Mockito.verify(dao).scrape(uri,infoHash);
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
	
}
