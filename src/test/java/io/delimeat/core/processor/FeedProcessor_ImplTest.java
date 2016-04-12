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
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowDao;
import io.delimeat.core.show.ShowNotFoundException;
import io.delimeat.core.torrent.Torrent;
import io.delimeat.core.torrent.TorrentDao;
import io.delimeat.core.torrent.TorrentException;
import io.delimeat.core.torrent.TorrentInfo;
import io.delimeat.core.torrent.TorrentNotFoundException;

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
  	public void toStringTest(){
     	Assert.assertEquals("FeedProcessor_Impl [show=null, config=null, active=false, listeners=[], showDao=null, feedDaos=null, torrentDao=null, feedResultValidators=[], torrentValidators=[], resultComparator=null, torrentWriter=null]", processor.toString());
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
      Comparator<FeedResult> mockedComparator = Mockito.mock(Comparator.class);
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
    public void listenerTest(){
      Assert.assertNotNull(processor.getListeners());
      Assert.assertTrue(processor.getListeners().isEmpty());
      ProcessorListener listener = Mockito.mock(ProcessorListener.class);
      processor.addListener(listener);
      Assert.assertEquals(1, processor.getListeners().size());
      Assert.assertEquals(listener, processor.getListeners().get(0));
      processor.removeListener(listener);
      Assert.assertTrue(processor.getListeners().isEmpty());    
    }
  	
    @Test
    public void alertListenersCompleteTest(){
      ProcessorListener listener = Mockito.mock(ProcessorListener.class);
      processor.addListener(listener);
      processor.alertListenersComplete();

      Mockito.verify(listener).alertComplete(processor);
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
		result1.getFeedResultRejections().add(FeedResultRejection.CONTAINS_COMPRESSED);
		FeedResult result2 = new FeedResult();

     	processor.setActive(true);
		List<FeedResult> outResults = processor.validateFeedResults(Arrays.asList(result1, result2), show);
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
		Mockito.verify(validator, Mockito.times(2)).validate(Mockito.any(Torrent.class), Mockito.any(Show.class),Mockito.any(Config.class));
		
     	Assert.assertEquals(1, result.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.CONTAINS_COMPRESSED, result.getFeedResultRejections().get(0));
	}

	@Test(expected = ValidationException.class)
	public void validateTorrentExceptionTest() throws ValidationException {
		TorrentValidator validator = Mockito.mock(TorrentValidator.class);
		Mockito.when(validator.validate(Mockito.any(Torrent.class),Mockito.any(Show.class), Mockito.any(Config.class)))
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

		FeedResult result = processor.selectResult(Arrays.asList(result1, result2), config);

		Assert.assertNotNull(result);
		Assert.assertEquals(result1, result);
	}

	@Test
	public void validateResultTorrentsNotStartedTest()
			throws ValidationException {
		FeedResult result = new FeedResult();
		processor.abort();

		List<FeedResult> results = processor.validateResultTorrents(Arrays.asList(result), null, null);
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
		FeedResult result7 = new FeedResult();
		result7.setTorrentURL("http://test.com");

		TorrentDao dao = Mockito.mock(TorrentDao.class);
		Torrent torrent = new Torrent();
		Mockito.when(dao.read(Mockito.any(URI.class)))
        		.thenReturn(torrent)
				.thenThrow(IOException.class)
        		.thenThrow(TorrentException.class)
        		.thenThrow(TorrentNotFoundException.class)
				.thenReturn(torrent);

		processor.setTorrentDao(dao);
		processor.setActive(true);

		TorrentValidator validator = Mockito.mock(TorrentValidator.class);
		Mockito.when(validator.validate(Mockito.any(Torrent.class),Mockito.any(Show.class), Mockito.any(Config.class)))
        				.thenReturn(true)
        				.thenReturn(false);
		Mockito.when(validator.getRejection()).thenReturn(FeedResultRejection.CONTAINS_COMPRESSED);
		processor.setTorrentValidators(Arrays.asList(validator));

		Config config = new Config();

		List<FeedResult> results = processor.validateResultTorrents(Arrays.asList(result1, result2, result3, result4, result5, result6, result7),null, config);
		Mockito.verify(dao, Mockito.times(5)).read(Mockito.any(URI.class));
		Mockito.verify(validator, Mockito.times(2)).validate(Mockito.any(Torrent.class), Mockito.any(Show.class), Mockito.any(Config.class));
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(result1, results.get(0));
		Assert.assertTrue(result1.getFeedResultRejections().isEmpty());
		Assert.assertEquals(1, result2.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.UNNABLE_TO_GET_TORRENT, result2.getFeedResultRejections().get(0));
		Assert.assertEquals(1, result3.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.UNNABLE_TO_GET_TORRENT, result3.getFeedResultRejections().get(0));
		Assert.assertEquals(1, result4.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.UNNABLE_TO_GET_TORRENT, result4.getFeedResultRejections().get(0));
		Assert.assertEquals(1, result5.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.UNNABLE_TO_GET_TORRENT, result5.getFeedResultRejections().get(0));
		Assert.assertEquals(1, result6.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.UNNABLE_TO_GET_TORRENT, result6.getFeedResultRejections().get(0));
		Assert.assertEquals(1, result7.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.CONTAINS_COMPRESSED, result7.getFeedResultRejections().get(0));
	}

	@Test(expected = ValidationException.class)
	public void validatResultTorrentsExceptionTest() throws Exception {
		Config config = new Config();
		FeedResult result = new FeedResult();
		result.setTorrentURL("http://test.com");

		TorrentDao dao = Mockito.mock(TorrentDao.class);
		Torrent torrent = new Torrent();
		Mockito.when(dao.read(Mockito.any(URI.class)))
        				.thenReturn(torrent);
		processor.setTorrentDao(dao);
		processor.setActive(true);

		TorrentValidator validator = Mockito.mock(TorrentValidator.class);
		Mockito.when(validator.validate(Mockito.any(Torrent.class),Mockito.any(Show.class), Mockito.any(Config.class)))
        				.thenThrow(ValidationException.class);
		processor.setTorrentValidators(Arrays.asList(validator));

		processor.validateResultTorrents(Arrays.asList(result), null, config);
     
     	Assert.assertEquals(torrent, result.getTorrent());
     
     	Mockito.verify(dao,Mockito.times(1)).read(Mockito.any(URI.class));
		Mockito.verify(validator).validate(Mockito.any(Torrent.class),Mockito.any(Show.class), Mockito.any(Config.class));
	}

  	@Test
  	public void updateShowTest() throws Exception{
     	Show show = new Show();
     	Episode nextEp = new Episode();
     	show.setNextEpisode(nextEp);
     	ShowDao dao = Mockito.mock(ShowDao.class);
     	Mockito.when(dao.readNextEpisode(nextEp)).thenThrow(ShowNotFoundException.class);
     	processor.setShowDao(dao);
     
     	processor.updateShow(show);
     
     	Assert.assertEquals(nextEp, show.getPreviousEpisode());
     	Assert.assertNull(show.getNextEpisode());
     	Assert.assertNotNull(show.getLastFeedUpdate());
     
     	Mockito.verify(dao).readNextEpisode(nextEp);
     	Mockito.verify(dao).createOrUpdate(show); 
   }
  
  	@Test
  	public void processTest() throws Exception{
     	Config config = new Config();
     	processor.setConfig(config);
     
     	Show show = new Show();
     	show.setShowId(1);
     	show.setTitle("TITLE");
     	Episode nextEpisode = new Episode();
     	show.setNextEpisode(nextEpisode);
     	processor.setShow(show);
     
     	ShowDao showDao = Mockito.mock(ShowDao.class);
     	Mockito.when(showDao.readAndLock(1)).thenReturn(show);
     	Episode newNextEpisode = new Episode();
     	Mockito.when(showDao.readNextEpisode(nextEpisode)).thenReturn(newNextEpisode);
     	processor.setShowDao(showDao);
     
     	FeedDao feedDao = Mockito.mock(FeedDao.class);
     	FeedResult feedResult = new FeedResult();
     	feedResult.setTorrentURL("http://test.com");
     	Mockito.when(feedDao.read("TITLE")).thenReturn(Arrays.asList(feedResult));
     	processor.setFeedDaos(Arrays.asList(feedDao));
     	
     	FeedResultValidator feedResultValidator = Mockito.mock(FeedResultValidator.class);
     	processor.setFeedResultValidators(Arrays.asList(feedResultValidator));
     
     	TorrentDao torrentDao = Mockito.mock(TorrentDao.class);
     	Torrent torrent = new Torrent();
     	torrent.setBytes("BYTES".getBytes());
     	TorrentInfo info = new TorrentInfo();
     	info.setName("TORRENT");
     	torrent.setInfo(info);
     	Mockito.when(torrentDao.read(Mockito.any(URI.class))).thenReturn(torrent);
     	processor.setTorrentDao(torrentDao);
     
     	TorrentValidator torrentValidator = Mockito.mock(TorrentValidator.class);
     	Mockito.when(torrentValidator.validate(torrent, show, config)).thenReturn(true);
     	processor.setTorrentValidators(Arrays.asList(torrentValidator));

		@SuppressWarnings("unchecked")
		Comparator<FeedResult> comparator = Mockito.mock(Comparator.class);
		processor.setResultComparator(comparator);
     
     	TorrentWriter torrentWriter = Mockito.mock(TorrentWriter.class);
     	processor.setTorrentWriter(torrentWriter);
     
     	ProcessorListener processorListener = Mockito.mock(ProcessorListener.class);
		processor.addListener(processorListener);

     	processor.process();
  
     	Assert.assertEquals(torrent, feedResult.getTorrent());
     	Assert.assertEquals(0,feedResult.getFeedResultRejections().size());
     	Assert.assertEquals(nextEpisode, show.getPreviousEpisode());
     	Assert.assertEquals(newNextEpisode, show.getNextEpisode());
     
     	Mockito.verify(showDao).readAndLock(1L);
     	Mockito.verify(feedDao).read("TITLE");
     	Mockito.verify(feedResultValidator).validate(Mockito.anyListOf(FeedResult.class), Mockito.any(Show.class));
     	Mockito.verify(torrentDao).read(Mockito.any(URI.class));
    	Mockito.verify(torrentValidator).validate(torrent, show, config);
     	Mockito.verify(comparator, Mockito.times(0)).compare(feedResult, null);
     	Mockito.verify(torrentWriter).write("TORRENT.torrent","BYTES".getBytes(), config);
     	Mockito.verify(showDao).readNextEpisode(nextEpisode);
     	Mockito.verify(processorListener).alertComplete(processor);      	
     	
   }

}
