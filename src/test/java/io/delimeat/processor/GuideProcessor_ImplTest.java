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

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.config.domain.Config;
import io.delimeat.guide.GuideService;
import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.guide.domain.GuideInfo;
import io.delimeat.processor.GuideProcessor_Impl;
import io.delimeat.processor.ProcessorListener;
import io.delimeat.show.EpisodeService;
import io.delimeat.show.ShowService;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.EpisodeStatus;
import io.delimeat.show.domain.Show;

public class GuideProcessor_ImplTest {

	private GuideProcessor_Impl processor;

	@Before
	public void setUp() throws Exception {
		processor = new GuideProcessor_Impl();
	}

	@Test
	public void processEntityTest() {
		Assert.assertNull(processor.getProcessEntity());
		Show show = new Show();
		processor.setProcessEntity(show);
		Assert.assertEquals(show, processor.getProcessEntity());
	}

	@Test
	public void configTest() {
		Assert.assertNull(processor.getConfig());
		Config config = new Config();
		processor.setConfig(config);
		Assert.assertEquals(config, processor.getConfig());
	}

	@Test
	public void showServiceTest() {
		Assert.assertNull(processor.getShowService());
		ShowService showService = Mockito.mock(ShowService.class);
		processor.setShowService(showService);
		Assert.assertEquals(showService, processor.getShowService());
	}

	@Test
	public void guideServiceTest() {
		Assert.assertNull(processor.getGuideService());
		GuideService guideService = Mockito.mock(GuideService.class);
		processor.setGuideService(guideService);
		Assert.assertEquals(guideService, processor.getGuideService());
	}
	
