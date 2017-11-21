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

public class TitleFilter_ImplTest {

	private TitleFilter_Impl filter;
	
	@BeforeEach
	public void setUp(){
		filter = new TitleFilter_Impl();
	}
	
	@Test
	public void emptyTitleFeedResultTest() throws Exception{
		Show show = new Show();
		show.setTitle("SHOW_TITLE");
		Episode episode = new Episode();
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(0, results.size());

	}
	
	@Test
	public void rejectTest() throws Exception{
		Show show = new Show();
		show.setTitle("SHOW_TITLE");
		Episode episode = new Episode();
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("other_text_DIFFERENT_TITLE_other_text");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));		

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(0, results.size());
	}

	@Test
	public void acceptTest() throws Exception{
		Show show = new Show();
		show.setTitle("SHOW_TITLE");
		Episode episode = new Episode();
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("other_text_SHOW_TITLE_other_text");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));		

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(1, results.size());	
		Assertions.assertEquals(result, results.get(0));	
	}
	
	@Test
	public void acceptRejectTest() throws Exception{
		Show show = new Show();
		show.setTitle("SHOW_TITLE");
		Episode episode = new Episode();
		episode.setShow(show);
		
		FeedResult result1 = new FeedResult();
		result1.setTitle("other_text_DIFFERENT_TITLE_other_text");

		FeedResult result2 = new FeedResult();
		result2.setTitle("other_text_SHOW_TITLE_other_text");
		
		FeedResult result3 = new FeedResult();
		result3.setTitle("");
		
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result1, result2, result3));

		filter.filter(results, episode, new Config());
		Assertions.assertEquals(1, results.size());	
		Assertions.assertEquals(result2, results.get(0));	
	
	}
}
