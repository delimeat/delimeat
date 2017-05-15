/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.delimeat.processor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Executor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.config.ConfigService;
import io.delimeat.config.domain.Config;
import io.delimeat.guide.exception.GuideException;
import io.delimeat.processor.FeedProcessorFactory;
import io.delimeat.processor.GuideProcessorFactory;
import io.delimeat.processor.Processor;
import io.delimeat.processor.ProcessorService_Impl;
import io.delimeat.show.EpisodeService;
import io.delimeat.show.ShowService;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.Show;


public class ProcessorService_ImplTest {

	private ProcessorService_Impl service;

	@Before
	public void setUp() throws Exception {
		service = new ProcessorService_Impl();
	}

	@Test
	public void showServiceTest() {
		Assert.assertNull(service.getShowService());
		ShowService showService= Mockito.mock(ShowService.class);
		service.setShowService(showService);
		Assert.assertEquals(showService, service.getShowService());
	}

	@Test
	public void configServiceTest() {
		Assert.assertNull(service.getConfigService());
		ConfigService configService = Mockito.mock(ConfigService.class);
		service.setConfigService(configService);
		Assert.assertEquals(configService, service.getConfigService());
	}
	
	@Test
	public void episodeServiceTest() {
		Assert.assertNull(service.getEpisodeService());
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		service.setEpisodeService(episodeService);
		Assert.assertEquals(episodeService, service.getEpisodeService());
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
		FeedProcessorFactory factory = Mockito.mock(FeedProcessorFactory.class);
		service.setFeedProcessorFactory(factory);
		Assert.assertEquals(factory, service.getFeedProcessorFactory());
	}

	@Test
	public void guideProcessorFactoryTest() {
		Assert.assertNull(service.getGuideProcessorFactory());
		GuideProcessorFactory factory = Mockito.mock(GuideProcessorFactory.class);
		service.setGuideProcessorFactory(factory);
		Assert.assertEquals(factory, service.getGuideProcessorFactory());
	}

	@Test
	public void processAllGuideUpdatesNoShowsTest() throws Exception {
		ShowService showService = Mockito.mock(ShowService.class);
		Mockito.when(showService.readAll()).thenReturn(Collections.<Show> emptyList());
		service.setShowService(showService);

		ConfigService configService = Mockito.mock(ConfigService.class);
		Config config = new Config();
		Mockito.when(configService.read()).thenReturn(config);
		service.setConfigService(configService);

		service.processAllGuideUpdates();

		Assert.assertEquals(0, service.getProcessors().size());

		Mockito.verify(showService).readAll();
		Mockito.verifyNoMoreInteractions(showService);
		
		Mockito.verifyZeroInteractions(configService);

	}

	@Test
	public void processAllGuideUpdatesShowExceptionTest() throws Exception {
		ShowService showService = Mockito.mock(ShowService.class);
		Mockito.when(showService.readAll()).thenThrow(GuideException.class);
		service.setShowService(showService);
		
		try {
			service.processAllGuideUpdates();
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof GuideException);
		}

		Assert.assertEquals(0, service.getProcessors().size());

