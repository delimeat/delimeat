package io.delimeat.core.feed.validation;

import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FeedResultSeasonValidator_ImplTest {

	private FeedResultSeasonValidator_Impl validator;
	private List<FeedResult> results;
	private Show show;
	
	@Before
	public void setUp(){
		validator = new FeedResultSeasonValidator_Impl();
		results = new ArrayList<FeedResult>();
		show = new Show();
	}
	
	@Test
	public void nullTitleFeedResultTest() throws Exception{	
		Episode episode = new Episode();
		episode.setSeasonNum(1);
		episode.setEpisodeNum(1);
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle(null);
		results.add(result);

		validator.validate(results, show);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_SEASON_RESULT, result.getFeedResultRejections().get(0));	
	}

	@Test
	public void emptyTitleFeedResultTest() throws Exception{	
		Episode episode = new Episode();
		episode.setSeasonNum(1);
		episode.setEpisodeNum(1);
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle("");
		results.add(result);

		validator.validate(results, show);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_SEASON_RESULT, result.getFeedResultRejections().get(0));	
	}
	
	@Test
	public void noNextEpisodeTest() throws Exception{
		FeedResult result = new FeedResult();
		result.setTitle("testtext12.02");
		results.add(result);

		validator.validate(results, show);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_SEASON_RESULT, result.getFeedResultRejections().get(0));	
	}
	
	@Test
	public void zeroSeasonTest() throws Exception{	
		Episode episode = new Episode();
		episode.setSeasonNum(0);
		episode.setEpisodeNum(1);
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle("TITLE");
		results.add(result);

		validator.validate(results, show);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_SEASON_RESULT, result.getFeedResultRejections().get(0));	
	}
	
	@Test
	public void zeroEpisodeTest() throws Exception{	
		Episode episode = new Episode();
		episode.setSeasonNum(1);
		episode.setEpisodeNum(0);
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle("TITLE");
		results.add(result);

		validator.validate(results, show);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_SEASON_RESULT, result.getFeedResultRejections().get(0));	
	}
	
	@Test
	public void validFeedResultTest() throws Exception{
		Episode episode = new Episode();
		episode.setSeasonNum(1);
		episode.setEpisodeNum(19);
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle("texttextS01E19TEXTTEXT");
		results.add(result);

		validator.validate(results, show);
		Assert.assertEquals(0, result.getFeedResultRejections().size());	
	}
	
	@Test
	public void rejectNotASeriesEpisode() throws Exception{
		Episode episode = new Episode();
		episode.setSeasonNum(1);
		episode.setEpisodeNum(19);
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle("texttext2012.01.05_03of99TEXTTEXT");
		results.add(result);

		validator.validate(results, show);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_SEASON_RESULT, result.getFeedResultRejections().get(0));
	}
	
	@Test
	public void rejectIncorrectSeasonEpisode() throws Exception{
		Episode episode = new Episode();
		episode.setSeasonNum(1);
		episode.setEpisodeNum(19);
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle("texttextS02E19TEXTTEXT");
		results.add(result);

		validator.validate(results, show);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_SEASON_RESULT, result.getFeedResultRejections().get(0));
	}
	
	@Test
	public void rejectIncorrectEpisodeEpisode() throws Exception{
		Episode episode = new Episode();
		episode.setSeasonNum(1);
		episode.setEpisodeNum(19);
		show.setNextEpisode(episode);
		
		FeedResult result = new FeedResult();
		result.setTitle("texttextS01E18TEXTTEXT");
		results.add(result);

		validator.validate(results, show);
		Assert.assertEquals(1, result.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_SEASON_RESULT, result.getFeedResultRejections().get(0));
	}
	
	@Test
	public void validRejectRejectRejectTest() throws Exception{
		Episode episode = new Episode();
		episode.setSeasonNum(1);
		episode.setEpisodeNum(19);
		show.setNextEpisode(episode);
		
		FeedResult result1 = new FeedResult();
		result1.setTitle("texttextS01E19TEXTTEXT");
		results.add(result1);
		FeedResult result2 = new FeedResult();
		result2.setTitle("texttext2012.01.05_03of99TEXTTEXT");
		results.add(result2);
		FeedResult result3 = new FeedResult();
		result3.setTitle("texttextS02E19TEXTTEXT");
		results.add(result3);
		FeedResult result4 = new FeedResult();
		result4.setTitle("texttextS01E18TEXTTEXT");
		results.add(result4);
		FeedResult result5 = new FeedResult();
		result5.setTitle(null);
		results.add(result5);
		FeedResult result6 = new FeedResult();
		result6.setTitle("");
		results.add(result6);
		
		validator.validate(results, show);
		Assert.assertEquals(0, result1.getFeedResultRejections().size());
		
		Assert.assertEquals(1, result2.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_SEASON_RESULT, result2.getFeedResultRejections().get(0));
		
		Assert.assertEquals(1, result3.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_SEASON_RESULT, result3.getFeedResultRejections().get(0));
		
		Assert.assertEquals(1, result4.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_SEASON_RESULT, result4.getFeedResultRejections().get(0));
		
		Assert.assertEquals(1, result5.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_SEASON_RESULT, result5.getFeedResultRejections().get(0));
		
		Assert.assertEquals(1, result6.getFeedResultRejections().size());	
		Assert.assertEquals(FeedResultRejection.INVALID_SEASON_RESULT, result6.getFeedResultRejections().get(0));
		
	}

	
	
}
