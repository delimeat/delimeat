package io.delimeat.core.service;

import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.core.guide.GuideInfo;
import io.delimeat.core.guide.GuideDao;
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
		Mockito.when(mockedDao.search(Mockito.anyString())).thenReturn(results);

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
		Mockito.when(mockedDao.info(Mockito.anyString()))
				.thenReturn(mockedInfo);
		service.setGuideDao(mockedDao);
		
		GuideInfo actualInfo = service.read("guideid");
		Assert.assertNotNull(actualInfo);
		Assert.assertEquals(mockedInfo, actualInfo);
	}
	
	@Test
	public void readEpisodesTest() throws IOException, Exception {
		GuideDao mockedDao = Mockito.mock(GuideDao.class);
		GuideEpisode mockedEpisode = Mockito.mock(GuideEpisode.class);
		Mockito.when(mockedEpisode.getSeasonNum()).thenReturn(1);
		Mockito.when(mockedEpisode.getAirDate()).thenReturn(new Date());

		List<GuideEpisode> expectedEpisodes = Arrays.asList(mockedEpisode);
		Mockito.when(mockedDao.episodes(Mockito.anyString()))
				.thenReturn(expectedEpisodes);
		service.setGuideDao(mockedDao);	
		
		List<GuideEpisode> actualEpisodes = service.readEpisodes("guideId");
		Assert.assertNotNull(actualEpisodes);
		Assert.assertEquals(1, actualEpisodes.size());
		Assert.assertEquals(mockedEpisode,actualEpisodes.get(0));
	}

}
