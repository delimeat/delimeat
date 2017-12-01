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
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import io.delimeat.guide.GuideService;
import io.delimeat.guide.entity.GuideEpisode;
import io.delimeat.guide.entity.GuideInfo;
import io.delimeat.show.EpisodeService;
import io.delimeat.show.ShowService;
import io.delimeat.show.entity.Episode;
import io.delimeat.show.entity.EpisodeStatus;
import io.delimeat.show.entity.Show;

public class GuideItemProcessor_ImplTest {

	private GuideItemProcessor_Impl processor;
	
	@BeforeEach
	public void setUp(){
		processor = new GuideItemProcessor_Impl();
	}

	@Test
	public void toStringTest(){
		Assertions.assertEquals("GuideItemProcessor_Impl []", processor.toString());
	}
	
	@Test
	public void showServiceTest() {
		Assertions.assertNull(processor.getShowService());
		ShowService showService = Mockito.mock(ShowService.class);
		processor.setShowService(showService);
		Assertions.assertEquals(showService, processor.getShowService());
	}

	@Test
	public void guideServiceTest() {
		Assertions.assertNull(processor.getGuideService());
		GuideService guideService = Mockito.mock(GuideService.class);
		processor.setGuideService(guideService);
		Assertions.assertEquals(guideService, processor.getGuideService());
	}
	
	@Test
	public void episodeServiceTest() {
		Assertions.assertNull(processor.getEpisodeService());
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		processor.setEpisodeService(episodeService);
		Assertions.assertEquals(episodeService, processor.getEpisodeService());
	}
	
	@Test
	public void minPendingAirDateTest(){
		Episode skippedEpisode = new Episode();
		skippedEpisode.setStatus(EpisodeStatus.SKIPPED);
		skippedEpisode.setAirDate(LocalDate.of(2000, 12, 31));
		
		Episode foundEpisode = new Episode();
		foundEpisode.setStatus(EpisodeStatus.FOUND);
		foundEpisode.setAirDate(LocalDate.of(2017, 01, 01));
		
		Episode minPendingEpisode = new Episode();
		minPendingEpisode.setStatus(EpisodeStatus.PENDING);
		minPendingEpisode.setAirDate(LocalDate.of(2017, 01, 02));
		
		Episode maxPendingEpisode = new Episode();
		maxPendingEpisode.setStatus(EpisodeStatus.PENDING);
		maxPendingEpisode.setAirDate(LocalDate.of(2017, 12, 31));
		
		LocalDate result = processor.minPendingAirDate(Arrays.asList(skippedEpisode,foundEpisode,minPendingEpisode,maxPendingEpisode));
		
		Assertions.assertEquals(LocalDate.of(2017, 01, 02), result);
	}
	
	@Test
	public void minPendingAirDateNoPendingTest(){
		Episode skippedEpisode = new Episode();
		skippedEpisode.setStatus(EpisodeStatus.SKIPPED);
		skippedEpisode.setAirDate(LocalDate.of(2000, 12, 31));
		
		Episode foundEpisode = new Episode();
		foundEpisode.setStatus(EpisodeStatus.FOUND);
		foundEpisode.setAirDate(LocalDate.of(2017, 01, 01));
		
		LocalDate result = processor.minPendingAirDate(Arrays.asList(skippedEpisode,foundEpisode));
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(LocalDate.now(),result);
	}
	
	@Test
	public void createEpisodesTest() throws Exception {
		Episode existingEp = new Episode();
		existingEp.setSeasonNum(1);
		existingEp.setEpisodeNum(1);
		existingEp.setAirDate(LocalDate.MIN);
		existingEp.setStatus(EpisodeStatus.PENDING);
		List<Episode> showEps = Arrays.asList(existingEp);
		
		GuideEpisode existingGuideEp = new GuideEpisode();
		existingGuideEp.setSeasonNum(1);
		existingGuideEp.setEpisodeNum(1);
		existingGuideEp.setAirDate(LocalDate.MIN);
		
		GuideEpisode newGuideEp = new GuideEpisode();
		newGuideEp.setSeasonNum(2);
		newGuideEp.setEpisodeNum(3);
		newGuideEp.setAirDate(LocalDate.of(2017, 9, 8));
		
		List<GuideEpisode> guideEps = Arrays.asList(existingGuideEp, newGuideEp);

		Show show = new Show();
		
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		ArgumentCaptor<Episode> argumentCaptor = ArgumentCaptor.forClass(Episode.class);
		Mockito.when(episodeService.create(argumentCaptor.capture())).thenReturn(null);
		processor.setEpisodeService(episodeService);
		
		Assertions.assertTrue(processor.createEpisodes(guideEps, showEps,show));
		
		Episode result = argumentCaptor.getValue();
		Assertions.assertEquals(LocalDate.of(2017, 9, 8), result.getAirDate());
		Assertions.assertEquals(EpisodeStatus.PENDING, result.getStatus());
		Assertions.assertEquals(2, result.getSeasonNum());
		Assertions.assertEquals(3, result.getEpisodeNum());
		Assertions.assertEquals(show, result.getShow());
		
		Mockito.verify(episodeService).create(Mockito.any());
		Mockito.verifyNoMoreInteractions(episodeService);
	}
	
