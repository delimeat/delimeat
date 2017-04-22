package io.delimeat.show.domain;

import org.junit.Assert;
import org.junit.Test;

import io.delimeat.show.domain.ShowType;

public class ShowTypeTest {
  
  	@Test
  	public void unknownTest(){
     	Assert.assertEquals(0, ShowType.UNKNOWN.getValue());
   }
  
  	@Test
  	public void animatedTest(){
     	Assert.assertEquals(1, ShowType.ANIMATED.getValue());
   } 
  
  	@Test
  	public void dailyTest(){
     	Assert.assertEquals(2, ShowType.DAILY.getValue());
   } 
  
  	@Test
  	public void miniSeriesTest(){
     	Assert.assertEquals(3, ShowType.MINI_SERIES.getValue());
   } 
  
  	@Test
  	public void seasonTest(){
     	Assert.assertEquals(4, ShowType.SEASON.getValue());
   }
}
