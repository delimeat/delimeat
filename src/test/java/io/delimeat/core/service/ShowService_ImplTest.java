package io.delimeat.core.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.core.guide.GuideInfoDao;
import io.delimeat.core.guide.GuideSource;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowDao;
import io.delimeat.core.show.ShowGuideSource;
import io.delimeat.core.show.ShowGuideSourcePK;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
	public void createNoGuideSourceTest() throws Exception {
		ShowDao mockedShowDao = Mockito.mock(ShowDao.class);
		Show mockedShow = Mockito.mock(Show.class);
		Mockito.when(mockedShowDao.createOrUpdate(Mockito.any(Show.class)))
				.thenReturn(mockedShow);
		service.setShowDao(mockedShowDao);

		GuideInfoDao mockedGuideDao = Mockito.mock(GuideInfoDao.class);
		service.setGuideDao(mockedGuideDao);

		Show actualShow = service.create(new Show());
		Mockito.verify(mockedShowDao).createOrUpdate(Mockito.any(Show.class));
		Mockito.verify(mockedShowDao, Mockito.times(0)).createOrUpdateEpisode(
				Mockito.any(Episode.class));
		Mockito.verify(mockedGuideDao, Mockito.times(0)).episodes(
				Mockito.anyString());
		Assert.assertEquals(mockedShow, actualShow);
	}

	@Test
	public void createWrongGuideSourceTest() throws Exception {
		ShowDao mockedShowDao = Mockito.mock(ShowDao.class);
		Show show = new Show();
		// this guide source should be ignored because it is does not match the
		// guideDao
		ShowGuideSource sgs1 = new ShowGuideSource();
		sgs1.setGuideId("1");
		ShowGuideSourcePK sgs1pk = new ShowGuideSourcePK();
		sgs1pk.setGuideSource(GuideSource.IMDB);
		sgs1.setId(sgs1pk);
		show.getGuideSources().add(sgs1);
		Mockito.when(mockedShowDao.createOrUpdate(Mockito.any(Show.class)))
				.thenReturn(show);
		service.setShowDao(mockedShowDao);

		GuideInfoDao mockedGuideDao = Mockito.mock(GuideInfoDao.class);
		Mockito.when(mockedGuideDao.getGuideSource()).thenReturn(
				GuideSource.TVDB);
		service.setGuideDao(mockedGuideDao);

		Show actualShow = service.create(new Show());
		Mockito.verify(mockedShowDao).createOrUpdate(Mockito.any(Show.class));
		Mockito.verify(mockedShowDao, Mockito.times(0)).createOrUpdateEpisode(
				Mockito.any(Episode.class));
		Mockito.verify(mockedGuideDao, Mockito.times(0)).episodes(
				Mockito.anyString());

		Assert.assertEquals(show, actualShow);
	}

	@Test
	public void createGuideSourceTest() throws Exception {
		ShowDao mockedShowDao = Mockito.mock(ShowDao.class);
		Show show = new Show();
		// this guide source should be ignored because it is does not match the
		// guideDao
		ShowGuideSource sgs1 = new ShowGuideSource();
		sgs1.setGuideId("1");
		ShowGuideSourcePK sgs1pk = new ShowGuideSourcePK();
		sgs1pk.setGuideSource(GuideSource.IMDB);
		sgs1.setId(sgs1pk);
		show.getGuideSources().add(sgs1);
		// this guide source should be used because it matches guideDao
		ShowGuideSource sgs2 = new ShowGuideSource();
		sgs2.setGuideId("2");
		ShowGuideSourcePK sgs2pk = new ShowGuideSourcePK();
		sgs2pk.setGuideSource(GuideSource.TVDB);
		sgs2.setId(sgs2pk);
		show.getGuideSources().add(sgs2);
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
		Mockito.when(mockedShowDao.createOrUpdate(Mockito.any(Show.class)))
				.thenReturn(show);
		service.setShowDao(mockedShowDao);

		GuideInfoDao mockedGuideDao = Mockito.mock(GuideInfoDao.class);
		Mockito.when(mockedGuideDao.getGuideSource()).thenReturn(
				GuideSource.TVDB);
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
		Mockito.verify(mockedGuideDao).episodes("2");// should be guideId = 2
														// because that is the
														// correct GuideSource
		Mockito.verify(mockedShowDao, Mockito.times(2)).createOrUpdateEpisode(
				Mockito.any(Episode.class));
		Assert.assertEquals(show, actualShow); // show returned by showDao
												// should match the show
												// returned by the service
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
		ShowGuideSource showGuideSource = new ShowGuideSource();
		show.getGuideSources().add(showGuideSource);

		Episode nextEpisode = new Episode();
		show.setNextEpisode(nextEpisode);

		Episode prevEpisode = new Episode();
		show.setPreviousEpisode(prevEpisode);

		Show updatedShow = service.prepareShow(show);

		Assert.assertNotNull(updatedShow);
		Assert.assertEquals(updatedShow, show);
		Assert.assertNotNull(show.getGuideSources());
		Assert.assertEquals(1, show.getGuideSources().size());
		Assert.assertEquals(showGuideSource, show.getGuideSources().get(0));
		Assert.assertEquals(show, show.getGuideSources().get(0).getShow());

		Assert.assertNotNull(show.getNextEpisode());
		Assert.assertEquals(show, show.getNextEpisode().getShow());

		Assert.assertNotNull(show.getPreviousEpisode());
		Assert.assertEquals(show, show.getPreviousEpisode().getShow());
	}
}
