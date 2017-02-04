package io.delimeat.core.show;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.util.EntityException;
import io.delimeat.util.EntityConcurrencyException;
import io.delimeat.util.EntityNotFoundException;

public class EpisodeJpaDao_ImplTest {

	private EpisodeJpaDao_Impl dao;
	
	@Before
	public void setUp() throws Exception {
		dao = new EpisodeJpaDao_Impl();
	}
	
	@Test
	public void entityManagerTest() {
		Assert.assertNull(dao.getEm());
		EntityManager entityManager = Mockito.mock(EntityManager.class);
		dao.setEm(entityManager);
		Assert.assertEquals(entityManager, dao.getEm());
	}
	

	@Test
	public void createTest() throws Exception {
		Episode ep = new Episode();

		EntityManager entityManager = Mockito.mock(EntityManager.class);
		dao.setEm(entityManager);

		dao.create(ep);

		Mockito.verify(entityManager).persist(ep);
		Mockito.verifyNoMoreInteractions(entityManager);
	}

	@Test(expected = EntityException.class)
	public void createExceptionTest() throws Exception {
		Episode ep = new Episode();

		EntityManager entityManager = Mockito.mock(EntityManager.class);
		Mockito.doThrow(PersistenceException.class).when(entityManager).persist(ep);
		dao.setEm(entityManager);

		dao.create(ep);
	}
	
	@Test
	public void readTest() throws Exception {
		Episode ep = new Episode();

		EntityManager entityManager = Mockito.mock(EntityManager.class);
		Mockito.when(entityManager.getReference(Episode.class, 1L)).thenReturn(ep);
		dao.setEm(entityManager);

		Episode newEp = dao.read(1L);
		Assert.assertEquals(ep, newEp);

		Mockito.verify(entityManager).getReference(Episode.class, 1L);
		Mockito.verifyNoMoreInteractions(entityManager);

	}

	@Test(expected = EntityNotFoundException.class)
	public void readNotFoundExceptionTest() throws Exception {
		EntityManager entityManager = Mockito.mock(EntityManager.class);
		Mockito.when(entityManager.getReference(Episode.class, 1L)).thenThrow(javax.persistence.EntityNotFoundException.class);
		dao.setEm(entityManager);

		dao.read(1L);

	}

	@Test(expected = EntityException.class)
	public void readExceptionTest() throws Exception {
		EntityManager entityManager = Mockito.mock(EntityManager.class);
		Mockito.when(entityManager.getReference(Episode.class, 1L)).thenThrow(PersistenceException.class);
		dao.setEm(entityManager);

		dao.read(1L);
	}

	@Test
	public void updateTest() throws Exception {
		Episode ep = new Episode();

		EntityManager entityManager = Mockito.mock(EntityManager.class);
		Mockito.when(entityManager.merge(ep)).thenReturn(ep);
		dao.setEm(entityManager);

		Episode newEp = dao.update(ep);

		Assert.assertEquals(ep, newEp);

		Mockito.verify(entityManager).merge(ep);
		Mockito.verifyNoMoreInteractions(entityManager);
	}

	@Test(expected = EntityConcurrencyException.class)
	public void updateOptimisticLockExceptionTest() throws Exception {
		Episode ep = new Episode();

		EntityManager entityManager = Mockito.mock(EntityManager.class);
		Mockito.when(entityManager.merge(ep)).thenThrow(OptimisticLockException.class);
		dao.setEm(entityManager);

		dao.update(ep);
	}

	@Test(expected = EntityException.class)
	public void updateExceptionTest() throws Exception {
		Episode ep = new Episode();

		EntityManager entityManager = Mockito.mock(EntityManager.class);
		Mockito.when(entityManager.merge(ep)).thenThrow(PersistenceException.class);
		dao.setEm(entityManager);

		dao.update(ep);
	}

	@Test
	public void deleteTest() throws Exception {
		Episode ep = new Episode();

		EntityManager entityManager = Mockito.mock(EntityManager.class);
		Mockito.when(entityManager.getReference(Episode.class, 1L)).thenReturn(ep);
		dao.setEm(entityManager);

		dao.delete(1L);

		Mockito.verify(entityManager).getReference(Episode.class, 1L);
		Mockito.verify(entityManager).remove(ep);
		Mockito.verifyNoMoreInteractions(entityManager);
	}

	@Test(expected = EntityException.class)
	public void deleteExceptionTest() throws Exception {
		Episode ep = new Episode();

		EntityManager entityManager = Mockito.mock(EntityManager.class);
		Mockito.when(entityManager.getReference(Episode.class, 1L)).thenReturn(ep);
		Mockito.doThrow(PersistenceException.class).when(entityManager).remove(ep);
		dao.setEm(entityManager);

		dao.delete(1L);
	}
	
