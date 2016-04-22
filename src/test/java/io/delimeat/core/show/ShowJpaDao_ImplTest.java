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

}
