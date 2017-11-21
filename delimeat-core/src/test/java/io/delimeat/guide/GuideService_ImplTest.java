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
package io.delimeat.guide;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.delimeat.guide.GuideDataSource;
import io.delimeat.guide.GuideService_Impl;
import io.delimeat.guide.entity.GuideEpisode;
import io.delimeat.guide.entity.GuideInfo;
import io.delimeat.guide.entity.GuideSearchResult;

public class GuideService_ImplTest {

	private GuideService_Impl service;

	@BeforeEach
	public void setUp() throws Exception {
		service = new GuideService_Impl();
	}

	@Test
	public void guideDaoTest() {
		Assertions.assertNull(service.getGuideDataSource());
		GuideDataSource mockedDataSource = Mockito.mock(GuideDataSource.class);
		service.setGuideDataSource(mockedDataSource);
		Assertions.assertEquals(mockedDataSource, service.getGuideDataSource());
	}

	@Test
	public void toStringTest() {
		Assertions.assertEquals("GuideService_Impl []", service.toString());
	}

	@Test
	public void readLikeTest() throws IOException, Exception {
		GuideDataSource mockedDataSource = Mockito.mock(GuideDataSource.class);
		List<GuideSearchResult> results = new ArrayList<GuideSearchResult>();
		GuideSearchResult mockedResult = Mockito.mock(GuideSearchResult.class);
		results.add(mockedResult);
		Mockito.when(mockedDataSource.search("ANYTHING")).thenReturn(results);

		service.setGuideDataSource(mockedDataSource);

		List<GuideSearchResult> actualResults = service.readLike("ANYTHING");
		Assertions.assertNotNull(actualResults);
		Assertions.assertEquals(1, actualResults.size());
		Assertions.assertEquals(mockedResult, actualResults.get(0));
	}

	@Test
	public void readTest() throws IOException, Exception {
		GuideDataSource mockedDataSource = Mockito.mock(GuideDataSource.class);
		GuideInfo mockedInfo = Mockito.mock(GuideInfo.class);
		Mockito.when(mockedDataSource.info("guideid")).thenReturn(mockedInfo);
		service.setGuideDataSource(mockedDataSource);

		GuideInfo actualInfo = service.read("guideid");
		Assertions.assertNotNull(actualInfo);
		Assertions.assertEquals(mockedInfo, actualInfo);
	}

	@Test
	public void readEpisodesTest() throws IOException, Exception {
		GuideDataSource mockedDataSource = Mockito.mock(GuideDataSource.class);
		GuideEpisode guideEp1 = new GuideEpisode();
		guideEp1.setSeasonNum(1);
		guideEp1.setAirDate(LocalDate.ofEpochDay(0));

		GuideEpisode guideEp2 = new GuideEpisode();
		guideEp2.setSeasonNum(0);
		guideEp2.setAirDate(LocalDate.ofEpochDay(0));

		GuideEpisode guideEp3 = new GuideEpisode();
		guideEp3.setSeasonNum(1);
		guideEp3.setAirDate(null);

		GuideEpisode guideEp4 = new GuideEpisode();
		guideEp4.setSeasonNum(null);
		guideEp4.setAirDate(LocalDate.ofEpochDay(0));

		List<GuideEpisode> expectedEpisodes = new ArrayList<GuideEpisode>();
		expectedEpisodes.add(guideEp1);
		expectedEpisodes.add(guideEp2);
		expectedEpisodes.add(guideEp3);
		expectedEpisodes.add(guideEp4);
		Mockito.when(mockedDataSource.episodes("guideId")).thenReturn(expectedEpisodes);
		service.setGuideDataSource(mockedDataSource);

		List<GuideEpisode> actualEpisodes = service.readEpisodes("guideId");
		Assertions.assertNotNull(actualEpisodes);
		Assertions.assertEquals(1, actualEpisodes.size());
		Assertions.assertEquals(guideEp1, actualEpisodes.get(0));
	}

}
