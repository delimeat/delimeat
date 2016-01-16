package io.delimeat.core.feed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MaxSeederFeedResultComparator_Impl_Test {

	private MaxSeederFeedResultComparator_Impl comparator;
	private List<FeedResult> results;
	
	@Before
	public void setUp(){
		comparator = new MaxSeederFeedResultComparator_Impl();
		results = new ArrayList<FeedResult>();
	}
	
	@Test
	public void selectSortedTest() throws Exception{
		FeedResult result1 = new FeedResult();
		result1.setSeeders(100);
		results.add(result1);
		FeedResult result2 = new FeedResult();
		results.add(result2);
		result2.setSeeders(20);
		FeedResult result3 = new FeedResult();
		result3.setSeeders(200);
		results.add(result3);
		
		Collections.sort(results, comparator);
		Assert.assertEquals(result3, results.get(0));
		Assert.assertEquals(result1, results.get(1));
		Assert.assertEquals(result2, results.get(2));
	}
}
