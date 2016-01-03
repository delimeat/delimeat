package io.delimeat.core.service;

import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.core.guide.GuideInfo;
import io.delimeat.core.guide.GuideInfoDao;
import io.delimeat.core.guide.GuideSearchDao;
import io.delimeat.core.guide.GuideSearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class GuideService_ImplTest {

	private GuideService_Impl service;

	@Before
	public void setUp() throws Exception {
		service = new GuideService_Impl();
	}

	@Test
	public void infoDaoTest() {
		Assert.assertNull(service.getInfoDao());
		GuideInfoDao mockedDao = Mockito.mock(GuideInfoDao.class);
		service.setInfoDao(mockedDao);
		Assert.assertEquals(mockedDao, service.getInfoDao());
	}

	@Test
	public void searchDaoTest() {
		Assert.assertNull(service.getSearchDao());
		GuideSearchDao mockedDao = Mockito.mock(GuideSearchDao.class);
		service.setSearchDao(mockedDao);
		Assert.assertEquals(mockedDao, service.getSearchDao());
	}

	@Test
	public void readLikeTest() throws IOException, Exception {
		GuideSearchDao mockedDao = Mockito.mock(GuideSearchDao.class);
		List<GuideSearchResult> results = new ArrayList<GuideSearchResult>();
		GuideSearchResult mockedResult = Mockito.mock(GuideSearchResult.class);
		results.add(mockedResult);
		Mockito.when(mockedDao.search(Mockito.anyString())).thenReturn(results);

		service.setSearchDao(mockedDao);

		List<GuideSearchResult> actualResults = service.readLike("ANYTHING");
		Assert.assertNotNull(actualResults);
		Assert.assertEquals(1, actualResults.size());
		Assert.assertEquals(mockedResult, actualResults.get(0));
	}

	@Test
	public void readTest() throws IOException, Exception {
		GuideInfoDao mockedDao = Mockito.mock(GuideInfoDao.class);
		GuideInfo mockedInfo = Mockito.mock(GuideInfo.class);
		Mockito.when(mockedDao.info(Mockito.anyString()))
				.thenReturn(mockedInfo);
		service.setInfoDao(mockedDao);
		
		GuideInfo actualInfo = service.read("guideid");
		Assert.assertNotNull(actualInfo);
		Assert.assertEquals(mockedInfo, actualInfo);
	}
	
	@Test
	public void readEpisodesTest() throws IOException, Exception {
		GuideInfoDao mockedDao = Mockito.mock(GuideInfoDao.class);
		GuideEpisode mockedEpisode = Mockito.mock(GuideEpisode.class);
		Mockito.when(mockedEpisode.getSeasonNum()).thenReturn(1);
		Mockito.when(mockedEpisode.getAirDate()).thenReturn(new Date());

		List<GuideEpisode> expectedEpisodes = Arrays.asList(mockedEpisode);
		Mockito.when(mockedDao.episodes(Mockito.anyString()))
				.thenReturn(expectedEpisodes);
		service.setInfoDao(mockedDao);	
		
		List<GuideEpisode> actualEpisodes = service.readEpisodes("guideId");
		Assert.assertNotNull(actualEpisodes);
		Assert.assertEquals(1, actualEpisodes.size());
		Assert.assertEquals(mockedEpisode,actualEpisodes.get(0));
	}

}
