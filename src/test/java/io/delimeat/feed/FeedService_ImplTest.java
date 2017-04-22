package io.delimeat.feed;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.feed.FeedDao;
import io.delimeat.feed.FeedService_Impl;
import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.exception.FeedException;
import io.delimeat.show.domain.Show;

public class FeedService_ImplTest {
	

	private FeedService_Impl service;

	@Before
	public void setUp() {
		service = new FeedService_Impl();
	}
	
	@Test
	public void setFeedDaos(){
		Assert.assertNull(service.getFeedDaos());
		FeedDao dao = Mockito.mock(FeedDao.class);
		service.setFeedDaos(Arrays.asList(dao));
		
		Assert.assertNotNull(service.getFeedDaos());
		Assert.assertEquals(1, service.getFeedDaos().size());
		Assert.assertEquals(dao, service.getFeedDaos().get(0));
	}


	@Test
	public void fetchResultsSuccessTest() throws Exception {
		FeedDao dao = Mockito.mock(FeedDao.class);
		FeedResult feedResult = new FeedResult();
		feedResult.setTitle("TITLE");
		Mockito.when(dao.read("TITLE")).thenReturn(Arrays.asList(feedResult));

		service.setFeedDaos(Arrays.asList(dao));

		Show show = new Show();
		show.setTitle("TITLE");

		List<FeedResult> results = service.read("TITLE");
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(feedResult, results.get(0));
		
		Mockito.verify(dao).read("TITLE");
		Mockito.verify(dao, Mockito.times(2)).getFeedSource();
		Mockito.verifyNoMoreInteractions(dao);
	}

	@Test
	public void fetchResultsOneErrorTest() throws Exception {
		FeedDao dao = Mockito.mock(FeedDao.class);
		FeedResult feedResult = new FeedResult();
		feedResult.setTitle("TITLE");
		Mockito.when(dao.read("TITLE"))
				.thenThrow(FeedException.class)
				.thenReturn(Arrays.asList(feedResult));

		service.setFeedDaos(Arrays.asList(dao,dao));

		Show show = new Show();
		show.setTitle("TITLE");

		List<FeedResult> results = service.read("TITLE");
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(feedResult, results.get(0));
		
		Mockito.verify(dao, Mockito.times(2)).read("TITLE");
		Mockito.verify(dao, Mockito.times(4)).getFeedSource();
		Mockito.verifyNoMoreInteractions(dao);
	}
}
