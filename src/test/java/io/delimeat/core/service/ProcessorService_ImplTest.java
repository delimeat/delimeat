package io.delimeat.core.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.Executor;

import io.delimeat.core.config.Config;
import io.delimeat.core.config.ConfigDao;
import io.delimeat.core.config.ConfigException;
import io.delimeat.core.processor.Processor;
import io.delimeat.core.processor.ProcessorFactory;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowDao;
import io.delimeat.core.show.ShowException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ProcessorService_ImplTest {

	private ProcessorService_Impl service;
	
	@Before
	public void setUp() throws Exception {
		service = new ProcessorService_Impl();
	}
	
	@Test
	public void showDaoTest() {
		Assert.assertNull(service.getShowDao());
		ShowDao mockedShowDao = Mockito.mock(ShowDao.class);
		service.setShowDao(mockedShowDao);
		Assert.assertEquals(mockedShowDao, service.getShowDao());
	}
	
	@Test
	public void configDaoTest() {
		Assert.assertNull(service.getConfigDao());
		ConfigDao mockedDao = Mockito.mock(ConfigDao.class);
		service.setConfigDao(mockedDao);
		Assert.assertEquals(mockedDao, service.getConfigDao());
	}
	
	@Test
	public void executorTest() {
		Assert.assertNull(service.getExecutor());
		Executor executor = Mockito.mock(Executor.class);
		service.setExecutor(executor);
		Assert.assertEquals(executor, service.getExecutor());
	}
	
	@Test
	public void feedProcessorFactoryTest() {
		Assert.assertNull(service.getFeedProcessorFactory());
		ProcessorFactory factory = Mockito.mock(ProcessorFactory.class);
		service.setFeedProcessorFactory(factory);
		Assert.assertEquals(factory, service.getFeedProcessorFactory());
	}
	
	@Test
	public void guideProcessorFactoryTest() {
		Assert.assertNull(service.getGuideProcessorFactory());
		ProcessorFactory factory = Mockito.mock(ProcessorFactory.class);
		service.setGuideProcessorFactory(factory);
		Assert.assertEquals(factory, service.getGuideProcessorFactory());
	}
  
  	@Test
  	public void processAllGuideUpdatesNullShowsTest() throws Exception{
     	ShowDao showDao = Mockito.mock(ShowDao.class);
      Mockito.when(showDao.readAll()).thenReturn(null);
     	service.setShowDao(showDao);
     
      ConfigDao configDao = Mockito.mock(ConfigDao.class);
      service.setConfigDao(configDao);
     
		ProcessorFactory factory = Mockito.mock(ProcessorFactory.class);
     	service.setGuideProcessorFactory(factory);
     
		Executor executor = Mockito.mock(Executor.class);
		service.setExecutor(executor);
     
      service.processAllGuideUpdates();
     
      Assert.assertEquals(0, service.getProcessors().size()); 
     
      Mockito.verify(showDao, Mockito.times(1)).readAll();
      Mockito.verify(configDao, Mockito.times(0)).read();
      Mockito.verify(factory,  Mockito.times(0)).build(Mockito.any(Show.class),Mockito.any(Config.class));
      Mockito.verify(executor, Mockito.times(0)).execute(Mockito.any(Runnable.class));
   }
  
  	@Test
  	public void processAllGuideUpdatesNoShowsTest() throws Exception{
     	ShowDao showDao = Mockito.mock(ShowDao.class);
      Mockito.when(showDao.readAll()).thenReturn(Collections.<Show>emptyList());
     	service.setShowDao(showDao);  
     
      ConfigDao configDao = Mockito.mock(ConfigDao.class);
      service.setConfigDao(configDao);
     
		ProcessorFactory factory = Mockito.mock(ProcessorFactory.class);
     	service.setGuideProcessorFactory(factory);
     
		Executor executor = Mockito.mock(Executor.class);
		service.setExecutor(executor);
     
      service.processAllGuideUpdates();
   
      Assert.assertEquals(0, service.getProcessors().size()); 

      Mockito.verify(showDao, Mockito.times(1)).readAll();
      Mockito.verify(configDao, Mockito.times(0)).read();
      Mockito.verify(factory,  Mockito.times(0)).build(Mockito.any(Show.class),Mockito.any(Config.class));
      Mockito.verify(executor, Mockito.times(0)).execute(Mockito.any(Runnable.class));
   }
  
	@Test
  	public void processAllGuideUpdatesShowExceptionTest() throws Exception{
     	ShowDao showDao = Mockito.mock(ShowDao.class);
      Mockito.when(showDao.readAll()).thenThrow(ShowException.class);
     	service.setShowDao(showDao);
     
      ConfigDao configDao = Mockito.mock(ConfigDao.class);
      service.setConfigDao(configDao);
     
		ProcessorFactory factory = Mockito.mock(ProcessorFactory.class);
     	service.setGuideProcessorFactory(factory);
     
		Executor executor = Mockito.mock(Executor.class);
		service.setExecutor(executor);
     
     	try{
      	service.processAllGuideUpdates();
      }catch(Exception ex){
        Assert.assertTrue(ex instanceof ShowException);
      }
     
      Assert.assertEquals(0, service.getProcessors().size()); 
     
      Mockito.verify(showDao, Mockito.times(1)).readAll();
      Mockito.verify(configDao, Mockito.times(0)).read();
      Mockito.verify(factory,  Mockito.times(0)).build(Mockito.any(Show.class),Mockito.any(Config.class));
      Mockito.verify(executor, Mockito.times(0)).execute(Mockito.any(Runnable.class));
   }

	@Test
  	public void processAllGuideUpdatesConfigExceptionTest() throws Exception{
     	ShowDao showDao = Mockito.mock(ShowDao.class);
      Show show = new Show();
      Mockito.when(showDao.readAll()).thenReturn(Arrays.asList(show));
     	service.setShowDao(showDao);
     
      ConfigDao configDao = Mockito.mock(ConfigDao.class);
      Mockito.when(configDao.read()).thenThrow(ConfigException.class);
      service.setConfigDao(configDao);
     
		ProcessorFactory factory = Mockito.mock(ProcessorFactory.class);
     	service.setGuideProcessorFactory(factory);
     
		Executor executor = Mockito.mock(Executor.class);
		service.setExecutor(executor);
     
     	try{
      	service.processAllGuideUpdates();
      }catch(Exception ex){
        Assert.assertTrue(ex instanceof ConfigException);
      }
     
      Assert.assertEquals(0, service.getProcessors().size()); 
     
      Mockito.verify(showDao, Mockito.times(1)).readAll();
      Mockito.verify(configDao, Mockito.times(1)).read();
      Mockito.verify(factory,  Mockito.times(0)).build(Mockito.any(Show.class),Mockito.any(Config.class));
      Mockito.verify(executor, Mockito.times(0)).execute(Mockito.any(Runnable.class));
   }
  
	@Test
  	public void processAllGuideUpdatesTest() throws Exception{
     	ShowDao showDao = Mockito.mock(ShowDao.class);
      Show show = new Show();
      Mockito.when(showDao.readAll()).thenReturn(Arrays.asList(show,show));
     	service.setShowDao(showDao);
     
      ConfigDao configDao = Mockito.mock(ConfigDao.class);
      Config config = new Config();
      Mockito.when(configDao.read()).thenReturn(config);
      service.setConfigDao(configDao);
     
		ProcessorFactory factory = Mockito.mock(ProcessorFactory.class);
      Processor processor = Mockito.mock(Processor.class);
     	Mockito.when(factory.build(Mockito.any(Show.class),Mockito.any(Config.class))).thenReturn(processor);
     	service.setGuideProcessorFactory(factory);
     
		Executor executor = Mockito.mock(Executor.class);
		service.setExecutor(executor);
     
     	service.processAllGuideUpdates();
     
     	Assert.assertEquals(2, service.getProcessors().size());
      Assert.assertEquals(processor, service.getProcessors().get(0));
      Assert.assertEquals(processor, service.getProcessors().get(1));     
     
      Mockito.verify(showDao, Mockito.times(1)).readAll();
      Mockito.verify(configDao, Mockito.times(1)).read();
      Mockito.verify(factory,  Mockito.times(2)).build(Mockito.any(Show.class),Mockito.any(Config.class));
      Mockito.verify(executor, Mockito.times(2)).execute(Mockito.any(Runnable.class));
   }
  
  	@Test
  	public void processAllFeedUpdatesNullShowsTest() throws Exception{
     	ShowDao showDao = Mockito.mock(ShowDao.class);
      Mockito.when(showDao.readAll()).thenReturn(null);
     	service.setShowDao(showDao);
     
      ConfigDao configDao = Mockito.mock(ConfigDao.class);
      service.setConfigDao(configDao);
     
		ProcessorFactory factory = Mockito.mock(ProcessorFactory.class);
     	service.setFeedProcessorFactory(factory);
     
		Executor executor = Mockito.mock(Executor.class);
		service.setExecutor(executor);
     
      service.processAllFeedUpdates();
     
      Assert.assertEquals(0, service.getProcessors().size()); 
     
      Mockito.verify(showDao, Mockito.times(1)).readAll();
      Mockito.verify(configDao, Mockito.times(0)).read();
      Mockito.verify(factory,  Mockito.times(0)).build(Mockito.any(Show.class),Mockito.any(Config.class));
      Mockito.verify(executor, Mockito.times(0)).execute(Mockito.any(Runnable.class));
   }
  
  	@Test
  	public void processAllFeedUpdatesNoShowsTest() throws Exception{
     	ShowDao showDao = Mockito.mock(ShowDao.class);
      Mockito.when(showDao.readAll()).thenReturn(Collections.<Show>emptyList());
     	service.setShowDao(showDao);  
     
      ConfigDao configDao = Mockito.mock(ConfigDao.class);
      service.setConfigDao(configDao);
     
		ProcessorFactory factory = Mockito.mock(ProcessorFactory.class);
     	service.setFeedProcessorFactory(factory);
     
		Executor executor = Mockito.mock(Executor.class);
		service.setExecutor(executor);
     
      service.processAllFeedUpdates();
 
      Assert.assertEquals(0, service.getProcessors().size()); 
     
      Mockito.verify(showDao, Mockito.times(1)).readAll();
      Mockito.verify(configDao, Mockito.times(0)).read();
      Mockito.verify(factory,  Mockito.times(0)).build(Mockito.any(Show.class),Mockito.any(Config.class));
      Mockito.verify(executor, Mockito.times(0)).execute(Mockito.any(Runnable.class));
   }
  
	@Test
  	public void processAllFeedUpdatesShowExceptionTest() throws Exception{
     	ShowDao showDao = Mockito.mock(ShowDao.class);
      Mockito.when(showDao.readAll()).thenThrow(ShowException.class);
     	service.setShowDao(showDao);
     
      ConfigDao configDao = Mockito.mock(ConfigDao.class);
      service.setConfigDao(configDao);
     
		ProcessorFactory factory = Mockito.mock(ProcessorFactory.class);
     	service.setFeedProcessorFactory(factory);
     
		Executor executor = Mockito.mock(Executor.class);
		service.setExecutor(executor);
     
     	try{
      	service.processAllFeedUpdates();
      }catch(Exception ex){
        Assert.assertTrue(ex instanceof ShowException);
      }

      Assert.assertEquals(0, service.getProcessors().size()); 

      Mockito.verify(showDao, Mockito.times(1)).readAll();
      Mockito.verify(configDao, Mockito.times(0)).read();
      Mockito.verify(factory,  Mockito.times(0)).build(Mockito.any(Show.class),Mockito.any(Config.class));
      Mockito.verify(executor, Mockito.times(0)).execute(Mockito.any(Runnable.class));
   }

	@Test
  	public void processAllFeedUpdatesConfigExceptionTest() throws Exception{
     	ShowDao showDao = Mockito.mock(ShowDao.class);
      Show show = new Show();
      Mockito.when(showDao.readAll()).thenReturn(Arrays.asList(show));
     	service.setShowDao(showDao);
     
      ConfigDao configDao = Mockito.mock(ConfigDao.class);
      Mockito.when(configDao.read()).thenThrow(ConfigException.class);
      service.setConfigDao(configDao);
     
		ProcessorFactory factory = Mockito.mock(ProcessorFactory.class);
     	service.setFeedProcessorFactory(factory);
     
		Executor executor = Mockito.mock(Executor.class);
		service.setExecutor(executor);
     
     	try{
      	service.processAllFeedUpdates();
      }catch(Exception ex){
        Assert.assertTrue(ex instanceof ConfigException);
      }

      Assert.assertEquals(0, service.getProcessors().size()); 
     
      Mockito.verify(showDao, Mockito.times(1)).readAll();
      Mockito.verify(configDao, Mockito.times(1)).read();
      Mockito.verify(factory,  Mockito.times(0)).build(Mockito.any(Show.class),Mockito.any(Config.class));
      Mockito.verify(executor, Mockito.times(0)).execute(Mockito.any(Runnable.class));
   }
  
	@Test
  	public void processAllFeedUpdatesNoNextEpTest() throws Exception{
     	ShowDao showDao = Mockito.mock(ShowDao.class);
      Show show = new Show();
     	show.setNextEpisode(null);
      Mockito.when(showDao.readAll()).thenReturn(Arrays.asList(show));
     	service.setShowDao(showDao);
     
      ConfigDao configDao = Mockito.mock(ConfigDao.class);
      Config config = new Config();
      Mockito.when(configDao.read()).thenReturn(config);
      service.setConfigDao(configDao);
     
		ProcessorFactory factory = Mockito.mock(ProcessorFactory.class);
      Processor processor = Mockito.mock(Processor.class);
     	Mockito.when(factory.build(Mockito.any(Show.class),Mockito.any(Config.class))).thenReturn(processor);
     	service.setFeedProcessorFactory(factory);
     
		Executor executor = Mockito.mock(Executor.class);
		service.setExecutor(executor);
     
     	service.processAllFeedUpdates();
     
     	Assert.assertEquals(0, service.getProcessors().size());     
     
      Mockito.verify(showDao, Mockito.times(1)).readAll();
      Mockito.verify(configDao, Mockito.times(1)).read();
      Mockito.verify(factory,  Mockito.times(0)).build(Mockito.any(Show.class),Mockito.any(Config.class));
      Mockito.verify(executor, Mockito.times(0)).execute(Mockito.any(Runnable.class));
   }
  
	@Test
  	public void processAllFeedUpdatesNextEpAfterNowTest() throws Exception{
     	ShowDao showDao = Mockito.mock(ShowDao.class);
      Show show = new Show();
      show.setTimezone("EST");
     	Episode episode = new Episode();
      Date later = new Date(System.currentTimeMillis() +100000);
     	episode.setAirDate(later);
     	show.setNextEpisode(episode);
      Mockito.when(showDao.readAll()).thenReturn(Arrays.asList(show));
     	service.setShowDao(showDao);
     
      ConfigDao configDao = Mockito.mock(ConfigDao.class);
      Config config = new Config();
      Mockito.when(configDao.read()).thenReturn(config);
      service.setConfigDao(configDao);
     
		ProcessorFactory factory = Mockito.mock(ProcessorFactory.class);
      Processor processor = Mockito.mock(Processor.class);
     	Mockito.when(factory.build(Mockito.any(Show.class),Mockito.any(Config.class))).thenReturn(processor);
     	service.setFeedProcessorFactory(factory);
     
		Executor executor = Mockito.mock(Executor.class);
		service.setExecutor(executor);
     
     	service.processAllFeedUpdates();
     
     	Assert.assertEquals(0, service.getProcessors().size());     
     
      Mockito.verify(showDao, Mockito.times(1)).readAll();
      Mockito.verify(configDao, Mockito.times(1)).read();
      Mockito.verify(factory,  Mockito.times(0)).build(Mockito.any(Show.class),Mockito.any(Config.class));
      Mockito.verify(executor, Mockito.times(0)).execute(Mockito.any(Runnable.class));
   }
  
	@Test
  	public void processAllFeedUpdatesNextEpBeforeNowTest() throws Exception{
     	ShowDao showDao = Mockito.mock(ShowDao.class);
      Show show = new Show();
      show.setTimezone("EST");
     	Episode episode = new Episode();
      Date earlier = new Date( 0 );
     	episode.setAirDate(earlier);
     	show.setNextEpisode(episode);
      Mockito.when(showDao.readAll()).thenReturn(Arrays.asList(show));
     	service.setShowDao(showDao);
     
      ConfigDao configDao = Mockito.mock(ConfigDao.class);
      Config config = new Config();
      Mockito.when(configDao.read()).thenReturn(config);
      service.setConfigDao(configDao);
     
		ProcessorFactory factory = Mockito.mock(ProcessorFactory.class);
      Processor processor = Mockito.mock(Processor.class);
     	Mockito.when(factory.build(Mockito.any(Show.class),Mockito.any(Config.class))).thenReturn(processor);
     	service.setFeedProcessorFactory(factory);
     
		Executor executor = Mockito.mock(Executor.class);
		service.setExecutor(executor);
     
     	service.processAllFeedUpdates();
     
     	Assert.assertEquals(1, service.getProcessors().size());     
     	Assert.assertEquals(processor, service.getProcessors().get(0));
     
      Mockito.verify(showDao, Mockito.times(1)).readAll();
      Mockito.verify(configDao, Mockito.times(1)).read();
      Mockito.verify(factory,  Mockito.times(1)).build(Mockito.any(Show.class),Mockito.any(Config.class));
      Mockito.verify(executor, Mockito.times(1)).execute(Mockito.any(Runnable.class));
   }
  
  	@Test
  	public void alertCompleteTest(){
   	Processor processor = Mockito.mock(Processor.class);
		service.getProcessors().add(processor);

      Assert.assertEquals(1, service.getProcessors().size());     
     	
     	service.alertComplete(processor);
      
     	Assert.assertEquals(0, service.getProcessors().size());     
     
	   Mockito.verify(processor, Mockito.times(1)).removeListener(service); 
   }
}
