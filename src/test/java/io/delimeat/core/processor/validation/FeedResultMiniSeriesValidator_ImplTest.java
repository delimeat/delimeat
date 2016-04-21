package io.delimeat.core.processor.validation;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.processor.validation.FeedResultMiniSeriesValidator_Impl;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FeedResultMiniSeriesValidator_ImplTest {
	
	private FeedResultMiniSeriesValidator_Impl validator;
	private List<FeedResult> results;
	private Show show;
  	private Config config;

	@Before
	public void setUp(){
		validator = new FeedResultMiniSeriesValidator_Impl();
		results = new ArrayList<FeedResult>();
		show = new Show();
     	config = new Config();
	}

	@Test
	public void nullTitleFeedResultTest() throws Exception{	
		Episode episode = new Episode();
		episode.setEpisodeNum(1);
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle(null);
		results.add(result);

		validator.validate(results, show, config);
		Assert.assertEquals(1, result.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INVALID_MINI_SERIES_RESULT, result.getFeedResultRejections().get(0));	
	}
	
	@Test
	public void emptyTitleFeedResultTest() throws Exception{	
		Episode episode = new Episode();
		episode.setEpisodeNum(1);
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle("");
		results.add(result);

		validator.validate(results, show, config);
		Assert.assertEquals(1, result.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INVALID_MINI_SERIES_RESULT, result.getFeedResultRejections().get(0));	
	}
	
	@Test
	public void noNextEpisodeTest() throws Exception{
		FeedResult result = new FeedResult();
		result.setTitle("testtext12.02");
		results.add(result);

		validator.validate(results, show, config);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_MINI_SERIES_RESULT, result.getFeedResultRejections().get(0));	
	}
	
	@Test
	public void zeorEpisodeNumTest() throws Exception{
		Episode episode = new Episode();
		episode.setEpisodeNum(0);
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext12.02");
		results.add(result);

		validator.validate(results, show, config);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_MINI_SERIES_RESULT, result.getFeedResultRejections().get(0));	
	}
	
	@Test
	public void rejectNotMiniSeriesFeedResultTest() throws Exception{
		Episode episode = new Episode();
		episode.setEpisodeNum(1);
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle("SHOW_TITLE_S01E02_2012.12.12");
		results.add(result);

		validator.validate(results, show, config);
		Assert.assertEquals(1, result.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INVALID_MINI_SERIES_RESULT, result.getFeedResultRejections().get(0));
	}
	@Test
	public void rejectIncorrectEpisodeFeedResultTest() throws Exception{
		Episode episode = new Episode();
		episode.setEpisodeNum(1);
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle("SHOW_TITLE_S01E02_2012.12.12_03Of99");
		results.add(result);

		validator.validate(results, show, config);
		Assert.assertEquals(1, result.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INVALID_MINI_SERIES_RESULT, result.getFeedResultRejections().get(0));
	}
	@Test
	public void validFeedResultTest() throws Exception{
		Episode episode = new Episode();
		episode.setEpisodeNum(1);
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle("SHOW_TITLE_S01E02_2012.12.12_01Of99");
		results.add(result);

		validator.validate(results, show, config);
		Assert.assertEquals(0, result.getFeedResultRejections().size());
	}
	
	@Test
	public void validRejectRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setEpisodeNum(1);
		show.setNextEpisode(episode);
		
		FeedResult result1 = new FeedResult();
		result1.setTitle("SHOW_TITLE_S01E02_2012.12.12_01Of99");
		results.add(result1);
		FeedResult result2 = new FeedResult();
		result2.setTitle("SHOW_TITLE_S01E02_2012.12.12_03Of99");
		results.add(result2);
		FeedResult result3 = new FeedResult();
		result3.setTitle("SHOW_TITLE_S01E02_2012.12.12");
		results.add(result3);
		FeedResult result4 = new FeedResult();
		result3.setTitle(null);
		results.add(result4);
		FeedResult result5 = new FeedResult();
		result5.setTitle("");
		results.add(result5);
		
		validator.validate(results, show, config);
		Assert.assertEquals(0, result1.getFeedResultRejections().size());
		
		Assert.assertEquals(1, result2.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INVALID_MINI_SERIES_RESULT, result2.getFeedResultRejections().get(0));
		
		Assert.assertEquals(1, result3.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INVALID_MINI_SERIES_RESULT, result3.getFeedResultRejections().get(0));

		Assert.assertEquals(1, result4.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INVALID_MINI_SERIES_RESULT, result4.getFeedResultRejections().get(0));
		
		Assert.assertEquals(1, result5.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INVALID_MINI_SERIES_RESULT, result5.getFeedResultRejections().get(0));
	}
}
