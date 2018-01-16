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

import io.delimeat.guide.GuideService;
import io.delimeat.guide.entity.GuideEpisode;
import io.delimeat.guide.entity.GuideInfo;
import io.delimeat.guide.entity.GuideSearchResult;
import io.delimeat.guide.exception.GuideException;

public class GuideResourceTest {
	
	private GuideResource resource;
	
	@BeforeEach
	public void setUp() {
		resource = new GuideResource();
	}
	
	@Test
	public void searchTest() throws GuideException {
		resource.service = Mockito.mock(GuideService.class);
		GuideSearchResult searchResult = new GuideSearchResult();
		Mockito.when(resource.service.readLike("TITLE")).thenReturn(Arrays.asList(searchResult));
		
		List<GuideSearchResult> results = resource.search("TITLE");
		
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals(searchResult, results.get(0));
		
		Mockito.verify(resource.service).readLike("TITLE");
		Mockito.verifyNoMoreInteractions(resource.service);
	}
	
	@Test
	public void infoTest() throws GuideException {
		resource.service = Mockito.mock(GuideService.class);
		GuideInfo info = new GuideInfo();
		Mockito.when(resource.service.read("GUIDEID")).thenReturn(info);
		
		GuideInfo result = resource.info("GUIDEID");
		
		Assertions.assertEquals(info, result);
		
		Mockito.verify(resource.service).read("GUIDEID");
		Mockito.verifyNoMoreInteractions(resource.service);
	}
	
	@Test
	public void episodesTest() throws GuideException {
		resource.service = Mockito.mock(GuideService.class);
		GuideEpisode guideEpisode = new GuideEpisode();
		Mockito.when(resource.service.readEpisodes("GUIDEID")).thenReturn(Arrays.asList(guideEpisode));
		
		List<GuideEpisode> results = resource.episodes("GUIDEID");
		
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals(guideEpisode, results.get(0));
		
		Mockito.verify(resource.service).readEpisodes("GUIDEID");
		Mockito.verifyNoMoreInteractions(resource.service);
	}
}
