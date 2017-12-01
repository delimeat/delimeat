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

import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.delimeat.show.entity.Episode;
import io.delimeat.show.entity.EpisodeStatus;
import io.delimeat.show.exception.ShowConcurrencyException;
import io.delimeat.show.exception.ShowException;
import io.delimeat.show.exception.ShowNotFoundException;

public class EpisodeService_ImplTest {

	EpisodeService_Impl service;

	@BeforeEach
	public void setUp() throws Exception {
		service = new EpisodeService_Impl();
	}

	@Test
	public void episodeDaoTest() {
		Assertions.assertNull(service.getEpisodeDao());
		EpisodeDao dao = Mockito.mock(EpisodeDao.class);
		service.setEpisodeDao(dao);
		Assertions.assertEquals(dao, service.getEpisodeDao());
	}

	@Test
	public void toStringTest() {
		Assertions.assertEquals("EpisodeService_Impl []", service.toString());
	}

	@Test
	public void createTest() throws Exception {
		Episode ep = new Episode();

		EpisodeDao dao = Mockito.mock(EpisodeDao.class);
		service.setEpisodeDao(dao);

		service.create(ep);

		Mockito.verify(dao).create(ep);
		Mockito.verifyNoMoreInteractions(dao);
	}

	@Test
	public void createExceptionTest() throws Exception {
		Episode ep = new Episode();
		
		EpisodeDao dao = Mockito.mock(EpisodeDao.class);
		Mockito.when(dao.create(ep)).thenThrow(PersistenceException.class);
		service.setEpisodeDao(dao);

		ShowException ex = Assertions.assertThrows(ShowException.class, () -> {
			service.create(ep);
		});

		Assertions.assertEquals("javax.persistence.PersistenceException", ex.getMessage());
	}

	@Test
	public void readTest() throws Exception {
		Episode ep = new Episode();

		EpisodeDao dao = Mockito.mock(EpisodeDao.class);
		Mockito.doReturn(ep).when(dao).read(1L);
		service.setEpisodeDao(dao);

		Episode resultEp = service.read(1L);
		Assertions.assertEquals(ep, resultEp);

		Mockito.verify(dao).read(1L);
		Mockito.verifyNoMoreInteractions(dao);
	}

	@Test
	public void readNotFoundExceptionTest() throws Exception {
		EpisodeDao dao = Mockito.mock(EpisodeDao.class);
		Mockito.doThrow(EntityNotFoundException.class).when(dao).read(1L);
		service.setEpisodeDao(dao);

		ShowNotFoundException ex = Assertions.assertThrows(ShowNotFoundException.class, () -> {
			service.read(1L);
		});

		// TODO better message
		Assertions.assertNull(ex.getMessage());
	}

	@Test
	public void readExceptionTest() throws Exception {
		EpisodeDao dao = Mockito.mock(EpisodeDao.class);
		Mockito.doThrow(PersistenceException.class).when(dao).read(1L);
		service.setEpisodeDao(dao);

		ShowException ex = Assertions.assertThrows(ShowException.class, () -> {
			service.read(1L);
		});

		// TODO better message
		Assertions.assertEquals("javax.persistence.PersistenceException", ex.getMessage());

	}

	@Test
	public void updateTest() throws Exception {
		Episode ep = new Episode();

		EpisodeDao dao = Mockito.mock(EpisodeDao.class);
		Mockito.doReturn(ep).when(dao).update(ep);
		service.setEpisodeDao(dao);

		Episode result = service.update(ep);
		Assertions.assertEquals(ep, result);
		
		Mockito.verify(dao).update(ep);
		Mockito.verifyNoMoreInteractions(dao);
	}

	@Test
	public void updateConcurrencyExceptionTest() throws Exception {
		Episode ep = new Episode();

		EpisodeDao dao = Mockito.mock(EpisodeDao.class);
		Mockito.doThrow(OptimisticLockException.class).when(dao).update(ep);
		service.setEpisodeDao(dao);

		ShowConcurrencyException ex = Assertions.assertThrows(ShowConcurrencyException.class, () -> {
			service.update(ep);
		});

		// TODO better message
		Assertions.assertEquals("javax.persistence.OptimisticLockException", ex.getMessage());
	}

	@Test
	public void updateExceptionTest() throws Exception {
		Episode ep = new Episode();

		EpisodeDao dao = Mockito.mock(EpisodeDao.class);
		Mockito.doThrow(PersistenceException.class).when(dao).update(ep);
		service.setEpisodeDao(dao);

		ShowException ex = Assertions.assertThrows(ShowException.class, () -> {
			service.update(ep);
		});

		// TODO better message
		Assertions.assertEquals("javax.persistence.PersistenceException", ex.getMessage());
	}

	@Test
	public void deleteTest() throws Exception {
		EpisodeDao dao = Mockito.mock(EpisodeDao.class);
		service.setEpisodeDao(dao);

		service.delete(1L);

		Mockito.verify(dao).delete(1L);
		Mockito.verifyNoMoreInteractions(dao);
	}

	@Test
	public void deleteExceptionTest() throws Exception {
		EpisodeDao dao = Mockito.mock(EpisodeDao.class);
		Mockito.doThrow(PersistenceException.class).when(dao).delete(1L);
		service.setEpisodeDao(dao);

		ShowException ex = Assertions.assertThrows(ShowException.class, () -> {
			service.delete(1L);
		});

		// TODO better message
		Assertions.assertEquals("javax.persistence.PersistenceException", ex.getMessage());
	}

	@Test
	public void findAllPendingTest() throws Exception {
		Episode ep = new Episode();
		
		EpisodeDao dao = Mockito.mock(EpisodeDao.class);
		Mockito.doReturn(Arrays.asList(ep)).when(dao).findByStatus(Arrays.asList(EpisodeStatus.PENDING));
		service.setEpisodeDao(dao);

		List<Episode> result = service.findAllPending();
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(ep, result.get(0));

		Mockito.verify(dao).findByStatus(Arrays.asList(EpisodeStatus.PENDING));
		Mockito.verifyNoMoreInteractions(dao);
	}

	@Test
	public void findAllPendingExceptionTest() throws Exception {

		EpisodeDao dao = Mockito.mock(EpisodeDao.class);
		Mockito.doThrow(PersistenceException.class).when(dao).findByStatus(Arrays.asList(EpisodeStatus.PENDING));
		service.setEpisodeDao(dao);

		ShowException ex = Assertions.assertThrows(ShowException.class, () -> {
			service.findAllPending();
		});

		// TODO better message
		Assertions.assertEquals("javax.persistence.PersistenceException", ex.getMessage());
	}

}
