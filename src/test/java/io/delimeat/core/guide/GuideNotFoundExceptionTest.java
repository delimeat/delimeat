package io.delimeat.core.guide;

import org.junit.Assert;
import org.junit.Test;

public class GuideNotFoundExceptionTest {
  
  	@Test
  	public void messageConstructorTest(){
     	GuideNotFoundException ex = new GuideNotFoundException("TEST");
     	Assert.assertEquals("TEST", ex.getMessage());
   }
  	
  	@Test
  	public void causeConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	GuideNotFoundException ex = new GuideNotFoundException(throwable);
     	Assert.assertEquals(throwable, ex.getCause());
   }
  	
  	@Test
  	public void messageCauseConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	GuideError error = new GuideError();
     	error.setMessage("TEST");
     	GuideNotFoundException ex = new GuideNotFoundException("TEST", throwable);
     	Assert.assertEquals("TEST", ex.getMessage());
     	Assert.assertEquals(throwable, ex.getCause());     	
   }
}
