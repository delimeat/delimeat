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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.EpisodeStatus;
import io.delimeat.show.domain.Show;

public class EpisodeService_ImplTest {

	EpisodeService_Impl service;
	
	@Before
	public void setUp() throws Exception {
		service = new EpisodeService_Impl();
	}
	
	@Test
	public void episodeRepositoryTest() {
		Assert.assertNull(service.getEpisodeRepository());
		EpisodeRepository repository = Mockito.mock(EpisodeRepository.class);
		service.setEpisodeRepository(repository);
		Assert.assertEquals(repository, service.getEpisodeRepository());
	}
	
	@Test
	public void saveTest() throws Exception{
		Episode ep = new Episode();
		
		EpisodeRepository repository = Mockito.mock(EpisodeRepository.class);
		service.setEpisodeRepository(repository);
		
		service.update(ep);
		
		Mockito.verify(repository).save(ep);
		Mockito.verifyNoMoreInteractions(repository);
	}
	
	@Test
	public void readTest() throws Exception{
		Episode ep = new Episode();
		
		EpisodeRepository repository = Mockito.mock(EpisodeRepository.class);
		Mockito.doReturn(ep)
			.when(repository)
			.findOne(1L);
		service.setEpisodeRepository(repository);
		
		Episode resultEp = service.read(1L);
		Assert.assertEquals("read episode",ep, resultEp);
		
		Mockito.verify(repository).findOne(1L);
		Mockito.verifyNoMoreInteractions(repository);				
	}
	
	@Test
	public void deleteTest() throws Exception{
		EpisodeRepository repository = Mockito.mock(EpisodeRepository.class);
		service.setEpisodeRepository(repository);
		
		service.delete(1L);
		
		Mockito.verify(repository).delete(1L);
		Mockito.verifyNoMoreInteractions(repository);				
	}
	
	@Test
	public void findAllPendingTest() throws Exception{
		Episode ep = new Episode();
		
		EpisodeRepository repository = Mockito.mock(EpisodeRepository.class);
		Mockito.doReturn(Arrays.asList(ep))
			.when(repository)
			.findByStatusIn(Arrays.asList(EpisodeStatus.PENDING));
		service.setEpisodeRepository(repository);	
		
		List<Episode> result = service.findAllPending();
		Assert.assertEquals("results size",1, result.size());
		Assert.assertEquals("first result", ep, result.get(0));
		
		Mockito.verify(repository).findByStatusIn(Arrays.asList(EpisodeStatus.PENDING));
		Mockito.verifyNoMoreInteractions(repository);
	}
	
	@Test
	public void findByShowTest() throws Exception{
		Episode ep = new Episode();
		Show show = new Show();
		show.setShowId(Long.MAX_VALUE);
		ep.setShow(show);
		
		EpisodeRepository repository = Mockito.mock(EpisodeRepository.class);
		Mockito.doReturn(Arrays.asList(ep))
			.when(repository)
			.findByShowShowId(Long.MAX_VALUE);
		service.setEpisodeRepository(repository);	
		
		List<Episode> result = service.findByShow(Long.MAX_VALUE);
		Assert.assertEquals("results size",1, result.size());
		Assert.assertEquals("first result", ep, result.get(0));
		
		Mockito.verify(repository).findByShowShowId(Long.MAX_VALUE);
		Mockito.verifyNoMoreInteractions(repository);		
	}

}