		Mockito.verify(showService).readAll();
		Mockito.verifyNoMoreInteractions(showService);

	}

	@Test
	public void processAllGuideUpdatesConfigExceptionTest() throws Exception {
		ShowService showService = Mockito.mock(ShowService.class);
		Show show = new Show();
		show.setEnabled(true);
		Mockito.when(showService.readAll()).thenReturn(Arrays.asList(show));
		service.setShowService(showService);

		ConfigService configService = Mockito.mock(ConfigService.class);
		Mockito.when(configService.read()).thenThrow(GuideException.class);
		service.setConfigService(configService);


		try {
			service.processAllGuideUpdates();
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof GuideException);
		}

		Assert.assertEquals(0, service.getProcessors().size());

		Mockito.verify(showService).readAll();
		Mockito.verifyNoMoreInteractions(showService);
		
		Mockito.verify(configService).read();
		Mockito.verifyNoMoreInteractions(configService);
	}

	@Test
	public void processAllGuideUpdatesTest() throws Exception {
		ShowService showService = Mockito.mock(ShowService.class);
		Show show = new Show();
		show.setEnabled(true);
		Mockito.when(showService.readAll()).thenReturn(Arrays.asList(show, show));
		service.setShowService(showService);

		ConfigService configService = Mockito.mock(ConfigService.class);
		Config config = new Config();
		Mockito.when(configService.read()).thenReturn(config);
		service.setConfigService(configService);

		GuideProcessorFactory factory = Mockito.mock(GuideProcessorFactory.class);
		Processor processor = Mockito.mock(Processor.class);
		Mockito.when(factory.build(show,config)).thenReturn(processor);
		service.setGuideProcessorFactory(factory);

		Executor executor = Mockito.mock(Executor.class);
		service.setExecutor(executor);

		service.processAllGuideUpdates();

		Assert.assertEquals(2, service.getProcessors().size());
		Assert.assertEquals(processor, service.getProcessors().get(0));
		Assert.assertEquals(processor, service.getProcessors().get(1));

		Mockito.verify(showService).readAll();
		Mockito.verifyNoMoreInteractions(showService);
		
		Mockito.verify(configService).read();
		Mockito.verifyNoMoreInteractions(configService);
		
		Mockito.verify(factory, Mockito.times(2)).build(show, config);
		Mockito.verifyNoMoreInteractions(factory);
		
		Mockito.verify(executor, Mockito.times(2)).execute(Mockito.any(Runnable.class));
		Mockito.verifyNoMoreInteractions(executor);

	}

	@Test
	public void processAllGuideUpdatesDisabledTest() throws Exception {
		ShowService showService = Mockito.mock(ShowService.class);
		Show show = new Show();
		show.setEnabled(false);
		Mockito.when(showService.readAll()).thenReturn(Arrays.asList(show));
		service.setShowService(showService);

		ConfigService configService = Mockito.mock(ConfigService.class);
		Config config = new Config();
		Mockito.when(configService.read()).thenReturn(config);
		service.setConfigService(configService);

		service.processAllGuideUpdates();

		Assert.assertEquals(0, service.getProcessors().size());

		Mockito.verify(showService).readAll();
		Mockito.verifyNoMoreInteractions(showService);
		
		Mockito.verifyZeroInteractions(configService);

	}

	@Test
	public void processAllFeedUpdatesNoShowsTest() throws Exception {
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		Mockito.when(episodeService.findAllPending()).thenReturn(Collections.<Episode> emptyList());
		service.setEpisodeService(episodeService);

		ConfigService configService = Mockito.mock(ConfigService.class);
		Config config = new Config();
		Mockito.when(configService.read()).thenReturn(config);
		service.setConfigService(configService);

		service.processAllFeedUpdates();

		Assert.assertEquals(0, service.getProcessors().size());

		Mockito.verify(episodeService).findAllPending();
		Mockito.verifyNoMoreInteractions(episodeService);
		
		Mockito.verifyZeroInteractions(configService);

	}

	@Test
	public void processAllFeedUpdatesShowExceptionTest() throws Exception {
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		Mockito.when(episodeService.findAllPending()).thenThrow(GuideException.class);
		service.setEpisodeService(episodeService);


		try {
			service.processAllFeedUpdates();
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof GuideException);
		}

		Assert.assertEquals(0, service.getProcessors().size());

		Mockito.verify(episodeService).findAllPending();
		Mockito.verifyNoMoreInteractions(episodeService);

	}

	@Test
	public void processAllFeedUpdatesConfigExceptionTest() throws Exception {
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		Episode episode = new Episode();
		Show show = new Show();
		show.setEnabled(true);
		episode.setShow(show);
		Mockito.when(episodeService.findAllPending()).thenReturn(Arrays.asList(episode));
		service.setEpisodeService(episodeService);

		ConfigService configService = Mockito.mock(ConfigService.class);
		Mockito.when(configService.read()).thenThrow(GuideException.class);
		service.setConfigService(configService);


		try {
			service.processAllFeedUpdates();
		} catch (Exception ex) {
			Assert.assertTrue(ex instanceof GuideException);
		}

		Assert.assertEquals(0, service.getProcessors().size());

		Mockito.verify(episodeService).findAllPending();
		Mockito.verifyNoMoreInteractions(episodeService);
		
		Mockito.verify(configService).read();
		Mockito.verifyNoMoreInteractions(configService);
	}

	@Test
	public void processAllFeedUpdatesNotEnabledTest() throws Exception {

		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		Episode episode = new Episode();
		Show show = new Show();
     	show.setEnabled(false);
     	episode.setShow(show);
		Mockito.when(episodeService.findAllPending()).thenReturn(Arrays.asList(episode));
		service.setEpisodeService(episodeService);

		ConfigService configService = Mockito.mock(ConfigService.class);
		Config config = new Config();
		Mockito.when(configService.read()).thenReturn(config);
		service.setConfigService(configService);

		service.processAllFeedUpdates();

		Assert.assertEquals(0, service.getProcessors().size());

		Mockito.verify(episodeService).findAllPending();
		Mockito.verifyNoMoreInteractions(episodeService);
		
		Mockito.verifyZeroInteractions(configService);
	}
  
	@Test
	public void processAllFeedUpdatesNoNextEpTest() throws Exception {
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		Episode episode = new Episode();
		Show show = new Show();
     	show.setEnabled(false);
     	episode.setShow(show);
		
		Mockito.when(episodeService.findAllPending()).thenReturn(Arrays.asList(episode));
		service.setEpisodeService(episodeService);

		ConfigService configService = Mockito.mock(ConfigService.class);
		Config config = new Config();
		Mockito.when(configService.read()).thenReturn(config);
		service.setConfigService(configService);

		service.processAllFeedUpdates();

		Assert.assertEquals(0, service.getProcessors().size());

		Mockito.verify(episodeService).findAllPending();
		Mockito.verifyNoMoreInteractions(episodeService);
		
		Mockito.verifyZeroInteractions(configService);
	}

	@Test
	public void processAllFeedUpdatesNextEpAfterNowTest() throws Exception {		
		Show show = new Show();
		show.setTimezone("US/Pacific");
		show.setEnabled(true);
		Episode episode = new Episode();
		LocalDate later = LocalDate.MAX;
		episode.setAirDate(later);
		episode.setShow(show);
		
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		Mockito.when(episodeService.findAllPending()).thenReturn(Arrays.asList(episode));
		service.setEpisodeService(episodeService);


		ConfigService configService = Mockito.mock(ConfigService.class);
		Config config = new Config();
		Mockito.when(configService.read()).thenReturn(config);
		service.setConfigService(configService);

		service.processAllFeedUpdates();

		Assert.assertEquals(0, service.getProcessors().size());

		Mockito.verify(episodeService).findAllPending();
		Mockito.verifyNoMoreInteractions(episodeService);
		
		Mockito.verify(configService).read();
		Mockito.verifyNoMoreInteractions(configService);
	}

	@Test
	public void processAllFeedUpdatesNextEpBeforeNowTest() throws Exception {
		Show show = new Show();
		show.setEnabled(true);
		show.setTimezone("UTC");
		Episode episode = new Episode();
		LocalDate earlier = LocalDate.ofEpochDay(0);
		episode.setAirDate(earlier);
		episode.setShow(show);
		
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		Mockito.when(episodeService.findAllPending()).thenReturn(Arrays.asList(episode));
		service.setEpisodeService(episodeService);

		ConfigService configService = Mockito.mock(ConfigService.class);
		Config config = new Config();
		Mockito.when(configService.read()).thenReturn(config);
		service.setConfigService(configService);

		FeedProcessorFactory factory = Mockito.mock(FeedProcessorFactory.class);
		Processor processor = Mockito.mock(Processor.class);
		Mockito.when(factory.build(episode,config)).thenReturn(processor);
		service.setFeedProcessorFactory(factory);

		Executor executor = Mockito.mock(Executor.class);
		service.setExecutor(executor);

		service.processAllFeedUpdates();

		Assert.assertEquals(1, service.getProcessors().size());
		Assert.assertEquals(processor, service.getProcessors().get(0));

		Mockito.verify(episodeService).findAllPending();
		Mockito.verifyNoMoreInteractions(episodeService);
		
		Mockito.verify(configService).read();
		Mockito.verifyNoMoreInteractions(configService);
		
		Mockito.verify(factory).build(episode, config);
		Mockito.verifyNoMoreInteractions(factory);
		
		Mockito.verify(executor).execute(Mockito.any(Runnable.class));
		Mockito.verifyNoMoreInteractions(executor);
	}
	
	@Test
	public void processAllFeedUpdatesNextEpBeforeNowBeforeWindowTest() throws Exception {
		Show show = new Show();
		show.setEnabled(true);
		show.setTimezone("UTC");
		Episode episode = new Episode();
		episode.setLastFeedCheck(Instant.EPOCH);
		episode.setAirDate(LocalDate.MIN);
		episode.setShow(show);
		
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		Mockito.when(episodeService.findAllPending()).thenReturn(Arrays.asList(episode));
		service.setEpisodeService(episodeService);

		ConfigService configService = Mockito.mock(ConfigService.class);
		Config config = new Config();
		Mockito.when(configService.read()).thenReturn(config);
		service.setConfigService(configService);

		FeedProcessorFactory factory = Mockito.mock(FeedProcessorFactory.class);
		Processor processor = Mockito.mock(Processor.class);
		Mockito.when(factory.build(episode,config)).thenReturn(processor);
		service.setFeedProcessorFactory(factory);

		Executor executor = Mockito.mock(Executor.class);
		service.setExecutor(executor);

		service.processAllFeedUpdates();

		Assert.assertEquals(1, service.getProcessors().size());
		Assert.assertEquals(processor, service.getProcessors().get(0));

		Mockito.verify(episodeService).findAllPending();
		Mockito.verifyNoMoreInteractions(episodeService);
		
		Mockito.verify(configService).read();
		Mockito.verifyNoMoreInteractions(configService);
		
		Mockito.verify(factory).build(episode, config);
		Mockito.verifyNoMoreInteractions(factory);
		
		Mockito.verify(executor).execute(Mockito.any(Runnable.class));
		Mockito.verifyNoMoreInteractions(executor);
	}
	
	@Test
	public void processAllFeedUpdatesNextEpBeforeNowAfterWindowTest() throws Exception {
		Show show = new Show();
		show.setEnabled(true);
		show.setTimezone("EST");
		Episode episode = new Episode();
		episode.setLastFeedCheck(Instant.now());
		episode.setAirDate(LocalDate.MIN);
		episode.setShow(show);
		
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		Mockito.when(episodeService.findAllPending()).thenReturn(Arrays.asList(episode));
		service.setEpisodeService(episodeService);

		ConfigService configService = Mockito.mock(ConfigService.class);
		Config config = new Config();
		config.setSearchInterval(Integer.MAX_VALUE);
		Mockito.when(configService.read()).thenReturn(config);
		service.setConfigService(configService);

		service.processAllFeedUpdates();
		
		Assert.assertEquals(0, service.getProcessors().size());

		Mockito.verify(episodeService).findAllPending();
		Mockito.verifyNoMoreInteractions(episodeService);
		
		Mockito.verify(configService).read();
		Mockito.verifyNoMoreInteractions(configService);
	}

	@Test
	public void processAllFeedUpdatesDisabledTest() throws Exception {
		Show show = new Show();
		show.setEnabled(false);
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.MIN);
		episode.setShow(show);
		
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		Mockito.when(episodeService.findAllPending()).thenReturn(Arrays.asList(episode));
		service.setEpisodeService(episodeService);

		ConfigService configService = Mockito.mock(ConfigService.class);
		Config config = new Config();
		Mockito.when(configService.read()).thenReturn(config);
		service.setConfigService(configService);

		service.processAllFeedUpdates();

		Assert.assertEquals(0, service.getProcessors().size());

		Mockito.verify(episodeService).findAllPending();
		Mockito.verifyNoMoreInteractions(episodeService);
		
		Mockito.verifyZeroInteractions(configService);
	}

	@Test
	public void alertCompleteTest() {
		Processor processor = Mockito.mock(Processor.class);
		service.getProcessors().add(processor);

		Assert.assertEquals(1, service.getProcessors().size());

		service.alertComplete(processor);

		Assert.assertEquals(0, service.getProcessors().size());

		Mockito.verify(processor).removeListener(service);
		Mockito.verifyNoMoreInteractions(processor);
	}
}
