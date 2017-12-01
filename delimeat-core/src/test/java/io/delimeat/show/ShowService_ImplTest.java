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

import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import io.delimeat.guide.GuideService;
import io.delimeat.guide.entity.GuideEpisode;
import io.delimeat.guide.exception.GuideException;
import io.delimeat.show.entity.Episode;
import io.delimeat.show.entity.EpisodeStatus;
import io.delimeat.show.entity.Show;
import io.delimeat.show.exception.ShowConcurrencyException;
import io.delimeat.show.exception.ShowException;
import io.delimeat.show.exception.ShowNotFoundException;

public class ShowService_ImplTest {

	private ShowService_Impl service;

	@BeforeEach
	public void setUp() {
		service = new ShowService_Impl();
	}

	@Test
	public void showDaoTest() {
		Assertions.assertNull(service.getShowDao());
		ShowDao showDao = Mockito.mock(ShowDao.class);
		service.setShowDao(showDao);
		Assertions.assertEquals(showDao, service.getShowDao());
	}

	@Test
	public void guideServiceTest() {
		Assertions.assertNull(service.getGuideService());
		GuideService guideService = Mockito.mock(GuideService.class);
		service.setGuideService(guideService);
		Assertions.assertEquals(guideService, service.getGuideService());
	}

	@Test
	public void episodeServiceTest() {
		Assertions.assertNull(service.getEpisodeService());
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		service.setEpisodeService(episodeService);
		Assertions.assertEquals(episodeService, service.getEpisodeService());
	}

	@Test
	public void ToStringTest() {
		Assertions.assertEquals("ShowService_Impl []", service.toString());
	}

	@Test
	public void createTest() throws Exception {
		Show show = new Show();
		show.setTitle("TITLE");
		show.setGuideId("GUIDEID");
		show.setAirTime(LocalTime.NOON);
		show.setTimezone(ZoneId.systemDefault().getId());
		ShowDao showDao = Mockito.mock(ShowDao.class);
		service.setShowDao(showDao);

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

		Mockito.verify(showDao).create(show);
		Mockito.verifyNoMoreInteractions(showDao);

		ArgumentCaptor<Episode> argumentCaptor = ArgumentCaptor.forClass(Episode.class);
		Mockito.verify(episodeService, Mockito.times(2)).create(argumentCaptor.capture());
		Assertions.assertEquals(2, argumentCaptor.getAllValues().size());
		Assertions.assertEquals(EpisodeStatus.SKIPPED, argumentCaptor.getAllValues().get(0).getStatus());
		Assertions.assertEquals(EpisodeStatus.PENDING, argumentCaptor.getAllValues().get(1).getStatus());
		Mockito.verifyNoMoreInteractions(episodeService);

		Mockito.verify(guideService).readEpisodes("GUIDEID");
		Mockito.verifyNoMoreInteractions(showDao, guideService);

	}

	@Test
	public void createExceptionTest() throws Exception {
		ShowDao showDao = Mockito.mock(ShowDao.class);
		Show show = new Show();
		show.setTitle("TITLE");
		show.setGuideId("GUIDEID");
		show.setAirTime(LocalTime.NOON);
		show.setTimezone(ZoneId.systemDefault().getId());
		service.setShowDao(showDao);

		GuideService guideService = Mockito.mock(GuideService.class);
		GuideEpisode guideEp1 = new GuideEpisode();
		guideEp1.setAirDate(LocalDate.ofEpochDay(0));
		GuideEpisode guideEp2 = new GuideEpisode();
		guideEp2.setAirDate(LocalDate.now().plusDays(30));
		Mockito.when(guideService.readEpisodes("GUIDEID")).thenThrow(GuideException.class);
		service.setGuideService(guideService);

		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		service.setEpisodeService(episodeService);

		ShowException ex = Assertions.assertThrows(ShowException.class, () -> {
			service.create(show);
		});

		// TODO better error message
		Assertions.assertEquals("io.delimeat.guide.exception.GuideException", ex.getMessage());

	}

	@Test
	public void readTest() throws Exception {
		ShowDao showDao = Mockito.mock(ShowDao.class);
		Show show = new Show();
		Mockito.when(showDao.read(Long.MAX_VALUE)).thenReturn(show);
		service.setShowDao(showDao);

		Show actualShow = service.read(Long.MAX_VALUE);
		Assertions.assertEquals(show, actualShow);

		Mockito.verify(showDao).read(Long.MAX_VALUE);
		Mockito.verifyNoMoreInteractions(showDao);
	}

	@Test
	public void readNotFoundExceptionTest() throws Exception {
		ShowDao showDao = Mockito.mock(ShowDao.class);
		Mockito.when(showDao.read(Long.MAX_VALUE)).thenThrow(EntityNotFoundException.class);
		service.setShowDao(showDao);

		ShowNotFoundException ex = Assertions.assertThrows(ShowNotFoundException.class, () -> {
			service.read(Long.MAX_VALUE);
		});

		// TODO better error message
		Assertions.assertNull(ex.getMessage());
	}

