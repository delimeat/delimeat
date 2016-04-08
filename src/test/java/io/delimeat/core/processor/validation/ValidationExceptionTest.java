package io.delimeat.core.processor.validation;

import org.junit.Assert;
import org.junit.Test;

public class ValidationExceptionTest {
  
  	@Test
  	public void messageConstructorTest(){
     	ValidationException ex = new ValidationException("TEST");
     	Assert.assertEquals("TEST", ex.getMessage());
   }
  	
  	@Test
  	public void causeConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	ValidationException ex = new ValidationException(throwable);
     	Assert.assertEquals(throwable, ex.getCause());
   }
  	
  	@Test
  	public void messageCauseConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	ValidationException ex = new ValidationException("TEST", throwable);
     	Assert.assertEquals("TEST", ex.getMessage());
     	Assert.assertEquals(throwable, ex.getCause());     	
   }
}
