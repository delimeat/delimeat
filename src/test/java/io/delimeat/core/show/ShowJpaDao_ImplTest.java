package io.delimeat.core.show;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ShowJpaDao_ImplTest {

	//private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
 	
	private ShowJpaDao_Impl dao;

	@Before
   public void before() throws Exception {	
     dao = new ShowJpaDao_Impl();
    }
     
  
  	public Show buildShow(int airTime, String timezone, String guideId, String title, boolean airing, ShowType showType, Date lastGuideUpdate, Date lastFeedUpdate, boolean enabled, int minSize, int maxSize){
     	Show show = new Show();
     	show.setAirTime(airTime);
     	show.setTimezone(timezone);
     	show.setGuideId(guideId);
     	show.setTitle(title);
     	show.setAiring(airing);
     	show.setShowType(showType);
     	show.setLastGuideUpdate(lastGuideUpdate);
     	show.setLastFeedUpdate(lastFeedUpdate);
     	show.setEnabled(enabled);
     	show.setMinSize(minSize);
     	show.setMaxSize(maxSize);
     	return show;
   }
  
  	public Episode buildEpisode(Date airDate, boolean doubleEp, int seasonNum, int episodeNum, String title, Show show){
     	Episode ep = new Episode();
     	ep.setAirDate(airDate);
     	ep.setDoubleEp(doubleEp);
     	ep.setSeasonNum(seasonNum);
     	ep.setEpisodeNum(episodeNum);
     	ep.setTitle(title);
     	ep.setShow(show);
     	return ep;
   }
  
  	@Test
  	public void entityManagerTest(){
     	Assert.assertNull(dao.getEm());
     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	dao.setEm(entityManager);
     	Assert.assertEquals(entityManager,dao.getEm());
   }

	@Test
	public void createShowTest() throws Exception {  	
		Show show = new Show();

     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.merge(show)).thenReturn(show);
     	dao.setEm(entityManager);

		Show newShow = dao.createOrUpdate(show);

     	Assert.assertEquals(show, newShow);
     
     	Mockito.verify(entityManager).merge(show);
     	Mockito.verifyNoMoreInteractions(entityManager);

	}
  
	@Test(expected=ShowConcurrencyException.class)
	public void createShowOptimisticLockExceptionTest() throws Exception {  	
		Show show = new Show();

     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.merge(show)).thenThrow(OptimisticLockException.class);
     	dao.setEm(entityManager);

		dao.createOrUpdate(show);
	}
  
	@Test(expected=ShowException.class)
	public void createShowExceptionTest() throws Exception {  	
		Show show = new Show();

     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.merge(show)).thenThrow(PersistenceException.class);
     	dao.setEm(entityManager);

		dao.createOrUpdate(show);
	}

	@Test
	public void readShowTest() throws Exception {
		Show show = new Show();
     
     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.getReference(Show.class, 1L)).thenReturn(show);
     	dao.setEm(entityManager);
     
		Show newShow = dao.read(1);
		Assert.assertEquals(show, newShow);		
     	
     	Mockito.verify(entityManager).getReference(Show.class,1L);
     	Mockito.verifyNoMoreInteractions(entityManager);

	}
  
	@Test(expected=ShowNotFoundException.class)
	public void readShowNotFoundExceptionTest() throws Exception {    
     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.getReference(Show.class, 1L)).thenThrow(EntityNotFoundException.class);
     	dao.setEm(entityManager);
     
		dao.read(1);
	}
  
	@Test(expected=ShowException.class)
	public void readShowExceptionTest() throws Exception {    
     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.getReference(Show.class, 1L)).thenThrow(PersistenceException.class);
     	dao.setEm(entityManager);
     
		dao.read(1);
	}
 	@SuppressWarnings("unchecked")
  	@Test
  	public void deleteTest() throws Exception{
		Show show = new Show();
     	Episode ep = new Episode();
     	ep.setEpisodeId(2);
     	show.setNextEpisode(ep);
     	show.setPreviousEpisode(ep);
     
     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.getReference(Show.class, 1L)).thenReturn(show);
		TypedQuery<Episode> episodesQuery = Mockito.mock(TypedQuery.class);
     	Mockito.when(episodesQuery.setParameter("show", show)).thenReturn(episodesQuery);
     	Mockito.when(episodesQuery.getResultList()).thenReturn(Arrays.asList(ep));
     	Mockito.when(entityManager.createNamedQuery("Show.getAllEpisodes",Episode.class)).thenReturn(episodesQuery);
     	Mockito.when(entityManager.getReference(Episode.class, 2L)).thenReturn(ep);

     	dao.setEm(entityManager);
     
		dao.delete(1);
     	
     	Assert.assertNull(show.getNextEpisode());
     	Assert.assertNull(show.getPreviousEpisode());
     
     	Mockito.verify(entityManager, Mockito.times(2)).getReference(Show.class,1L);
     	Mockito.verify(entityManager).createNamedQuery("Show.getAllEpisodes",Episode.class);
     	Mockito.verify(entityManager).remove(show);
     	Mockito.verify(entityManager).getReference(Episode.class,2L);
     	Mockito.verify(entityManager).remove(ep);
     	Mockito.verifyNoMoreInteractions(entityManager);
     	Mockito.verify(episodesQuery).setParameter("show", show);
     	Mockito.verify(episodesQuery).getResultList();
     	Mockito.verifyNoMoreInteractions(episodesQuery);
   }
 	@SuppressWarnings("unchecked")
  	@Test(expected=ShowNotFoundException.class)
  	public void deleteNotFoundTest() throws Exception{
		Show show = new Show();
     	Episode ep = new Episode();
     	show.setNextEpisode(ep);
     	show.setPreviousEpisode(ep);
     
     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.getReference(Show.class, 1L)).thenThrow(EntityNotFoundException.class);
     	TypedQuery<Episode> episodesQuery = Mockito.mock(TypedQuery.class);
     	Mockito.when(episodesQuery.setParameter("show", show)).thenReturn(episodesQuery);
     	Mockito.when(episodesQuery.getResultList()).thenReturn(Arrays.asList(ep));
     	Mockito.when(entityManager.createNamedQuery("Show.getAllEpisodes",Episode.class)).thenReturn(episodesQuery);

     	dao.setEm(entityManager);
     
		dao.delete(1);

   }
 	@SuppressWarnings("unchecked")  
  	@Test(expected=ShowException.class)
  	public void deleteExceptionTest() throws Exception{
		Show show = new Show();
     	Episode ep = new Episode();
     	show.setNextEpisode(ep);
     	show.setPreviousEpisode(ep);
     
     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.getReference(Show.class, 1L)).thenReturn(show);
     	TypedQuery<Episode> episodesQuery = Mockito.mock(TypedQuery.class);
     	Mockito.when(episodesQuery.setParameter("show", show)).thenReturn(episodesQuery);
     	Mockito.when(episodesQuery.getResultList()).thenReturn(Arrays.asList(ep));
     	Mockito.when(entityManager.createNamedQuery("Show.getAllEpisodes",Episode.class)).thenReturn(episodesQuery);
     	Mockito.doThrow(PersistenceException.class).when(entityManager).remove(show);
      dao.setEm(entityManager);
     
		dao.delete(1);
   }
  
 	@SuppressWarnings("unchecked")
 	@Test
  	public void readAllTest() throws Exception{
     	Show show = new Show();
     
     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	TypedQuery<Show> query = Mockito.mock(TypedQuery.class);
     	Mockito.when(query.getResultList()).thenReturn(Arrays.asList(show));
     	Mockito.when(entityManager.createNamedQuery("Show.getAll",Show.class)).thenReturn(query); 
     	
     	dao.setEm(entityManager);
     
     	List<Show> shows = dao.readAll();
     	Assert.assertNotNull(shows);
     	Assert.assertEquals(1, shows.size());
     	Assert.assertEquals(show, shows.get(0));
     
     	Mockito.verify(entityManager).createNamedQuery("Show.getAll",Show.class);
     	Mockito.verifyNoMoreInteractions(entityManager);
     	Mockito.verify(query).getResultList();
     	Mockito.verifyNoMoreInteractions(query);
   }
  
 	@SuppressWarnings("unchecked")
  	@Test(expected=ShowException.class)
  	public void readAllExceptionTest() throws Exception{    
     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	TypedQuery<Show> query = Mockito.mock(TypedQuery.class);
     	Mockito.when(query.getResultList()).thenThrow(PersistenceException.class);
     	Mockito.when(entityManager.createNamedQuery("Show.getAll",Show.class)).thenReturn(query); 
     	
     	dao.setEm(entityManager);
     
     	dao.readAll();
   }
  
  	@Test
  	public void readAndLockTest() throws Exception{
     	Show show = new Show();
     
     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.getReference(Show.class, 1L)).thenReturn(show); 
     	dao.setEm(entityManager);
     
     	Show newShow = dao.readAndLock(1L);
     	Assert.assertEquals(show, newShow);
     
     	Mockito.verify(entityManager).getReference(Show.class, 1L);
     	Mockito.verify(entityManager).lock(show, LockModeType.PESSIMISTIC_WRITE);
     	Mockito.verifyNoMoreInteractions(entityManager);
   }
  
  	@Test(expected=ShowException.class)
  	public void readAndLockExceptionTest() throws Exception{ 
     	Show show = new Show();

     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.getReference(Show.class, 1L)).thenReturn(show); 
     	Mockito.doThrow(PersistenceException.class).when(entityManager).lock(show, LockModeType.PESSIMISTIC_WRITE);
     	dao.setEm(entityManager);
     
     	dao.readAndLock(1L);
   }
  
 	@SuppressWarnings("unchecked")
  	@Test
  	public void readAllEpisodesTest() throws Exception{
		Show show = new Show();
     	Episode ep = new Episode();
     	show.setNextEpisode(ep);
     	show.setPreviousEpisode(ep);
     
     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.getReference(Show.class, 1L)).thenReturn(show);
     	TypedQuery<Episode> episodesQuery = Mockito.mock(TypedQuery.class);
     	Mockito.when(episodesQuery.setParameter("show", show)).thenReturn(episodesQuery);
     	Mockito.when(episodesQuery.getResultList()).thenReturn(Arrays.asList(ep));
     	Mockito.when(entityManager.createNamedQuery("Show.getAllEpisodes",Episode.class)).thenReturn(episodesQuery);
      dao.setEm(entityManager);
     	
     	List<Episode> eps = dao.readAllEpisodes(1L);
     	Assert.assertNotNull(eps);
     	Assert.assertEquals(1, eps.size());
     	Assert.assertEquals(ep, eps.get(0));
     
     	Mockito.verify(entityManager, Mockito.times(1)).getReference(Show.class,1L);
     	Mockito.verify(entityManager).createNamedQuery("Show.getAllEpisodes",Episode.class);
     	Mockito.verifyNoMoreInteractions(entityManager);
     	Mockito.verify(episodesQuery).setParameter("show", show);
     	Mockito.verify(episodesQuery).getResultList();
     	Mockito.verifyNoMoreInteractions(episodesQuery);
   }
  
 	@SuppressWarnings("unchecked")
  	@Test(expected=ShowException.class)
  	public void readAllEpisodesExceptionTest() throws Exception{
		Show show = new Show();
     	Episode ep = new Episode();
     	show.setNextEpisode(ep);
     	show.setPreviousEpisode(ep);
     
     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.getReference(Show.class, 1L)).thenReturn(show);
     	TypedQuery<Episode> episodesQuery = Mockito.mock(TypedQuery.class);
     	Mockito.when(episodesQuery.setParameter("show", show)).thenReturn(episodesQuery);
     	Mockito.when(episodesQuery.getResultList()).thenThrow(PersistenceException.class);
     	Mockito.when(entityManager.createNamedQuery("Show.getAllEpisodes",Episode.class)).thenReturn(episodesQuery);
      dao.setEm(entityManager);
     	
     	dao.readAllEpisodes(1L);
   }

	@Test
	public void readEpisodeTest() throws Exception {
		Episode ep = new Episode();
     
     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.getReference(Episode.class, 1L)).thenReturn(ep);
     	dao.setEm(entityManager);
     
		Episode newEp = dao.readEpisode(1);
		Assert.assertEquals(ep, newEp);		
     	
     	Mockito.verify(entityManager).getReference(Episode.class,1L);
     	Mockito.verifyNoMoreInteractions(entityManager);

	}

	@Test(expected=ShowNotFoundException.class)
	public void readEpisodeNotFoundExceptionTest() throws Exception {    
     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.getReference(Episode.class, 1L)).thenThrow(EntityNotFoundException.class);
     	dao.setEm(entityManager);
     
		dao.readEpisode(1);

	}
  
	@Test(expected=ShowException.class)
	public void readEpisodeExceptionTest() throws Exception {    
     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.getReference(Episode.class, 1L)).thenThrow(PersistenceException.class);
     	dao.setEm(entityManager);
     
		dao.readEpisode(1);
	}
  
	@Test
	public void createOrUpdateEpisodeTest() throws Exception {  	
		Episode ep = new Episode();

     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.merge(ep)).thenReturn(ep);
     	dao.setEm(entityManager);

		Episode newEp = dao.createOrUpdateEpisode(ep);

     	Assert.assertEquals(ep, newEp);
     
     	Mockito.verify(entityManager).merge(ep);
     	Mockito.verifyNoMoreInteractions(entityManager);
	}
  
  	@Test(expected=ShowConcurrencyException.class)
  	public void createOrUpdateEpisodeOptimisticLockExceptionTest() throws Exception{
		Episode ep = new Episode();
     
     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.merge(ep)).thenThrow(OptimisticLockException.class);
     	dao.setEm(entityManager);

		dao.createOrUpdateEpisode(ep);     
   }
  
  	@Test(expected=ShowException.class)
  	public void createOrUpdateEpisodeExceptionTest() throws Exception{
		Episode ep = new Episode();
     
     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.merge(ep)).thenThrow(PersistenceException.class);
     	dao.setEm(entityManager);

		dao.createOrUpdateEpisode(ep);     
   }
  
	@Test
	public void deleteEpisodeTest() throws Exception {  	
		Episode ep = new Episode();
     
     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.getReference(Episode.class, 1L)).thenReturn(ep);
     	dao.setEm(entityManager);
     
		dao.deleteEpisode(1);
     	
     	Mockito.verify(entityManager).getReference(Episode.class,1L);
     	Mockito.verify(entityManager).remove(ep);
     	Mockito.verifyNoMoreInteractions(entityManager);
	}

	@Test(expected=ShowException.class)
	public void deleteEpisodeExceptionTest() throws Exception {  	
		Episode ep = new Episode();
     
     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.getReference(Episode.class, 1L)).thenReturn(ep);
     	Mockito.doThrow(PersistenceException.class).when(entityManager).remove(ep);
     	dao.setEm(entityManager);
     
		dao.deleteEpisode(1);
	}
  
	@Test
	public void createOrUpdateEpisodesTest() throws Exception {  	
		Episode ep = new Episode();

     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.merge(ep)).thenReturn(ep);
     	dao.setEm(entityManager);

		List<Episode> newEps = dao.createOrUpdateEpisodes(Arrays.asList(ep,ep));

     	Assert.assertNotNull(newEps);
     	Assert.assertEquals(2, newEps.size());
     	Assert.assertEquals(ep, newEps.get(0));
     	Assert.assertEquals(ep, newEps.get(1));
     
     	Mockito.verify(entityManager, Mockito.times(2)).merge(ep);
     	Mockito.verifyNoMoreInteractions(entityManager);
	}
  
	@Test(expected=ShowException.class)
	public void createOrUpdateEpisodesExceptionTest() throws Exception {  	
		Episode ep = new Episode();

     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.merge(ep)).thenReturn(ep).thenThrow(PersistenceException.class);
     	dao.setEm(entityManager);

		dao.createOrUpdateEpisodes(Arrays.asList(ep,ep));
	}
  
 	@SuppressWarnings("unchecked")
	@Test
  	public void readNextEpisodeTest() throws Exception{
		Episode ep = new Episode();
     	ep.setSeasonNum(1);
      ep.setEpisodeNum(2);
     	Show show = new Show();
     	ep.setShow(show);
     	Episode ep2 = new Episode();

     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	TypedQuery<Episode> query = Mockito.mock(TypedQuery.class);
     	Mockito.when(query.setParameter("show", show)).thenReturn(query);
     	Mockito.when(query.setParameter("seasonNum", 1)).thenReturn(query);
     	Mockito.when(query.setParameter("episodeNum", 2)).thenReturn(query);
     	Mockito.when(query.setMaxResults(1)).thenReturn(query);
     	Mockito.when(query.getSingleResult()).thenReturn(ep2);
     	Mockito.when(entityManager.createNamedQuery("Show.getNextEpisode",Episode.class)).thenReturn(query);
     	dao.setEm(entityManager);
     
     	Episode newEp = dao.readNextEpisode(ep);
     	Assert.assertEquals(ep2, newEp);
     
     	Mockito.verify(entityManager).createNamedQuery("Show.getNextEpisode",Episode.class);
     	Mockito.verifyNoMoreInteractions(entityManager);
     	Mockito.verify(query).setParameter("show", show);
     	Mockito.verify(query).setParameter("seasonNum", 1);
     	Mockito.verify(query).setParameter("episodeNum", 2);
     	Mockito.verify(query).setMaxResults(1);
     	Mockito.verify(query).getSingleResult();
     	Mockito.verifyNoMoreInteractions(query);
   }

 	@SuppressWarnings("unchecked")  	
 	@Test
  	public void readNextEpisodeNoResultTest() throws Exception{
		Episode ep = new Episode();
     	ep.setSeasonNum(1);
      ep.setEpisodeNum(2);
     	Show show = new Show();
     	ep.setShow(show);

     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	TypedQuery<Episode> query = Mockito.mock(TypedQuery.class);
     	Mockito.when(query.setParameter("show", show)).thenReturn(query);
     	Mockito.when(query.setParameter("seasonNum", 1)).thenReturn(query);
     	Mockito.when(query.setParameter("episodeNum", 2)).thenReturn(query);
     	Mockito.when(query.setMaxResults(1)).thenReturn(query);
     	Mockito.when(query.getSingleResult()).thenThrow(NoResultException.class);
     	Mockito.when(entityManager.createNamedQuery("Show.getNextEpisode",Episode.class)).thenReturn(query);
     	dao.setEm(entityManager);
     
     	Assert.assertNull(dao.readNextEpisode(ep));
     
     	Mockito.verify(entityManager).createNamedQuery("Show.getNextEpisode",Episode.class);
     	Mockito.verifyNoMoreInteractions(entityManager);
     	Mockito.verify(query).setParameter("show", show);
     	Mockito.verify(query).setParameter("seasonNum", 1);
     	Mockito.verify(query).setParameter("episodeNum", 2);
     	Mockito.verify(query).setMaxResults(1);
     	Mockito.verify(query).getSingleResult();
     	Mockito.verifyNoMoreInteractions(query);
   }
  
 	@SuppressWarnings("unchecked")
 	@Test(expected=ShowException.class)
  	public void readNextEpisodeExceptionTest() throws Exception{
		Episode ep = new Episode();
     	ep.setSeasonNum(1);
      ep.setEpisodeNum(2);
     	Show show = new Show();
     	ep.setShow(show);

     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	TypedQuery<Episode> query = Mockito.mock(TypedQuery.class);
     	Mockito.when(query.setParameter("show", show)).thenReturn(query);
     	Mockito.when(query.setParameter("seasonNum", 1)).thenReturn(query);
     	Mockito.when(query.setParameter("episodeNum", 2)).thenReturn(query);
     	Mockito.when(query.setMaxResults(1)).thenReturn(query);
     	Mockito.when(query.getSingleResult()).thenThrow(PersistenceException.class);
     	Mockito.when(entityManager.createNamedQuery("Show.getNextEpisode",Episode.class)).thenReturn(query);
     	dao.setEm(entityManager);
     
     	dao.readNextEpisode(ep);
   }
	/*


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

		Assert.assertEquals("GUIDEID", show.getGuideId());
	}

	@Test
	public void updateTest() throws Exception {
		Connection connection = ((EntityManagerImpl) (entityManager.getDelegate())).getServerSession().getAccessor()
				.getConnection();

		InputStream is = System.class.getResourceAsStream(SQL_FILE);
		ij.runScript(connection, is, "UTF-8", System.out, "UTF-8");

		Show show = dao.read(1);
		show.setTitle("UPDATED TITLE");
		show.setGuideId(null);
		show.setNextEpisode(null);
		show.getPreviousEpisode().setTitle("UPDATED PREV EP");
		Show updatedShow = dao.createOrUpdate(show);

		Assert.assertEquals("UPDATED TITLE", updatedShow.getTitle());
		Assert.assertEquals(null, updatedShow.getGuideId());
		Assert.assertNull(updatedShow.getNextEpisode());
		Assert.assertEquals("UPDATED PREV EP", updatedShow.getPreviousEpisode().getTitle());


		Show readShow = dao.read(1);
		Assert.assertEquals("UPDATED TITLE", readShow.getTitle());
		Assert.assertEquals(null, readShow.getGuideId());
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

		Assert.assertEquals("GUIDEID", show.getGuideId());
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
  
	@Test
   public void readNextEpisodeTest() throws Exception{
     		Connection connection = ((EntityManagerImpl) (entityManager.getDelegate())).getServerSession().getAccessor()
				.getConnection();

		InputStream is = System.class.getResourceAsStream(SQL_FILE);
		ij.runScript(connection, is, "UTF-8", System.out, "UTF-8");
     
     	Show show = dao.read(1);
     
		Episode ep = dao.readNextEpisode(show.getNextEpisode());
		Assert.assertEquals(3, ep.getEpisodeId());
		Assert.assertEquals("1988-12-25", SDF.format(ep.getAirDate()));
		Assert.assertEquals(2, ep.getSeasonNum());
		Assert.assertEquals(3, ep.getEpisodeNum());
		Assert.assertEquals("PREVIOUS EPISODE", ep.getTitle());
		Assert.assertTrue(ep.isDoubleEp());
		Assert.assertEquals(4, ep.getVersion());
		Assert.assertNotNull(ep.getShow());
		Assert.assertEquals(1, ep.getShow().getShowId());
   }
	*/
}
