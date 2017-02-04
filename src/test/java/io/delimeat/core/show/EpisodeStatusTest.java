package io.delimeat.core.show;

import org.junit.Assert;
import org.junit.Test;

public class EpisodeStatusTest {

	@Test
	public void pendingTest(){
		Assert.assertEquals(0, EpisodeStatus.PENDING.getValue());
	}
	
	@Test
	public void foundTest(){
		Assert.assertEquals(1, EpisodeStatus.FOUND.getValue());
	}
	
	@Test
	public void skippedTest(){
		Assert.assertEquals(2, EpisodeStatus.SKIPPED.getValue());
	}
}
