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
package io.delimeat.show;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;

import io.delimeat.guide.GuideService;
import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.guide.exception.GuideException;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.EpisodeStatus;
import io.delimeat.show.domain.Show;
import io.delimeat.show.exception.ShowConcurrencyException;
import io.delimeat.show.exception.ShowException;
import io.delimeat.show.exception.ShowNotFoundException;

public class ShowService_ImplTest {

	private ShowService_Impl service;

	@Before
	public void setUp() {
		service = new ShowService_Impl();
	}

	@Test
	public void showRepositoryTest() {
		Assert.assertNull(service.getShowRepository());
		ShowRepository showRepository = Mockito.mock(ShowRepository.class);
		service.setShowRepository(showRepository);
		Assert.assertEquals(showRepository, service.getShowRepository());
	}

	@Test
	public void guideServiceTest() {
		Assert.assertNull(service.getGuideService());
		GuideService guideService = Mockito.mock(GuideService.class);
		service.setGuideService(guideService);
		Assert.assertEquals(guideService, service.getGuideService());
	}

	@Test
	public void episodeServiceTest() {
		Assert.assertNull(service.getEpisodeService());
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		service.setEpisodeService(episodeService);
		Assert.assertEquals(episodeService, service.getEpisodeService());
	}
	
	@Test
	public void ToStringTest(){
		Assert.assertEquals("ShowService_Impl []", service.toString());
	}

	@Test
	public void createTest() throws Exception {
		ShowRepository showRepository = Mockito.mock(ShowRepository.class);
		Show show = new Show();
		show.setTitle("TITLE");
		show.setGuideId("GUIDEID");
		show.setAirTime(LocalTime.NOON);
		show.setTimezone(ZoneId.systemDefault().getId());
		service.setShowRepository(showRepository);

		GuideService guideService = Mockito.mock(GuideService.class);
		GuideEpisode guideEp1 = new GuideEpisode();
		guideEp1.setAirDate(LocalDate.ofEpochDay(0));
		GuideEpisode guideEp2 = new GuideEpisode();
		guideEp2.setAirDate(LocalDate.now().plusDays(30));
		Mockito.when(guideService.readEpisodes("GUIDEID")).thenReturn(Arrays.asList(guideEp1, guideEp2));
		service.setGuideService(guideService);

		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		service.setEpisodeService(episodeService);

		service.create(show);

		Mockito.verify(showRepository).save(show);
		Mockito.verifyNoMoreInteractions(showRepository);

		Mockito.verify(episodeService, Mockito.times(2)).update(Mockito.any());
		ArgumentCaptor<Episode> argumentCaptor = ArgumentCaptor.forClass(Episode.class);
		Mockito.verify(episodeService, Mockito.times(2)).update(argumentCaptor.capture());
		Assert.assertEquals(2, argumentCaptor.getAllValues().size());
		Assert.assertEquals(EpisodeStatus.SKIPPED, argumentCaptor.getAllValues().get(0).getStatus());
		Assert.assertEquals(EpisodeStatus.PENDING, argumentCaptor.getAllValues().get(1).getStatus());
		Mockito.verifyNoMoreInteractions(episodeService);

		Mockito.verify(guideService).readEpisodes("GUIDEID");
		Mockito.verifyNoMoreInteractions(showRepository, guideService);

	}

	@Test(expected = ShowException.class)
	public void createExceptionTest() throws Exception {
		ShowRepository showRepository = Mockito.mock(ShowRepository.class);
		Show show = new Show();
		show.setTitle("TITLE");
		show.setGuideId("GUIDEID");
		show.setAirTime(LocalTime.NOON);
		show.setTimezone(ZoneId.systemDefault().getId());
		service.setShowRepository(showRepository);

		GuideService guideService = Mockito.mock(GuideService.class);
		GuideEpisode guideEp1 = new GuideEpisode();
		guideEp1.setAirDate(LocalDate.ofEpochDay(0));
		GuideEpisode guideEp2 = new GuideEpisode();
		guideEp2.setAirDate(LocalDate.now().plusDays(30));
		Mockito.when(guideService.readEpisodes("GUIDEID")).thenThrow(GuideException.class);
		service.setGuideService(guideService);

		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		service.setEpisodeService(episodeService);

		service.create(show);
	}

	@Test
	public void readTest() throws Exception {
		ShowRepository showRepository = Mockito.mock(ShowRepository.class);
		Show show = new Show();
		Mockito.when(showRepository.findOne(Long.MAX_VALUE)).thenReturn(show);
		service.setShowRepository(showRepository);

		Show actualShow = service.read(Long.MAX_VALUE);
		Assert.assertEquals(show, actualShow);

		Mockito.verify(showRepository).findOne(Long.MAX_VALUE);
		Mockito.verifyNoMoreInteractions(showRepository);
	}

	@Test(expected = ShowNotFoundException.class)
	public void readNotFoundExceptionTest() throws Exception {
		ShowRepository showRepository = Mockito.mock(ShowRepository.class);
		Mockito.when(showRepository.findOne(Long.MAX_VALUE)).thenReturn(null);
		service.setShowRepository(showRepository);

		service.read(Long.MAX_VALUE);
	}

