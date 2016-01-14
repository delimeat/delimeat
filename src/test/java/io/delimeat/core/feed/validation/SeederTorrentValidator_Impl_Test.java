package io.delimeat.core.feed.validation;

import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.feed.TorrentRejection;
import io.delimeat.core.show.Show;
import io.delimeat.core.torrent.ScrapeResult;
import io.delimeat.core.torrent.Torrent;
import io.delimeat.core.torrent.TorrentDao;
import io.delimeat.core.torrent.TorrentInfo;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SeederTorrentValidator_Impl_Test {

	private TorrentSeederValidator_Impl validator;
	private FeedResult result;

	@Before
	public void setUp() {
		validator = new TorrentSeederValidator_Impl();
		result = new FeedResult();
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
		TorrentInfo mockedTorrentInfo = Mockito.mock(TorrentInfo.class);
		Mockito.when(mockedTorrentInfo.getInfoHash()).thenReturn(
				"INFOHASH".getBytes());

		Torrent mockedTorrent = Mockito.mock(Torrent.class);
		Mockito.when(mockedTorrent.getTrackers()).thenReturn(null);
		Mockito.when(mockedTorrent.getTracker()).thenReturn("http://test.com");
		Mockito.when(mockedTorrent.getInfo()).thenReturn(mockedTorrentInfo);

		ScrapeResult mockedScrapeResult = Mockito.mock(ScrapeResult.class);
		Mockito.when(mockedScrapeResult.getSeeders())
				.thenReturn(Long.MAX_VALUE);
		Mockito.when(mockedScrapeResult.getLeechers()).thenReturn(
				Long.MIN_VALUE);

		TorrentDao mockedTorrentDao = Mockito.mock(TorrentDao.class);
		Mockito.when(
				mockedTorrentDao.scrape(Mockito.any(URI.class),
						Mockito.any(byte[].class))).thenReturn(
				mockedScrapeResult);

		Show mockedShow = Mockito.mock(Show.class);

		result.setTorrent(mockedTorrent);

		validator.setTorrentDao(mockedTorrentDao);
		validator.validate(result, mockedShow);

		Assert.assertEquals(0, result.getTorrentRejections().size());
		Assert.assertEquals(Long.MAX_VALUE, result.getSeeders());
		Assert.assertEquals(Long.MIN_VALUE, result.getLeechers());
	}

	@Test
	public void singleTrackerBelowMinTest() throws Exception {
		TorrentInfo mockedTorrentInfo = Mockito.mock(TorrentInfo.class);
		Mockito.when(mockedTorrentInfo.getInfoHash()).thenReturn(
				"INFOHASH".getBytes());

		Torrent mockedTorrent = Mockito.mock(Torrent.class);
		Mockito.when(mockedTorrent.getTrackers()).thenReturn(null);
		Mockito.when(mockedTorrent.getTracker()).thenReturn("http://test.com");
		Mockito.when(mockedTorrent.getInfo()).thenReturn(mockedTorrentInfo);

		ScrapeResult mockedScrapeResult = Mockito.mock(ScrapeResult.class);
		Mockito.when(mockedScrapeResult.getSeeders()).thenReturn((long) 10);
		Mockito.when(mockedScrapeResult.getLeechers()).thenReturn(
				Long.MIN_VALUE);

		TorrentDao mockedTorrentDao = Mockito.mock(TorrentDao.class);
		Mockito.when(
				mockedTorrentDao.scrape(Mockito.any(URI.class),
						Mockito.any(byte[].class))).thenReturn(
				mockedScrapeResult);

		Show mockedShow = Mockito.mock(Show.class);

		result.setTorrent(mockedTorrent);

		validator.setTorrentDao(mockedTorrentDao);
		validator.validate(result, mockedShow);

		Assert.assertEquals(1, result.getTorrentRejections().size());
		Assert.assertEquals(TorrentRejection.INSUFFICENT_SEEDERS, result
				.getTorrentRejections().get(0));
		Assert.assertEquals((long) 10, result.getSeeders());
		Assert.assertEquals(Long.MIN_VALUE, result.getLeechers());
	}

	@Test
	public void multipleTrackersAboveMinTest() throws Exception {
		TorrentInfo mockedTorrentInfo = Mockito.mock(TorrentInfo.class);
		Mockito.when(mockedTorrentInfo.getInfoHash()).thenReturn(
				"INFOHASH".getBytes());

		List<String> trackers = new ArrayList<String>();
		trackers.add("http://test.com");
		trackers.add("udp://test.com:8080");
		Torrent mockedTorrent = Mockito.mock(Torrent.class);
		Mockito.when(mockedTorrent.getTrackers()).thenReturn(trackers);
		Mockito.when(mockedTorrent.getInfo()).thenReturn(mockedTorrentInfo);

		ScrapeResult mockedScrapeResult1 = Mockito.mock(ScrapeResult.class);
		Mockito.when(mockedScrapeResult1.getSeeders()).thenReturn(
				Long.MAX_VALUE);
		Mockito.when(mockedScrapeResult1.getLeechers()).thenReturn(
				Long.MIN_VALUE);

		ScrapeResult mockedScrapeResult2 = Mockito.mock(ScrapeResult.class);
		Mockito.when(mockedScrapeResult2.getSeeders()).thenReturn((long) 20);
		Mockito.when(mockedScrapeResult2.getLeechers()).thenReturn((long) 30);

		TorrentDao mockedTorrentDao = Mockito.mock(TorrentDao.class);
		Mockito.when(
				mockedTorrentDao.scrape(Mockito.any(URI.class),
						Mockito.any(byte[].class)))
				.thenReturn(mockedScrapeResult1)
				.thenReturn(mockedScrapeResult2);

		Show mockedShow = Mockito.mock(Show.class);

		result.setTorrent(mockedTorrent);

		validator.setTorrentDao(mockedTorrentDao);
		validator.validate(result, mockedShow);

		Assert.assertEquals(0, result.getTorrentRejections().size());
		Assert.assertEquals(Long.MAX_VALUE, result.getSeeders());
		Assert.assertEquals(Long.MIN_VALUE, result.getLeechers());
	}

	@Test
	public void multipleTrackersBelowMinTest() throws Exception {
		TorrentInfo mockedTorrentInfo = Mockito.mock(TorrentInfo.class);
		Mockito.when(mockedTorrentInfo.getInfoHash()).thenReturn(
				"INFOHASH".getBytes());

		List<String> trackers = new ArrayList<String>();
		trackers.add("http://test.com");
		trackers.add("udp://test.com:8080");
		Torrent mockedTorrent = Mockito.mock(Torrent.class);
		Mockito.when(mockedTorrent.getTrackers()).thenReturn(trackers);
		Mockito.when(mockedTorrent.getInfo()).thenReturn(mockedTorrentInfo);

		ScrapeResult mockedScrapeResult1 = Mockito.mock(ScrapeResult.class);
		Mockito.when(mockedScrapeResult1.getSeeders()).thenReturn((long) 30);
		Mockito.when(mockedScrapeResult1.getLeechers()).thenReturn(
				Long.MIN_VALUE);

		ScrapeResult mockedScrapeResult2 = Mockito.mock(ScrapeResult.class);
		Mockito.when(mockedScrapeResult2.getSeeders()).thenReturn((long) 20);
		Mockito.when(mockedScrapeResult2.getLeechers()).thenReturn((long) 30);

		TorrentDao mockedTorrentDao = Mockito.mock(TorrentDao.class);
		Mockito.when(
				mockedTorrentDao.scrape(Mockito.any(URI.class),
						Mockito.any(byte[].class)))
				.thenReturn(mockedScrapeResult1)
				.thenReturn(mockedScrapeResult2);

		Show mockedShow = Mockito.mock(Show.class);

		result.setTorrent(mockedTorrent);

		validator.setTorrentDao(mockedTorrentDao);
		validator.validate(result, mockedShow);

		Assert.assertEquals(1, result.getTorrentRejections().size());
		Assert.assertEquals(TorrentRejection.INSUFFICENT_SEEDERS, result
				.getTorrentRejections().get(0));
		Assert.assertEquals((long) 30, result.getSeeders());
		Assert.assertEquals(Long.MIN_VALUE, result.getLeechers());
	}

}
