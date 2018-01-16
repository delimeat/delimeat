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

import io.delimeat.show.entity.Episode;
import io.delimeat.show.entity.EpisodeStatus;

public class EpisodeDao_ImplTest {

	private EpisodeDao_Impl dao;

	@BeforeEach
	public void setUp() {
		dao = new EpisodeDao_Impl();
	}

	@Test
	public void createTest() {
		Episode episode = new Episode();
		EntityManager entityManager = Mockito.mock(EntityManager.class);
		dao.setEntityManager(entityManager);

		dao.create(episode);

		Mockito.verify(entityManager).persist(episode);
		Mockito.verifyNoMoreInteractions(entityManager);
	}

	@Test
	public void readTest() {
		Episode episode = new Episode();
		EntityManager entityManager = Mockito.mock(EntityManager.class);
		Mockito.when(entityManager.getReference(Episode.class, 1L)).thenReturn(episode);
		dao.setEntityManager(entityManager);

		Episode result = dao.read(1L);
		Assertions.assertEquals(episode, result);

		Mockito.verify(entityManager).getReference(Episode.class, 1L);
		Mockito.verifyNoMoreInteractions(entityManager);
	}

	@Test
	public void updateTest() {
		Episode episode = new Episode();

		EntityManager entityManager = Mockito.mock(EntityManager.class);
		Mockito.when(entityManager.merge(episode)).thenReturn(episode);
		dao.setEntityManager(entityManager);

		Episode result = dao.update(episode);
		Assertions.assertEquals(episode, result);

		Mockito.verify(entityManager).merge(episode);
		Mockito.verifyNoMoreInteractions(entityManager);
	}

	@Test
	public void deleteTest() {
		Episode episode = new Episode();
		EntityManager entityManager = Mockito.mock(EntityManager.class);
		Mockito.when(entityManager.getReference(Episode.class, 1L)).thenReturn(episode);
		dao.setEntityManager(entityManager);

		dao.delete(1L);

		Mockito.verify(entityManager).getReference(Episode.class, 1L);
		Mockito.verify(entityManager).remove(episode);
		Mockito.verifyNoMoreInteractions(entityManager);
	}

	@Test
	public void findByStatusTest() {
		Episode episode = new Episode();

		EntityManager entityManager = Mockito.mock(EntityManager.class);
		@SuppressWarnings("unchecked")
		TypedQuery<Episode> query = Mockito.mock(TypedQuery.class);
		Mockito.when(query.getResultList()).thenReturn(Arrays.asList(episode));
		Mockito.when(entityManager.createNamedQuery("Episode.findByStatus", Episode.class)).thenReturn(query);
		Mockito.when(query.setParameter("list", Arrays.asList(EpisodeStatus.PENDING))).thenReturn(query);
		dao.setEntityManager(entityManager);

		List<Episode> result = dao.findByStatus(Arrays.asList(EpisodeStatus.PENDING));

		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(episode, result.get(0));

		Mockito.verify(entityManager).createNamedQuery("Episode.findByStatus", Episode.class);
		Mockito.verifyNoMoreInteractions(entityManager);

	}
}
