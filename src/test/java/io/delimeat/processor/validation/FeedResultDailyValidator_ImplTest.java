package io.delimeat.processor.validation;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.processor.validation.FeedResultDailyValidator_Impl;
import io.delimeat.show.domain.Episode;

public class FeedResultDailyValidator_ImplTest {
	
	private FeedResultDailyValidator_Impl validator;
	
	@Before
	public void setUp(){
		validator = new FeedResultDailyValidator_Impl();
	}

		
	@Test
	public void nullTitleFeedResultTest() throws Exception{	
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.parse("2012-02-03"));
		
		FeedResult result = new FeedResult();
		result.setTitle(null);
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result.getFeedResultRejections().get(0));	
	}

	@Test
	public void emptyTitleFeedResultTest() throws Exception{	
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.parse("2012-02-03"));
		
		FeedResult result = new FeedResult();
		result.setTitle("");
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(1, result.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result.getFeedResultRejections().get(0));	
	}
	
	@Test
	public void noAirDateTest() throws Exception{	
		Episode episode = new Episode();
		episode.setAirDate(null);
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext12.02");
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(1, result.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result.getFeedResultRejections().get(0));	
	}
	
	@Test
	public void noYearRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.parse("2012-02-03"));
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext12.02");
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result.getFeedResultRejections().get(0));	
	}
	
	@Test
	public void noMonthRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.parse("2012-02-03"));
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext2012.JAN.02");
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result.getFeedResultRejections().get(0));	
	}
	@Test
	public void noDayRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.parse("2012-02-03"));
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext2012.02.TUES");
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result.getFeedResultRejections().get(0));	
	}
  
	@Test
	public void wrongYearRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.parse("2012-02-03"));
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext2013.02.03");
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result.getFeedResultRejections().get(0));	
	}
    
	@Test
	public void wrongMonthRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.parse("2012-02-03"));
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext2012.13.03");
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result.getFeedResultRejections().get(0));	
	}
	@Test
	public void wrongDayRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.parse("2012-02-03"));
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext2012.02.14");
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result.getFeedResultRejections().get(0));	
	}
  
	@Test
	public void validFeedResultTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.parse("2012-02-03"));
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext2012.02.03");
		List<FeedResult> results = Arrays.asList(result);

		validator.validate(results, episode, null);
		Assert.assertEquals(0, result.getFeedResultRejections().size());	
	}
	
	@Test
	public void validAndRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.parse("2012-02-03"));
		
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

		List<FeedResult> results = Arrays.asList(result1,result2,result3,result4,result5,result6,result7,result8,result9);

		validator.validate(results, episode, null);
		Assert.assertEquals(0, result1.getFeedResultRejections().size());	
		
		Assert.assertEquals(1, result2.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result2.getFeedResultRejections().get(0));	
		
		Assert.assertEquals(1, result3.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result3.getFeedResultRejections().get(0));	
		
		Assert.assertEquals(1, result4.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result4.getFeedResultRejections().get(0));	
		
		Assert.assertEquals(1, result5.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result5.getFeedResultRejections().get(0));
		
		Assert.assertEquals(1, result6.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result6.getFeedResultRejections().get(0));	
		
		Assert.assertEquals(1, result7.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result7.getFeedResultRejections().get(0));	
		
		Assert.assertEquals(1, result8.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result8.getFeedResultRejections().get(0));	

		Assert.assertEquals(1, result9.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result9.getFeedResultRejections().get(0));	

	}
}
