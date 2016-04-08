package io.delimeat.core.feed;

import org.junit.Assert;
import org.junit.Test;

public class FeedSourceTest {
  
  	@Test
  	public void katTest(){
     	Assert.assertEquals(0, FeedSource.KAT.getValue());
   }
  
  	@Test
  	public void strikeTest(){
     	Assert.assertEquals(1, FeedSource.STRIKE.getValue());
   }  
}