	@Test(expected = EntityNotFoundException.class)
	public void deleteNotFoundExceptionTest() throws Exception {
		Episode ep = new Episode();

		EntityManager entityManager = Mockito.mock(EntityManager.class);
		Mockito.when(entityManager.getReference(Episode.class, 1L)).thenReturn(ep);
		Mockito.doThrow(javax.persistence.EntityNotFoundException.class).when(entityManager).getReference(Episode.class, 1L);
		dao.setEm(entityManager);

		dao.delete(1L);
	}
	
	@Test
	public void readAllTest() throws Exception {
		Episode ep = new Episode();

		EntityManager entityManager = Mockito.mock(EntityManager.class);
		@SuppressWarnings("unchecked")
		TypedQuery<Episode> episodesQuery = Mockito.mock(TypedQuery.class);
		Mockito.when(episodesQuery.setParameter("statusList", Arrays.asList(EpisodeStatus.PENDING))).thenReturn(episodesQuery);
		Mockito.when(episodesQuery.setMaxResults(3)).thenReturn(episodesQuery);
		Mockito.when(episodesQuery.setFirstResult(3)).thenReturn(episodesQuery);
		Mockito.when(episodesQuery.getResultList()).thenReturn(Arrays.asList(ep));
		Mockito.when(entityManager.createNamedQuery("Episode.getAll", Episode.class)).thenReturn(episodesQuery);
		dao.setEm(entityManager);

		List<Episode> eps = dao.readAll(2,3,Arrays.asList(EpisodeStatus.PENDING));
		Assert.assertNotNull(eps);
		Assert.assertEquals(1, eps.size());
		Assert.assertEquals(ep, eps.get(0));

		Mockito.verify(entityManager).createNamedQuery("Episode.getAll", Episode.class);
		Mockito.verifyNoMoreInteractions(entityManager);
		Mockito.verify(episodesQuery).setParameter("statusList", Arrays.asList(EpisodeStatus.PENDING));
		Mockito.verify(episodesQuery).setMaxResults(3);
		Mockito.verify(episodesQuery).setFirstResult(3);
		Mockito.verify(episodesQuery).getResultList();
		Mockito.verifyNoMoreInteractions(episodesQuery);
	}

	@Test(expected = EntityException.class)
	public void readAllExceptionTest() throws Exception {
		EntityManager entityManager = Mockito.mock(EntityManager.class);
		@SuppressWarnings("unchecked")
		TypedQuery<Episode> episodesQuery = Mockito.mock(TypedQuery.class);
		Mockito.when(episodesQuery.setParameter("statusList", Arrays.asList(EpisodeStatus.PENDING))).thenReturn(episodesQuery);
		Mockito.when(episodesQuery.setMaxResults(3)).thenReturn(episodesQuery);
		Mockito.when(episodesQuery.setFirstResult(3)).thenReturn(episodesQuery);
		Mockito.when(episodesQuery.getResultList()).thenThrow(EntityException.class);
		Mockito.when(entityManager.createNamedQuery("Episode.getAll", Episode.class)).thenReturn(episodesQuery);
		dao.setEm(entityManager);

		dao.readAll(2,3,Arrays.asList(EpisodeStatus.PENDING));
	}
	
	@Test
	public void countAllTest() throws Exception {
		EntityManager entityManager = Mockito.mock(EntityManager.class);
		@SuppressWarnings("unchecked")
		TypedQuery<Long> episodesQuery = Mockito.mock(TypedQuery.class);
		Mockito.when(episodesQuery.setParameter("statusList", Arrays.asList(EpisodeStatus.PENDING))).thenReturn(episodesQuery);
		Mockito.when(episodesQuery.getSingleResult()).thenReturn(99L);
		Mockito.when(entityManager.createNamedQuery("Episode.getAll.count", Long.class)).thenReturn(episodesQuery);
		dao.setEm(entityManager);

		long count = dao.countAll(Arrays.asList(EpisodeStatus.PENDING));
		Assert.assertEquals(99L,count);

		Mockito.verify(entityManager).createNamedQuery("Episode.getAll.count", Long.class);
		Mockito.verifyNoMoreInteractions(entityManager);
		Mockito.verify(episodesQuery).setParameter("statusList", Arrays.asList(EpisodeStatus.PENDING));
		Mockito.verify(episodesQuery).getSingleResult();
		Mockito.verifyNoMoreInteractions(episodesQuery);
	}

	@Test(expected = EntityException.class)
	public void countAllExceptionTest() throws Exception {
		EntityManager entityManager = Mockito.mock(EntityManager.class);
		@SuppressWarnings("unchecked")
		TypedQuery<Long> episodesQuery = Mockito.mock(TypedQuery.class);
		Mockito.when(episodesQuery.setParameter("statusList", Arrays.asList(EpisodeStatus.PENDING))).thenReturn(episodesQuery);
		Mockito.when(episodesQuery.getSingleResult()).thenThrow(PersistenceException.class);
		Mockito.when(entityManager.createNamedQuery("Episode.getAll.count", Long.class)).thenReturn(episodesQuery);
		dao.setEm(entityManager);

		dao.countAll(Arrays.asList(EpisodeStatus.PENDING));
	}
}
