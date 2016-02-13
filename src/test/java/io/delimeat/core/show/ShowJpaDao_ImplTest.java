package io.delimeat.core.show;

import io.delimeat.core.feed.FeedSource;
import io.delimeat.core.guide.GuideSource;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;

import org.apache.derby.tools.ij;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.eclipse.persistence.jpa.PersistenceProvider;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ShowJpaDao_ImplTest {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	private static final String SQL_FILE = "/io/delimeat/core/show/derby_show_test.sql";
	private static final String CLEANUP_FILE = "/io/delimeat/core/show/derby_show_cleanup.sql";
	
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
	public void cleanup() throws UnsupportedEncodingException{
		Connection connection = ((EntityManagerImpl) (entityManager.getDelegate())).getServerSession().getAccessor()
				.getConnection();

		InputStream is = System.class.getResourceAsStream(CLEANUP_FILE);
		ij.runScript(connection, is, "UTF-8", System.out, "UTF-8");
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
      show.setMinSize(Integer.MIN_VALUE);
      show.setMaxSize(Integer.MAX_VALUE);

		Episode prevEpisode = new Episode();
		prevEpisode.setAirDate(SDF.parse("2015-10-15"));
		prevEpisode.setDoubleEp(true);
		prevEpisode.setEpisodeNum(1);
		prevEpisode.setSeasonNum(2);
		prevEpisode.setShow(show);
		prevEpisode.setTitle("TITLE");
		show.setPreviousEpisode(prevEpisode);

		Episode nextEpisode = new Episode();
		nextEpisode.setAirDate(SDF.parse("2000-01-01"));
		nextEpisode.setDoubleEp(false);
		nextEpisode.setEpisodeNum(2);
		nextEpisode.setSeasonNum(1);
		nextEpisode.setShow(show);
		nextEpisode.setTitle("TITLE_TWO");
		show.setNextEpisode(nextEpisode);

		Show newShow = dao.createOrUpdate(show);

		Assert.assertNotEquals(0, newShow.getShowId());
		Assert.assertTrue(newShow.isAiring());
		Assert.assertEquals(1, show.getAirTime());
      Assert.assertEquals(Integer.MIN_VALUE, show.getMinSize());
      Assert.assertEquals(Integer.MAX_VALUE, show.getMaxSize());

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
		Assert.assertEquals("2015-10-15", SDF.format(newShow.getPreviousEpisode().getAirDate()));
		Assert.assertTrue(newShow.getPreviousEpisode().isDoubleEp());
		Assert.assertEquals(1, newShow.getPreviousEpisode().getEpisodeNum());
		Assert.assertEquals(2, newShow.getPreviousEpisode().getSeasonNum());
		Assert.assertEquals("TITLE", newShow.getPreviousEpisode().getTitle());

		Assert.assertNotNull(newShow.getNextEpisode());
		Assert.assertNotEquals(0, newShow.getNextEpisode().getEpisodeId());
		Assert.assertEquals("2000-01-01", SDF.format(newShow.getNextEpisode().getAirDate()));
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
		Assert.assertEquals(100, show.getMinSize());
     	Assert.assertEquals(101, show.getMaxSize());
     
		Assert.assertNotNull(show.getNextEpisode());
		Assert.assertEquals(2, show.getNextEpisode().getEpisodeId());
		Assert.assertEquals("1988-12-25", SDF.format(show.getNextEpisode().getAirDate()));
		Assert.assertEquals(1, show.getNextEpisode().getSeasonNum());
		Assert.assertEquals(2, show.getNextEpisode().getEpisodeNum());
		Assert.assertEquals("NEXT EPISODE", show.getNextEpisode().getTitle());
		Assert.assertFalse(show.getNextEpisode().isDoubleEp());
		Assert.assertEquals(3, show.getNextEpisode().getVersion());
		Assert.assertNotNull(show.getNextEpisode().getShow());
		Assert.assertEquals(1, show.getNextEpisode().getShow().getShowId());

		Assert.assertNotNull(show.getPreviousEpisode());
		Assert.assertEquals(3, show.getPreviousEpisode().getEpisodeId());
		Assert.assertEquals("1988-12-25", SDF.format(show.getPreviousEpisode().getAirDate()));
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
		
	}

	@Test(expected = ShowNotFoundException.class)
	public void notFoundReadShowTest() throws Exception {
		dao.read(3);
	}

	@Test(expected = ShowNotFoundException.class)
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
		Assert.assertEquals(100, show.getMinSize());
		Assert.assertEquals(101, show.getMaxSize());
     

		Assert.assertNotNull(show.getNextEpisode());
		Assert.assertEquals(2, show.getNextEpisode().getEpisodeId());
		Assert.assertEquals("1988-12-25", SDF.format(show.getNextEpisode().getAirDate()));
		Assert.assertEquals(1, show.getNextEpisode().getSeasonNum());
		Assert.assertEquals(2, show.getNextEpisode().getEpisodeNum());
		Assert.assertEquals("NEXT EPISODE", show.getNextEpisode().getTitle());
		Assert.assertFalse(show.getNextEpisode().isDoubleEp());
		Assert.assertEquals(3, show.getNextEpisode().getVersion());
		Assert.assertNotNull(show.getNextEpisode().getShow());
		Assert.assertEquals(1, show.getNextEpisode().getShow().getShowId());

		Assert.assertNotNull(show.getPreviousEpisode());
		Assert.assertEquals(3, show.getPreviousEpisode().getEpisodeId());
		Assert.assertEquals("1988-12-25", SDF.format(show.getPreviousEpisode().getAirDate()));
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

	}

	@Test
	public void updateTest() throws Exception {
		Connection connection = ((EntityManagerImpl) (entityManager.getDelegate())).getServerSession().getAccessor()
				.getConnection();

		InputStream is = System.class.getResourceAsStream(SQL_FILE);
		ij.runScript(connection, is, "UTF-8", System.out, "UTF-8");

		Show show = dao.read(1);
		show.setTitle("UPDATED TITLE");
		show.getGuideSources().remove(0);
		show.setNextEpisode(null);
		show.getPreviousEpisode().setTitle("UPDATED PREV EP");
		Show updatedShow = dao.createOrUpdate(show);

		Assert.assertEquals("UPDATED TITLE", updatedShow.getTitle());
		Assert.assertEquals(0, updatedShow.getGuideSources().size());
		Assert.assertNull(updatedShow.getNextEpisode());
		Assert.assertEquals("UPDATED PREV EP", updatedShow.getPreviousEpisode().getTitle());


		Show readShow = dao.read(1);
		Assert.assertEquals("UPDATED TITLE", readShow.getTitle());
		Assert.assertEquals(0, readShow.getGuideSources().size());
		Assert.assertNull(readShow.getNextEpisode());
		Assert.assertEquals("UPDATED PREV EP", readShow.getPreviousEpisode().getTitle());

	}
  
	@Test
	public void readAndLockShowTest() throws Exception {

		Connection connection = ((EntityManagerImpl) (entityManager.getDelegate())).getServerSession().getAccessor()
				.getConnection();

		InputStream is = System.class.getResourceAsStream(SQL_FILE);
		ij.runScript(connection, is, "UTF-8", System.out, "UTF-8");

		Show show = dao.readAndLock(1);
      Assert.assertEquals(LockModeType.PESSIMISTIC_WRITE, entityManager.getLockMode(show));
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
		Assert.assertEquals(100, show.getMinSize());
     	Assert.assertEquals(101, show.getMaxSize());
     
		Assert.assertNotNull(show.getNextEpisode());
		Assert.assertEquals(2, show.getNextEpisode().getEpisodeId());
		Assert.assertEquals("1988-12-25", SDF.format(show.getNextEpisode().getAirDate()));
		Assert.assertEquals(1, show.getNextEpisode().getSeasonNum());
		Assert.assertEquals(2, show.getNextEpisode().getEpisodeNum());
		Assert.assertEquals("NEXT EPISODE", show.getNextEpisode().getTitle());
		Assert.assertFalse(show.getNextEpisode().isDoubleEp());
		Assert.assertEquals(3, show.getNextEpisode().getVersion());
		Assert.assertNotNull(show.getNextEpisode().getShow());
		Assert.assertEquals(1, show.getNextEpisode().getShow().getShowId());

		Assert.assertNotNull(show.getPreviousEpisode());
		Assert.assertEquals(3, show.getPreviousEpisode().getEpisodeId());
		Assert.assertEquals("1988-12-25", SDF.format(show.getPreviousEpisode().getAirDate()));
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
	}
	
	@Test
	public void readAllEpisodesTest() throws Exception {
		Connection connection = ((EntityManagerImpl) (entityManager.getDelegate())).getServerSession().getAccessor()
				.getConnection();

		InputStream is = System.class.getResourceAsStream(SQL_FILE);
		ij.runScript(connection, is, "UTF-8", System.out, "UTF-8");

		List<Episode> episodes = dao.readAllEpisodes(1);
		
		Assert.assertEquals(3,episodes.size());
	}
  
   @Test
   public void readAllEpisodesNoResultTest() throws Exception{
		List<Episode> episodes = dao.readAllEpisodes(1);
		
		Assert.assertNotNull(episodes);
    	Assert.assertEquals(0,episodes.size());
   }
   
   @Test
   public void readEpisodeAfterTest() throws Exception{
     		Connection connection = ((EntityManagerImpl) (entityManager.getDelegate())).getServerSession().getAccessor()
				.getConnection();

		InputStream is = System.class.getResourceAsStream(SQL_FILE);
		ij.runScript(connection, is, "UTF-8", System.out, "UTF-8");
     
		Episode ep = dao.readEpisodeAfter(1,new Date(0));
		Assert.assertEquals(3, ep.getEpisodeId());
		Assert.assertEquals("1988-12-25", SDF.format(ep.getAirDate()));
		Assert.assertEquals(2, ep.getSeasonNum());
		Assert.assertEquals(3, ep.getEpisodeNum());
		Assert.assertEquals("PREVIOUS EPISODE", ep.getTitle());
		Assert.assertTrue(ep.isDoubleEp());
		Assert.assertEquals(4, ep.getVersion());
		Assert.assertNotNull(ep.getShow());
		Assert.assertEquals(1, ep.getShow().getShowId());
		Assert.assertNotNull(ep.getResults());
		Assert.assertEquals(1, ep.getResults().size());
		Assert.assertEquals(1, ep.getResults().get(0).getEpisodeResultId());
		Assert.assertEquals(FeedSource.KAT, ep.getResults().get(0).getSource());
		Assert.assertEquals("http://www.test.com", ep.getResults().get(0).getUrl());
		Assert.assertEquals("BYTES", ep.getResults().get(0).getResult());
		Assert.assertTrue(ep.getResults().get(0).isValid());
		Assert.assertEquals(99, ep.getResults().get(0).getVersion());
		Assert.assertNotNull(ep.getResults().get(0).getEpisode());
		Assert.assertEquals(3, ep.getResults().get(0).getEpisode().getEpisodeId());
   }
  
	@Test(expected=ShowNotFoundException.class)
   public void readEpisodeAfterNoResultTest() throws Exception{ 
      dao.readEpisodeAfter(1,SDF.parse("1988-12-25"));
   }

   @Test
   public void readEpisodeTest() throws Exception{
     		Connection connection = ((EntityManagerImpl) (entityManager.getDelegate())).getServerSession().getAccessor()
				.getConnection();

		InputStream is = System.class.getResourceAsStream(SQL_FILE);
		ij.runScript(connection, is, "UTF-8", System.out, "UTF-8");
     
		Episode ep = dao.readEpisode(3);
		Assert.assertEquals(3, ep.getEpisodeId());
		Assert.assertEquals("1988-12-25", SDF.format(ep.getAirDate()));
		Assert.assertEquals(2, ep.getSeasonNum());
		Assert.assertEquals(3, ep.getEpisodeNum());
		Assert.assertEquals("PREVIOUS EPISODE", ep.getTitle());
		Assert.assertTrue(ep.isDoubleEp());
		Assert.assertEquals(4, ep.getVersion());
		Assert.assertNotNull(ep.getShow());
		Assert.assertEquals(1, ep.getShow().getShowId());
		Assert.assertNotNull(ep.getResults());
		Assert.assertEquals(1, ep.getResults().size());
		Assert.assertEquals(1, ep.getResults().get(0).getEpisodeResultId());
		Assert.assertEquals(FeedSource.KAT, ep.getResults().get(0).getSource());
		Assert.assertEquals("http://www.test.com", ep.getResults().get(0).getUrl());
		Assert.assertEquals("BYTES", ep.getResults().get(0).getResult());
		Assert.assertTrue(ep.getResults().get(0).isValid());
		Assert.assertEquals(99, ep.getResults().get(0).getVersion());
		Assert.assertNotNull(ep.getResults().get(0).getEpisode());
		Assert.assertEquals(3, ep.getResults().get(0).getEpisode().getEpisodeId());
   }
  
	@Test(expected=ShowNotFoundException.class)
   public void readEpisodeNotFoundTest() throws Exception{ 
      dao.readEpisode(1);	
   }
  
   @Test
  	public void createEpisodeTest() throws Exception{
     		Connection connection = ((EntityManagerImpl) (entityManager.getDelegate())).getServerSession().getAccessor()
				.getConnection();

		InputStream is = System.class.getResourceAsStream(SQL_FILE);
		ij.runScript(connection, is, "UTF-8", System.out, "UTF-8");
      
      Show show = dao.read(1);
		Episode newEpisode = new Episode();
		newEpisode.setAirDate(SDF.parse("2000-01-01"));
		newEpisode.setDoubleEp(true);
		newEpisode.setEpisodeNum(99);
		newEpisode.setSeasonNum(100);
		newEpisode.setShow(show);
		newEpisode.setTitle("TITLE_TWO");
     
      Episode createdEpisode = dao.createOrUpdateEpisode(newEpisode);
		Assert.assertNotEquals(0, createdEpisode.getEpisodeId());
		Assert.assertEquals("2000-01-01", SDF.format(createdEpisode.getAirDate()));
		Assert.assertTrue(createdEpisode.isDoubleEp());
		Assert.assertEquals(99, createdEpisode.getEpisodeNum());
		Assert.assertEquals(100,createdEpisode.getSeasonNum());
		Assert.assertEquals("TITLE_TWO", createdEpisode.getTitle());
     	Assert.assertEquals(show, createdEpisode.getShow());
   }
  
 	@Test
  	public void updateEpisodeTest() throws Exception{
     		Connection connection = ((EntityManagerImpl) (entityManager.getDelegate())).getServerSession().getAccessor()
				.getConnection();

		InputStream is = System.class.getResourceAsStream(SQL_FILE);
		ij.runScript(connection, is, "UTF-8", System.out, "UTF-8");
      
      Show show = dao.read(1);
		show.getPreviousEpisode().setAirDate(SDF.parse("2000-01-01"));
		show.getPreviousEpisode().setDoubleEp(true);
		show.getPreviousEpisode().setEpisodeNum(99);
		show.getPreviousEpisode().setSeasonNum(100);
		show.getPreviousEpisode().setShow(show);
		show.getPreviousEpisode().setTitle("TITLE_TWO");
     
      Episode createdEpisode = dao.createOrUpdateEpisode(show.getPreviousEpisode());
		Assert.assertNotEquals(0, createdEpisode.getEpisodeId());
		Assert.assertEquals("2000-01-01", SDF.format(createdEpisode.getAirDate()));
		Assert.assertTrue(createdEpisode.isDoubleEp());
		Assert.assertEquals(99, createdEpisode.getEpisodeNum());
		Assert.assertEquals(100,createdEpisode.getSeasonNum());
		Assert.assertEquals("TITLE_TWO", createdEpisode.getTitle());
     	Assert.assertEquals(show, createdEpisode.getShow());
   }
  
 	@Test
  	public void deleteEpisodeTest() throws Exception{
     		Connection connection = ((EntityManagerImpl) (entityManager.getDelegate())).getServerSession().getAccessor()
				.getConnection();

		InputStream is = System.class.getResourceAsStream(SQL_FILE);
		ij.runScript(connection, is, "UTF-8", System.out, "UTF-8");

      // get the episode to confirm it exists
      try{
      	dao.readEpisode(4);
      }catch(ShowNotFoundException e){
      	Assert.fail("episode must exist");
      }
      // delete it
      dao.deleteEpisode(4);
      // try getting the episode again to confirm it doesnt exist
      try{
      	dao.readEpisode(4);
      }catch(ShowNotFoundException e){
         // we want an exception
      	Assert.assertTrue(true);
         return;
      }
      // no exception means episode still exists
     	Assert.fail("episode must not exist");
   }
  
 	@Test(expected=ShowNotFoundException.class)
  	public void deleteEpisodeNotFoundTest() throws Exception{     
      dao.deleteEpisode(4);
      Episode ep = dao.readEpisode(4);
      System.out.println(ep);
   }
  
   @Test
  	public void createEpisodesTest() throws Exception{
     		Connection connection = ((EntityManagerImpl) (entityManager.getDelegate())).getServerSession().getAccessor()
				.getConnection();

		InputStream is = System.class.getResourceAsStream(SQL_FILE);
		ij.runScript(connection, is, "UTF-8", System.out, "UTF-8");
      
      Show show = dao.read(1);
     	Episode nextEpisode = show.getNextEpisode();
     	nextEpisode.setTitle("UPDATED");
		Episode newEpisode = new Episode();
		newEpisode.setAirDate(SDF.parse("2000-01-01"));
		newEpisode.setDoubleEp(true);
		newEpisode.setEpisodeNum(99);
		newEpisode.setSeasonNum(100);
		newEpisode.setShow(show);
		newEpisode.setTitle("TITLE_TWO");
     
      List<Episode> episodes = dao.createOrUpdateEpisodes(Arrays.asList(nextEpisode,newEpisode));
     	Assert.assertEquals(2, episodes.size());
     	
		Assert.assertNotEquals(0, episodes.get(1).getEpisodeId());
     	Assert.assertEquals(nextEpisode, episodes.get(0));
		Assert.assertEquals("2000-01-01", SDF.format(episodes.get(1).getAirDate()));
		Assert.assertTrue(episodes.get(1).isDoubleEp());
		Assert.assertEquals(99, episodes.get(1).getEpisodeNum());
		Assert.assertEquals(100,episodes.get(1).getSeasonNum());
		Assert.assertEquals("TITLE_TWO", episodes.get(1).getTitle());
     	Assert.assertEquals(show, episodes.get(1).getShow());
   }
	
}
