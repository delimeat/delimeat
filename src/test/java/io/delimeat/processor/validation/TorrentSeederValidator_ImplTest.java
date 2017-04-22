package io.delimeat.processor.validation;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.processor.validation.TorrentSeederValidator_Impl;
import io.delimeat.show.domain.Show;
import io.delimeat.torrent.TorrentService;
import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.torrent.domain.Torrent;
import io.delimeat.torrent.domain.TorrentInfo;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.Optional;

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
	public void TorrentServiceTest() {
		Assert.assertNull(validator.getTorrentService());
		TorrentService torrentService = Mockito.mock(TorrentService.class);
		validator.setTorrentService(torrentService);
		Assert.assertEquals(torrentService, validator.getTorrentService());
	}

	@Test
	public void singleTrackerAboveMinTest() throws Exception {
		TorrentInfo info = new TorrentInfo();
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		info.setInfoHash(infoHash);
		torrent.setInfo(info);
		torrent.setTracker("http://test.com");

		TorrentService dao = Mockito.mock(TorrentService.class);
		ScrapeResult scrape = new ScrapeResult(21, Long.MIN_VALUE);
		Mockito.when(dao.scrape(Mockito.any(URI.class), Mockito.any(InfoHash.class))).thenReturn(scrape);
		validator.setTorrentService(dao);

		Assert.assertEquals(Optional.empty(), validator.validate(torrent, show, config));

		Mockito.verify(dao).scrape(Mockito.any(URI.class), Mockito.any(InfoHash.class));

	}

	@Test
	public void singleTrackerBelowMinTest() throws Exception {
		TorrentInfo info = new TorrentInfo();
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		info.setInfoHash(infoHash);
		torrent.setInfo(info);
		torrent.setTracker("http://test.com");

		TorrentService dao = Mockito.mock(TorrentService.class);
		ScrapeResult scrape = new ScrapeResult(19, Long.MIN_VALUE);
		Mockito.when(dao.scrape(Mockito.any(URI.class), Mockito.any(InfoHash.class))).thenReturn(scrape);
		validator.setTorrentService(dao);

		Assert.assertEquals(Optional.of(FeedResultRejection.INSUFFICENT_SEEDERS), validator.validate(torrent, show, config));

		Mockito.verify(dao).scrape(Mockito.any(URI.class), Mockito.any(InfoHash.class));

	}

	@Test
	public void multipleTrackersAboveMinTest() throws Exception {
		TorrentInfo info = new TorrentInfo();
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		info.setInfoHash(infoHash);
		torrent.setInfo(info);
		torrent.getTrackers().add("http://test.com");
		torrent.getTrackers().add("udp://test.com:8080");

		TorrentService dao = Mockito.mock(TorrentService.class);
		ScrapeResult scrape = new ScrapeResult(21, Long.MIN_VALUE);
		Mockito.when(dao.scrape(Mockito.any(URI.class), Mockito.any(InfoHash.class))).thenThrow(new IOException())
				.thenReturn(scrape);
		validator.setTorrentService(dao);

		Assert.assertEquals(Optional.empty(), validator.validate(torrent, show, config));

		Mockito.verify(dao, Mockito.times(2)).scrape(Mockito.any(URI.class), Mockito.any(InfoHash.class));
	}

	@Test
	public void multipleTrackersBelowMinTest() throws Exception {
		TorrentInfo info = new TorrentInfo();
		InfoHash infoHash = new InfoHash("INFO_HASH".getBytes());
		info.setInfoHash(infoHash);
		torrent.setInfo(info);
		torrent.getTrackers().add("http://test.com");
		torrent.getTrackers().add("udp://test.com:8080");

		TorrentService dao = Mockito.mock(TorrentService.class);
		ScrapeResult scrape = new ScrapeResult(19, Long.MIN_VALUE);
		Mockito.when(dao.scrape(Mockito.any(URI.class), Mockito.any(InfoHash.class))).thenReturn(scrape);
		validator.setTorrentService(dao);

		Assert.assertEquals(Optional.of(FeedResultRejection.INSUFFICENT_SEEDERS), validator.validate(torrent, show, config));

		Mockito.verify(dao).scrape(Mockito.any(URI.class), Mockito.any(InfoHash.class));
	}

	@Test
	public void scrapeTimeoutExceptionTest() throws Exception {
		TorrentService dao = Mockito.mock(TorrentService.class);
		Mockito.when(dao.scrape(Mockito.any(URI.class), Mockito.any(InfoHash.class)))
				.thenThrow(new SocketTimeoutException());
		validator.setTorrentService(dao);

		Assert.assertNull(validator.scrape("http:test.com", new InfoHash("bytes".getBytes())));

		Mockito.verify(dao).scrape(Mockito.any(URI.class), Mockito.any(InfoHash.class));
	}

	@Test
	public void scrapeExceptionTest() throws Exception {
		TorrentService dao = Mockito.mock(TorrentService.class);
		Mockito.when(dao.scrape(Mockito.any(URI.class), Mockito.any(InfoHash.class))).thenThrow(new IOException());
		validator.setTorrentService(dao);

		Assert.assertNull(validator.scrape("http:test.com", new InfoHash("bytes".getBytes())));

		Mockito.verify(dao).scrape(Mockito.any(URI.class), Mockito.any(InfoHash.class));
	}

}
