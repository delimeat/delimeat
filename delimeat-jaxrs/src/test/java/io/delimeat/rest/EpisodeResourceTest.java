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

import io.delimeat.show.EpisodeService;
import io.delimeat.show.entity.Episode;
import io.delimeat.show.exception.ShowException;

public class EpisodeResourceTest {
	
	private EpisodeResource resource;
	
	@BeforeEach
	public void setUp() {
		resource = new EpisodeResource();
	}
	
	@Test
	public void readTest() throws ShowException {
		resource.service = Mockito.mock(EpisodeService.class);
		Episode episode = new Episode();
		Mockito.when(resource.service.findAllPending()).thenReturn(Arrays.asList(episode));
		
		List<Episode> results = resource.read();
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals(episode, results.get(0));
		
		Mockito.verify(resource.service).findAllPending();
		Mockito.verifyNoMoreInteractions(resource.service);
	}
	
	@Test
	public void updateTest() throws ShowException {
		resource.service = Mockito.mock(EpisodeService.class);
		Episode episode = new Episode();
		Mockito.when(resource.service.update(episode)).thenReturn(episode);
		
		Episode result = resource.update(1L, episode);
		Assertions.assertEquals(episode, result);
		
		Mockito.verify(resource.service).update(episode);
		Mockito.verifyNoMoreInteractions(resource.service);
	}
}
