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
