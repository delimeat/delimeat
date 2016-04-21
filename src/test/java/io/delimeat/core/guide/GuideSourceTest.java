package io.delimeat.core.guide;

import org.junit.Assert;
import org.junit.Test;

public class GuideSourceTest {
  
  	@Test
  	public void tvdbTest(){
     	Assert.assertEquals(0, GuideSource.TVDB.getValue());
   }
  
  	@Test
  	public void imdbTest(){
     	Assert.assertEquals(1, GuideSource.IMDB.getValue());
   }
  
  	@Test
  	public void tvrageTest(){
     	Assert.assertEquals(2, GuideSource.TVRAGE.getValue());
   }
  
  	@Test
  	public void tmdbTest(){
     	Assert.assertEquals(3, GuideSource.TMDB.getValue());
   }
  
  	@Test
  	public void tvmazeTest(){
     	Assert.assertEquals(4, GuideSource.TVMAZE.getValue());
   }
}
