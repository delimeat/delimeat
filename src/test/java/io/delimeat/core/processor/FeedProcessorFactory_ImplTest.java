package io.delimeat.core.processor;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.processor.validation.TorrentValidator;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowType;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;

public class FeedProcessorFactory_ImplTest {

	public FeedProcessorFactory_Impl factory;
	
	@Before
	public void setUp() throws Exception {
		factory = new FeedProcessorFactory_Impl();
	}

	@Test
	public void beanFactoryTest(){
		Assert.assertNull(factory.getBeanFactory());
		BeanFactory beanFactory = Mockito.mock(BeanFactory.class);
		factory.setBeanFactory(beanFactory);
		Assert.assertEquals(beanFactory, factory.getBeanFactory());
	}

	@Test
	public void processorTypesTest(){
		Assert.assertNotNull(factory.getProcessorTypes());
		Assert.assertTrue(factory.getProcessorTypes().isEmpty());
		Map<ShowType, String> types = new HashMap<ShowType, String>();
		types.put(ShowType.ANIMATED, "VALUE");
		factory.setProcessorTypes(types);
		Assert.assertNotNull(factory.getProcessorTypes());
		Assert.assertEquals(1, factory.getProcessorTypes().size());
		Assert.assertEquals("VALUE", factory.getProcessorTypes().get(ShowType.ANIMATED));
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
	public void buildTest(){
		factory.getProcessorTypes().put(ShowType.SEASON, "STRING");
		
		BeanFactory beanFactory = Mockito.mock(BeanFactory.class);
		FeedProcessor_Impl actualprocessor = new FeedProcessor_Impl();
		Mockito.when(beanFactory.getBean(Mockito.anyString())).thenReturn(actualprocessor);
		factory.setBeanFactory(beanFactory);
		
		TorrentValidator folderTorrentValidator = Mockito.mock(TorrentValidator.class);
		factory.setFolderTorrentValidator(folderTorrentValidator);
		
		@SuppressWarnings("unchecked")
		Comparator<FeedResult> comparator = Mockito.mock(Comparator.class);
		factory.setPreferFilesComparator(comparator);
		
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
		/*
		Assert.assertEquals(showDao, castProcessor.getShowDao());
		Assert.assertEquals(1, castProcessor.getFeedDaos().size());
		Assert.assertEquals(feedDao, castProcessor.getFeedDaos().get(0));
		Assert.assertEquals(1, castProcessor.getFeedResultValidators().size());
		Assert.assertEquals(resultValidator, castProcessor.getFeedResultValidators().get(0));
		*/
		Assert.assertEquals(1, castProcessor.getTorrentValidators().size());
		Assert.assertEquals(folderTorrentValidator, castProcessor.getTorrentValidators().get(0));
		Assert.assertEquals(comparator, castProcessor.getResultComparator());
		/*
		Assert.assertEquals(writer, castProcessor.getTorrentWriter());
		*/
	}
}