	@Test
	public void readExceptionTest() throws Exception {
		ShowDao showDao = Mockito.mock(ShowDao.class);
		Mockito.when(showDao.read(Long.MAX_VALUE)).thenThrow(OptimisticLockException.class);
		service.setShowDao(showDao);

		ShowException ex = Assertions.assertThrows(ShowException.class, () -> {
			service.read(Long.MAX_VALUE);
		});

		// TODO better error message
		Assertions.assertEquals("javax.persistence.OptimisticLockException", ex.getMessage());
	}

	@Test
	public void readAllTest() throws Exception {
		ShowDao showDao = Mockito.mock(ShowDao.class);
		List<Show> expectedShows = new ArrayList<Show>();
		Show show = new Show();
		expectedShows.add(show);
		Mockito.when(showDao.readAll()).thenReturn(expectedShows);
		service.setShowDao(showDao);

		List<Show> actualShows = service.readAll();
		Assertions.assertEquals(1, actualShows.size());
		Assertions.assertEquals(show, actualShows.get(0));

		Mockito.verify(showDao).readAll();
		Mockito.verifyNoMoreInteractions(showDao);
	}

	@Test
	public void readAllExceptionTest() throws Exception {
		ShowDao showDao = Mockito.mock(ShowDao.class);
		List<Show> expectedShows = new ArrayList<Show>();
		Show show = new Show();
		expectedShows.add(show);
		Mockito.when(showDao.readAll()).thenThrow(OptimisticLockException.class);
		service.setShowDao(showDao);

		ShowException ex = Assertions.assertThrows(ShowException.class, () -> {
			service.readAll();
		});

		// TODO better error message
		Assertions.assertEquals("javax.persistence.OptimisticLockException", ex.getMessage());
	}

	@Test
	public void updateTest() throws Exception {
		ShowDao showDao = Mockito.mock(ShowDao.class);
		Show show = new Show();
		Mockito.when(showDao.update(show)).thenReturn(show);
		service.setShowDao(showDao);

		Show actualShow = service.update(new Show());
		Assertions.assertEquals(show, actualShow);

		Mockito.verify(showDao).update(show);
		Mockito.verifyNoMoreInteractions(showDao);
	}

	@Test
	public void updateConcurrencyExceptionTest() throws Exception {
		ShowDao showDao = Mockito.mock(ShowDao.class);
		Show show = new Show();
		Mockito.when(showDao.update(show)).thenThrow(OptimisticLockException.class);
		service.setShowDao(showDao);

		ShowConcurrencyException ex = Assertions.assertThrows(ShowConcurrencyException.class, () -> {
			service.update(new Show());
		});

		// TODO better error message
		Assertions.assertEquals("javax.persistence.OptimisticLockException", ex.getMessage());
	}

	@Test
	public void updateExceptionTest() throws Exception {
		ShowDao showDao = Mockito.mock(ShowDao.class);
		Show show = new Show();
		Mockito.when(showDao.update(show)).thenThrow(PersistenceException.class);
		service.setShowDao(showDao);

		ShowException ex = Assertions.assertThrows(ShowException.class, () -> {
			service.update(new Show());
		});

		// TODO better error message
		Assertions.assertEquals("javax.persistence.PersistenceException", ex.getMessage());
	}

	@Test
	public void deleteTest() throws Exception {
		ShowDao showDao = Mockito.mock(ShowDao.class);
		service.setShowDao(showDao);

		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		service.setEpisodeService(episodeService);

		service.delete(Long.MAX_VALUE);

		Mockito.verify(showDao).delete(Long.MAX_VALUE);
		Mockito.verifyNoMoreInteractions(showDao, episodeService);
	}

	@Test
	public void deleteExceptionTest() throws Exception {
		ShowDao showDao = Mockito.mock(ShowDao.class);
		service.setShowDao(showDao);
		Mockito.doThrow(PersistenceException.class).when(showDao).delete(Long.MAX_VALUE);

		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		service.setEpisodeService(episodeService);

		ShowException ex = Assertions.assertThrows(ShowException.class, () -> {
			service.delete(Long.MAX_VALUE);

		});

		// TODO better error message
		Assertions.assertEquals("javax.persistence.PersistenceException", ex.getMessage());
	}

	@Test
	public void readAllEpisodesTest() throws Exception {
		Show show = new Show();
		Episode ep = new Episode();
		show.setEpisodes(Arrays.asList(ep));

		ShowDao showDao = Mockito.mock(ShowDao.class);
		Mockito.when(showDao.read(1L)).thenReturn(show);
		service.setShowDao(showDao);

		List<Episode> episodes = service.readAllEpisodes(1L);
		Assertions.assertNotNull(episodes);
		Assertions.assertEquals(1, episodes.size());
		Assertions.assertEquals(ep, episodes.get(0));

		Mockito.verify(showDao).read(1L);
		Mockito.verifyNoMoreInteractions(showDao);
	}

	@Test
	public void readAllEpisodesExceptionTest() throws Exception {
		ShowDao showDao = Mockito.mock(ShowDao.class);
		Mockito.when(showDao.read(1L)).thenThrow(PersistenceException.class);
		service.setShowDao(showDao);

		ShowException ex = Assertions.assertThrows(ShowException.class, () -> {
			service.readAllEpisodes(1L);
		});

		// TODO better error message
		Assertions.assertEquals("javax.persistence.PersistenceException", ex.getMessage());
	}

}
