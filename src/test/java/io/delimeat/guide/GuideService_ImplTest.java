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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.guide.GuideDao;
import io.delimeat.guide.GuideService_Impl;
import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.guide.domain.GuideInfo;
import io.delimeat.guide.domain.GuideSearchResult;

public class GuideService_ImplTest {

	private GuideService_Impl service;

	@Before
	public void setUp() throws Exception {
		service = new GuideService_Impl();
	}

	@Test
	public void guideDaoTest() {
		Assert.assertNull(service.getGuideDao());
		GuideDao mockedDao = Mockito.mock(GuideDao.class);
		service.setGuideDao(mockedDao);
		Assert.assertEquals(mockedDao, service.getGuideDao());
	}

	@Test
	public void readLikeTest() throws IOException, Exception {
		GuideDao mockedDao = Mockito.mock(GuideDao.class);
		List<GuideSearchResult> results = new ArrayList<GuideSearchResult>();
		GuideSearchResult mockedResult = Mockito.mock(GuideSearchResult.class);
		results.add(mockedResult);
		Mockito.when(mockedDao.search("ANYTHING")).thenReturn(results);

		service.setGuideDao(mockedDao);

		List<GuideSearchResult> actualResults = service.readLike("ANYTHING");
		Assert.assertNotNull(actualResults);
		Assert.assertEquals(1, actualResults.size());
		Assert.assertEquals(mockedResult, actualResults.get(0));
	}

	@Test
	public void readTest() throws IOException, Exception {
		GuideDao mockedDao = Mockito.mock(GuideDao.class);
		GuideInfo mockedInfo = Mockito.mock(GuideInfo.class);
		Mockito.when(mockedDao.info("guideid")).thenReturn(mockedInfo);
		service.setGuideDao(mockedDao);
		
		GuideInfo actualInfo = service.read("guideid");
		Assert.assertNotNull(actualInfo);
		Assert.assertEquals(mockedInfo, actualInfo);
	}
	
	@Test
	public void readEpisodesTest() throws IOException, Exception {
		GuideDao mockedDao = Mockito.mock(GuideDao.class);
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
     	Mockito.when(mockedDao.episodes("guideId")).thenReturn(expectedEpisodes);
		service.setGuideDao(mockedDao);	
		
		List<GuideEpisode> actualEpisodes = service.readEpisodes("guideId");
		Assert.assertNotNull(actualEpisodes);
		Assert.assertEquals(1, actualEpisodes.size());
		Assert.assertEquals(guideEp1,actualEpisodes.get(0));
	}

}
