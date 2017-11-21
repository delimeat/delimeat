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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.delimeat.feed.entity.FeedResult;
import io.delimeat.feed.exception.FeedException;

public class FeedService_ImplTest {

	private FeedService_Impl service;

	@BeforeEach
	public void setUp() {
		service = new FeedService_Impl();
	}

	@Test
	public void feedDataSourcesTest() {
		Assertions.assertNull(service.getFeedDataSources());
		FeedDataSource dao = Mockito.mock(FeedDataSource.class);
		service.setFeedDataSources(Arrays.asList(dao));

		Assertions.assertNotNull(service.getFeedDataSources());
		Assertions.assertEquals(1, service.getFeedDataSources().size());
		Assertions.assertEquals(dao, service.getFeedDataSources().get(0));
	}

	@Test
	public void toStringTest() {
		Assertions.assertEquals("FeedService_Impl []", service.toString());
	}

	@Test
	public void readSuccessTest() throws Exception {
		FeedDataSource dao = Mockito.mock(FeedDataSource.class);
		FeedResult feedResult = new FeedResult();
		feedResult.setTitle("TITLE");
		Mockito.when(dao.read("TITLE")).thenReturn(Arrays.asList(feedResult));

		service.setFeedDataSources(Arrays.asList(dao));

		List<FeedResult> results = service.read("TITLE");
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals(feedResult, results.get(0));

		Mockito.verify(dao).read("TITLE");
		Mockito.verify(dao, Mockito.times(1)).getFeedSource();
		Mockito.verifyNoMoreInteractions(dao);
	}

	@Test
	public void readOneErrorTest() throws Exception {
		FeedDataSource dao = Mockito.mock(FeedDataSource.class);
		FeedResult feedResult = new FeedResult();
		feedResult.setTitle("TITLE");
		Mockito.when(dao.read("TITLE")).thenThrow(FeedException.class).thenReturn(Arrays.asList(feedResult));

		service.setFeedDataSources(Arrays.asList(dao, dao));

		List<FeedResult> results = service.read("TITLE");
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals(feedResult, results.get(0));

		Mockito.verify(dao, Mockito.times(2)).read("TITLE");
		Mockito.verify(dao, Mockito.times(2)).getFeedSource();
		Mockito.verifyNoMoreInteractions(dao);
	}

}
