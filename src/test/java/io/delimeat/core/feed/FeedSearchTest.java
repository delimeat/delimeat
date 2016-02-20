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
  
  	@Test
  	public void hashCodeTest(){
   	search.getResults().add(new FeedResult());
		Assert.assertEquals(887503743,search.hashCode());
   }
  
  	@Test
  	public void toStringTest(){
		search.getResults().add(new FeedResult());
     	Assert.assertEquals("FeedSearch{results=[FeedResult{title=null, torrentURL=null, contentLength=0, seeders=0, leechers=0, torrent=null, feedResultRejections=[]}]}",search.toString());
   }

}
