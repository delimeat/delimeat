package io.delimeat.core.guide;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GuideSearchTest {

	private GuideSearch search;

	@Before
	public void setUp() throws Exception {
		search = new GuideSearch();
	}

	@Test
	public void resultsTest() {
		Assert.assertNotNull(search.getResults());
		Assert.assertEquals(0, search.getResults().size());
		GuideSearchResult result = new GuideSearchResult();
		search.setResults(Arrays.asList(result));
		Assert.assertEquals(1, search.getResults().size());
		Assert.assertEquals(result, search.getResults().get(0));
	}
  
  	@Test
  	public void hashCodeTest(){
   	search.getResults().add(new GuideSearchResult());
		Assert.assertEquals(1023,search.hashCode());
   }
  
  	@Test
  	public void toStringTest(){
		search.getResults().add(new GuideSearchResult());
     	Assert.assertEquals("GuideSearch{results=[GuideSearchResult{title=null, firstAired=null, guideId=null, description=null, lastUpdated=null}]}",search.toString());
   }

}
