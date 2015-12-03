package io.delimeat.core.guide;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
		List<GuideSearchResult> results = new ArrayList<GuideSearchResult>();
		GuideSearchResult mockedResult = Mockito.mock(GuideSearchResult.class);
		results.add(mockedResult);
		search.setResults(results);
		Assert.assertEquals(1, search.getResults().size());
		Assert.assertEquals(mockedResult, search.getResults().get(0));
	}

}
