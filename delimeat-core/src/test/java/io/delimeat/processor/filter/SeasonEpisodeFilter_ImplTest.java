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
package io.delimeat.processor.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.delimeat.config.entity.Config;
import io.delimeat.feed.entity.FeedResult;
import io.delimeat.show.entity.Episode;
import io.delimeat.show.entity.Show;
import io.delimeat.show.entity.ShowType;

public class SeasonEpisodeFilter_ImplTest {

	private SeasonEpisodeFilter_Impl filter;
	
	@BeforeEach
	public void setUp(){
		filter = new SeasonEpisodeFilter_Impl();
	}
	
	
	@Test
	public void notSeasonFeedResultTest() throws Exception{
		Episode episode = new Episode();
		episode.setSeasonNum(1);
		episode.setEpisodeNum(19);
		Show show = new Show();
		show.setShowType(ShowType.UNKNOWN);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("texttextS01E19TEXTTEXT");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(1,results.size());
		Assertions.assertEquals(result, results.get(0));
		
	}
	
	@Test
	public void emptyTitleFeedResultTest() throws Exception{	
		Episode episode = new Episode();
		episode.setSeasonNum(1);
		episode.setEpisodeNum(1);
		Show show = new Show();
		show.setShowType(ShowType.SEASON);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(0,results.size());	
	}
	
	@Test
	public void zeroSeasonTest() throws Exception{	
		Episode episode = new Episode();
		episode.setSeasonNum(0);
		episode.setEpisodeNum(1);
		Show show = new Show();
		show.setShowType(ShowType.SEASON);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("TITLE");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(0,results.size());	

	}
	
	@Test
	public void zeroEpisodeTest() throws Exception{	
		Episode episode = new Episode();
		episode.setSeasonNum(1);
		episode.setEpisodeNum(0);
		Show show = new Show();
		show.setShowType(ShowType.SEASON);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("TITLE");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(0, results.size());	
	
	}
	
	@Test
	public void validFeedResultTest() throws Exception{
		Episode episode = new Episode();
		episode.setSeasonNum(1);
		episode.setEpisodeNum(19);
		Show show = new Show();
		show.setShowType(ShowType.SEASON);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("texttextS01E19TEXTTEXT");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(1,results.size());
		Assertions.assertEquals(result, results.get(0));

	}
	
	@Test
	public void rejectNotASeriesEpisode() throws Exception{
		Episode episode = new Episode();
		episode.setSeasonNum(1);
		episode.setEpisodeNum(19);
		Show show = new Show();
		show.setShowType(ShowType.SEASON);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("texttext2012.01.05_03of99TEXTTEXT");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(0,results.size());	

	}
	
	@Test
	public void rejectIncorrectSeasonEpisode() throws Exception{
		Episode episode = new Episode();
		episode.setSeasonNum(1);
		episode.setEpisodeNum(19);
		Show show = new Show();
		show.setShowType(ShowType.SEASON);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("texttextS02E19TEXTTEXT");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(0,results.size());	

	}
	
	@Test
	public void rejectIncorrectEpisodeEpisode() throws Exception{
		Episode episode = new Episode();
		episode.setSeasonNum(1);
		episode.setEpisodeNum(19);
		Show show = new Show();
		show.setShowType(ShowType.SEASON);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("texttextS01E18TEXTTEXT");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(0, results.size());	

	}
	
	@Test
	public void validRejectRejectRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setSeasonNum(1);
		episode.setEpisodeNum(19);
		Show show = new Show();
		show.setShowType(ShowType.SEASON);
		episode.setShow(show);
		
		FeedResult result1 = new FeedResult();
		result1.setTitle("texttextS01E19TEXTTEXT");

		FeedResult result2 = new FeedResult();
		result2.setTitle("texttext2012.01.05_03of99TEXTTEXT");
 
		FeedResult result3 = new FeedResult();
		result3.setTitle("texttextS02E19TEXTTEXT");

		FeedResult result4 = new FeedResult();
		result4.setTitle("texttextS01E18TEXTTEXT");

		FeedResult result5 = new FeedResult();
		result5.setTitle("");

		List<FeedResult> results = new ArrayList<>(Arrays.asList(result1,result2,result3,result4,result5));

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(1,results.size());
		Assertions.assertEquals(result1, results.get(0));
		
	}
}
