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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;

import io.delimeat.show.entity.Episode;
import io.delimeat.show.entity.EpisodeStatus;
import io.delimeat.show.entity.Show;
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
	public void episodeRepositoryTest() {
		Assertions.assertNull(service.getEpisodeRepository());
		EpisodeRepository repository = Mockito.mock(EpisodeRepository.class);
		service.setEpisodeRepository(repository);
		Assertions.assertEquals(repository, service.getEpisodeRepository());
	}

	@Test
	public void toStringTest() {
		Assertions.assertEquals("EpisodeService_Impl []", service.toString());
	}

	@Test
	public void createTest() throws Exception {
		Episode ep = new Episode();

		EpisodeRepository repository = Mockito.mock(EpisodeRepository.class);
		service.setEpisodeRepository(repository);

		service.create(ep);

		Mockito.verify(repository).save(ep);
		Mockito.verifyNoMoreInteractions(repository);
	}

	@Test
	public void createExceptionTest() throws Exception {
		Episode ep = new Episode();

		EpisodeRepository repository = Mockito.mock(EpisodeRepository.class);
		Mockito.when(repository.save(ep)).thenThrow(new DataSourceLookupFailureException("EX"));
		service.setEpisodeRepository(repository);

		ShowException ex = Assertions.assertThrows(ShowException.class, () -> {
			service.create(ep);
		});

		Assertions.assertEquals("org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException: EX",
				ex.getMessage());
	}

	@Test
	public void readTest() throws Exception {
		Episode ep = new Episode();

		EpisodeRepository repository = Mockito.mock(EpisodeRepository.class);
		Mockito.doReturn(ep).when(repository).findOne(1L);
		service.setEpisodeRepository(repository);

		Episode resultEp = service.read(1L);
		Assertions.assertEquals(ep, resultEp);

		Mockito.verify(repository).findOne(1L);
		Mockito.verifyNoMoreInteractions(repository);
	}

	@Test
	public void readNotFoundExceptionTest() throws Exception {
		EpisodeRepository repository = Mockito.mock(EpisodeRepository.class);
		Mockito.doReturn(null).when(repository).findOne(1L);

		service.setEpisodeRepository(repository);

		ShowNotFoundException ex = Assertions.assertThrows(ShowNotFoundException.class, () -> {
			service.read(1L);
		});

		// TODO better message
		Assertions.assertNull(ex.getMessage());
	}

	@Test
	public void readExceptionTest() throws Exception {
		EpisodeRepository repository = Mockito.mock(EpisodeRepository.class);
		Mockito.doThrow(new DataSourceLookupFailureException("EX")).when(repository).findOne(1L);

		service.setEpisodeRepository(repository);

		ShowException ex = Assertions.assertThrows(ShowException.class, () -> {
			service.read(1L);
		});

		// TODO better message
		Assertions.assertEquals("org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException: EX",
				ex.getMessage());

	}

	@Test
	public void updateTest() throws Exception {
		Episode ep = new Episode();

		EpisodeRepository repository = Mockito.mock(EpisodeRepository.class);
		service.setEpisodeRepository(repository);

		service.update(ep);

		Mockito.verify(repository).save(ep);
		Mockito.verifyNoMoreInteractions(repository);
	}

	@Test
	public void updateConcurrencyExceptionTest() throws Exception {
		Episode ep = new Episode();

		EpisodeRepository repository = Mockito.mock(EpisodeRepository.class);
		Mockito.when(repository.save(ep)).thenThrow(new ConcurrencyFailureException("EX"));

		service.setEpisodeRepository(repository);

		ShowConcurrencyException ex = Assertions.assertThrows(ShowConcurrencyException.class, () -> {
			service.update(ep);
		});

		// TODO better message
		Assertions.assertEquals("org.springframework.dao.ConcurrencyFailureException: EX", ex.getMessage());
	}

	@Test
	public void updateExceptionTest() throws Exception {
		Episode ep = new Episode();

		EpisodeRepository repository = Mockito.mock(EpisodeRepository.class);
		Mockito.when(repository.save(ep)).thenThrow(new DataSourceLookupFailureException("EX"));

		service.setEpisodeRepository(repository);

		ShowException ex = Assertions.assertThrows(ShowException.class, () -> {
			service.update(ep);
		});

		// TODO better message
		Assertions.assertEquals("org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException: EX",
				ex.getMessage());
	}

	@Test
	public void deleteTest() throws Exception {
		EpisodeRepository repository = Mockito.mock(EpisodeRepository.class);
		service.setEpisodeRepository(repository);

		service.delete(1L);

		Mockito.verify(repository).delete(1L);
		Mockito.verifyNoMoreInteractions(repository);
	}

	@Test
	public void deleteExceptionTest() throws Exception {
		EpisodeRepository repository = Mockito.mock(EpisodeRepository.class);
		Mockito.doThrow(new DataSourceLookupFailureException("EX")).when(repository).delete(1L);

		service.setEpisodeRepository(repository);

		ShowException ex = Assertions.assertThrows(ShowException.class, () -> {
			service.delete(1L);
		});

		// TODO better message
		Assertions.assertEquals("org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException: EX",
				ex.getMessage());
	}

	@Test
	public void findAllPendingTest() throws Exception {
		Episode ep = new Episode();

		EpisodeRepository repository = Mockito.mock(EpisodeRepository.class);
		Mockito.doReturn(Arrays.asList(ep)).when(repository).findByStatusIn(Arrays.asList(EpisodeStatus.PENDING));
		service.setEpisodeRepository(repository);

		List<Episode> result = service.findAllPending();
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(ep, result.get(0));

		Mockito.verify(repository).findByStatusIn(Arrays.asList(EpisodeStatus.PENDING));
		Mockito.verifyNoMoreInteractions(repository);
	}

	@Test
	public void findAllPendingExceptionTest() throws Exception {

		EpisodeRepository repository = Mockito.mock(EpisodeRepository.class);
		Mockito.doThrow(new DataSourceLookupFailureException("EX")).when(repository)
				.findByStatusIn(Arrays.asList(EpisodeStatus.PENDING));
		service.setEpisodeRepository(repository);

		ShowException ex = Assertions.assertThrows(ShowException.class, () -> {
			service.findAllPending();
		});

		// TODO better message
		Assertions.assertEquals("org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException: EX",
				ex.getMessage());
	}

	@Test
	public void findByShowTest() throws Exception {
		Episode ep = new Episode();
		Show show = new Show();
		show.setShowId(Long.MAX_VALUE);
		ep.setShow(show);

		EpisodeRepository repository = Mockito.mock(EpisodeRepository.class);
		Mockito.doReturn(Arrays.asList(ep)).when(repository).findEpisodeByShowShowId(Long.MAX_VALUE);
		service.setEpisodeRepository(repository);

		List<Episode> result = service.findByShow(Long.MAX_VALUE);
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals(ep, result.get(0));

		Mockito.verify(repository).findEpisodeByShowShowId(Long.MAX_VALUE);
		Mockito.verifyNoMoreInteractions(repository);
	}

	@Test
	public void findByShowExceptionTest() throws Exception {

		EpisodeRepository repository = Mockito.mock(EpisodeRepository.class);
		Mockito.doThrow(new DataSourceLookupFailureException("EX")).when(repository)
				.findEpisodeByShowShowId(Long.MAX_VALUE);
		service.setEpisodeRepository(repository);

		ShowException ex = Assertions.assertThrows(ShowException.class, () -> {
			service.findByShow(Long.MAX_VALUE);
		});

		// TODO better message
		Assertions.assertEquals("org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException: EX",
				ex.getMessage());
	}

	@Test
	public void deleteByShowTest() throws Exception {
		EpisodeRepository repository = Mockito.mock(EpisodeRepository.class);
		service.setEpisodeRepository(repository);

		service.deleteByShow(Long.MAX_VALUE);

		Mockito.verify(repository).deleteByShowShowId(Long.MAX_VALUE);
		Mockito.verifyNoMoreInteractions(repository);
	}

	@Test
	public void deleteByShowExceptionTest() throws Exception {
		EpisodeRepository repository = Mockito.mock(EpisodeRepository.class);
		Mockito.doThrow(new DataSourceLookupFailureException("EX")).when(repository).deleteByShowShowId(Long.MAX_VALUE);
		service.setEpisodeRepository(repository);

		ShowException ex = Assertions.assertThrows(ShowException.class, () -> {
			service.deleteByShow(Long.MAX_VALUE);
		});

		// TODO better message
		Assertions.assertEquals("org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException: EX",
				ex.getMessage());
	}

}