	@Test
	public void episodeServiceTest() {
		Assert.assertNull(processor.getEpisodeService());
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		processor.setEpisodeService(episodeService);
		Assert.assertEquals(episodeService, processor.getEpisodeService());
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
	public void listenerTest() {
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
	public void getEpisodesToCreateTest(){
		Episode existingEp = new Episode();
		existingEp.setSeasonNum(1);
		existingEp.setEpisodeNum(1);
		List<Episode> showEps = Arrays.asList(existingEp);
		
		GuideEpisode existingGuideEp = new GuideEpisode();
		existingGuideEp.setSeasonNum(1);
		existingGuideEp.setEpisodeNum(1);
		
		GuideEpisode newGuideEp = new GuideEpisode();
		newGuideEp.setSeasonNum(2);
		newGuideEp.setEpisodeNum(3);
		
		List<GuideEpisode> guideEps = Arrays.asList(existingGuideEp, newGuideEp);
		
		List<Episode> result = processor.getEpisodesToCreate(guideEps, showEps);
		
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(2, result.get(0).getSeasonNum());
		Assert.assertEquals(3, result.get(0).getEpisodeNum());
	}
	
	@Test
	public void getEpisodesToDeleteTest(){
		// not deleted because it is in the guide and found
		Episode existingFoundEp = new Episode();
		existingFoundEp.setSeasonNum(1);
		existingFoundEp.setEpisodeNum(2);
		existingFoundEp.setStatus(EpisodeStatus.FOUND);
		
		//this ep should be deleted because it is not in the guide and is pending
		Episode existingPendingEp = new Episode();
		existingPendingEp.setSeasonNum(2);
		existingPendingEp.setEpisodeNum(3);
		existingPendingEp.setStatus(EpisodeStatus.PENDING);
		
		// not deleted because it is skipped even though its not in the guide
		Episode existingSkippedEp = new Episode();
		existingSkippedEp.setSeasonNum(3);
		existingSkippedEp.setEpisodeNum(4);
		existingSkippedEp.setStatus(EpisodeStatus.SKIPPED);
		
		List<Episode> showEps = Arrays.asList(existingFoundEp,existingPendingEp, existingSkippedEp);
		
		GuideEpisode existingGuideEp = new GuideEpisode();
		existingGuideEp.setSeasonNum(1);
		existingGuideEp.setEpisodeNum(2);
		
		List<GuideEpisode> guideEps = Arrays.asList(existingGuideEp);
		
		List<Episode> result = processor.getEpisodesToDelete(guideEps, showEps);
		
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(existingPendingEp, result.get(0));
	}
	
	@Test
	public void getEpisodesToUpdateTest() throws ParseException{
		Episode existingNotPendingEp = new Episode();
		existingNotPendingEp.setSeasonNum(2);
		existingNotPendingEp.setEpisodeNum(3);
		existingNotPendingEp.setAirDate(LocalDate.MIN);
		existingNotPendingEp.setTitle("TITLE");
		existingNotPendingEp.setStatus(EpisodeStatus.SKIPPED);
		
		Episode existingNoUpdateEp = new Episode();
		existingNoUpdateEp.setSeasonNum(3);
		existingNoUpdateEp.setEpisodeNum(4);
		existingNoUpdateEp.setAirDate(LocalDate.parse("1970-01-01"));
		existingNoUpdateEp.setTitle("TITLE");
		existingNoUpdateEp.setStatus(EpisodeStatus.PENDING);
		
		Episode existingUpdateTitleEp = new Episode();
		existingUpdateTitleEp.setSeasonNum(4);
		existingUpdateTitleEp.setEpisodeNum(5);
		existingUpdateTitleEp.setAirDate(LocalDate.MIN);
		existingUpdateTitleEp.setTitle("TITLE");
		existingUpdateTitleEp.setStatus(EpisodeStatus.PENDING);
		
		Episode existingUpdateAirDateEp = new Episode();
		existingUpdateAirDateEp.setSeasonNum(5);
		existingUpdateAirDateEp.setEpisodeNum(6);
		existingUpdateAirDateEp.setAirDate(LocalDate.MIN);
		existingUpdateAirDateEp.setTitle("TITLE");
		existingUpdateAirDateEp.setStatus(EpisodeStatus.PENDING);
		
		List<Episode> showEps = Arrays.asList(existingNotPendingEp,existingNoUpdateEp,existingUpdateTitleEp,existingUpdateAirDateEp);
		
		GuideEpisode notExistingGuideEp = new GuideEpisode();
		notExistingGuideEp.setSeasonNum(1);
		notExistingGuideEp.setEpisodeNum(2);
		notExistingGuideEp.setAirDate(LocalDate.ofEpochDay(0));
		notExistingGuideEp.setTitle("TITLE");
		
		GuideEpisode existingNotPendingGuideEp = new GuideEpisode();
		existingNotPendingGuideEp.setSeasonNum(2);
		existingNotPendingGuideEp.setEpisodeNum(3);
		existingNotPendingGuideEp.setAirDate(LocalDate.ofEpochDay(0));
		existingNotPendingGuideEp.setTitle("TITLE");
		
		GuideEpisode existingNoUpdateGuideEp = new GuideEpisode();
		existingNoUpdateGuideEp.setSeasonNum(3);
		existingNoUpdateGuideEp.setEpisodeNum(4);
		existingNoUpdateGuideEp.setAirDate(LocalDate.ofEpochDay(0));
		existingNoUpdateGuideEp.setTitle("TITLE");
		
		GuideEpisode existingUpdateTitleGuideEp = new GuideEpisode();
		existingUpdateTitleGuideEp.setSeasonNum(4);
		existingUpdateTitleGuideEp.setEpisodeNum(5);
		existingUpdateTitleGuideEp.setAirDate(LocalDate.ofEpochDay(0));
		existingUpdateTitleGuideEp.setTitle("UPDATE_TITLE");	
		
		GuideEpisode existingUpdateAirDateGuideEp = new GuideEpisode();
		existingUpdateAirDateGuideEp.setSeasonNum(5);
		existingUpdateAirDateGuideEp.setEpisodeNum(6);
		existingUpdateAirDateGuideEp.setAirDate(LocalDate.parse("1990-02-03"));
		existingUpdateAirDateGuideEp.setTitle("TITLE");
		
		List<GuideEpisode> guideEps = Arrays.asList(notExistingGuideEp,existingNotPendingGuideEp,existingNoUpdateGuideEp,existingNoUpdateGuideEp,existingUpdateTitleGuideEp,existingUpdateAirDateGuideEp);
		
		List<Episode> result = processor.getEpisodesToUpdate(guideEps, showEps);
		
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(existingUpdateTitleEp, result.get(0));
		Assert.assertEquals("UPDATE_TITLE", result.get(0).getTitle());
		Assert.assertEquals(existingUpdateAirDateEp, result.get(1));
		Assert.assertEquals(LocalDate.parse("1990-02-03"), result.get(1).getAirDate());
		
	}

	@Test
	public void processAlreadyUpdatedTest() throws Exception {
		Date testStart = new Date();
		Config config = new Config();
		processor.setConfig(config);

		ProcessorListener listener = Mockito.mock(ProcessorListener.class);
		processor.addListener(listener);

		Show show = new Show();
		show.setShowId(Long.MAX_VALUE);
		show.setGuideId("GUIDEID");
		show.setLastGuideUpdate(Instant.MAX);
		processor.setProcessEntity(show);

		ShowService showService = Mockito.mock(ShowService.class);
		Mockito.when(showService.read(Long.MAX_VALUE)).thenReturn(show);
		processor.setShowService(showService);

		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		processor.setEpisodeService(episodeService);
		
		GuideInfo info = new GuideInfo();
		info.setLastUpdated(LocalDate.parse("2000-01-01"));
		GuideService guideService = Mockito.mock(GuideService.class);
		Mockito.when(guideService.read("GUIDEID")).thenReturn(info);
		processor.setGuideService(guideService);

		processor.process();
		
		Date testEnd = new Date();
		
		Assert.assertFalse(processor.isActive());
		Assert.assertEquals(Instant.MAX, show.getLastGuideUpdate());
		Assert.assertTrue(show.getLastGuideCheck().toEpochMilli()>=testStart.getTime());
		Assert.assertTrue(show.getLastGuideCheck().toEpochMilli()<=testEnd.getTime());

		Mockito.verify(listener).alertComplete(processor);
		Mockito.verifyNoMoreInteractions(listener);

		Mockito.verify(showService).read(Long.MAX_VALUE);
		Mockito.verify(showService).update(show);
		Mockito.verifyNoMoreInteractions(showService);

		Mockito.verify(guideService).read("GUIDEID");
		Mockito.verifyNoMoreInteractions(guideService);
		
		Mockito.verifyZeroInteractions(episodeService);
	}

	@Test
	public void processUpdateTest() throws Exception {
		Date testStart = new Date();
		
		Config config = new Config();
		processor.setConfig(config);

		ProcessorListener listener = Mockito.mock(ProcessorListener.class);
		processor.addListener(listener);

		Show show = new Show();
		show.setShowId(Long.MAX_VALUE);
		show.setGuideId("GUIDEID");
		show.setLastGuideUpdate(Instant.EPOCH);
		show.setAiring(true);
		
		Episode showEpToDelete = new Episode();
		showEpToDelete.setSeasonNum(1);
		showEpToDelete.setEpisodeNum(2);
		showEpToDelete.setEpisodeId(Long.MIN_VALUE);
		showEpToDelete.setStatus(EpisodeStatus.PENDING);
		
		Episode showEpToUpdate = new Episode();
		showEpToUpdate.setSeasonNum(2);
		showEpToUpdate.setEpisodeNum(3);
		showEpToUpdate.setTitle("TITLE");
		showEpToUpdate.setAirDate(LocalDate.parse("2000-01-01"));
		showEpToUpdate.setStatus(EpisodeStatus.PENDING);
		
		processor.setProcessEntity(show);

		ShowService showService = Mockito.mock(ShowService.class);
		Mockito.when(showService.read(Long.MAX_VALUE)).thenReturn(show);
		Mockito.when(showService.readAllEpisodes(Long.MAX_VALUE)).thenReturn(Arrays.asList(showEpToDelete, showEpToUpdate));
		
		processor.setShowService(showService);
		
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
	
		processor.setEpisodeService(episodeService);

		GuideInfo info = new GuideInfo();
		info.setLastUpdated(LocalDate.parse("2016-01-29"));
		info.setAiring(false);
		
		
		GuideEpisode guideEpToUpdate = new GuideEpisode();
		guideEpToUpdate.setSeasonNum(2);
		guideEpToUpdate.setEpisodeNum(3);
		guideEpToUpdate.setTitle("UPDATED_TITLE");
		guideEpToUpdate.setAirDate(LocalDate.parse("2017-03-28"));
		
		GuideEpisode guideEpToCreate = new GuideEpisode();
		guideEpToCreate.setSeasonNum(3);
		guideEpToCreate.setEpisodeNum(4);
		
		GuideService guideService = Mockito.mock(GuideService.class);
		Mockito.when(guideService.read("GUIDEID")).thenReturn(info);
		Mockito.when(guideService.readEpisodes("GUIDEID")).thenReturn(Arrays.asList(guideEpToUpdate,guideEpToCreate));
		processor.setGuideService(guideService);
		
		processor.process();
		
		Date testEnd = new Date();

		Assert.assertFalse(processor.isActive());
		Assert.assertFalse(show.isAiring());
		Assert.assertTrue(show.getLastGuideUpdate().toEpochMilli()>=testStart.getTime());
		Assert.assertTrue(show.getLastGuideUpdate().toEpochMilli()<=testEnd.getTime());
		Assert.assertTrue(show.getLastGuideCheck().toEpochMilli()>=testStart.getTime());
		Assert.assertTrue(show.getLastGuideCheck().toEpochMilli()<=testEnd.getTime());

		Mockito.verify(listener).alertComplete(processor);
		Mockito.verifyNoMoreInteractions(listener);

		Mockito.verify(showService).read(Long.MAX_VALUE);
		Mockito.verify(showService).readAllEpisodes(Long.MAX_VALUE);
		Mockito.verify(showService).update(show);
		Mockito.verifyNoMoreInteractions(showService);
		
		Mockito.verify(episodeService).delete(showEpToDelete.getEpisodeId());
		Mockito.verify(episodeService, Mockito.times(2)).save(Mockito.any(Episode.class));
		Mockito.verifyNoMoreInteractions(episodeService);

		Mockito.verify(guideService).read("GUIDEID");
		Mockito.verify(guideService).readEpisodes("GUIDEID");
		Mockito.verifyNoMoreInteractions(guideService);
		
	}
	
	@Test
	public void processNoUpdateTest() throws Exception {
		Date testStart = new Date();
		
		Config config = new Config();
		processor.setConfig(config);

		ProcessorListener listener = Mockito.mock(ProcessorListener.class);
		processor.addListener(listener);

		Show show = new Show();
		show.setShowId(Long.MAX_VALUE);
		show.setGuideId("GUIDEID");
		show.setLastGuideUpdate(Instant.EPOCH);
		show.setAiring(true);
		
		Episode showEp = new Episode();
		showEp.setSeasonNum(1);
		showEp.setEpisodeNum(2);
		showEp.setTitle("TITLE");
		showEp.setAirDate(LocalDate.parse("2017-03-28"));
		showEp.setEpisodeId(Long.MIN_VALUE);
		showEp.setStatus(EpisodeStatus.PENDING);
		
		processor.setProcessEntity(show);

		ShowService showService = Mockito.mock(ShowService.class);
		Mockito.when(showService.read(Long.MAX_VALUE)).thenReturn(show);
		Mockito.when(showService.readAllEpisodes(Long.MAX_VALUE)).thenReturn(Arrays.asList(showEp));
		
		processor.setShowService(showService);
		
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		processor.setEpisodeService(episodeService);

		GuideInfo info = new GuideInfo();
		info.setLastUpdated(LocalDate.parse("2016-01-29"));
		info.setAiring(true);
		
		GuideEpisode guideEp = new GuideEpisode();
		guideEp.setSeasonNum(1);
		guideEp.setEpisodeNum(2);
		guideEp.setTitle("TITLE");
		guideEp.setAirDate(LocalDate.parse("2017-03-28"));
		
		GuideEpisode guideEpToCreate = new GuideEpisode();
		guideEpToCreate.setSeasonNum(3);
		guideEpToCreate.setEpisodeNum(4);
		
		GuideService guideService = Mockito.mock(GuideService.class);
		Mockito.when(guideService.read("GUIDEID")).thenReturn(info);
		Mockito.when(guideService.readEpisodes("GUIDEID")).thenReturn(Arrays.asList(guideEp));
		processor.setGuideService(guideService);
		
		processor.process();
		
		Date testEnd = new Date();

		Assert.assertFalse(processor.isActive());
		Assert.assertTrue(show.isAiring());
		Assert.assertEquals(Instant.EPOCH, show.getLastGuideUpdate());
		Assert.assertTrue(show.getLastGuideCheck().toEpochMilli()>=testStart.getTime());
		Assert.assertTrue(show.getLastGuideCheck().toEpochMilli()<=testEnd.getTime());

		Mockito.verify(listener).alertComplete(processor);
		Mockito.verifyNoMoreInteractions(listener);

		Mockito.verify(showService).read(Long.MAX_VALUE);
		Mockito.verify(showService).readAllEpisodes(Long.MAX_VALUE);
		Mockito.verify(showService).update(show);
		Mockito.verifyNoMoreInteractions(showService);
		
		Mockito.verifyZeroInteractions(episodeService);

		Mockito.verify(guideService).read("GUIDEID");
		Mockito.verify(guideService).readEpisodes("GUIDEID");
		Mockito.verifyNoMoreInteractions(guideService);
		
	}


	@Test
	public void toStringTest() {
		Assert.assertEquals("GuideProcessor_Impl(showService=null, episodeService=null, guideService=null)",processor.toString());
	}
}
