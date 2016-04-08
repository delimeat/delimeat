package io.delimeat.core.guide;

import org.junit.Assert;
import org.junit.Test;

public class GuideNotAuthorisedExceptionTest {
  
  	@Test
  	public void messageConstructorTest(){
     	GuideNotAuthorisedException ex = new GuideNotAuthorisedException("TEST");
     	Assert.assertEquals("TEST", ex.getMessage());
   }
  	
  	@Test
  	public void causeConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	GuideNotAuthorisedException ex = new GuideNotAuthorisedException(throwable);
     	Assert.assertEquals(throwable, ex.getCause());
   }
  	
  	@Test
  	public void messageCauseConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	GuideNotAuthorisedException ex = new GuideNotAuthorisedException("TEST", throwable);
     	Assert.assertEquals("TEST", ex.getMessage());
     	Assert.assertEquals(throwable, ex.getCause());     	
   }
}
