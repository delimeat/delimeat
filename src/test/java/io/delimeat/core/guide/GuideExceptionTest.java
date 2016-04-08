package io.delimeat.core.guide;

import org.junit.Assert;
import org.junit.Test;

public class GuideExceptionTest {
  
  	@Test
  	public void messageConstructorTest(){
     	GuideException ex = new GuideException("TEST");
     	Assert.assertEquals("TEST", ex.getMessage());
   }
  	
  	@Test
  	public void causeConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	GuideException ex = new GuideException(throwable);
     	Assert.assertEquals(throwable, ex.getCause());
   }
  	
  	@Test
  	public void messageCauseConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	GuideException ex = new GuideException("TEST", throwable);
     	Assert.assertEquals("TEST", ex.getMessage());
     	Assert.assertEquals(throwable, ex.getCause());     	
   }
}
