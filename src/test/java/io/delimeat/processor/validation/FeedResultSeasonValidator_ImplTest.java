package io.delimeat.processor.validation;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.processor.validation.FeedResultSeasonValidator_Impl;
import io.delimeat.show.domain.Episode;

public class FeedResultSeasonValidator_ImplTest {

	private FeedResultSeasonValidator_Impl validator;
	
	@Before
	public void setUp(){
		validator = new FeedResultSeasonValidator_Impl();
	}

	@Test
	public void emptyTitleFeedResultTest() throws Exception{	
		Episode episode = new Episode();
		episode.setSeasonNum(1);
		episode.setEpisodeNum(1);
		
		FeedResult result = new FeedResult();
		result.setTitle("");
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_SEASON_RESULT, result.getFeedResultRejections().get(0));	
	}
	
	@Test
	public void zeroSeasonTest() throws Exception{	
		Episode episode = new Episode();
		episode.setSeasonNum(0);
		episode.setEpisodeNum(1);
		
		FeedResult result = new FeedResult();
		result.setTitle("TITLE");
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_SEASON_RESULT, result.getFeedResultRejections().get(0));	
	}
	
	@Test
	public void zeroEpisodeTest() throws Exception{	
		Episode episode = new Episode();
		episode.setSeasonNum(1);
		episode.setEpisodeNum(0);
		
		FeedResult result = new FeedResult();
		result.setTitle("TITLE");
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_SEASON_RESULT, result.getFeedResultRejections().get(0));	
	}
	
	@Test
	public void validFeedResultTest() throws Exception{
		Episode episode = new Episode();
		episode.setSeasonNum(1);
		episode.setEpisodeNum(19);
		
		FeedResult result = new FeedResult();
		result.setTitle("texttextS01E19TEXTTEXT");
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(0, result.getFeedResultRejections().size());	
	}
	
	@Test
	public void rejectNotASeriesEpisode() throws Exception{
		Episode episode = new Episode();
		episode.setSeasonNum(1);
		episode.setEpisodeNum(19);
		
		FeedResult result = new FeedResult();
		result.setTitle("texttext2012.01.05_03of99TEXTTEXT");
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_SEASON_RESULT, result.getFeedResultRejections().get(0));
	}
	
	@Test
	public void rejectIncorrectSeasonEpisode() throws Exception{
		Episode episode = new Episode();
		episode.setSeasonNum(1);
		episode.setEpisodeNum(19);
		
		FeedResult result = new FeedResult();
		result.setTitle("texttextS02E19TEXTTEXT");
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_SEASON_RESULT, result.getFeedResultRejections().get(0));
	}
	
	@Test
	public void rejectIncorrectEpisodeEpisode() throws Exception{
		Episode episode = new Episode();
		episode.setSeasonNum(1);
		episode.setEpisodeNum(19);
		
		FeedResult result = new FeedResult();
		result.setTitle("texttextS01E18TEXTTEXT");
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_SEASON_RESULT, result.getFeedResultRejections().get(0));
	}
	
	@Test
	public void validRejectRejectRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setSeasonNum(1);
		episode.setEpisodeNum(19);
		
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

		List<FeedResult> results = Arrays.asList(result1,result2,result3,result4,result5);
		
		validator.validate(results, episode, null);
		Assert.assertEquals(0, result1.getFeedResultRejections().size());
		
		Assert.assertEquals(1, result2.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_SEASON_RESULT, result2.getFeedResultRejections().get(0));
		
		Assert.assertEquals(1, result3.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_SEASON_RESULT, result3.getFeedResultRejections().get(0));
		
		Assert.assertEquals(1, result4.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_SEASON_RESULT, result4.getFeedResultRejections().get(0));
		
		Assert.assertEquals(1, result5.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_SEASON_RESULT, result5.getFeedResultRejections().get(0));
		
	}

	
	
}
