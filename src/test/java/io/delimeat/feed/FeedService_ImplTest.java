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
package io.delimeat.feed;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.exception.FeedException;

public class FeedService_ImplTest {
	

	private FeedService_Impl service;

	@Before
	public void setUp() {
		service = new FeedService_Impl();
	}
	
	@Test
	public void feedDataSourcesTest(){
		Assert.assertNull(service.getFeedDataSources());
		FeedDataSource dao = Mockito.mock(FeedDataSource.class);
		service.setFeedDataSources(Arrays.asList(dao));
		
		Assert.assertNotNull(service.getFeedDataSources());
		Assert.assertEquals(1, service.getFeedDataSources().size());
		Assert.assertEquals(dao, service.getFeedDataSources().get(0));
	}
	
	@Test
	public void toStringTest(){
		Assert.assertEquals("FeedService_Impl []", service.toString());
	}


	@Test
	public void readSuccessTest() throws Exception {
		FeedDataSource dao = Mockito.mock(FeedDataSource.class);
		FeedResult feedResult = new FeedResult();
		feedResult.setTitle("TITLE");
		Mockito.when(dao.read("TITLE")).thenReturn(Arrays.asList(feedResult));

		service.setFeedDataSources(Arrays.asList(dao));

		List<FeedResult> results = service.read("TITLE");
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(feedResult, results.get(0));
		
		Mockito.verify(dao).read("TITLE");
		Mockito.verify(dao, Mockito.times(1)).getFeedSource();
		Mockito.verifyNoMoreInteractions(dao);
	}

	@Test
	public void readOneErrorTest() throws Exception {
		FeedDataSource dao = Mockito.mock(FeedDataSource.class);
		FeedResult feedResult = new FeedResult();
		feedResult.setTitle("TITLE");
		Mockito.when(dao.read("TITLE"))
				.thenThrow(FeedException.class)
				.thenReturn(Arrays.asList(feedResult));

		service.setFeedDataSources(Arrays.asList(dao,dao));

		List<FeedResult> results = service.read("TITLE");
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(feedResult, results.get(0));
		
		Mockito.verify(dao, Mockito.times(2)).read("TITLE");
		Mockito.verify(dao, Mockito.times(2)).getFeedSource();
		Mockito.verifyNoMoreInteractions(dao);
	}
	
}
