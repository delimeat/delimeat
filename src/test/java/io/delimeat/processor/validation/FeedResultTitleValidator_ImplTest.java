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
package io.delimeat.processor.validation;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.processor.validation.FeedResultTitleValidator_Impl;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.Show;

public class FeedResultTitleValidator_ImplTest {

	private FeedResultTitleValidator_Impl validator;
	
	@Before
	public void setUp(){
		validator = new FeedResultTitleValidator_Impl();

	}
		

	@Test
	public void emptyTitleFeedResultTest() throws Exception{
		Show show = new Show();
		show.setTitle("SHOW_TITLE");
		Episode episode = new Episode();
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("");
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(1, result.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INCORRECT_TITLE, result.getFeedResultRejections().get(0));

	}
	
	@Test
	public void rejectTest() throws Exception{
		Show show = new Show();
		show.setTitle("SHOW_TITLE");
		Episode episode = new Episode();
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("other_text_DIFFERENT_TITLE_other_text");
		List<FeedResult> results = Arrays.asList(result);		

		validator.validate(results, episode, null);
		Assert.assertEquals(1, result.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INCORRECT_TITLE, result.getFeedResultRejections().get(0));
	}

	@Test
	public void acceptTest() throws Exception{
		Show show = new Show();
		show.setTitle("SHOW_TITLE");
		Episode episode = new Episode();
		episode.setShow(show);
		
		FeedResult result = new FeedResult();
		result.setTitle("other_text_SHOW_TITLE_other_text");
		List<FeedResult> results = Arrays.asList(result);		

		validator.validate(results, episode, null);
		Assert.assertEquals(0, result.getFeedResultRejections().size());
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
		
		List<FeedResult> results = Arrays.asList(result1, result2, result3);

		validator.validate(results, episode, null);
		Assert.assertEquals(1, result1.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INCORRECT_TITLE, result1.getFeedResultRejections().get(0));
		
		Assert.assertEquals(0, result2.getFeedResultRejections().size());

		Assert.assertEquals(1, result3.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INCORRECT_TITLE, result3.getFeedResultRejections().get(0));
	
	}
}
