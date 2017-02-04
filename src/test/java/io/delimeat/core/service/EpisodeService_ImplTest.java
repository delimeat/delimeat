package io.delimeat.core.service;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.core.show.Episode;
import io.delimeat.core.show.EpisodeDao;
import io.delimeat.core.show.EpisodeStatus;
import io.delimeat.util.PaginatedResults;

public class EpisodeService_ImplTest {

	EpisodeService_Impl service;
	
	@Before
	public void setUp() throws Exception {
		service = new EpisodeService_Impl();
	}
	
	@Test
	public void episodeDaoTest() {
		Assert.assertNull(service.getEpisodeDao());
		EpisodeDao mockedDao = Mockito.mock(EpisodeDao.class);
		service.setEpisodeDao(mockedDao);
		Assert.assertEquals(mockedDao, service.getEpisodeDao());
	}
	
	@Test
	public void createTest() throws Exception{
		Episode ep = new Episode();
		
		EpisodeDao mockedDao = Mockito.mock(EpisodeDao.class);
		service.setEpisodeDao(mockedDao);
		
		service.create(ep);
		
		Mockito.verify(mockedDao).create(ep);
		Mockito.verifyNoMoreInteractions(mockedDao);
	}
	
	@Test
	public void readTest() throws Exception{
		Episode ep = new Episode();
		
		EpisodeDao mockedDao = Mockito.mock(EpisodeDao.class);
		Mockito.doReturn(ep)
			.when(mockedDao)
			.read(1L);
		service.setEpisodeDao(mockedDao);
		
		Episode resultEp = service.read(1L);
		Assert.assertEquals("read episode",ep, resultEp);
		
		Mockito.verify(mockedDao).read(1L);
		Mockito.verifyNoMoreInteractions(mockedDao);				
	}
	
	@Test
	public void updateTest() throws Exception{
		Episode ep = new Episode();
		
		EpisodeDao mockedDao = Mockito.mock(EpisodeDao.class);
		Mockito.doReturn(ep)
			.when(mockedDao)
			.update(ep);

		service.setEpisodeDao(mockedDao);
		
		Episode resultEp = service.update(ep);
		Assert.assertEquals("updated episode", ep, resultEp);
		
		Mockito.verify(mockedDao).update(ep);
		Mockito.verifyNoMoreInteractions(mockedDao);
	}
	
	@Test
	public void deleteTest() throws Exception{
		EpisodeDao mockedDao = Mockito.mock(EpisodeDao.class);
		service.setEpisodeDao(mockedDao);
		
		service.delete(1L);
		
		Mockito.verify(mockedDao).delete(1L);
		Mockito.verifyNoMoreInteractions(mockedDao);				
	}
	
	@Test
	public void readAllTest() throws Exception{
		Episode ep = new Episode();
		
		EpisodeDao mockedDao = Mockito.mock(EpisodeDao.class);
		Mockito.doReturn(Arrays.asList(ep))
			.when(mockedDao)
			.readAll(1, 2, Arrays.asList(EpisodeStatus.PENDING));
		Mockito.doReturn(2L)
			.when(mockedDao)
			.countAll(Arrays.asList(EpisodeStatus.PENDING));
		service.setEpisodeDao(mockedDao);	
		
		PaginatedResults<Episode> result = service.readAll(1, 2, false);
		Assert.assertEquals("count",2, result.getCount());
		Assert.assertEquals("results size",1, result.getResults().size());
		Assert.assertEquals("first result", ep, result.getResults().get(0));
		
		Mockito.verify(mockedDao).readAll(1, 2, Arrays.asList(EpisodeStatus.PENDING));
		Mockito.verify(mockedDao).countAll(Arrays.asList(EpisodeStatus.PENDING));
		Mockito.verifyNoMoreInteractions(mockedDao);
	}
	
	@Test
	public void readAllIncludeAllTest() throws Exception{
		Episode ep = new Episode();
		
		EpisodeDao mockedDao = Mockito.mock(EpisodeDao.class);
		Mockito.doReturn(Arrays.asList(ep))
			.when(mockedDao)
			.readAll(1, 2, Arrays.asList(EpisodeStatus.PENDING,EpisodeStatus.FOUND,EpisodeStatus.SKIPPED));
		Mockito.doReturn(2L)
			.when(mockedDao)
			.countAll(Arrays.asList(EpisodeStatus.PENDING,EpisodeStatus.FOUND,EpisodeStatus.SKIPPED));
		service.setEpisodeDao(mockedDao);	
		
		PaginatedResults<Episode> result = service.readAll(1, 2, true);
		Assert.assertEquals("count",2, result.getCount());
		Assert.assertEquals("results size",1, result.getResults().size());
		Assert.assertEquals("first result", ep, result.getResults().get(0));
		
		Mockito.verify(mockedDao)
			.readAll(1, 2, Arrays.asList(EpisodeStatus.PENDING,EpisodeStatus.FOUND,EpisodeStatus.SKIPPED));
		Mockito.verify(mockedDao)
			.countAll(Arrays.asList(EpisodeStatus.PENDING,EpisodeStatus.FOUND,EpisodeStatus.SKIPPED));
		Mockito.verifyNoMoreInteractions(mockedDao);
	}

}
