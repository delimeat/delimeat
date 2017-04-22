package io.delimeat.processor.validation;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.processor.validation.FeedResultMiniSeriesValidator_Impl;
import io.delimeat.show.domain.Episode;

public class FeedResultMiniSeriesValidator_ImplTest {
	
	private FeedResultMiniSeriesValidator_Impl validator;

	@Before
	public void setUp(){
		validator = new FeedResultMiniSeriesValidator_Impl();
	}

	@Test
	public void nullTitleFeedResultTest() throws Exception{	
		Episode episode = new Episode();
		episode.setEpisodeNum(1);

		
		FeedResult result = new FeedResult();
		result.setTitle(null);
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(1, result.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INVALID_MINI_SERIES_RESULT, result.getFeedResultRejections().get(0));	
	}
	
	@Test
	public void emptyTitleFeedResultTest() throws Exception{	
		Episode episode = new Episode();
		episode.setEpisodeNum(1);
		
		FeedResult result = new FeedResult();
		result.setTitle("");
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(1, result.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INVALID_MINI_SERIES_RESULT, result.getFeedResultRejections().get(0));	
	}
	
	@Test
	public void zeorEpisodeNumTest() throws Exception{
		Episode episode = new Episode();
		episode.setEpisodeNum(0);
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext12.02");
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_MINI_SERIES_RESULT, result.getFeedResultRejections().get(0));	
	}
	
	@Test
	public void rejectNotMiniSeriesFeedResultTest() throws Exception{
		Episode episode = new Episode();
		episode.setEpisodeNum(1);
		
		FeedResult result = new FeedResult();
		result.setTitle("SHOW_TITLE_S01E02_2012.12.12");
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(1, result.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INVALID_MINI_SERIES_RESULT, result.getFeedResultRejections().get(0));
	}
	@Test
	public void rejectIncorrectEpisodeFeedResultTest() throws Exception{
		Episode episode = new Episode();
		episode.setEpisodeNum(1);
		
		FeedResult result = new FeedResult();
		result.setTitle("SHOW_TITLE_S01E02_2012.12.12_03Of99");
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(1, result.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INVALID_MINI_SERIES_RESULT, result.getFeedResultRejections().get(0));
	}
	@Test
	public void validFeedResultTest() throws Exception{
		Episode episode = new Episode();
		episode.setEpisodeNum(1);
		
		FeedResult result = new FeedResult();
		result.setTitle("SHOW_TITLE_S01E02_2012.12.12_01Of99");
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(0, result.getFeedResultRejections().size());
	}
	
	@Test
	public void validRejectRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setEpisodeNum(1);
		
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
		
		List<FeedResult> results = Arrays.asList(result1,result2,result3,result4,result5);
		
		validator.validate(results, episode, null);
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