	@Test(expected = ShowException.class)
	public void readExceptionTest() throws Exception {
		ShowRepository showRepository = Mockito.mock(ShowRepository.class);
		Mockito.when(showRepository.findOne(Long.MAX_VALUE)).thenThrow(new ConcurrencyFailureException("EX"));
		service.setShowRepository(showRepository);

		service.read(Long.MAX_VALUE);
	}

	@Test
	public void readAllTest() throws Exception {
		ShowRepository showRepository = Mockito.mock(ShowRepository.class);
		List<Show> expectedShows = new ArrayList<Show>();
		Show show = new Show();
		expectedShows.add(show);
		Mockito.when(showRepository.findAll()).thenReturn(expectedShows);
		service.setShowRepository(showRepository);

		List<Show> actualShows = service.readAll();
		Assert.assertEquals(1, actualShows.size());
		Assert.assertEquals(show, actualShows.get(0));

		Mockito.verify(showRepository).findAll();
		Mockito.verifyNoMoreInteractions(showRepository);
	}

	@Test(expected = ShowException.class)
	public void readAllExceptionTest() throws Exception {
		ShowRepository showRepository = Mockito.mock(ShowRepository.class);
		List<Show> expectedShows = new ArrayList<Show>();
		Show show = new Show();
		expectedShows.add(show);
		Mockito.when(showRepository.findAll()).thenThrow(new ConcurrencyFailureException("EX"));
		service.setShowRepository(showRepository);

		service.readAll();
	}

	@Test
	public void updateTest() throws Exception {
		ShowRepository showRepository = Mockito.mock(ShowRepository.class);
		Show show = new Show();
		Mockito.when(showRepository.save(show)).thenReturn(show);
		service.setShowRepository(showRepository);

		Show actualShow = service.update(new Show());
		Assert.assertEquals(show, actualShow);

		Mockito.verify(showRepository).save(show);
		Mockito.verifyNoMoreInteractions(showRepository);
	}

	@Test(expected = ShowConcurrencyException.class)
	public void updateConcurrencyExceptionTest() throws Exception {
		ShowRepository showRepository = Mockito.mock(ShowRepository.class);
		Show show = new Show();
		Mockito.when(showRepository.save(show)).thenThrow(new ConcurrencyFailureException("EX"));
		service.setShowRepository(showRepository);

		service.update(new Show());
	}

	@Test(expected = ShowException.class)
	public void updateExceptionTest() throws Exception {
		ShowRepository showRepository = Mockito.mock(ShowRepository.class);
		Show show = new Show();
		Mockito.when(showRepository.save(show)).thenThrow(new DataSourceLookupFailureException("EX"));
		service.setShowRepository(showRepository);

		service.update(new Show());
	}

	@Test
	public void deleteTest() throws Exception {
		ShowRepository showRepository = Mockito.mock(ShowRepository.class);
		service.setShowRepository(showRepository);
		service.delete(Long.MAX_VALUE);

		Mockito.verify(showRepository).delete(Long.MAX_VALUE);
		Mockito.verifyNoMoreInteractions(showRepository);
	}

	@Test(expected = ShowException.class)
	public void deleteExceptionTest() throws Exception {
		ShowRepository showRepository = Mockito.mock(ShowRepository.class);
		service.setShowRepository(showRepository);
		Mockito.doThrow(new DataSourceLookupFailureException("EX")).when(showRepository).delete(Long.MAX_VALUE);

		service.delete(Long.MAX_VALUE);
	}

	@Test
	public void readAllEpisodesTest() throws Exception {
		Episode ep = new Episode();

		ShowRepository showRepository = Mockito.mock(ShowRepository.class);
		service.setShowRepository(showRepository);

		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		Mockito.when(episodeService.findByShow(1L)).thenReturn(Arrays.asList(ep));
		service.setEpisodeService(episodeService);

		List<Episode> episodes = service.readAllEpisodes(1L);
		Assert.assertNotNull(episodes);
		Assert.assertEquals(1, episodes.size());
		Assert.assertEquals(ep, episodes.get(0));

		Mockito.verify(episodeService).findByShow(1L);
		Mockito.verifyNoMoreInteractions(episodeService);
	}

	@Test(expected = ShowException.class)
	public void readAllEpisodesExceptionTest() throws Exception {
		ShowRepository showRepository = Mockito.mock(ShowRepository.class);
		service.setShowRepository(showRepository);

		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		Mockito.when(episodeService.findByShow(1L)).thenThrow(new DataSourceLookupFailureException("EX"));
		service.setEpisodeService(episodeService);

		service.readAllEpisodes(1L);
	}

	@Test
	public void cleanTitleTest() {
		Show show = new Show();
		show.setTitle("This is 2015 a V'ery Nice $#10293734521,.<>~???! title (2016)");

		Show result = service.cleanTitle(show);

		Assert.assertEquals(show, result);
		Assert.assertEquals("This is 2015 a Very Nice 10293734521 title", result.getTitle());
	}

}
