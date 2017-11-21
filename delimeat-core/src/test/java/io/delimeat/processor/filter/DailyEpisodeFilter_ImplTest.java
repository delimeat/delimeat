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

import java.time.LocalDate;
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

public class DailyEpisodeFilter_ImplTest {

	private DailyEpisodeFilter_Impl filter;
	
	@BeforeEach
	public void setUp(){
		filter = new DailyEpisodeFilter_Impl();
	}
	
	@Test
	public void notDailyFeedResultTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.parse("2012-02-03"));
		Show show = new Show();
		show.setShowType(ShowType.UNKNOWN);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext2012.02.03");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(1, results.size());	
		Assertions.assertEquals(result, results.get(0));	
	}
		
	@Test
	public void nullTitleFeedResultTest() throws Exception{	
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.parse("2012-02-03"));
		Show show = new Show();
		show.setShowType(ShowType.DAILY);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle(null);
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		System.out.println(results.size());
		
		filter.filter(results, episode, new Config());
		Assertions.assertEquals(0, results.size());
	}

	@Test
	public void emptyTitleFeedResultTest() throws Exception{	
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.parse("2012-02-03"));
		Show show = new Show();
		show.setShowType(ShowType.DAILY);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(0, results.size());
	}
	
	@Test
	public void noAirDateTest() throws Exception{	
		Episode episode = new Episode();
		episode.setAirDate(null);
		Show show = new Show();
		show.setShowType(ShowType.DAILY);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext12.02");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(0, results.size());
	}
	
	@Test
	public void noYearRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.parse("2012-02-03"));
		Show show = new Show();
		show.setShowType(ShowType.DAILY);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext12.02");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(0, results.size());
	}
	
	@Test
	public void noMonthRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.parse("2012-02-03"));
		Show show = new Show();
		show.setShowType(ShowType.DAILY);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext2012.JAN.02");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(0, results.size());
	}
	@Test
	public void noDayRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.parse("2012-02-03"));
		Show show = new Show();
		show.setShowType(ShowType.DAILY);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext2012.02.TUES");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(0, results.size());
	}
  
	@Test
	public void wrongYearRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.parse("2012-02-03"));
		Show show = new Show();
		show.setShowType(ShowType.DAILY);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext2013.02.03");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(0, results.size());
	}
    
	@Test
	public void wrongMonthRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.parse("2012-02-03"));
		Show show = new Show();
		show.setShowType(ShowType.DAILY);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext2012.13.03");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(0, results.size());
	}
	@Test
	public void wrongDayRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.parse("2012-02-03"));
		Show show = new Show();
		show.setShowType(ShowType.DAILY);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext2012.02.14");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(0, results.size());
	}
  
	@Test
	public void validFeedResultTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.parse("2012-02-03"));
		Show show = new Show();
		show.setShowType(ShowType.DAILY);
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext2012.02.03");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals(result, results.get(0));
	}
	
	@Test
	public void validAndRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.parse("2012-02-03"));
		Show show = new Show();
		show.setShowType(ShowType.DAILY);
		episode.setShow(show);
		
		FeedResult result1 = new FeedResult();
		result1.setTitle("testtext2012.02.03");

		FeedResult result2 = new FeedResult();
		result2.setTitle("testtext2012.02.14");

		FeedResult result3 = new FeedResult();
		result3.setTitle("testtext2012.13.03");

		FeedResult result4 = new FeedResult();
		result4.setTitle("testtext2013.02.03");

		FeedResult result5 = new FeedResult();
		result5.setTitle("testtext2012.02.TUES");

		FeedResult result6 = new FeedResult();
		result6.setTitle("testtext2012.JAN.02");

		FeedResult result7 = new FeedResult();
		result7.setTitle("testtext12.02");

		FeedResult result8 = new FeedResult();
		result8.setTitle(null);

		FeedResult result9 = new FeedResult();
		result9.setTitle("");

		List<FeedResult> results = new ArrayList<>(Arrays.asList(result1,result2,result3,result4,result5,result6,result7,result8,result9));

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals(result1, results.get(0));	

	}
}
