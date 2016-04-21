package io.delimeat.core.processor.validation;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.processor.validation.FeedResultTitleValidator_Impl;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FeedResultTitleValidator_ImplTest {

	private FeedResultTitleValidator_Impl validator;
	private List<FeedResult> results;
	private Show show;
  	private Config config;
	
	@Before
	public void setUp(){
		validator = new FeedResultTitleValidator_Impl();
		results = new ArrayList<FeedResult>();
		show = new Show();
     	config = new Config();
	}
		
	@Test
	public void nullTitleFeedResultTest() throws Exception{	
		Episode episode = new Episode();
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle(null);
		results.add(result);

		validator.validate(results, show, config);
		Assert.assertEquals(1, result.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INCORRECT_TITLE, result.getFeedResultRejections().get(0));
	}

	@Test
	public void emptyTitleFeedResultTest() throws Exception{	
		Episode episode = new Episode();
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle("");
		results.add(result);

		validator.validate(results, show, config);
		Assert.assertEquals(1, result.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INCORRECT_TITLE, result.getFeedResultRejections().get(0));

	}
	
	@Test
	public void rejectTest() throws Exception{
		show.setTitle("SHOW_TITLE");
		
		FeedResult result = new FeedResult();
		result.setTitle("other_text_DIFFERENT_TITLE_other_text");
		results.add(result);		

		validator.validate(results, show, config);
		Assert.assertEquals(1, result.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INCORRECT_TITLE, result.getFeedResultRejections().get(0));
	}

	@Test
	public void acceptTest() throws Exception{
		show.setTitle("SHOW_TITLE");
		
		FeedResult result = new FeedResult();
		result.setTitle("other_text_SHOW_TITLE_other_text");
		results.add(result);		

		validator.validate(results, show, config);
		Assert.assertEquals(0, result.getFeedResultRejections().size());
	}
	@Test
	public void acceptRejectTest() throws Exception{
		show.setTitle("SHOW_TITLE");
		
		FeedResult result1 = new FeedResult();
		result1.setTitle("other_text_DIFFERENT_TITLE_other_text");
		results.add(result1);	
		FeedResult result2 = new FeedResult();
		result2.setTitle("other_text_SHOW_TITLE_other_text");
		results.add(result2);	
		FeedResult result3 = new FeedResult();
		result3.setTitle(null);
		results.add(result3);	
		FeedResult result4 = new FeedResult();
		result4.setTitle("");
		results.add(result4);	

		validator.validate(results, show, config);
		Assert.assertEquals(1, result1.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INCORRECT_TITLE, result1.getFeedResultRejections().get(0));
		
		Assert.assertEquals(0, result2.getFeedResultRejections().size());
		
		Assert.assertEquals(1, result3.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INCORRECT_TITLE, result3.getFeedResultRejections().get(0));

		Assert.assertEquals(1, result4.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INCORRECT_TITLE, result4.getFeedResultRejections().get(0));
	
	}
}
