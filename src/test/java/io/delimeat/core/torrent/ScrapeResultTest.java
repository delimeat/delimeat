package io.delimeat.core.torrent;

import org.junit.Assert;
import org.junit.Test;

public class ScrapeResultTest {

	private ScrapeResult result;
	
	@Test
	public void createResult(){
		result = new ScrapeResult(Long.MIN_VALUE,Long.MAX_VALUE);
		Assert.assertEquals(Long.MIN_VALUE, result.getSeeders());
		Assert.assertEquals(Long.MAX_VALUE, result.getLeechers());
	}
}
