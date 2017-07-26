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
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.guide.GuideService;
import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.guide.domain.GuideInfo;
import io.delimeat.show.EpisodeService;
import io.delimeat.show.ShowService;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.EpisodeStatus;
import io.delimeat.show.domain.Show;

public class GuideItemProcessor_ImplTest {

	private GuideItemProcessor_Impl processor;
	
	@Before
	public void setUp(){
		processor = new GuideItemProcessor_Impl();
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
	public void createEpisodesTest(){
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

		Show show = new Show();
		
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		processor.setEpisodeService(episodeService);
		
		Assert.assertTrue(processor.createEpisodes(guideEps, showEps,show));
		
		Mockito.verify(episodeService).update(Mockito.any());
		Mockito.verifyNoMoreInteractions(episodeService);
	}
	
	@Test
	public void createEpisodesNoEpisodesTest(){
		
		Assert.assertFalse(processor.createEpisodes(Collections.emptyList(), Collections.emptyList(), null));
	}
	
	@Test
	public void deleteEpisodeTest(){
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
		
		Assert.assertTrue(processor.deleteEpisodes(guideEps, showEps));
		
		Mockito.verify(episodeService).delete(Long.MAX_VALUE);
		Mockito.verifyNoMoreInteractions(episodeService);
	}

	@Test
	public void deleteEpisodesNoEpisodesTest(){
		
		Assert.assertFalse(processor.deleteEpisodes(Collections.emptyList(), Collections.emptyList()));
	}
	
	@Test
	public void updateEpisodesTest() throws ParseException{
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
		
		Assert.assertTrue(processor.updateEpisodes(guideEps, showEps));
		
		Mockito.verify(episodeService, Mockito.times(2)).update(Mockito.any());
		Mockito.verifyNoMoreInteractions(episodeService);
	}

	@Test
	public void updateEpisodesNoEpisodesTest(){
		
		Assert.assertFalse(processor.updateEpisodes(Collections.emptyList(), Collections.emptyList()));
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
		
		Assert.assertEquals(Instant.MAX, show.getLastGuideUpdate());
		Assert.assertTrue(show.getLastGuideCheck().toEpochMilli()>=testStart.getTime());
		Assert.assertTrue(show.getLastGuideCheck().toEpochMilli()<=testEnd.getTime());

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
		
		Episode showEpToUpdate = new Episode();
		showEpToUpdate.setSeasonNum(2);
		showEpToUpdate.setEpisodeNum(3);
		showEpToUpdate.setTitle("TITLE");
		showEpToUpdate.setAirDate(LocalDate.parse("2000-01-01"));
		showEpToUpdate.setStatus(EpisodeStatus.PENDING);
		
		ShowService showService = Mockito.mock(ShowService.class);
		Mockito.when(showService.read(Long.MAX_VALUE)).thenReturn(show);
		
		processor.setShowService(showService);
		
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		Mockito.when(episodeService.findByShow(Long.MAX_VALUE)).thenReturn(Arrays.asList(showEpToDelete, showEpToUpdate));

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
		
		processor.process(show);
		
		Date testEnd = new Date();

		Assert.assertFalse(show.isAiring());
		Assert.assertTrue(show.getLastGuideUpdate().toEpochMilli()>=testStart.getTime());
		Assert.assertTrue(show.getLastGuideUpdate().toEpochMilli()<=testEnd.getTime());
		Assert.assertTrue(show.getLastGuideCheck().toEpochMilli()>=testStart.getTime());
		Assert.assertTrue(show.getLastGuideCheck().toEpochMilli()<=testEnd.getTime());

		Mockito.verify(showService).update(show);
		Mockito.verifyNoMoreInteractions(showService);
		
		Mockito.verify(episodeService).findByShow(Long.MAX_VALUE);
		Mockito.verify(episodeService).delete(Long.MIN_VALUE);
		Mockito.verify(episodeService, Mockito.times(2)).update(Mockito.any(Episode.class));
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
		Mockito.when(episodeService.findByShow(Long.MAX_VALUE)).thenReturn(Arrays.asList(showEp));
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

		Assert.assertTrue(show.isAiring());
		//Assert.assertEquals(Instant.EPOCH, show.getLastGuideUpdate());
		Assert.assertTrue(show.getLastGuideCheck().toEpochMilli()>=testStart.getTime());
		Assert.assertTrue(show.getLastGuideCheck().toEpochMilli()<=testEnd.getTime());

		Mockito.verify(showService).update(show);
		Mockito.verifyNoMoreInteractions(showService);
		
		Mockito.verify(episodeService).findByShow(Long.MAX_VALUE);
		Mockito.verifyNoMoreInteractions(episodeService);

		Mockito.verify(guideService).read("GUIDEID");
		Mockito.verify(guideService).readEpisodes("GUIDEID");
		Mockito.verifyNoMoreInteractions(guideService);
		
	}
	
}
