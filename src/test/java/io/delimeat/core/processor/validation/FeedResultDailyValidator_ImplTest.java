package io.delimeat.core.processor.validation;

import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.processor.validation.FeedResultDailyValidator_Impl;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FeedResultDailyValidator_ImplTest {
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	private FeedResultDailyValidator_Impl validator;
	private List<FeedResult> results;
	private Show show;
	
	@Before
	public void setUp(){
		validator = new FeedResultDailyValidator_Impl();
		results = new ArrayList<FeedResult>();
		show = new Show();
	}
		
	@Test
	public void nullTitleFeedResultTest() throws Exception{	
		Episode episode = new Episode();
		episode.setAirDateTime(SDF.parse("2012-02-03"));
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle(null);
		results.add(result);

		validator.validate(results, show);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result.getFeedResultRejections().get(0));	
	}

	@Test
	public void emptyTitleFeedResultTest() throws Exception{	
		Episode episode = new Episode();
		episode.setAirDateTime(SDF.parse("2012-02-03"));
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle("");
		results.add(result);

		validator.validate(results, show);	
		Assert.assertEquals(1, result.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result.getFeedResultRejections().get(0));	
	}

	@Test
	public void noNextEpisodeTest() throws Exception{
		FeedResult result = new FeedResult();
		result.setTitle("testtext12.02");
		results.add(result);

		validator.validate(results, show);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result.getFeedResultRejections().get(0));	
	}
	
	@Test
	public void noAirDateTest() throws Exception{	
		Episode episode = new Episode();
		episode.setAirDateTime(null);
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext12.02");
		results.add(result);

		validator.validate(results, show);	
		Assert.assertEquals(1, result.getFeedResultRejections().size());
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result.getFeedResultRejections().get(0));	
	}
	
	@Test
	public void noYearRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDateTime(SDF.parse("2012-02-03"));
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext12.02");
		results.add(result);

		validator.validate(results, show);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result.getFeedResultRejections().get(0));	
	}
	
	@Test
	public void noMonthRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDateTime(SDF.parse("2012-02-03"));
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext2012.JAN.02");
		results.add(result);

		validator.validate(results, show);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result.getFeedResultRejections().get(0));	
	}
	@Test
	public void noDayRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDateTime(SDF.parse("2012-02-03"));
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext2012.02.TUES");
		results.add(result);

		validator.validate(results, show);	
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result.getFeedResultRejections().get(0));	
	}
	@Test
	public void wrongYearRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDateTime(SDF.parse("2012-02-03"));
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext2013.02.03");
		results.add(result);

		validator.validate(results, show);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result.getFeedResultRejections().get(0));	
	}
	@Test
	public void wrongdMonthRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDateTime(SDF.parse("2012-02-03"));
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext2012.13.03");
		results.add(result);

		validator.validate(results, show);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result.getFeedResultRejections().get(0));	
	}
	@Test
	public void wrongDayRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDateTime(SDF.parse("2012-02-03"));
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext2012.02.14");
		results.add(result);

		validator.validate(results, show);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_DAILY_RESULT, result.getFeedResultRejections().get(0));	
	}
	@Test
	public void validFeedResultTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDateTime(SDF.parse("2012-02-03"));
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle("testtext2012.02.03");
		results.add(result);

		validator.validate(results, show);
		Assert.assertEquals(0, result.getFeedResultRejections().size());	
	}
	
	@Test
	public void validAndRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDateTime(SDF.parse("2012-02-03"));
		show.setNextEpisode(episode);
		
		FeedResult result1 = new FeedResult();
		result1.setTitle("testtext2012.02.03");
		results.add(result1);
		FeedResult result2 = new FeedResult();
		result2.setTitle("testtext2012.02.14");
		results.add(result2);
		FeedResult result3 = new FeedResult();
		result3.setTitle("testtext2012.13.03");
		results.add(result3);
		FeedResult result4 = new FeedResult();
		result4.setTitle("testtext2013.02.03");
		results.add(result4);
		FeedResult result5 = new FeedResult();
		result5.setTitle("testtext2012.02.TUES");
		results.add(result5);
		FeedResult result6 = new FeedResult();
		result6.setTitle("testtext2012.JAN.02");
		results.add(result6);
		FeedResult result7 = new FeedResult();
		result7.setTitle("testtext12.02");
		results.add(result7);
		FeedResult result8 = new FeedResult();
		result8.setTitle(null);
		results.add(result8);
		FeedResult result9 = new FeedResult();
		result9.setTitle("");
		results.add(result9);

		validator.validate(results, show);	
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
