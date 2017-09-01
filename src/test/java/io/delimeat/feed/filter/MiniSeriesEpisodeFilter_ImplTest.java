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
package io.delimeat.feed.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.Show;
import io.delimeat.show.domain.ShowType;

public class MiniSeriesEpisodeFilter_ImplTest {

	private MiniSeriesEpisodeFilter_Impl filter;
	
	@Before
	public void setUp(){
		filter = new MiniSeriesEpisodeFilter_Impl();
	}
	
	@Test
	public void notMiniSeriesFeedResultTest() throws Exception{
		Episode episode = new Episode();
		episode.setEpisodeNum(1);
		Show show = new Show();
		show.setShowType(ShowType.UNKNOWN);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("SHOW_TITLE_S01E02_2012.12.12_01Of99");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, episode, null);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(result, results.get(0));
	}
	
	@Test
	public void nullTitleFeedResultTest() throws Exception{	
		Episode episode = new Episode();
		episode.setEpisodeNum(1);
		Show show = new Show();
		show.setShowType(ShowType.MINI_SERIES);
		episode.setShow(show);

		FeedResult result = new FeedResult();
		result.setTitle(null);
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));
		
		filter.filter(results, episode, null);
		Assert.assertEquals(0, results.size());
	
	}
	
	@Test
	public void emptyTitleFeedResultTest() throws Exception{	
		Episode episode = new Episode();
		episode.setEpisodeNum(1);
		Show show = new Show();
		show.setShowType(ShowType.MINI_SERIES);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));
		
		filter.filter(results, episode, null);
		Assert.assertEquals(0, results.size());
	
	}
	
	@Test
	public void zeorEpisodeNumTest() throws Exception{
		Episode episode = new Episode();
		episode.setEpisodeNum(0);
		Show show = new Show();
		show.setShowType(ShowType.MINI_SERIES);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext12.02");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));
		
		filter.filter(results, episode, null);
		Assert.assertEquals(0, results.size());
	
	}
	
	@Test
	public void rejectNotMiniSeriesFeedResultTest() throws Exception{
		Episode episode = new Episode();
		episode.setEpisodeNum(1);
		Show show = new Show();
		show.setShowType(ShowType.MINI_SERIES);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("SHOW_TITLE_S01E02_2012.12.12");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));
		
		filter.filter(results, episode, null);
		Assert.assertEquals(0, results.size());

	}
	@Test
	public void rejectIncorrectEpisodeFeedResultTest() throws Exception{
		Episode episode = new Episode();
		episode.setEpisodeNum(1);
		Show show = new Show();
		show.setShowType(ShowType.MINI_SERIES);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("SHOW_TITLE_S01E02_2012.12.12_03Of99");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));
		
		filter.filter(results, episode, null);
		Assert.assertEquals(0, results.size());

	}
	@Test
	public void validFeedResultTest() throws Exception{
		Episode episode = new Episode();
		episode.setEpisodeNum(1);
		Show show = new Show();
		show.setShowType(ShowType.MINI_SERIES);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("SHOW_TITLE_S01E02_2012.12.12_01Of99");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));
		
		filter.filter(results, episode, null);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(result, results.get(0));
	}
	
	@Test
	public void validRejectRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setEpisodeNum(1);
		Show show = new Show();
		show.setShowType(ShowType.MINI_SERIES);
		episode.setShow(show);
		
		FeedResult result1 = new FeedResult();
		result1.setTitle("SHOW_TITLE_S01E02_2012.12.12_01Of99");

		FeedResult result2 = new FeedResult();
		result2.setTitle("SHOW_TITLE_S01E02_2012.12.12_03Of99");

		FeedResult result3 = new FeedResult();
		result3.setTitle("SHOW_TITLE_S01E02_2012.12.12");

		FeedResult result4 = new FeedResult();
		result3.setTitle(null);
		
		FeedResult result5 = new FeedResult();
		result5.setTitle("");
		

		List<FeedResult> results = new ArrayList<>(Arrays.asList(result1,result2,result3,result4,result5));
		
		filter.filter(results, episode, null);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(result1, results.get(0));
	}
}