	@Test
	public void createEpisodesNoEpisodesTest() throws Exception {
		
		Assertions.assertFalse(processor.createEpisodes(Collections.emptyList(), Collections.emptyList(), null));
	}
	
	@Test
	public void deleteEpisodeTest() throws Exception {
		// not deleted because it is in the guide and found
		Episode existingFoundEp = new Episode();
		existingFoundEp.setSeasonNum(1);
		existingFoundEp.setEpisodeNum(2);
		existingFoundEp.setStatus(EpisodeStatus.FOUND);
		
		//this ep should be deleted because it is not in the guide and is pending
		Episode existingPendingEp = new Episode();
		existingPendingEp.setEpisodeId(Long.MAX_VALUE);
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

		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		processor.setEpisodeService(episodeService);
		
		Assertions.assertTrue(processor.deleteEpisodes(guideEps, showEps));
		
		Mockito.verify(episodeService).delete(Long.MAX_VALUE);
		Mockito.verifyNoMoreInteractions(episodeService);
	}

	@Test
	public void deleteEpisodesNoEpisodesTest() throws Exception {
		
		Assertions.assertFalse(processor.deleteEpisodes(Collections.emptyList(), Collections.emptyList()));
	}
	
	@Test
	public void updateEpisodesTest() throws Exception{
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
		
		
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		processor.setEpisodeService(episodeService);
		
		Assertions.assertTrue(processor.updateEpisodes(guideEps, showEps));
		
		Mockito.verify(episodeService, Mockito.times(2)).update(Mockito.any());
		Mockito.verifyNoMoreInteractions(episodeService);
	}

	@Test
	public void updateEpisodesNoEpisodesTest() throws Exception {
		
		Assertions.assertFalse(processor.updateEpisodes(Collections.emptyList(), Collections.emptyList()));
	}
	
	@Test
	public void processAlreadyUpdatedTest() throws Exception {
		Date testStart = new Date();

		Show show = new Show();
		show.setShowId(Long.MAX_VALUE);
		show.setGuideId("GUIDEID");
		show.setLastGuideUpdate(Instant.MAX);

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

		processor.process(show);
		
		Date testEnd = new Date();
		
		Assertions.assertEquals(Instant.MAX, show.getLastGuideUpdate());
		Assertions.assertTrue(show.getLastGuideCheck().toEpochMilli()>=testStart.getTime());
		Assertions.assertTrue(show.getLastGuideCheck().toEpochMilli()<=testEnd.getTime());

		Mockito.verify(showService).update(show);
		Mockito.verifyNoMoreInteractions(showService);

		Mockito.verify(guideService).read("GUIDEID");
		Mockito.verifyNoMoreInteractions(guideService);
		
		Mockito.verifyZeroInteractions(episodeService);
	}

