package io.delimeat.core.feed;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FeedSearchTest {

	private FeedSearch search;
	
	@Before
	public void setUp() throws Exception {
		search = new FeedSearch();
	}
	
	@Test
	public void resultsTest(){
		Assert.assertTrue(search.getResults().isEmpty());
		FeedResult result = new FeedResult();
		search.setResults(Arrays.asList(result));
		Assert.assertFalse(search.getResults().isEmpty());
		Assert.assertEquals(1, search.getResults().size());
		Assert.assertEquals(result, search.getResults().get(0));
		
	}

}
