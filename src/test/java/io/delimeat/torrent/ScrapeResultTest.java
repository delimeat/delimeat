package io.delimeat.torrent;

import org.junit.Assert;
import org.junit.Test;

import io.delimeat.torrent.domain.ScrapeResult;

public class ScrapeResultTest {

	private ScrapeResult result;
	
	@Test
	public void createResult(){
		result = new ScrapeResult(Long.MIN_VALUE,Long.MAX_VALUE);
		Assert.assertEquals(Long.MIN_VALUE, result.getSeeders());
		Assert.assertEquals(Long.MAX_VALUE, result.getLeechers());
	}
  
  	@Test
  	public void toStringTest(){
 		result = new ScrapeResult(Long.MIN_VALUE,Long.MAX_VALUE);
		Assert.assertEquals("ScrapeResult{seeders=-9223372036854775808, leechers=9223372036854775807}", result.toString());    	
   }
}
