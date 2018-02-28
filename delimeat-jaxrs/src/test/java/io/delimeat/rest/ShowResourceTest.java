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
package io.delimeat.rest;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.delimeat.show.ShowService;
import io.delimeat.show.entity.Episode;
import io.delimeat.show.entity.Show;
import io.delimeat.show.exception.ShowException;

public class ShowResourceTest {
	
	private ShowResource resource;
	
	@BeforeEach
	public void setUp() {
		resource = new ShowResource();
	}
	
	@Test
	public void readAllTest() throws ShowException {
		resource.service = Mockito.mock(ShowService.class);
		Show show = new Show();
		Mockito.when(resource.service.readAll()).thenReturn(Arrays.asList(show));

		List<Show> results = resource.readAll();
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals(show, results.get(0));

		Mockito.verify(resource.service).readAll();
		Mockito.verifyNoMoreInteractions(resource.service);
	}
	
	@Test
	public void createTest() throws ShowException {
		resource.service = Mockito.mock(ShowService.class);
		Show show = new Show();
		
		Show result = resource.create(show);
		
		Assertions.assertEquals(show, result);
		
		Mockito.verify(resource.service).create(show);
		Mockito.verifyNoMoreInteractions(resource.service);
	}
	
	@Test
	public void readTest() throws ShowException {
		resource.service = Mockito.mock(ShowService.class);
		Show show = new Show();
		Mockito.when(resource.service.read(1L)).thenReturn(show);

		
		Show result = resource.read(1L);
		
		Assertions.assertEquals(show, result);
		
		Mockito.verify(resource.service).read(1L);
		Mockito.verifyNoMoreInteractions(resource.service);
	}
	
	@Test
	public void updateTest() throws ShowException {
		resource.service = Mockito.mock(ShowService.class);
		Show show = new Show();
		Mockito.when(resource.service.update(show)).thenReturn(show);

		
		Show result = resource.update(1L, show);
		
		Assertions.assertEquals(show, result);
		
		Mockito.verify(resource.service).update(show);
		Mockito.verifyNoMoreInteractions(resource.service);
	}
	
	@Test
	public void deleteTest() throws ShowException {
		resource.service = Mockito.mock(ShowService.class);
	
		resource.delete(1L);
		
		Mockito.verify(resource.service).delete(1L);
		Mockito.verifyNoMoreInteractions(resource.service);
	}
	
	@Test
	public void episodesTest() throws ShowException {
		resource.service = Mockito.mock(ShowService.class);
		Episode episode = new Episode();
		Mockito.when(resource.service.readAllEpisodes(1L)).thenReturn(Arrays.asList(episode));
		
		List<Episode> results = resource.episodes(1L);
		
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals(episode, results.get(0));
		
		Mockito.verify(resource.service).readAllEpisodes(1L);
		Mockito.verifyNoMoreInteractions(resource.service);		
	}
}
