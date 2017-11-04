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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.config.ConfigService;
import io.delimeat.config.entity.Config;
import io.delimeat.show.EpisodeService;
import io.delimeat.show.entity.Episode;
import io.delimeat.show.entity.Show;

public class FeedItemReader_ImplTest {

	private FeedItemReader_Impl reader;
	
	@Before
	public void setUp(){
		reader = new FeedItemReader_Impl();
	}
	
	@Test
	public void toStringTest(){
		Assert.assertEquals("FeedItemReader_Impl []", reader.toString());
	}
	
	@Test
	public void configServiceTest() {
		Assert.assertNull(reader.getConfigService());
		ConfigService configService = Mockito.mock(ConfigService.class);
		reader.setConfigService(configService);
		Assert.assertEquals(configService, reader.getConfigService());
	}
	
	@Test
	public void episodeServiceTest() {
		Assert.assertNull(reader.getEpisodeService());
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		reader.setEpisodeService(episodeService);
		Assert.assertEquals(episodeService, reader.getEpisodeService());
	}
	
	@Test
	public void readNotEnabledTest() throws Exception {

		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		Episode episode = new Episode();
		Show show = new Show();
     	show.setEnabled(false);
     	episode.setShow(show);
		Mockito.when(episodeService.findAllPending()).thenReturn(Arrays.asList(episode));
		reader.setEpisodeService(episodeService);

		ConfigService configService = Mockito.mock(ConfigService.class);
		Config config = new Config();
		Mockito.when(configService.read()).thenReturn(config);
		reader.setConfigService(configService);

		Assert.assertNull(reader.read());

		Mockito.verify(episodeService).findAllPending();
		Mockito.verifyNoMoreInteractions(episodeService);
		
		Mockito.verify(configService).read();
		Mockito.verifyNoMoreInteractions(configService);
	}
	
	  
	@Test
	public void readNoNextEpTest() throws Exception {
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		Episode episode = new Episode();
		Show show = new Show();
     	show.setEnabled(false);
     	episode.setShow(show);
		
		Mockito.when(episodeService.findAllPending()).thenReturn(Arrays.asList(episode));
		reader.setEpisodeService(episodeService);

		ConfigService configService = Mockito.mock(ConfigService.class);
		Config config = new Config();
		Mockito.when(configService.read()).thenReturn(config);
		reader.setConfigService(configService);

		Assert.assertNull(reader.read());

		Mockito.verify(episodeService).findAllPending();
		Mockito.verifyNoMoreInteractions(episodeService);
		
		Mockito.verify(configService).read();
		Mockito.verifyNoMoreInteractions(configService);
	}
	
	@Test
	public void readNextEpAfterNowTest() throws Exception {		
		Show show = new Show();
		show.setTimezone("US/Pacific");
		show.setEnabled(true);
		Episode episode = new Episode();
		LocalDate later = LocalDate.MAX;
		episode.setAirDate(later);
		episode.setShow(show);
		
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		Mockito.when(episodeService.findAllPending()).thenReturn(Arrays.asList(episode));
		reader.setEpisodeService(episodeService);


		ConfigService configService = Mockito.mock(ConfigService.class);
		Config config = new Config();
		Mockito.when(configService.read()).thenReturn(config);
		reader.setConfigService(configService);

		Assert.assertNull(reader.read());

		Mockito.verify(episodeService).findAllPending();
		Mockito.verifyNoMoreInteractions(episodeService);
		
		Mockito.verify(configService).read();
		Mockito.verifyNoMoreInteractions(configService);
	}
	
	@Test
	public void readNextEpBeforeNowTest() throws Exception {
		Show show = new Show();
		show.setEnabled(true);
		show.setTimezone("UTC");
		Episode episode = new Episode();
		LocalDate earlier = LocalDate.ofEpochDay(0);
		episode.setAirDate(earlier);
		episode.setShow(show);
		
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		Mockito.when(episodeService.findAllPending()).thenReturn(Arrays.asList(episode));
		reader.setEpisodeService(episodeService);

		ConfigService configService = Mockito.mock(ConfigService.class);
		Config config = new Config();
		Mockito.when(configService.read()).thenReturn(config);
		reader.setConfigService(configService);

		Assert.assertEquals(episode, reader.read());


		Mockito.verify(episodeService).findAllPending();
		Mockito.verifyNoMoreInteractions(episodeService);
		
		Mockito.verify(configService).read();
		Mockito.verifyNoMoreInteractions(configService);
	}
	
	@Test
	public void readNextEpBeforeNowBeforeWindowTest() throws Exception {
		Show show = new Show();
		show.setEnabled(true);
		show.setTimezone("UTC");
		Episode episode = new Episode();
		episode.setLastFeedCheck(Instant.EPOCH);
		episode.setAirDate(LocalDate.MIN);
		episode.setShow(show);
		
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		Mockito.when(episodeService.findAllPending()).thenReturn(Arrays.asList(episode));
		reader.setEpisodeService(episodeService);

		ConfigService configService = Mockito.mock(ConfigService.class);
		Config config = new Config();
		Mockito.when(configService.read()).thenReturn(config);
		reader.setConfigService(configService);

		Assert.assertEquals(episode, reader.read());

		Mockito.verify(episodeService).findAllPending();
		Mockito.verifyNoMoreInteractions(episodeService);
		
		Mockito.verify(configService).read();
		Mockito.verifyNoMoreInteractions(configService);
	}
	
	@Test
	public void readNextEpBeforeNowAfterWindowTest() throws Exception {
		Show show = new Show();
		show.setEnabled(true);
		show.setTimezone("EST");
		Episode episode = new Episode();
		episode.setLastFeedCheck(Instant.now());
		episode.setAirDate(LocalDate.MIN);
		episode.setShow(show);
		
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		Mockito.when(episodeService.findAllPending()).thenReturn(Arrays.asList(episode));
		reader.setEpisodeService(episodeService);

		ConfigService configService = Mockito.mock(ConfigService.class);
		Config config = new Config();
		config.setSearchInterval(Integer.MAX_VALUE);
		Mockito.when(configService.read()).thenReturn(config);
		reader.setConfigService(configService);

		Assert.assertNull(reader.read());

		Mockito.verify(episodeService).findAllPending();
		Mockito.verifyNoMoreInteractions(episodeService);
		
		Mockito.verify(configService).read();
		Mockito.verifyNoMoreInteractions(configService);
	}
	
	@Test
	public void readDisabledTest() throws Exception {
		Show show = new Show();
		show.setEnabled(false);
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.MIN);
		episode.setShow(show);
		
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		Mockito.when(episodeService.findAllPending()).thenReturn(Arrays.asList(episode));
		reader.setEpisodeService(episodeService);

		ConfigService configService = Mockito.mock(ConfigService.class);
		Config config = new Config();
		Mockito.when(configService.read()).thenReturn(config);
		reader.setConfigService(configService);

		Assert.assertNull(reader.read());

		Mockito.verify(episodeService).findAllPending();
		Mockito.verifyNoMoreInteractions(episodeService);
		
		Mockito.verify(configService).read();
		Mockito.verifyNoMoreInteractions(configService);
	}
}
