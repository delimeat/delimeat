package io.delimeat.core.processor.validation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.show.Show;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FeedResultExcludedKeywordValidator_ImplTest {
  
	private FeedResultExcludedKeywordValidator_Impl validator;
	private List<FeedResult> results;
	private Show show;
  	private Config config;
	
	@Before
	public void setUp(){
		validator = new FeedResultExcludedKeywordValidator_Impl();
		results = new ArrayList<FeedResult>();
		show = new Show();
     	config = new Config();
	}
  
  	@Test
  	public void nullExcludedKeywordsTest() throws Exception{
		FeedResult result = new FeedResult();
		results.add(result);
     
		validator.validate(results, show, config);
		Assert.assertEquals(0, result.getFeedResultRejections().size());
   }
   
  	@Test
  	public void emptyExcludedKeywordsTest() throws Exception{
     	config.setExcludedKeywords(Collections.<String>emptyList());
     
		FeedResult result = new FeedResult();
		results.add(result);
     
		validator.validate(results, show, config);
		Assert.assertEquals(0, result.getFeedResultRejections().size());
   }
  
  	@Test
  	public void foundTest() throws Exception{
     	config.setExcludedKeywords(Arrays.asList("FIND_ME","TEXT2"));
     
		FeedResult result = new FeedResult();
     	result.setTitle("BLAH_BLAHFIND_ME_BLAH");
		results.add(result);
     
		validator.validate(results, show, config);
		Assert.assertEquals(1, result.getFeedResultRejections().size());
     	Assert.assertEquals(FeedResultRejection.EXCLUDED_KEYWORD, result.getFeedResultRejections().get(0));
   }
  
  	@Test
  	public void notFoundTest() throws Exception{
     	config.setExcludedKeywords(Arrays.asList("FIND_ME","TEXT2"));
     
		FeedResult result = new FeedResult();
     	result.setTitle("BLAH_BLAH_BLAH");
		results.add(result);
     
		validator.validate(results, show, config);
		Assert.assertEquals(0, result.getFeedResultRejections().size());
   }
}