	@Test
	public void processUpdateTest() throws Exception {
		Date testStart = new Date();

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
		showEpToDelete.setAirDate(LocalDate.of(2017,9,8));
		
		Episode showEpToUpdate = new Episode();
		showEpToUpdate.setSeasonNum(2);
		showEpToUpdate.setEpisodeNum(3);
		showEpToUpdate.setTitle("TITLE");
		showEpToUpdate.setAirDate(LocalDate.of(2000,1,1));
		showEpToUpdate.setStatus(EpisodeStatus.PENDING);
		
		ShowService showService = Mockito.mock(ShowService.class);
		Mockito.when(showService.read(Long.MAX_VALUE)).thenReturn(show);
		Mockito.when(showService.readAllEpisodes(Long.MAX_VALUE)).thenReturn(Arrays.asList(showEpToDelete, showEpToUpdate));

		processor.setShowService(showService);
		
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		ArgumentCaptor<Episode> createCaptor = ArgumentCaptor.forClass(Episode.class);
		Mockito.when(episodeService.create(createCaptor.capture())).thenReturn(null);
		ArgumentCaptor<Long> deleteCaptor = ArgumentCaptor.forClass(Long.class);
		Mockito.doNothing().when(episodeService).delete(deleteCaptor.capture());
		ArgumentCaptor<Episode> updateCaptor = ArgumentCaptor.forClass(Episode.class);
		Mockito.when(episodeService.update(updateCaptor.capture())).thenReturn(null);

		processor.setEpisodeService(episodeService);

		GuideInfo info = new GuideInfo();
		info.setLastUpdated(LocalDate.parse("2016-01-29"));
		info.setAiring(false);
		
		
		GuideEpisode guideEpToUpdate = new GuideEpisode();
		guideEpToUpdate.setSeasonNum(2);
		guideEpToUpdate.setEpisodeNum(3);
		guideEpToUpdate.setTitle("UPDATED_TITLE");
		guideEpToUpdate.setAirDate(LocalDate.of(2017,03,28));
		
		GuideEpisode guideEpToCreate = new GuideEpisode();
		guideEpToCreate.setSeasonNum(3);
		guideEpToCreate.setEpisodeNum(4);
		guideEpToCreate.setAirDate(LocalDate.of(2017,03,28));
		
		GuideService guideService = Mockito.mock(GuideService.class);
		Mockito.when(guideService.read("GUIDEID")).thenReturn(info);
		Mockito.when(guideService.readEpisodes("GUIDEID")).thenReturn(Arrays.asList(guideEpToUpdate,guideEpToCreate));
		processor.setGuideService(guideService);
		
		processor.process(show);
		
		Date testEnd = new Date();

		Assertions.assertFalse(show.isAiring());
		Assertions.assertTrue(show.getLastGuideUpdate().toEpochMilli()>=testStart.getTime());
		Assertions.assertTrue(show.getLastGuideUpdate().toEpochMilli()<=testEnd.getTime());
		Assertions.assertTrue(show.getLastGuideCheck().toEpochMilli()>=testStart.getTime());
		Assertions.assertTrue(show.getLastGuideCheck().toEpochMilli()<=testEnd.getTime());
		
		Episode createdEpResult = createCaptor.getValue();
		Assertions.assertEquals(LocalDate.of(2017,03,28), createdEpResult.getAirDate());
		Assertions.assertEquals(3, createdEpResult.getSeasonNum());
		Assertions.assertEquals(4, createdEpResult.getEpisodeNum());
		Assertions.assertEquals(EpisodeStatus.PENDING, createdEpResult.getStatus());
		Assertions.assertEquals(show, createdEpResult.getShow());
		
		Episode updatedEpResult = updateCaptor.getValue();
		Assertions.assertEquals(LocalDate.of(2017,03,28), updatedEpResult.getAirDate());
		Assertions.assertEquals(2, updatedEpResult.getSeasonNum());
		Assertions.assertEquals(3, updatedEpResult.getEpisodeNum());
		Assertions.assertEquals("UPDATED_TITLE", updatedEpResult.getTitle());
		Assertions.assertEquals(EpisodeStatus.PENDING, updatedEpResult.getStatus());
		
		Assertions.assertEquals(Long.MIN_VALUE, deleteCaptor.getValue().longValue());
		
		Mockito.verify(showService).update(show);
		Mockito.verify(showService).readAllEpisodes(Long.MAX_VALUE);
		Mockito.verifyNoMoreInteractions(showService);
		
		Mockito.verify(episodeService).delete(Long.MIN_VALUE);
		Mockito.verify(episodeService).create(Mockito.any());
		Mockito.verify(episodeService).update(Mockito.any());
		Mockito.verifyNoMoreInteractions(episodeService);

		Mockito.verify(guideService).read("GUIDEID");
		Mockito.verify(guideService).readEpisodes("GUIDEID");
		Mockito.verifyNoMoreInteractions(guideService);
		
	}
	
	@Test
	public void processNoUpdateTest() throws Exception {
		Date testStart = new Date();

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
		
		processor.process(show);
		
		Date testEnd = new Date();

		Assertions.assertTrue(show.isAiring());
		//Assertions.assertEquals(Instant.EPOCH, show.getLastGuideUpdate());
		Assertions.assertTrue(show.getLastGuideCheck().toEpochMilli()>=testStart.getTime());
		Assertions.assertTrue(show.getLastGuideCheck().toEpochMilli()<=testEnd.getTime());

		Mockito.verify(showService).update(show);
		Mockito.verify(showService).readAllEpisodes(Long.MAX_VALUE);
		Mockito.verifyNoMoreInteractions(showService);
		
		Mockito.verifyZeroInteractions(episodeService);

		Mockito.verify(guideService).read("GUIDEID");
		Mockito.verify(guideService).readEpisodes("GUIDEID");
		Mockito.verifyNoMoreInteractions(guideService);
		
	}
	
}
