package io.delimeat.core.service;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedDao;
import io.delimeat.core.feed.FeedException;
import io.delimeat.core.feed.FeedProcessor;
import io.delimeat.core.feed.FeedProcessorStatus;
import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.feed.FeedResultWriter;
import io.delimeat.core.feed.validation.FeedResultValidator;
import io.delimeat.core.feed.validation.FeedValidationException;
import io.delimeat.core.feed.validation.TorrentValidator;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowType;
import io.delimeat.core.torrent.Torrent;
import io.delimeat.core.torrent.TorrentDao;
import io.delimeat.core.torrent.TorrentException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class FeedProcessorService_ImplTest {

	private FeedProcessorService_Impl service;
	
	@Before
	public void setUp() throws Exception {
		service =  new FeedProcessorService_Impl();
	}
	
	@Test
	public void configServiceTest(){
		Assert.assertNull(service.getConfigService());
		ConfigService mockedService = Mockito.mock(ConfigService.class);
		service.setConfigService(mockedService);
		Assert.assertEquals(mockedService, service.getConfigService());
	}
	
	@Test
	public void showServiceTest(){
		Assert.assertNull(service.getShowService());
		ShowService mockedService = Mockito.mock(ShowService.class);
		service.setShowService(mockedService);
		Assert.assertEquals(mockedService, service.getShowService());
	}

	@Test
	public void guideServiceTest(){
		Assert.assertNull(service.getGuideService());
		GuideService mockedService = Mockito.mock(GuideService.class);
		service.setGuideService(mockedService);
		Assert.assertEquals(mockedService, service.getGuideService());
	}
	
	@Test
	public void feedDaosTest(){
		Assert.assertNull(service.getFeedDaos());
		FeedDao mockedDao = Mockito.mock(FeedDao.class);
		service.setFeedDaos(Arrays.asList(mockedDao));
		Assert.assertNotNull(service.getFeedDaos());
		Assert.assertFalse(service.getFeedDaos().isEmpty());
		Assert.assertEquals(1, service.getFeedDaos().size());
		Assert.assertEquals(mockedDao, service.getFeedDaos().get(0));
	}
	
	@Test
	public void torrentDaoTest(){
		Assert.assertNull(service.getTorrentDao());
		TorrentDao mockedDao = Mockito.mock(TorrentDao.class);
		service.setTorrentDao(mockedDao);
		Assert.assertEquals(mockedDao, service.getTorrentDao());
	}
	
	@Test
	public void dailyFeedResultValidatorsTest(){
		Assert.assertNull(service.getDailyFeedResultValidators());
		FeedResultValidator mockedValidator = Mockito.mock(FeedResultValidator.class);
		service.setDailyFeedResultValidators(Arrays.asList(mockedValidator));
		Assert.assertNotNull(service.getDailyFeedResultValidators());
		Assert.assertFalse(service.getDailyFeedResultValidators().isEmpty());
		Assert.assertEquals(1, service.getDailyFeedResultValidators().size());
		Assert.assertEquals(mockedValidator, service.getDailyFeedResultValidators().get(0));
	}
	
	@Test
	public void seasonFeedResultValidatorsTest(){
		Assert.assertNull(service.getSeasonFeedResultValidators());
		FeedResultValidator mockedValidator = Mockito.mock(FeedResultValidator.class);
		service.setSeasonFeedResultValidators(Arrays.asList(mockedValidator));
		Assert.assertNotNull(service.getSeasonFeedResultValidators());
		Assert.assertFalse(service.getSeasonFeedResultValidators().isEmpty());
		Assert.assertEquals(1, service.getSeasonFeedResultValidators().size());
		Assert.assertEquals(mockedValidator, service.getSeasonFeedResultValidators().get(0));		
	}
	
	@Test
	public void torrentValidatorsTest(){
		Assert.assertNull(service.getTorrentValidators());
		TorrentValidator mockedValidator = Mockito.mock(TorrentValidator.class);
		service.setTorrentValidators(Arrays.asList(mockedValidator));
		Assert.assertNotNull(service.getTorrentValidators());
		Assert.assertFalse(service.getTorrentValidators().isEmpty());
		Assert.assertEquals(1, service.getTorrentValidators().size());
		Assert.assertEquals(mockedValidator, service.getTorrentValidators().get(0));		
	}
	
	@Test
	public void folderTorrentValidatorTest(){
		Assert.assertNull(service.getFolderTorrentValidator());
		TorrentValidator mockedValidator = Mockito.mock(TorrentValidator.class);
		service.setFolderTorrentValidator(mockedValidator);
		Assert.assertEquals(mockedValidator, service.getFolderTorrentValidator());		
	}
	
	@Test
	public void preferFilesComparatorTest(){
		Assert.assertNull(service.getPreferFilesComparator());
		@SuppressWarnings("unchecked")
		Comparator<FeedResult> mockedComparator = Mockito.mock(Comparator.class);
		service.setPreferFilesComparator(mockedComparator);
		Assert.assertEquals(mockedComparator, service.getPreferFilesComparator());		
	}
	
	@Test
	public void maxSeedersComparatorTest(){
		Assert.assertNull(service.getMaxSeedersComparator());
		@SuppressWarnings("unchecked")
		Comparator<FeedResult> mockedComparator = Mockito.mock(Comparator.class);
		service.setMaxSeedersComparator(mockedComparator);
		Assert.assertEquals(mockedComparator, service.getMaxSeedersComparator());		
	}
	
	@Test
	public void feedResultWriterTest(){
		Assert.assertNull(service.getFeedResultWriter());
		FeedResultWriter mockedWriter = Mockito.mock(FeedResultWriter.class);
		service.setFeedResultWriter(mockedWriter);
		Assert.assertEquals(mockedWriter, service.getFeedResultWriter());
	}
	
	@Test
	public void fetchResultsNotStartedTest() throws FeedException{	
		FeedDao dao = Mockito.mock(FeedDao.class);
		service.setFeedDaos(Arrays.asList(dao));
		
		FeedProcessor processor = Mockito.mock(FeedProcessor.class);
		Mockito.when(processor.getStatus()).thenReturn(FeedProcessorStatus.ENDED_ABORT);
		
		Show show = new Show();
		show.setTitle("TITLE");
		
		List<FeedResult> results = service.fetchResults(processor, show);
		
		Mockito.verifyZeroInteractions(dao);
		Assert.assertTrue(results.isEmpty());
	}
	@Test
	public void fetchResultsSuccessTest() throws FeedException{
		FeedDao dao = Mockito.mock(FeedDao.class);
		FeedResult feedResult = new FeedResult();
		feedResult.setTitle("TITLE");
		Mockito.when(dao.read(Mockito.anyString())).thenReturn(Arrays.asList(feedResult));
		service.setFeedDaos(Arrays.asList(dao));
		
		FeedProcessor processor = Mockito.mock(FeedProcessor.class);
		Mockito.when(processor.getStatus()).thenReturn(FeedProcessorStatus.STARTED);
		
		Show show = new Show();
		show.setTitle("TITLE");
		
		List<FeedResult> results = service.fetchResults(processor, show);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(feedResult, results.get(0));
	}
	
	@Test
	public void fetchResultsOneErrorTest() throws FeedException{
		FeedDao dao = Mockito.mock(FeedDao.class);
		FeedResult feedResult = new FeedResult();
		feedResult.setTitle("TITLE");
		Mockito.when(dao.read(Mockito.anyString())).thenThrow(FeedException.class).thenReturn(Arrays.asList(feedResult));
		service.setFeedDaos(Arrays.asList(dao,dao));
		
		FeedProcessor processor = Mockito.mock(FeedProcessor.class);
		Mockito.when(processor.getStatus()).thenReturn(FeedProcessorStatus.STARTED);
		
		Show show = new Show();
		show.setTitle("TITLE");
		
		List<FeedResult> results = service.fetchResults(processor, show);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(feedResult, results.get(0));
	}
	
	@Test
	public void validateFeedResultsNotStartedTest() throws FeedValidationException{
		FeedResultValidator validator = Mockito.mock(FeedResultValidator.class);
		service.setSeasonFeedResultValidators(Arrays.asList(validator));
		
		FeedProcessor processor = Mockito.mock(FeedProcessor.class);
		Mockito.when(processor.getStatus()).thenReturn(FeedProcessorStatus.ENDED_ABORT);
		
		Show show = new Show();
		show.setShowType(ShowType.SEASON);
		
		service.validateFeedResults(processor, show, new ArrayList<FeedResult>());
		
		Mockito.verifyZeroInteractions(validator);
	}
	
	@Test
	public void validateFeedResultsSeasonTest() throws FeedValidationException{
		FeedResultValidator validator = Mockito.mock(FeedResultValidator.class);
		service.setSeasonFeedResultValidators(Arrays.asList(validator));
		
		FeedProcessor processor = Mockito.mock(FeedProcessor.class);
		Mockito.when(processor.getStatus()).thenReturn(FeedProcessorStatus.STARTED);
		
		Show show = new Show();
		show.setShowType(ShowType.SEASON);
		
		FeedResult result1 = new FeedResult();
		result1.getFeedResultRejections().add(FeedResultRejection.CONTAINS_COMPRESSED);
		FeedResult result2 = new FeedResult();
		
		List<FeedResult> inResults = Arrays.asList(result1,result2);
		
		List<FeedResult> outResults = service.validateFeedResults(processor, show, inResults);
		Assert.assertEquals(1, outResults.size());
		Assert.assertEquals(result2, outResults.get(0));
	}
	
	@Test
	public void validateFeedResultsDailyTest() throws FeedValidationException{
		FeedResultValidator validator = Mockito.mock(FeedResultValidator.class);
		service.setDailyFeedResultValidators(Arrays.asList(validator));
		
		FeedProcessor processor = Mockito.mock(FeedProcessor.class);
		Mockito.when(processor.getStatus()).thenReturn(FeedProcessorStatus.STARTED);
		
		Show show = new Show();
		show.setShowType(ShowType.DAILY);
		
		FeedResult result1 = new FeedResult();
		FeedResult result2 = new FeedResult();
		result2.getFeedResultRejections().add(FeedResultRejection.CONTAINS_COMPRESSED);
		
		List<FeedResult> inResults = Arrays.asList(result1,result2);
		
		List<FeedResult> outResults = service.validateFeedResults(processor, show, inResults);
		Assert.assertEquals(1, outResults.size());
		Assert.assertEquals(result1, outResults.get(0));
	}

	@Test(expected=MalformedURLException.class)
	public void fetchTorrentMalformedURLTest() throws IOException, TorrentException, URISyntaxException{
		FeedResult result = new FeedResult();
		result.setTorrentURL("MALFORMEDURL");
		
		service.fetchTorrent(result);		
	}
	
	@Test(expected=URISyntaxException.class)
	public void fetchTorrentURISyntaxTest() throws IOException, TorrentException, URISyntaxException{
		FeedResult result = new FeedResult();
		result.setTorrentURL("http:URISYNTAXEXCEPTION");
		
		service.fetchTorrent(result);		
	}
	
	@Test
	public void fetchTorrentTest() throws IOException, TorrentException, URISyntaxException{
		FeedResult result = new FeedResult();
		result.setTorrentURL("http://test.com?removed=true");
		
		TorrentDao dao = Mockito.mock(TorrentDao.class);
		Torrent torrent = new Torrent();
		Mockito.when(dao.read(new URI("http://test.com"))).thenReturn(torrent);
		service.setTorrentDao(dao);
		
		Torrent outTorrent = service.fetchTorrent(result);	
		Assert.assertEquals(torrent,outTorrent);		
	}
	
	@Test(expected=TorrentException.class)
	public void fetchTorrentDaoExceptionTest() throws IOException, TorrentException, URISyntaxException{
		FeedResult result = new FeedResult();
		result.setTorrentURL("http://test.com?removed=true");
		
		TorrentDao dao = Mockito.mock(TorrentDao.class);
		Mockito.when(dao.read(new URI("http://test.com"))).thenThrow(TorrentException.class);
		service.setTorrentDao(dao);
		
		service.fetchTorrent(result);	
	}
	
	@Test
	public void validateResultTorrentNotStartedTest() throws FeedValidationException{
		TorrentValidator validator = Mockito.mock(TorrentValidator.class);
		service.setTorrentValidators(Arrays.asList(validator));

		FeedProcessor processor = Mockito.mock(FeedProcessor.class);
		Mockito.when(processor.getStatus()).thenReturn(FeedProcessorStatus.ENDED_ABORT);
		
		FeedResult result = new FeedResult();
		
		Config config = new Config();

		service.validateResultTorrent(processor, result, null, config, null);
		Mockito.verifyZeroInteractions(validator);
	}
	
	@Test
	public void validateResultTorrentTest() throws FeedValidationException{
		TorrentValidator validator = Mockito.mock(TorrentValidator.class);
		Mockito.when(validator.validate(Mockito.any(Torrent.class), Mockito.any(Show.class),  Mockito.any(Config.class))).thenReturn(true).thenReturn(false);
		Mockito.when(validator.getRejection()).thenReturn(FeedResultRejection.CONTAINS_COMPRESSED);
		service.setTorrentValidators(Arrays.asList(validator,validator));

		FeedProcessor processor = Mockito.mock(FeedProcessor.class);
		Mockito.when(processor.getStatus()).thenReturn(FeedProcessorStatus.STARTED);
		
		FeedResult result = new FeedResult();
		
		Config config = new Config();
		config.setIgnoreFolders(false);

		service.validateResultTorrent(processor, result, null, config, null);
		Mockito.verify(validator,Mockito.times(2)).validate(Mockito.any(Torrent.class), Mockito.any(Show.class),  Mockito.any(Config.class));
		Assert.assertEquals(1, result.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.CONTAINS_COMPRESSED, result.getFeedResultRejections().get(0));
	}
	
	@Test
	public void validateResultTorrentIgnoreFoldersTest() throws FeedValidationException{
		TorrentValidator validator = Mockito.mock(TorrentValidator.class);
		Mockito.when(validator.validate(Mockito.any(Torrent.class), Mockito.any(Show.class),  Mockito.any(Config.class))).thenReturn(true).thenReturn(true).thenReturn(false);
		Mockito.when(validator.getRejection()).thenReturn(FeedResultRejection.CONTAINS_COMPRESSED);
		service.setTorrentValidators(Arrays.asList(validator,validator));
		service.setFolderTorrentValidator(validator);

		FeedProcessor processor = Mockito.mock(FeedProcessor.class);
		Mockito.when(processor.getStatus()).thenReturn(FeedProcessorStatus.STARTED);
		
		FeedResult result = new FeedResult();
		
		Config config = new Config();
		config.setIgnoreFolders(true);

		service.validateResultTorrent(processor, result, null, config, null);
		Mockito.verify(validator,Mockito.times(3)).validate(Mockito.any(Torrent.class), Mockito.any(Show.class),  Mockito.any(Config.class));
		Assert.assertEquals(1, result.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.CONTAINS_COMPRESSED, result.getFeedResultRejections().get(0));
	}
	
	@Test(expected=FeedValidationException.class)
	public void validateResultTorrentExceptionTest() throws FeedValidationException{
		TorrentValidator validator = Mockito.mock(TorrentValidator.class);
		Mockito.when(validator.validate(Mockito.any(Torrent.class), Mockito.any(Show.class),  Mockito.any(Config.class))).thenThrow(FeedValidationException.class);
		service.setTorrentValidators(Arrays.asList(validator,validator));

		FeedProcessor processor = Mockito.mock(FeedProcessor.class);
		Mockito.when(processor.getStatus()).thenReturn(FeedProcessorStatus.STARTED);
		
		FeedResult result = new FeedResult();
		
		Config config = new Config();

		service.validateResultTorrent(processor, result, null, config, null);
	}	
	
	@Test
	public void selectResultNullResultsTest(){
		Config config = new Config();
		Assert.assertNull( service.selectResult(null,config) );
	}
	
	@Test
	public void selectResultEmptyResultsTest(){
		List<FeedResult> results = new ArrayList<FeedResult>();
		Config config = new Config();
		Assert.assertNull( service.selectResult(results,config) );		
	}
	
	@Test
	public void selectResultTest(){
		FeedResult result1 = new FeedResult();
		FeedResult result2 = new FeedResult();
		
		Config config = new Config();
		config.setPreferFiles(false);
		
		@SuppressWarnings("unchecked")
		Comparator<FeedResult> comparator = Mockito.mock(Comparator.class);
		
		service.setMaxSeedersComparator(comparator);
		
		FeedResult result = service.selectResult(Arrays.asList(result1,result2),config);	
		
		Assert.assertNotNull(result);
		Assert.assertEquals(result1, result);
		
	}

	@Test
	public void selectResultPreferFilesTest(){
		FeedResult result1 = new FeedResult();
		FeedResult result2 = new FeedResult();
		
		Config config = new Config();
		config.setPreferFiles(true);
		
		@SuppressWarnings("unchecked")
		Comparator<FeedResult> comparator = Mockito.mock(Comparator.class);
		
		service.setPreferFilesComparator(comparator);
		
		FeedResult result = service.selectResult(Arrays.asList(result1,result2),config);	
		
		Assert.assertNotNull(result);
		Assert.assertEquals(result1, result);
		
	}
}
