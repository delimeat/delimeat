package io.delimeat.core.show;

import io.delimeat.core.feed.FeedSource;
import io.delimeat.core.guide.GuideSource;

import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;

import org.apache.derby.tools.ij;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.persistence.jpa.PersistenceProvider;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class ShowJpaDao_ImplTest {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	private static final String SQL_FILE = "/io/delimeat/core/show/derby_show_test.sql";
	
	private static EntityManagerFactory factory;
	private static EntityManager entityManager;

	@BeforeClass
	public static void beforeClass() throws Exception {
		Properties properties = new Properties();
		PersistenceProvider provider = new PersistenceProvider();
		factory = provider.createEntityManagerFactory("testPU", properties);
		entityManager = factory.createEntityManager();
	}

	@AfterClass
	public static void afterClass() throws Exception {
		entityManager.close();
		factory.close();
	}

	private ShowJpaDao_Impl dao;

	@Before
	public void before() {
		dao = new ShowJpaDao_Impl();
		dao.setEm(entityManager);
	}

	@Before
	public void openTransaction() {
		entityManager.getTransaction().begin();
	}

	@After
	public void closeTransaction() {
		if (entityManager.getTransaction().isActive()) {
			entityManager.getTransaction().rollback();
		}
	}

	@Test
	public void createShowTest() throws Exception {
		Show show = new Show();
		show.setAiring(true);
		show.setAirTime(1);

		List<ShowGuideSource> guideSources = new ArrayList<ShowGuideSource>();
		ShowGuideSource source1 = new ShowGuideSource();
		source1.setGuideId("ID1");
		ShowGuideSourcePK pk = new ShowGuideSourcePK();
		pk.setGuideSource(GuideSource.IMDB);
		pk.setShowId(show.getShowId());
		source1.setId(pk);
		source1.setShow(show);
		guideSources.add(source1);
		show.setGuideSources(guideSources);

		show.setLastFeedUpdate(SDF.parse("2015-10-17"));
		show.setLastGuideUpdate(SDF.parse("2015-10-16"));
		show.setShowType(ShowType.ANIMATED);
		show.setTimezone("TIMEZONE");
		show.setTitle("TITLE");

		Episode prevEpisode = new Episode();
		prevEpisode.setAirDateTime(SDF.parse("2015-10-15"));
		prevEpisode.setDoubleEp(true);
		prevEpisode.setEpisodeNum(1);
		prevEpisode.setSeasonNum(2);
		prevEpisode.setShow(show);
		prevEpisode.setTitle("TITLE");
		show.setPreviousEpisode(prevEpisode);

		Episode nextEpisode = new Episode();
		nextEpisode.setAirDateTime(SDF.parse("2000-01-01"));
		nextEpisode.setDoubleEp(false);
		nextEpisode.setEpisodeNum(2);
		nextEpisode.setSeasonNum(1);
		nextEpisode.setShow(show);
		nextEpisode.setTitle("TITLE_TWO");
		show.setNextEpisode(nextEpisode);

		Show newShow = dao.create(show);

		Assert.assertNotEquals(0, newShow.getShowId());
		Assert.assertTrue(newShow.isAiring());
		Assert.assertEquals(1, show.getAirTime());

		Assert.assertNotNull(show.getGuideSources());
		Assert.assertEquals(1, newShow.getGuideSources().size());
		Assert.assertEquals("ID1", newShow.getGuideSources().get(0).getGuideId());
		Assert.assertEquals(GuideSource.IMDB, newShow.getGuideSources().get(0).getId().getGuideSource());
		Assert.assertEquals(newShow.getShowId(), newShow.getGuideSources().get(0).getShow().getShowId());

		Assert.assertEquals("2015-10-17", SDF.format(newShow.getLastFeedUpdate()));
		Assert.assertEquals("2015-10-16", SDF.format(newShow.getLastGuideUpdate()));
		Assert.assertEquals(ShowType.ANIMATED, newShow.getShowType());
		Assert.assertEquals("TIMEZONE", newShow.getTimezone());
		Assert.assertEquals("TITLE", newShow.getTitle());

		Assert.assertNotNull(newShow.getPreviousEpisode());
		Assert.assertNotEquals(0, newShow.getPreviousEpisode().getEpisodeId());
		Assert.assertEquals("2015-10-15", SDF.format(newShow.getPreviousEpisode().getAirDateTime()));
		Assert.assertTrue(newShow.getPreviousEpisode().isDoubleEp());
		Assert.assertEquals(1, newShow.getPreviousEpisode().getEpisodeNum());
		Assert.assertEquals(2, newShow.getPreviousEpisode().getSeasonNum());
		Assert.assertEquals("TITLE", newShow.getPreviousEpisode().getTitle());

		Assert.assertNotNull(newShow.getNextEpisode());
		Assert.assertNotEquals(0, newShow.getNextEpisode().getEpisodeId());
		Assert.assertEquals("2000-01-01", SDF.format(newShow.getNextEpisode().getAirDateTime()));
		Assert.assertFalse(newShow.getNextEpisode().isDoubleEp());
		Assert.assertEquals(2, newShow.getNextEpisode().getEpisodeNum());
		Assert.assertEquals(1, newShow.getNextEpisode().getSeasonNum());
		Assert.assertEquals("TITLE_TWO", newShow.getNextEpisode().getTitle());

	}

	@Test
	public void readShowTest() throws Exception {

		Connection connection = ((EntityManagerImpl) (entityManager.getDelegate())).getServerSession().getAccessor()
				.getConnection();

		InputStream is = System.class.getResourceAsStream(SQL_FILE);
		ij.runScript(connection, is, "UTF-8", System.out, "UTF-8");

		Show show = dao.read(1);
		Assert.assertEquals(1, show.getShowId());
		Assert.assertEquals(1200, show.getAirTime());
		Assert.assertTrue(show.isAiring());
		Assert.assertFalse(show.isEnabled());
		Assert.assertTrue(show.isIncludeSpecials());
		Assert.assertEquals("1988-12-25", SDF.format(show.getLastFeedUpdate()));
		Assert.assertNull(show.getLastGuideUpdate());
		Assert.assertEquals(ShowType.ANIMATED, show.getShowType());
		Assert.assertEquals("TIMEZONE", show.getTimezone());
		Assert.assertEquals("TITLE", show.getTitle());
		Assert.assertEquals(99, show.getVersion());

		Assert.assertNotNull(show.getNextEpisode());
		Assert.assertEquals(2, show.getNextEpisode().getEpisodeId());
		Assert.assertEquals("1988-12-25", SDF.format(show.getNextEpisode().getAirDateTime()));
		Assert.assertEquals(1, show.getNextEpisode().getSeasonNum());
		Assert.assertEquals(2, show.getNextEpisode().getEpisodeNum());
		Assert.assertEquals("NEXT EPISODE", show.getNextEpisode().getTitle());
		Assert.assertFalse(show.getNextEpisode().isDoubleEp());
		Assert.assertEquals(3, show.getNextEpisode().getVersion());
		Assert.assertNotNull(show.getNextEpisode().getShow());
		Assert.assertEquals(1, show.getNextEpisode().getShow().getShowId());

		Assert.assertNotNull(show.getPreviousEpisode());
		Assert.assertEquals(3, show.getPreviousEpisode().getEpisodeId());
		Assert.assertEquals("1988-12-25", SDF.format(show.getPreviousEpisode().getAirDateTime()));
		Assert.assertEquals(2, show.getPreviousEpisode().getSeasonNum());
		Assert.assertEquals(3, show.getPreviousEpisode().getEpisodeNum());
		Assert.assertEquals("PREVIOUS EPISODE", show.getPreviousEpisode().getTitle());
		Assert.assertTrue(show.getPreviousEpisode().isDoubleEp());
		Assert.assertEquals(4, show.getPreviousEpisode().getVersion());
		Assert.assertNotNull(show.getPreviousEpisode().getShow());
		Assert.assertEquals(1, show.getPreviousEpisode().getShow().getShowId());
		Assert.assertNotNull(show.getPreviousEpisode().getResults());
		Assert.assertEquals(1, show.getPreviousEpisode().getResults().size());
		Assert.assertEquals(1, show.getPreviousEpisode().getResults().get(0).getEpisodeResultId());
		Assert.assertEquals(FeedSource.KAT, show.getPreviousEpisode().getResults().get(0).getSource());
		Assert.assertEquals("http://www.test.com", show.getPreviousEpisode().getResults().get(0).getUrl());
		Assert.assertEquals("BYTES", show.getPreviousEpisode().getResults().get(0).getResult());
		Assert.assertTrue(show.getPreviousEpisode().getResults().get(0).isValid());
		Assert.assertEquals(99, show.getPreviousEpisode().getResults().get(0).getVersion());
		Assert.assertNotNull(show.getPreviousEpisode().getResults().get(0).getEpisode());
		Assert.assertEquals(3, show.getPreviousEpisode().getResults().get(0).getEpisode().getEpisodeId());

		Assert.assertEquals(1, show.getGuideSources().size());
		Assert.assertEquals("GUIDEID", show.getGuideSources().get(0).getGuideId());
		Assert.assertEquals(3, show.getGuideSources().get(0).getVersion());
		Assert.assertEquals(1, show.getGuideSources().get(0).getId().getShowId());
		Assert.assertEquals(GuideSource.IMDB, show.getGuideSources().get(0).getId().getGuideSource());

		connection.rollback();
	}

	@Test(expected = EntityNotFoundException.class)
	public void notFoundReadShowTest() throws Exception {
		dao.read(3);
	}

	@Test(expected = EntityNotFoundException.class)
	public void notFoundDeleteTest() throws Exception {
		dao.delete(1);
	}

	@Test
	public void deleteTest() throws Throwable {

		Connection connection = ((EntityManagerImpl) (entityManager.getDelegate())).getServerSession().getAccessor()
				.getConnection();

		InputStream is = System.class.getResourceAsStream(SQL_FILE);
		ij.runScript(connection, is, "UTF-8", System.out, "UTF-8");

		dao.delete(1L);

		Assert.assertNull(entityManager.find(Show.class, new Long(1)));
		Assert.assertNull(entityManager.find(Episode.class, 2L));
		Assert.assertNull(entityManager.find(Episode.class, 3L));
		ShowGuideSourcePK pk = new ShowGuideSourcePK();
		pk.setGuideSource(GuideSource.IMDB);
		pk.setShowId(1L);
		Assert.assertNull(entityManager.find(ShowGuideSource.class, pk));

		connection.rollback();
	}

	@Test
	@Ignore
	public void entityNotFoundReadAllShows() throws Exception {
		List<Show> shows = dao.readAll();
		System.out.println(shows);
		System.out.println(shows.size());
		Assert.assertEquals(0, shows.size());

	}

	@Test
	public void readAllShows() throws Exception {
		Connection connection = ((EntityManagerImpl) (entityManager.getDelegate())).getServerSession().getAccessor()
				.getConnection();

		InputStream is = System.class.getResourceAsStream(SQL_FILE);
		ij.runScript(connection, is, "UTF-8", System.out, "UTF-8");

		List<Show> shows = dao.readAll();
		Assert.assertNotNull(shows);
		Assert.assertEquals(1, shows.size());

		Show show = shows.get(0);
		Assert.assertEquals(1, show.getShowId());
		Assert.assertEquals(1200, show.getAirTime());
		Assert.assertTrue(show.isAiring());
		Assert.assertFalse(show.isEnabled());
		Assert.assertTrue(show.isIncludeSpecials());
		Assert.assertEquals("1988-12-25", SDF.format(show.getLastFeedUpdate()));
		Assert.assertNull(show.getLastGuideUpdate());
		Assert.assertEquals(ShowType.ANIMATED, show.getShowType());
		Assert.assertEquals("TIMEZONE", show.getTimezone());
		Assert.assertEquals("TITLE", show.getTitle());
		Assert.assertEquals(99, show.getVersion());

		Assert.assertNotNull(show.getNextEpisode());
		Assert.assertEquals(2, show.getNextEpisode().getEpisodeId());
		Assert.assertEquals("1988-12-25", SDF.format(show.getNextEpisode().getAirDateTime()));
		Assert.assertEquals(1, show.getNextEpisode().getSeasonNum());
		Assert.assertEquals(2, show.getNextEpisode().getEpisodeNum());
		Assert.assertEquals("NEXT EPISODE", show.getNextEpisode().getTitle());
		Assert.assertFalse(show.getNextEpisode().isDoubleEp());
		Assert.assertEquals(3, show.getNextEpisode().getVersion());
		Assert.assertNotNull(show.getNextEpisode().getShow());
		Assert.assertEquals(1, show.getNextEpisode().getShow().getShowId());

		Assert.assertNotNull(show.getPreviousEpisode());
		Assert.assertEquals(3, show.getPreviousEpisode().getEpisodeId());
		Assert.assertEquals("1988-12-25", SDF.format(show.getPreviousEpisode().getAirDateTime()));
		Assert.assertEquals(2, show.getPreviousEpisode().getSeasonNum());
		Assert.assertEquals(3, show.getPreviousEpisode().getEpisodeNum());
		Assert.assertEquals("PREVIOUS EPISODE", show.getPreviousEpisode().getTitle());
		Assert.assertTrue(show.getPreviousEpisode().isDoubleEp());
		Assert.assertEquals(4, show.getPreviousEpisode().getVersion());
		Assert.assertNotNull(show.getPreviousEpisode().getShow());
		Assert.assertEquals(1, show.getPreviousEpisode().getShow().getShowId());
		Assert.assertNotNull(show.getPreviousEpisode().getResults());
		Assert.assertEquals(1, show.getPreviousEpisode().getResults().size());
		Assert.assertEquals(1, show.getPreviousEpisode().getResults().get(0).getEpisodeResultId());
		Assert.assertEquals(FeedSource.KAT, show.getPreviousEpisode().getResults().get(0).getSource());
		Assert.assertEquals("http://www.test.com", show.getPreviousEpisode().getResults().get(0).getUrl());
		Assert.assertEquals("BYTES", show.getPreviousEpisode().getResults().get(0).getResult());
		Assert.assertTrue(show.getPreviousEpisode().getResults().get(0).isValid());
		Assert.assertEquals(99, show.getPreviousEpisode().getResults().get(0).getVersion());
		Assert.assertNotNull(show.getPreviousEpisode().getResults().get(0).getEpisode());
		Assert.assertEquals(3, show.getPreviousEpisode().getResults().get(0).getEpisode().getEpisodeId());

		Assert.assertEquals(1, show.getGuideSources().size());
		Assert.assertEquals("GUIDEID", show.getGuideSources().get(0).getGuideId());
		Assert.assertEquals(3, show.getGuideSources().get(0).getVersion());
		Assert.assertEquals(1, show.getGuideSources().get(0).getId().getShowId());
		Assert.assertEquals(GuideSource.IMDB, show.getGuideSources().get(0).getId().getGuideSource());

		connection.rollback();
	}

	// TODO full test case
	@Test
	public void updateTest() throws Exception {
		Connection connection = ((EntityManagerImpl) (entityManager.getDelegate())).getServerSession().getAccessor()
				.getConnection();

		InputStream is = System.class.getResourceAsStream(SQL_FILE);
		ij.runScript(connection, is, "UTF-8", System.out, "UTF-8");

		Show show = dao.read(1L);
		show.setTitle("UPDATED TITLE");
		show.getGuideSources().remove(0);
		show.setNextEpisode(null);
		show.getPreviousEpisode().setTitle("UPDATED PREV EP");
		Show updatedShow = dao.update(show);

		Assert.assertEquals("UPDATED TITLE", updatedShow.getTitle());
		Assert.assertEquals(0, updatedShow.getGuideSources().size());
		Assert.assertNull(updatedShow.getNextEpisode());
		Assert.assertEquals("UPDATED PREV EP", updatedShow.getPreviousEpisode().getTitle());

		Show readShow = dao.read(1L);
		Assert.assertEquals("UPDATED TITLE", readShow.getTitle());
		Assert.assertEquals(0, readShow.getGuideSources().size());
		Assert.assertNull(readShow.getNextEpisode());
		Assert.assertEquals("UPDATED PREV EP", readShow.getPreviousEpisode().getTitle());
		Assert.assertEquals(updatedShow.getVersion(), readShow.getVersion());

		connection.rollback();
	}

}