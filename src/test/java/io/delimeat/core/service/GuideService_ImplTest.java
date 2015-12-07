package io.delimeat.core.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.delimeat.core.guide.GuideInfo;
import io.delimeat.core.guide.GuideInfoDao;
import io.delimeat.core.guide.GuideSearchDao;
import io.delimeat.core.guide.GuideSearchResult;
import io.delimeat.core.guide.GuideSource;
import io.delimeat.core.service.exception.GuideNotFoundException;

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
		Assert.assertNotNull(service.getInfoDaos());
		Assert.assertEquals(0, service.getInfoDaos().size());
		GuideInfoDao mockedDao = Mockito.mock(GuideInfoDao.class);
		Map<GuideSource,GuideInfoDao> daos = new HashMap<GuideSource,GuideInfoDao>();
		daos.put(GuideSource.TVDB, mockedDao);
		service.setInfoDaos(daos);
		Assert.assertEquals(1, service.getInfoDaos().size());
		Assert.assertEquals(mockedDao, service.getInfoDaos().get(GuideSource.TVDB));
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
		service.getInfoDaos().put(GuideSource.TVDB, mockedDao);
		
		GuideInfo actualInfo = service.read(GuideSource.TVDB, "guideid");
		Assert.assertNotNull(actualInfo);
		Assert.assertEquals(mockedInfo, actualInfo);
	}
	
	@Test(expected=GuideNotFoundException.class)
	public void readGuideNotFoundTest() throws GuideNotFoundException, IOException, Exception{
		service.read(GuideSource.TMDB, "GUIDEID");
	}

}
