package io.delimeat.core.processor;

import java.util.Arrays;
import java.util.Comparator;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedDao;
import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.processor.FeedProcessorFactory_Impl;
import io.delimeat.core.processor.FeedProcessor_Impl;
import io.delimeat.core.processor.validation.FeedResultValidator;
import io.delimeat.core.processor.validation.TorrentValidator;
import io.delimeat.core.processor.writer.TorrentWriter;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowDao;
import io.delimeat.core.show.ShowType;
import io.delimeat.core.torrent.TorrentDao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class FeedProcessorFactory_ImplTest {

	public FeedProcessorFactory_Impl factory;
	
	@Before
	public void setUp() throws Exception {
		factory = new FeedProcessorFactory_Impl();
	}
	
	@Test
	public void showDaoTest(){
		Assert.assertNull(factory.getShowDao());
		ShowDao dao = Mockito.mock(ShowDao.class);
		factory.setShowDao(dao);
		Assert.assertEquals(dao, factory.getShowDao());
	}
	
	@Test
	public void feeDaosTest(){
		Assert.assertNotNull(factory.getFeedDaos());
		Assert.assertTrue(factory.getFeedDaos().isEmpty());
		FeedDao dao = Mockito.mock(FeedDao.class);
		factory.setFeedDaos(Arrays.asList(dao));
		Assert.assertEquals(1,factory.getFeedDaos().size());
		Assert.assertEquals(dao, factory.getFeedDaos().get(0));
	}
	
	@Test
	public void torrentDaoTest(){
		Assert.assertNull(factory.getTorrentDao());
		TorrentDao dao = Mockito.mock(TorrentDao.class);
		factory.setTorrentDao(dao);
		Assert.assertEquals(dao, factory.getTorrentDao());
	}
	
	@Test
	public void dailyResultValidatorsTest(){
		Assert.assertNotNull(factory.getDailyFeedResultValidators());
		Assert.assertTrue(factory.getDailyFeedResultValidators().isEmpty());
		FeedResultValidator validator = Mockito.mock(FeedResultValidator.class);
		factory.setDailyFeedResultValidators(Arrays.asList(validator));
		Assert.assertEquals(1,factory.getDailyFeedResultValidators().size());
		Assert.assertEquals(validator, factory.getDailyFeedResultValidators().get(0));
	}
	
	@Test
	public void seasonResultValidatorsTest(){
		Assert.assertNotNull(factory.getSeasonFeedResultValidators());
		Assert.assertTrue(factory.getSeasonFeedResultValidators().isEmpty());
		FeedResultValidator validator = Mockito.mock(FeedResultValidator.class);
		factory.setSeasonFeedResultValidators(Arrays.asList(validator));
		Assert.assertEquals(1,factory.getSeasonFeedResultValidators().size());
		Assert.assertEquals(validator, factory.getSeasonFeedResultValidators().get(0));
	}
	
	@Test
	public void torrentValidatorsTest(){
		Assert.assertNotNull(factory.getTorrentValidators());
		Assert.assertTrue(factory.getTorrentValidators().isEmpty());
		TorrentValidator validator = Mockito.mock(TorrentValidator.class);
		factory.setTorrentValidators(Arrays.asList(validator));
		Assert.assertEquals(1,factory.getTorrentValidators().size());
		Assert.assertEquals(validator, factory.getTorrentValidators().get(0));
	}

	@Test
	public void folderTorrentValidatorTest(){
		Assert.assertNull(factory.getFolderTorrentValidator());
		TorrentValidator validator = Mockito.mock(TorrentValidator.class);
		factory.setFolderTorrentValidator(validator);
		Assert.assertEquals(validator, factory.getFolderTorrentValidator());
	}
	
	@Test
	public void preferFilesComparatorTest(){
		Assert.assertNull(factory.getPreferFilesComparator());
		@SuppressWarnings("unchecked")
		Comparator<FeedResult> comparator = Mockito.mock(Comparator.class);
		factory.setPreferFilesComparator(comparator);
		Assert.assertEquals(comparator, factory.getPreferFilesComparator());
	}
	
	@Test
	public void maxSeedersComparatorTest(){
		Assert.assertNull(factory.getMaxSeedersComparator());
		@SuppressWarnings("unchecked")
		Comparator<FeedResult> comparator = Mockito.mock(Comparator.class);
		factory.setMaxSeedersComparator(comparator);
		Assert.assertEquals(comparator, factory.getMaxSeedersComparator());
	}
	
	@Test
	public void feedResultWriterTest(){
		Assert.assertNull(factory.getTorrentWriter());
		TorrentWriter writer = Mockito.mock(TorrentWriter.class);
		factory.setTorrentWriter(writer);
		Assert.assertEquals(writer, factory.getTorrentWriter());
	}
	
	@Test
	public void buildSeasonTest(){
		ShowDao showDao = Mockito.mock(ShowDao.class);
		factory.setShowDao(showDao);	
	
		FeedDao feedDao = Mockito.mock(FeedDao.class);
		factory.setFeedDaos(Arrays.asList(feedDao));
		
		TorrentDao torrentDao = Mockito.mock(TorrentDao.class);
		factory.setTorrentDao(torrentDao);
		
		FeedResultValidator resultValidator = Mockito.mock(FeedResultValidator.class);
		factory.setSeasonFeedResultValidators(Arrays.asList(resultValidator));
		
		TorrentValidator torrentValidator = Mockito.mock(TorrentValidator.class);
		factory.setTorrentValidators(Arrays.asList(torrentValidator));
		
		TorrentValidator folderTorrentValidator = Mockito.mock(TorrentValidator.class);
		factory.setFolderTorrentValidator(folderTorrentValidator);
		
		@SuppressWarnings("unchecked")
		Comparator<FeedResult> comparator = Mockito.mock(Comparator.class);
		factory.setPreferFilesComparator(comparator);
		
		TorrentWriter writer = Mockito.mock(TorrentWriter.class);
		factory.setTorrentWriter(writer);
		
		Show show = new Show();
		show.setShowType(ShowType.SEASON);
		
		Config config = new Config();
		config.setIgnoreFolders(true);
		config.setPreferFiles(true);
		
		Processor processor = factory.build(show, config);
		Assert.assertTrue(processor instanceof FeedProcessor_Impl);
		FeedProcessor_Impl castProcessor = (FeedProcessor_Impl)processor;
		Assert.assertEquals(show, castProcessor.getShow());
		Assert.assertEquals(config, castProcessor.getConfig());
		Assert.assertEquals(showDao, castProcessor.getShowDao());
		Assert.assertEquals(1, castProcessor.getFeedDaos().size());
		Assert.assertEquals(feedDao, castProcessor.getFeedDaos().get(0));
		Assert.assertEquals(1, castProcessor.getFeedResultValidators().size());
		Assert.assertEquals(resultValidator, castProcessor.getFeedResultValidators().get(0));
		Assert.assertEquals(2, castProcessor.getTorrentValidators().size());
		Assert.assertEquals(torrentValidator, castProcessor.getTorrentValidators().get(0));
		Assert.assertEquals(folderTorrentValidator, castProcessor.getTorrentValidators().get(1));
		Assert.assertEquals(comparator, castProcessor.getResultComparator());
		Assert.assertEquals(writer, castProcessor.getTorrentWriter());
	}
	
	@Test
	public void buildDailyTest(){
		ShowDao showDao = Mockito.mock(ShowDao.class);
		factory.setShowDao(showDao);	
	
		FeedDao feedDao = Mockito.mock(FeedDao.class);
		factory.setFeedDaos(Arrays.asList(feedDao));
		
		TorrentDao torrentDao = Mockito.mock(TorrentDao.class);
		factory.setTorrentDao(torrentDao);
		
		FeedResultValidator resultValidator = Mockito.mock(FeedResultValidator.class);
		factory.setDailyFeedResultValidators(Arrays.asList(resultValidator));
		
		TorrentValidator torrentValidator = Mockito.mock(TorrentValidator.class);
		factory.setTorrentValidators(Arrays.asList(torrentValidator));
		
		@SuppressWarnings("unchecked")
		Comparator<FeedResult> comparator = Mockito.mock(Comparator.class);
		factory.setMaxSeedersComparator(comparator);
		
		TorrentWriter writer = Mockito.mock(TorrentWriter.class);
		factory.setTorrentWriter(writer);
		
		Show show = new Show();
		show.setShowType(ShowType.DAILY);
		
		Config config = new Config();
		config.setIgnoreFolders(false);
		config.setPreferFiles(false);
		
		Processor processor = factory.build(show, config);
		Assert.assertTrue(processor instanceof FeedProcessor_Impl);
		FeedProcessor_Impl castProcessor = (FeedProcessor_Impl)processor;
		Assert.assertEquals(show, castProcessor.getShow());
		Assert.assertEquals(config, castProcessor.getConfig());
		Assert.assertEquals(showDao, castProcessor.getShowDao());
		Assert.assertEquals(1, castProcessor.getFeedDaos().size());
		Assert.assertEquals(feedDao, castProcessor.getFeedDaos().get(0));
		Assert.assertEquals(1, castProcessor.getFeedResultValidators().size());
		Assert.assertEquals(resultValidator, castProcessor.getFeedResultValidators().get(0));
		Assert.assertEquals(1, castProcessor.getTorrentValidators().size());
		Assert.assertEquals(torrentValidator, castProcessor.getTorrentValidators().get(0));
		Assert.assertEquals(comparator, castProcessor.getResultComparator());
		Assert.assertEquals(writer, castProcessor.getTorrentWriter());
	}
}
