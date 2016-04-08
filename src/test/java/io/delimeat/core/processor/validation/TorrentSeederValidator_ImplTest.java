package io.delimeat.core.processor.validation;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.processor.validation.TorrentSeederValidator_Impl;
import io.delimeat.core.show.Show;
import io.delimeat.core.torrent.InfoHash;
import io.delimeat.core.torrent.ScrapeResult;
import io.delimeat.core.torrent.Torrent;
import io.delimeat.core.torrent.TorrentDao;
import io.delimeat.core.torrent.TorrentInfo;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TorrentSeederValidator_ImplTest {

	private TorrentSeederValidator_Impl validator;
   private Show show;
   private Config config;
   private Torrent torrent;

	@Before
	public void setUp() {
		validator = new TorrentSeederValidator_Impl();
      show = new Show();
      config = new Config();
      torrent = new Torrent();
	}
	
   @Test
   public void rejectionTest(){
     Assert.assertEquals(FeedResultRejection.INSUFFICENT_SEEDERS, validator.getRejection());
   }
  
	@Test
	public void torrentDaoTest() {
		Assert.assertNull(validator.getTorrentDao());
		TorrentDao mockedTorrentDao = Mockito.mock(TorrentDao.class);
		validator.setTorrentDao(mockedTorrentDao);
		Assert.assertEquals(mockedTorrentDao, validator.getTorrentDao());
	}

	@Test
	public void singleTrackerAboveMinTest() throws Exception {
      TorrentInfo info = new TorrentInfo();
      InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
      info.setInfoHash(infoHash);
		torrent.setInfo(info);
		torrent.setTracker("http://test.com");

		TorrentDao dao = Mockito.mock(TorrentDao.class);
      ScrapeResult scrape = new ScrapeResult(21,Long.MIN_VALUE);
		Mockito.when(dao.scrape(Mockito.any(URI.class),Mockito.any(InfoHash.class))).thenReturn(scrape);
		validator.setTorrentDao(dao);
     
		Assert.assertTrue(validator.validate(torrent, show, config));	
     
     	Mockito.verify(dao).scrape(Mockito.any(URI.class),Mockito.any(InfoHash.class));

	}

	@Test
	public void singleTrackerBelowMinTest() throws Exception {
      TorrentInfo info = new TorrentInfo();
      InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
      info.setInfoHash(infoHash);
		torrent.setInfo(info);
		torrent.setTracker("http://test.com");

		TorrentDao dao = Mockito.mock(TorrentDao.class);
      ScrapeResult scrape = new ScrapeResult(19,Long.MIN_VALUE);
		Mockito.when(dao.scrape(Mockito.any(URI.class),Mockito.any(InfoHash.class))).thenReturn(scrape);
		validator.setTorrentDao(dao);
     
		Assert.assertFalse(validator.validate(torrent, show, config));		
     
     	Mockito.verify(dao).scrape(Mockito.any(URI.class),Mockito.any(InfoHash.class));

	}

	@Test
	public void multipleTrackersAboveMinTest() throws Exception {
      TorrentInfo info = new TorrentInfo();
      InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
      info.setInfoHash(infoHash);
		torrent.setInfo(info);
      torrent.getTrackers().add("http://test.com");
      torrent.getTrackers().add("udp://test.com:8080");


		TorrentDao dao = Mockito.mock(TorrentDao.class);
      ScrapeResult scrape = new ScrapeResult(21,Long.MIN_VALUE);
		Mockito.when(dao.scrape(Mockito.any(URI.class),Mockito.any(InfoHash.class))).thenThrow(new IOException()).thenReturn(scrape);
		validator.setTorrentDao(dao);
     
		Assert.assertTrue(validator.validate(torrent, show, config));	
     
     	Mockito.verify(dao,Mockito.times(2)).scrape(Mockito.any(URI.class),Mockito.any(InfoHash.class));
	}

	@Test
	public void multipleTrackersBelowMinTest() throws Exception {
      TorrentInfo info = new TorrentInfo();
      InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
      info.setInfoHash(infoHash);
		torrent.setInfo(info);
      torrent.getTrackers().add("http://test.com");
      torrent.getTrackers().add("udp://test.com:8080");


		TorrentDao dao = Mockito.mock(TorrentDao.class);
      ScrapeResult scrape = new ScrapeResult(19,Long.MIN_VALUE);
		Mockito.when(dao.scrape(Mockito.any(URI.class),Mockito.any(InfoHash.class))).thenReturn(scrape);
		validator.setTorrentDao(dao);
     
		Assert.assertFalse(validator.validate(torrent, show, config));

     	Mockito.verify(dao).scrape(Mockito.any(URI.class),Mockito.any(InfoHash.class));
	}
  
  	@Test
  	public void scrapeTimeoutExceptionTest() throws Exception{
		TorrentDao dao = Mockito.mock(TorrentDao.class);
     	Mockito.when(dao.scrape(Mockito.any(URI.class),Mockito.any(InfoHash.class))).thenThrow(new SocketTimeoutException());
		validator.setTorrentDao(dao);
     
     	Assert.assertNull(validator.scrape("http:test.com", new InfoHash("bytes".getBytes())));
     
     	Mockito.verify(dao).scrape(Mockito.any(URI.class),Mockito.any(InfoHash.class));
   }
  
  
  	@Test
  	public void scrapeExceptionTest() throws Exception{
		TorrentDao dao = Mockito.mock(TorrentDao.class);
     	Mockito.when(dao.scrape(Mockito.any(URI.class),Mockito.any(InfoHash.class))).thenThrow(new IOException());
		validator.setTorrentDao(dao);
     
     	Assert.assertNull(validator.scrape("http:test.com", new InfoHash("bytes".getBytes())));
     
     	Mockito.verify(dao).scrape(Mockito.any(URI.class),Mockito.any(InfoHash.class));
   }

}
