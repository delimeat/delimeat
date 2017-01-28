package io.delimeat.core.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.core.guide.GuideDao;
import io.delimeat.core.guide.GuideSource;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowDao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class ShowService_ImplTest {

	private static final SimpleDateFormat SDF = new SimpleDateFormat(
			"yyyy-MM-dd");

	private ShowService_Impl service;

	@Before
	public void setUp() {
		service = new ShowService_Impl();
	}

	@Test
	public void showDaoTest() {
		Assert.assertNull(service.getShowDao());
		ShowDao mockedShowDao = Mockito.mock(ShowDao.class);
		service.setShowDao(mockedShowDao);
		Assert.assertEquals(mockedShowDao, service.getShowDao());
	}
  
	@Test
	public void guideDaoTest() {
		Assert.assertNull(service.getGuideDao());
		GuideDao dao = Mockito.mock(GuideDao.class);
		service.setGuideDao(dao);
		Assert.assertEquals(dao, service.getGuideDao());
	}

	@Test
	public void createNoGuideSourceTest() throws Exception {
		ShowDao mockedShowDao = Mockito.mock(ShowDao.class);
		Show mockedShow = Mockito.mock(Show.class);
		Mockito.when(mockedShowDao.createOrUpdate(Mockito.any(Show.class)))
				.thenReturn(mockedShow);
		service.setShowDao(mockedShowDao);

		GuideDao mockedGuideDao = Mockito.mock(GuideDao.class);
		service.setGuideDao(mockedGuideDao);

		Show actualShow = service.create(new Show());
		Mockito.verify(mockedShowDao).createOrUpdate(Mockito.any(Show.class));
		Mockito.verify(mockedShowDao, Mockito.times(0)).createOrUpdateEpisode(
				Mockito.any(Episode.class));
		Mockito.verify(mockedGuideDao, Mockito.times(0)).episodes(
				Mockito.anyString());
		Assert.assertEquals(mockedShow, actualShow);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void createNextAndPreviousTest() throws Exception {
		ShowDao mockedShowDao = Mockito.mock(ShowDao.class);
		Show show = new Show();
		show.setGuideId("2");
		// next ep to verify we dont re-add it
		Episode nextEp = new Episode();
		nextEp.setSeasonNum(99);
		nextEp.setEpisodeNum(100);
		show.setNextEpisode(nextEp);
		// prev ep to verify we dont re-add it
		Episode prevEp = new Episode();
		prevEp.setSeasonNum(97);
		prevEp.setEpisodeNum(98);
		show.setPreviousEpisode(prevEp);
		show.setTimezone("EST");
		Mockito.when(mockedShowDao.createOrUpdate(Mockito.any(Show.class))).thenReturn(show);

		service.setShowDao(mockedShowDao);

		GuideDao mockedGuideDao = Mockito.mock(GuideDao.class);
		Mockito.when(mockedGuideDao.getGuideSource()).thenReturn(GuideSource.TVDB);
		// should be ignored becuase season = 0
		GuideEpisode ep1 = new GuideEpisode();
		ep1.setSeasonNum(0);
		ep1.setEpisodeNum(1);
		ep1.setTitle("title1");
		ep1.setAirDate(SDF.parse("2016-01-22"));
		// should be ignored because season = null
		GuideEpisode ep2 = new GuideEpisode();
		ep2.setSeasonNum(null);
		ep2.setEpisodeNum(2);
		ep2.setTitle("title2");
		ep2.setAirDate(SDF.parse("2016-01-22"));
		// should be ignored because airdate == null
		GuideEpisode ep3 = new GuideEpisode();
		ep3.setSeasonNum(1);
		ep3.setEpisodeNum(3);
		ep3.setTitle("title3");
		ep3.setAirDate(null);
		// should be added
		GuideEpisode ep4 = new GuideEpisode();
		ep4.setSeasonNum(1);
		ep4.setEpisodeNum(4);
		ep4.setTitle("title4");
		ep4.setAirDate(SDF.parse("2016-01-22"));
		// should be added
		GuideEpisode ep5 = new GuideEpisode();
		ep5.setSeasonNum(1);
		ep5.setEpisodeNum(5);
		ep5.setTitle("title5");
		ep5.setAirDate(SDF.parse("2016-01-23"));
		// should be ignored because it is next episode
		GuideEpisode ep6 = new GuideEpisode();
		ep6.setSeasonNum(99);
		ep6.setEpisodeNum(100);
		ep6.setTitle("title6");
		ep6.setAirDate(SDF.parse("2016-01-24"));
		// should be ignored because it is prev episode
		GuideEpisode ep7 = new GuideEpisode();
		ep7.setSeasonNum(97);
		ep7.setEpisodeNum(98);
		ep7.setTitle("title7");
		ep7.setAirDate(SDF.parse("2016-01-25"));
		// should be guideId = 2 because that is the correct GuideSource
		Mockito.when(mockedGuideDao.episodes("2")).thenReturn(
				Arrays.asList(ep1, ep2, ep3, ep5, ep4, ep6, ep7));
		service.setGuideDao(mockedGuideDao);

		Show actualShow = service.create(new Show());
		Mockito.verify(mockedShowDao).createOrUpdate(Mockito.any(Show.class));
		// should be guideId = 2 because that is the correct GuideSource
		Mockito.verify(mockedGuideDao).episodes("2");
		@SuppressWarnings("rawtypes")
		ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
		Mockito.verify(mockedShowDao).createOrUpdateEpisodes(ArgumentMatchers.anyList());
		Mockito.verify(mockedShowDao).createOrUpdateEpisodes(argument.capture());
		Assert.assertEquals(2, argument.getValue().size());
		
		// show returned by showDao should match the show returned by the service
		Assert.assertEquals(show, actualShow); 
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void createNextTest() throws Exception {
		ShowDao mockedShowDao = Mockito.mock(ShowDao.class);
		Show show = new Show();
		show.setGuideId("2");
		// next ep to verify we dont re-add it
		Episode nextEp = new Episode();
		nextEp.setSeasonNum(99);
		nextEp.setEpisodeNum(100);
		show.setNextEpisode(nextEp);

		show.setTimezone("EST");
		Mockito.when(mockedShowDao.createOrUpdate(Mockito.any(Show.class))).thenReturn(show);

		service.setShowDao(mockedShowDao);

		GuideDao mockedGuideDao = Mockito.mock(GuideDao.class);
		Mockito.when(mockedGuideDao.getGuideSource()).thenReturn(GuideSource.TVDB);

		// should be ignored because it is next episode
		GuideEpisode ep6 = new GuideEpisode();
		ep6.setSeasonNum(99);
		ep6.setEpisodeNum(100);
		ep6.setTitle("title6");
		ep6.setAirDate(SDF.parse("2016-01-24"));
		// should be added because it is prev episode
		GuideEpisode ep7 = new GuideEpisode();
		ep7.setSeasonNum(97);
		ep7.setEpisodeNum(98);
		ep7.setTitle("title7");
		ep7.setAirDate(SDF.parse("2016-01-25"));
		// should be guideId = 2 because that is the correct GuideSource
		Mockito.when(mockedGuideDao.episodes("2")).thenReturn(
				Arrays.asList(ep6, ep7));
		service.setGuideDao(mockedGuideDao);

		Show actualShow = service.create(new Show());
		Mockito.verify(mockedShowDao).createOrUpdate(Mockito.any(Show.class));
		// should be guideId = 2 because that is the correct GuideSource
		Mockito.verify(mockedGuideDao).episodes("2");
		@SuppressWarnings("rawtypes")
		ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
		Mockito.verify(mockedShowDao).createOrUpdateEpisodes(ArgumentMatchers.anyList());
		Mockito.verify(mockedShowDao).createOrUpdateEpisodes(argument.capture());
		Assert.assertEquals(1, argument.getValue().size());
		
		// show returned by showDao should match the show returned by the service
		Assert.assertEquals(show, actualShow); 
	}

	
	@SuppressWarnings("unchecked")
	@Test
	public void createPreviousTest() throws Exception {
		ShowDao mockedShowDao = Mockito.mock(ShowDao.class);
		Show show = new Show();
		show.setGuideId("2");
		// prev ep to verify we dont re-add it
		Episode prevEp = new Episode();
		prevEp.setSeasonNum(97);
		prevEp.setEpisodeNum(98);
		show.setPreviousEpisode(prevEp);
		show.setTimezone("EST");

		Mockito.when(mockedShowDao.createOrUpdate(Mockito.any(Show.class))).thenReturn(show);

		service.setShowDao(mockedShowDao);

		GuideDao mockedGuideDao = Mockito.mock(GuideDao.class);
		Mockito.when(mockedGuideDao.getGuideSource()).thenReturn(GuideSource.TVDB);

		// should be added because it is next episode
		GuideEpisode ep6 = new GuideEpisode();
		ep6.setSeasonNum(99);
		ep6.setEpisodeNum(100);
		ep6.setTitle("title6");
		ep6.setAirDate(SDF.parse("2016-01-24"));
		// should be ignored because it is prev episode
		GuideEpisode ep7 = new GuideEpisode();
		ep7.setSeasonNum(97);
		ep7.setEpisodeNum(98);
		ep7.setTitle("title7");
		ep7.setAirDate(SDF.parse("2016-01-25"));
		// should be guideId = 2 because that is the correct GuideSource
		Mockito.when(mockedGuideDao.episodes("2")).thenReturn(
				Arrays.asList(ep6, ep7));
		service.setGuideDao(mockedGuideDao);

		Show actualShow = service.create(new Show());
		Mockito.verify(mockedShowDao).createOrUpdate(Mockito.any(Show.class));
		// should be guideId = 2 because that is the correct GuideSource
		Mockito.verify(mockedGuideDao).episodes("2");
		@SuppressWarnings("rawtypes")
		ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
		Mockito.verify(mockedShowDao).createOrUpdateEpisodes(ArgumentMatchers.anyList());
		Mockito.verify(mockedShowDao).createOrUpdateEpisodes(argument.capture());
		Assert.assertEquals(1, argument.getValue().size());
		
		// show returned by showDao should match the show returned by the service
		Assert.assertEquals(show, actualShow); 
	}

	@Test
	public void readTest() throws Exception {
		ShowDao mockedShowDao = Mockito.mock(ShowDao.class);
		Show mockedShow = Mockito.mock(Show.class);
		Mockito.when(mockedShowDao.read(Mockito.anyLong())).thenReturn(
				mockedShow);
		service.setShowDao(mockedShowDao);

		Show actualShow = service.read(Long.MAX_VALUE);
		Assert.assertEquals(mockedShow, actualShow);
	}

	@Test
	public void readAllTest() throws Exception {
		ShowDao mockedShowDao = Mockito.mock(ShowDao.class);
		List<Show> expectedShows = new ArrayList<Show>();
		Show mockedShow = Mockito.mock(Show.class);
		expectedShows.add(mockedShow);
		Mockito.when(mockedShowDao.readAll()).thenReturn(expectedShows);
		service.setShowDao(mockedShowDao);

		List<Show> actualShows = service.readAll();
		Assert.assertEquals(1, actualShows.size());
		Assert.assertEquals(mockedShow, actualShows.get(0));
	}

	@Test
	public void updateTest() throws Exception {
		ShowDao mockedShowDao = Mockito.mock(ShowDao.class);
		Show mockedShow = Mockito.mock(Show.class);
		Mockito.when(mockedShowDao.createOrUpdate(Mockito.any(Show.class)))
				.thenReturn(mockedShow);
		service.setShowDao(mockedShowDao);

		Show actualShow = service.update(new Show());
		Assert.assertEquals(mockedShow, actualShow);
	}

	@Test
	public void deleteTest() throws Exception {
		ShowDao mockedShowDao = Mockito.mock(ShowDao.class);
		service.setShowDao(mockedShowDao);
		service.delete(Long.MAX_VALUE);
		Mockito.verify(mockedShowDao).delete(Mockito.anyLong());
	}

	@Test
	public void prepareShowTest() {
		Show show = new Show();

     	Episode nextEpisode = new Episode();
		show.setNextEpisode(nextEpisode);

		Episode prevEpisode = new Episode();
		show.setPreviousEpisode(prevEpisode);

		Show updatedShow = service.prepareShow(show);

		Assert.assertNotNull(updatedShow);
		Assert.assertEquals(updatedShow, show);

		Assert.assertNotNull(show.getNextEpisode());
		Assert.assertEquals(show, show.getNextEpisode().getShow());

		Assert.assertNotNull(show.getPreviousEpisode());
		Assert.assertEquals(show, show.getPreviousEpisode().getShow());
	}
  
  	@Test
  	public void readAllEpisodesTest() throws Exception{
     	Episode ep = new Episode();
		ShowDao showDao = Mockito.mock(ShowDao.class);
     	Mockito.when(showDao.readAllEpisodes(Mockito.anyLong())).thenReturn(Arrays.asList(ep));
     	service.setShowDao(showDao);
     
     	List<Episode> episodes = service.readAllEpisodes(Long.MAX_VALUE);
     	Assert.assertNotNull(episodes);
     	Assert.assertEquals(1, episodes.size());
     	Assert.assertEquals(ep, episodes.get(0));
   }
}
