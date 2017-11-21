package io.delimeat.show;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.delimeat.show.entity.Show;

public class ShowDao_ImplTest {

	private ShowDao_Impl dao;

	@BeforeEach
	public void setUp() {
		dao = new ShowDao_Impl();
	}

	@Test
	public void createTest() {
		Show show = new Show();
		EntityManager entityManager = Mockito.mock(EntityManager.class);
		dao.setEntityManager(entityManager);

		dao.create(show);

		Mockito.verify(entityManager).persist(show);
		Mockito.verifyNoMoreInteractions(entityManager);
	}

	@Test
	public void readTest() {
		Show show = new Show();
		EntityManager entityManager = Mockito.mock(EntityManager.class);
		Mockito.when(entityManager.getReference(Show.class, 1L)).thenReturn(show);
		dao.setEntityManager(entityManager);

		Show result = dao.read(1L);
		Assertions.assertEquals(show, result);

		Mockito.verify(entityManager).getReference(Show.class, 1L);
		Mockito.verifyNoMoreInteractions(entityManager);
	}

	@Test
	public void updateTest() {
		Show show = new Show();

		EntityManager entityManager = Mockito.mock(EntityManager.class);
		Mockito.when(entityManager.merge(show)).thenReturn(show);
		dao.setEntityManager(entityManager);

		Show result = dao.update(show);
		Assertions.assertEquals(show, result);

		Mockito.verify(entityManager).merge(show);
		Mockito.verifyNoMoreInteractions(entityManager);
	}

	@Test
	public void deleteTest() {
		Show show = new Show();
		EntityManager entityManager = Mockito.mock(EntityManager.class);
		Mockito.when(entityManager.getReference(Show.class, 1L)).thenReturn(show);
		dao.setEntityManager(entityManager);

		dao.delete(1L);

		Mockito.verify(entityManager).getReference(Show.class, 1L);
		Mockito.verify(entityManager).remove(show);
		Mockito.verifyNoMoreInteractions(entityManager);
	}

	@Test
	public void readAllTest() {
		Show show = new Show();

		EntityManager entityManager = Mockito.mock(EntityManager.class);
		@SuppressWarnings("unchecked")
		TypedQuery<Show> query = Mockito.mock(TypedQuery.class);
		Mockito.when(query.getResultList()).thenReturn(Arrays.asList(show));
		Mockito.when(entityManager.createNamedQuery("Show.findAll", Show.class)).thenReturn(query);
		dao.setEntityManager(entityManager);

		List<Show> result = dao.readAll();

		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(show, result.get(0));

		Mockito.verify(entityManager).createNamedQuery("Show.findAll", Show.class);
		Mockito.verifyNoMoreInteractions(entityManager);

	}
}
