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

import io.delimeat.guide.GuideService;
import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.show.EpisodeService;
import io.delimeat.show.ShowService_Impl;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.EpisodeStatus;
import io.delimeat.show.domain.Show;

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
		Mockito.when(guideService.readEpisodes("GUIDEID")).thenReturn(Arrays.asList(guideEp1,guideEp2));
		service.setGuideService(guideService);
		
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		service.setEpisodeService(episodeService);

		service.create(show);
		
		Mockito.verify(showRepository).save(show);
		Mockito.verifyNoMoreInteractions(showRepository);
		
		Mockito.verify(episodeService,Mockito.times(2)).save(Mockito.any());
		ArgumentCaptor<Episode> argumentCaptor = ArgumentCaptor.forClass(Episode.class);
		Mockito.verify(episodeService,Mockito.times(2)).save(argumentCaptor.capture());
		Assert.assertEquals(2,argumentCaptor.getAllValues().size());
		Assert.assertEquals(EpisodeStatus.SKIPPED, argumentCaptor.getAllValues().get(0).getStatus());
		Assert.assertEquals(EpisodeStatus.PENDING, argumentCaptor.getAllValues().get(1).getStatus());
		Mockito.verifyNoMoreInteractions(episodeService);
		
		Mockito.verify(guideService).readEpisodes("GUIDEID");
		Mockito.verifyNoMoreInteractions(showRepository, guideService);

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

	@Test
	public void updateTest() throws Exception {
		ShowRepository showRepository = Mockito.mock(ShowRepository.class);
		Show show = new Show();
		Mockito.when(showRepository.save(show))
				.thenReturn(show);
		service.setShowRepository(showRepository);

		Show actualShow = service.update(new Show());
		Assert.assertEquals(show, actualShow);
		
		Mockito.verify(showRepository).save(show);
		Mockito.verifyNoMoreInteractions(showRepository);
	}

	@Test
	public void deleteTest() throws Exception {
		ShowRepository showRepository = Mockito.mock(ShowRepository.class);
		service.setShowRepository(showRepository);
		service.delete(Long.MAX_VALUE);
		
		Mockito.verify(showRepository).delete(Long.MAX_VALUE);
		Mockito.verifyNoMoreInteractions(showRepository);
	}
  
  	@Test
  	public void readAllEpisodesTest() throws Exception{
  		Show show = new Show();
  		show.setShowId(1L);
     	Episode ep = new Episode();
		ShowRepository showRepository = Mockito.mock(ShowRepository.class);
		Mockito.when(showRepository.findOne(Long.MAX_VALUE)).thenReturn(show);
     	service.setShowRepository(showRepository);
     	
     	EpisodeService episodeService = Mockito.mock(EpisodeService.class);
     	Mockito.when(episodeService.findByShow(show)).thenReturn(Arrays.asList(ep));
     	service.setEpisodeService(episodeService);
     
     	List<Episode> episodes = service.readAllEpisodes(Long.MAX_VALUE);
     	Assert.assertNotNull(episodes);
     	Assert.assertEquals(1, episodes.size());
     	Assert.assertEquals(ep, episodes.get(0));
     	
     	Mockito.verify(showRepository).findOne(Long.MAX_VALUE);
     	Mockito.verifyNoMoreInteractions(showRepository);
     	
     	Mockito.verify(episodeService).findByShow(show);
     	Mockito.verifyNoMoreInteractions(episodeService);
   }
  	
  @Test
  public void cleanTitleTest(){
	  Show show = new Show();
	  show.setTitle("This is 2015 a V'ery Nice $#10293734521,.<>~???! title (2016)");
	  
	  Show result = service.cleanTitle(show);
	  
	  System.out.println(result.getTitle());
	  
	  Assert.assertEquals(show, result);
	  Assert.assertEquals("This is 2015 a Very Nice 10293734521 title", result.getTitle());
  }
  	

}
