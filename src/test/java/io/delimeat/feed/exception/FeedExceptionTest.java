package io.delimeat.feed.exception;

import org.junit.Assert;
import org.junit.Test;

import io.delimeat.feed.exception.FeedException;

public class FeedExceptionTest {
  
  	@Test
  	public void messageConstructorTest(){
     	FeedException ex = new FeedException("TEST");
     	Assert.assertEquals("TEST", ex.getMessage());
   }
  	
  	@Test
  	public void causeConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	FeedException ex = new FeedException(throwable);
     	Assert.assertEquals(throwable, ex.getCause());
   }
  	
  	@Test
  	public void messageCauseConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	FeedException ex = new FeedException("TEST", throwable);
     	Assert.assertEquals("TEST", ex.getMessage());
     	Assert.assertEquals(throwable, ex.getCause());     	
   }
}
