package io.delimeat.core.processor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedDao;
import io.delimeat.core.feed.FeedException;
import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.processor.FeedProcessor_Impl;
import io.delimeat.core.processor.validation.FeedResultValidator;
import io.delimeat.core.processor.validation.ValidationException;
import io.delimeat.core.processor.validation.TorrentValidator;
import io.delimeat.core.processor.writer.TorrentWriter;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowDao;
import io.delimeat.core.torrent.Torrent;
import io.delimeat.core.torrent.TorrentDao;
import io.delimeat.core.torrent.TorrentException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class FeedProcessor_ImplTest {

	private FeedProcessor_Impl processor;

	@Before
	public void setUp() throws Exception {
		processor = new FeedProcessor_Impl();
	}

	@Test
	public void showTest() {
		Assert.assertNull(processor.getShow());
		Show show = new Show();
		processor.setShow(show);
		Assert.assertEquals(show, processor.getShow());
	}
  
	@Test
	public void configTest() {
		Assert.assertNull(processor.getConfig());
		Config config = new Config();
		processor.setConfig(config);
		Assert.assertEquals(config, processor.getConfig());
	}

	@Test
	public void showDaoTest() {
		Assert.assertNull(processor.getShowDao());
		ShowDao mockedDao = Mockito.mock(ShowDao.class);
		processor.setShowDao(mockedDao);
		Assert.assertEquals(mockedDao, processor.getShowDao());
	}

	@Test
	public void feedDaosTest() {
		Assert.assertNull(processor.getFeedDaos());
		FeedDao mockedDao = Mockito.mock(FeedDao.class);
		processor.setFeedDaos(Arrays.asList(mockedDao));
		Assert.assertNotNull(processor.getFeedDaos());
		Assert.assertFalse(processor.getFeedDaos().isEmpty());
		Assert.assertEquals(1, processor.getFeedDaos().size());
		Assert.assertEquals(mockedDao, processor.getFeedDaos().get(0));
	}

	@Test
	public void torrentDaoTest() {
		Assert.assertNull(processor.getTorrentDao());
		TorrentDao mockedDao = Mockito.mock(TorrentDao.class);
		processor.setTorrentDao(mockedDao);
		Assert.assertEquals(mockedDao, processor.getTorrentDao());
	}

  	@Test
	public void abortTest() {
		Assert.assertFalse(processor.isActive());
		processor.setActive(true);
		Assert.assertTrue(processor.isActive());
		processor.abort();
		Assert.assertFalse(processor.isActive());
	}

	@Test
	public void feedResultValidatorsTest() {
		Assert.assertNotNull(processor.getFeedResultValidators());
		Assert.assertTrue(processor.getFeedResultValidators().isEmpty());
		FeedResultValidator mockedValidator = Mockito
				.mock(FeedResultValidator.class);
		processor.setFeedResultValidators(Arrays.asList(mockedValidator));
		Assert.assertNotNull(processor.getFeedResultValidators());
		Assert.assertFalse(processor.getFeedResultValidators().isEmpty());
		Assert.assertEquals(1, processor.getFeedResultValidators().size());
		Assert.assertEquals(mockedValidator, processor
				.getFeedResultValidators().get(0));
	}

	@Test
	public void torrentValidatorsTest() {
		Assert.assertNotNull(processor.getTorrentValidators());
		Assert.assertTrue(processor.getTorrentValidators().isEmpty());
		TorrentValidator mockedValidator = Mockito.mock(TorrentValidator.class);
		processor.setTorrentValidators(Arrays.asList(mockedValidator));
		Assert.assertNotNull(processor.getTorrentValidators());
		Assert.assertFalse(processor.getTorrentValidators().isEmpty());
		Assert.assertEquals(1, processor.getTorrentValidators().size());
		Assert.assertEquals(mockedValidator, processor.getTorrentValidators()
				.get(0));
	}

	@Test
	public void resultComparatorTest() {
		Assert.assertNull(processor.getResultComparator());
		@SuppressWarnings("unchecked")
		Comparator<FeedResult> mockedComparator = Mockito
				.mock(Comparator.class);
		processor.setResultComparator(mockedComparator);
		Assert.assertEquals(mockedComparator, processor.getResultComparator());
	}

	@Test
	public void feedResultWriterTest() {
		Assert.assertNull(processor.getTorrentWriter());
		TorrentWriter mockedWriter = Mockito.mock(TorrentWriter.class);
		processor.setTorrentWriter(mockedWriter);
		Assert.assertEquals(mockedWriter, processor.getTorrentWriter());
	}

	@Test
	public void fetchResultsNotStartedTest() throws FeedException {
		FeedDao dao = Mockito.mock(FeedDao.class);
		processor.setFeedDaos(Arrays.asList(dao));
		processor.abort();

		Show show = new Show();
		show.setTitle("TITLE");

		List<FeedResult> results = processor.fetchResults(show);

		Mockito.verifyZeroInteractions(dao);
		Assert.assertTrue(results.isEmpty());
	}

	@Test
	public void fetchResultsSuccessTest() throws FeedException {
		FeedDao dao = Mockito.mock(FeedDao.class);
		FeedResult feedResult = new FeedResult();
		feedResult.setTitle("TITLE");
		Mockito.when(dao.read(Mockito.anyString())).thenReturn(
				Arrays.asList(feedResult));

		processor.setFeedDaos(Arrays.asList(dao));
		processor.setActive(true);

		Show show = new Show();
		show.setTitle("TITLE");

		List<FeedResult> results = processor.fetchResults(show);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(feedResult, results.get(0));
	}

	@Test
	public void fetchResultsOneErrorTest() throws FeedException {
		FeedDao dao = Mockito.mock(FeedDao.class);
		FeedResult feedResult = new FeedResult();
		feedResult.setTitle("TITLE");
		Mockito.when(dao.read(Mockito.anyString()))
				.thenThrow(FeedException.class)
				.thenReturn(Arrays.asList(feedResult));

		processor.setFeedDaos(Arrays.asList(dao, dao));
		processor.setActive(true);

		Show show = new Show();
		show.setTitle("TITLE");

		List<FeedResult> results = processor.fetchResults(show);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(feedResult, results.get(0));
	}

	@Test
	public void validateFeedResultsNotStartedTest()
			throws ValidationException {
		FeedResultValidator validator = Mockito.mock(FeedResultValidator.class);
		processor.setFeedResultValidators(Arrays.asList(validator));
		processor.abort();

		Show show = new Show();

		processor.validateFeedResults(new ArrayList<FeedResult>(), show);

		Mockito.verifyZeroInteractions(validator);
	}

	@Test
	public void validateFeedResultsTest() throws ValidationException {
		FeedResultValidator validator = Mockito.mock(FeedResultValidator.class);
		processor.setFeedResultValidators(Arrays.asList(validator));

		Show show = new Show();

		FeedResult result1 = new FeedResult();
		result1.getFeedResultRejections().add(
				FeedResultRejection.CONTAINS_COMPRESSED);
		FeedResult result2 = new FeedResult();

		List<FeedResult> inResults = Arrays.asList(result1, result2);

		List<FeedResult> outResults = processor.validateFeedResults(inResults,
				show);
		Assert.assertEquals(1, outResults.size());
		Assert.assertEquals(result2, outResults.get(0));
	}

	@Test(expected = MalformedURLException.class)
	public void fetchTorrentMalformedURLTest() throws IOException,
			TorrentException, URISyntaxException {
		FeedResult result = new FeedResult();
		result.setTorrentURL("MALFORMEDURL");

		processor.fetchTorrent(result);
	}

	@Test(expected = URISyntaxException.class)
	public void fetchTorrentURISyntaxTest() throws IOException,
			TorrentException, URISyntaxException {
		FeedResult result = new FeedResult();
		result.setTorrentURL("http:URISYNTAXEXCEPTION");

		processor.fetchTorrent(result);
	}

	@Test
	public void fetchTorrentTest() throws IOException, TorrentException,
			URISyntaxException {
		FeedResult result = new FeedResult();
		result.setTorrentURL("http://test.com?removed=true");

		TorrentDao dao = Mockito.mock(TorrentDao.class);
		Torrent torrent = new Torrent();
		Mockito.when(dao.read(new URI("http://test.com"))).thenReturn(torrent);
		processor.setTorrentDao(dao);

		Torrent outTorrent = processor.fetchTorrent(result);
		Assert.assertEquals(torrent, outTorrent);
	}

	@Test(expected = TorrentException.class)
	public void fetchTorrentDaoExceptionTest() throws IOException,
			TorrentException, URISyntaxException {
		FeedResult result = new FeedResult();
		result.setTorrentURL("http://test.com?removed=true");

		TorrentDao dao = Mockito.mock(TorrentDao.class);
		Mockito.when(dao.read(new URI("http://test.com"))).thenThrow(
				TorrentException.class);
		processor.setTorrentDao(dao);

		processor.fetchTorrent(result);
	}

	@Test
	public void validateTorrentNotStartedTest() throws ValidationException {
		TorrentValidator validator = Mockito.mock(TorrentValidator.class);
		processor.setTorrentValidators(Arrays.asList(validator));
		processor.abort();

		FeedResult result = new FeedResult();

		Config config = new Config();

		processor.validateTorrent(result, null, config, null);
		Mockito.verifyZeroInteractions(validator);
	}

	@Test
	public void validateTorrentTest() throws ValidationException {
		TorrentValidator validator = Mockito.mock(TorrentValidator.class);
		Mockito.when(
				validator.validate(Mockito.any(Torrent.class),
						Mockito.any(Show.class), Mockito.any(Config.class)))
				.thenReturn(true).thenReturn(false);
		Mockito.when(validator.getRejection()).thenReturn(
				FeedResultRejection.CONTAINS_COMPRESSED);

		processor.setTorrentValidators(Arrays.asList(validator, validator));
		processor.setActive(true);

		FeedResult result = new FeedResult();

		Config config = new Config();
		config.setIgnoreFolders(false);

		processor.validateTorrent(result, null, config, null);
		Mockito.verify(validator, Mockito.times(2)).validate(
				Mockito.any(Torrent.class), Mockito.any(Show.class),
				Mockito.any(Config.class));
		Assert.assertEquals(1, result.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.CONTAINS_COMPRESSED, result
				.getFeedResultRejections().get(0));
	}

	@Test(expected = ValidationException.class)
	public void validateTorrentExceptionTest() throws ValidationException {
		TorrentValidator validator = Mockito.mock(TorrentValidator.class);
		Mockito.when(
				validator.validate(Mockito.any(Torrent.class),
						Mockito.any(Show.class), Mockito.any(Config.class)))
				.thenThrow(ValidationException.class);

		processor.setTorrentValidators(Arrays.asList(validator, validator));
		processor.setActive(true);

		FeedResult result = new FeedResult();

		Config config = new Config();

		processor.validateTorrent(result, null, config, null);
	}

	@Test
	public void selectResultNullResultsTest() {
		Config config = new Config();
		Assert.assertNull(processor.selectResult(null, config));
	}

	@Test
	public void selectResultEmptyResultsTest() {
		List<FeedResult> results = new ArrayList<FeedResult>();
		Config config = new Config();
		Assert.assertNull(processor.selectResult(results, config));
	}

	@Test
	public void selectResultTest() {
		FeedResult result1 = new FeedResult();
		FeedResult result2 = new FeedResult();

		Config config = new Config();

		@SuppressWarnings("unchecked")
		Comparator<FeedResult> comparator = Mockito.mock(Comparator.class);

		processor.setResultComparator(comparator);

		FeedResult result = processor.selectResult(
				Arrays.asList(result1, result2), config);

		Assert.assertNotNull(result);
		Assert.assertEquals(result1, result);
	}

	@Test
	public void validateResultTorrentsNotStartedTest()
			throws ValidationException {
		FeedResult result = new FeedResult();
		processor.abort();

		List<FeedResult> results = processor.validateResultTorrents(
				Arrays.asList(result), null, null);
		Assert.assertNotNull(results);
		Assert.assertTrue(results.isEmpty());
	}

	@Test
	public void validatResultTorrentsTest() throws Exception {
		FeedResult result1 = new FeedResult();
		result1.setTorrentURL("http://test.com");
		FeedResult result2 = new FeedResult();
		result2.setTorrentURL(null);
		FeedResult result3 = new FeedResult();
		result3.setTorrentURL("http:URISYNTAXEXCEPTION");
		FeedResult result4 = new FeedResult();
		result4.setTorrentURL("http://test.com");
		FeedResult result5 = new FeedResult();
		result5.setTorrentURL("http://test.com");
		FeedResult result6 = new FeedResult();
		result6.setTorrentURL("http://test.com");

		TorrentDao dao = Mockito.mock(TorrentDao.class);
		Torrent torrent = new Torrent();
		Mockito.when(dao.read(Mockito.any(URI.class))).thenReturn(torrent)
				.thenThrow(IOException.class).thenThrow(TorrentException.class)
				.thenReturn(torrent);

		processor.setTorrentDao(dao);
		processor.setActive(true);

		TorrentValidator validator = Mockito.mock(TorrentValidator.class);
		Mockito.when(
				validator.validate(Mockito.any(Torrent.class),
						Mockito.any(Show.class), Mockito.any(Config.class)))
				.thenReturn(true).thenReturn(false);
		Mockito.when(validator.getRejection()).thenReturn(
				FeedResultRejection.CONTAINS_COMPRESSED);
		processor.setTorrentValidators(Arrays.asList(validator));

		Config config = new Config();

		List<FeedResult> results = processor.validateResultTorrents(Arrays
				.asList(result1, result2, result3, result4, result5, result6),
				null, config);
		Mockito.verify(dao, Mockito.times(4)).read(Mockito.any(URI.class));
		Mockito.verify(validator, Mockito.times(2)).validate(
				Mockito.any(Torrent.class), Mockito.any(Show.class),
				Mockito.any(Config.class));
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(result1, results.get(0));
		Assert.assertTrue(result1.getFeedResultRejections().isEmpty());
		Assert.assertEquals(1, result2.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.UNNABLE_TO_GET_TORRENT, result2
				.getFeedResultRejections().get(0));
		Assert.assertEquals(1, result3.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.UNNABLE_TO_GET_TORRENT, result3
				.getFeedResultRejections().get(0));
		Assert.assertEquals(1, result4.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.UNNABLE_TO_GET_TORRENT, result4
				.getFeedResultRejections().get(0));
		Assert.assertEquals(1, result5.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.UNNABLE_TO_GET_TORRENT, result5
				.getFeedResultRejections().get(0));
		Assert.assertEquals(1, result6.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.CONTAINS_COMPRESSED, result6
				.getFeedResultRejections().get(0));
	}

	@Test(expected = ValidationException.class)
	public void validatResultTorrentsExceptionTest() throws Exception {
		FeedResult result = new FeedResult();
		result.setTorrentURL("http://test.com");

		TorrentDao dao = Mockito.mock(TorrentDao.class);
		Torrent torrent = new Torrent();
		Mockito.when(dao.read(Mockito.any(URI.class))).thenReturn(torrent);
		processor.setTorrentDao(dao);
		processor.setActive(true);

		TorrentValidator validator = Mockito.mock(TorrentValidator.class);
		Mockito.when(
				validator.validate(Mockito.any(Torrent.class),
						Mockito.any(Show.class), Mockito.any(Config.class)))
				.thenThrow(ValidationException.class);
		processor.setTorrentValidators(Arrays.asList(validator));

		Config config = new Config();

		processor.validateResultTorrents(Arrays.asList(result), null, config);
	}
}
