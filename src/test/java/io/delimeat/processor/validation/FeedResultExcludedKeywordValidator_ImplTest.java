package io.delimeat.processor.validation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.processor.validation.FeedResultExcludedKeywordValidator_Impl;

public class FeedResultExcludedKeywordValidator_ImplTest {
  
	private FeedResultExcludedKeywordValidator_Impl validator;
	
	@Before
	public void setUp(){
		validator = new FeedResultExcludedKeywordValidator_Impl();
	}
  
  	@Test
  	public void nullExcludedKeywordsTest() throws Exception{
  		Config config = new Config();
		FeedResult result = new FeedResult();
		List<FeedResult> results = Arrays.asList(result);
     
		validator.validate(results, null, config);
		Assert.assertEquals(0, result.getFeedResultRejections().size());
   }
   
  	@Test
  	public void emptyExcludedKeywordsTest() throws Exception{
  		Config config = new Config();
     	config.setExcludedKeywords(Collections.<String>emptyList());
     
		FeedResult result = new FeedResult();
		List<FeedResult> results = Arrays.asList(result);
     
		validator.validate(results, null, config);
		Assert.assertEquals(0, result.getFeedResultRejections().size());
   }
  
  	@Test
  	public void foundTest() throws Exception{
  		Config config = new Config();
     	config.setExcludedKeywords(Arrays.asList("FIND_ME","TEXT2"));
     
		FeedResult result = new FeedResult();
     	result.setTitle("BLAH_BLAHFIND_ME_BLAH");
		List<FeedResult> results = Arrays.asList(result);
     
		validator.validate(results, null, config);
		Assert.assertEquals(1, result.getFeedResultRejections().size());
     	Assert.assertEquals(FeedResultRejection.EXCLUDED_KEYWORD, result.getFeedResultRejections().get(0));
   }
  
  	@Test
  	public void notFoundTest() throws Exception{
  		Config config = new Config();
     	config.setExcludedKeywords(Arrays.asList("FIND_ME","TEXT2"));
     
		FeedResult result = new FeedResult();
     	result.setTitle("BLAH_BLAH_BLAH");
		List<FeedResult> results = Arrays.asList(result);
     
		validator.validate(results, null, config);
		Assert.assertEquals(0, result.getFeedResultRejections().size());
   }
}
