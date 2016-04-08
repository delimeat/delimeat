package io.delimeat.core.show;

import org.junit.Assert;
import org.junit.Test;

public class ShowConcurrencyExceptionTest {
      
  	@Test
  	public void messageConstructorTest(){
     	ShowConcurrencyException ex = new ShowConcurrencyException("TEST");
     	Assert.assertEquals("TEST", ex.getMessage());
   }
  	
  	@Test
  	public void causeConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	ShowConcurrencyException ex = new ShowConcurrencyException(throwable);
     	Assert.assertEquals(throwable, ex.getCause());
   }
  	
  	@Test
  	public void messageCauseConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	ShowConcurrencyException ex = new ShowConcurrencyException("TEST", throwable);
     	Assert.assertEquals("TEST", ex.getMessage());
     	Assert.assertEquals(throwable, ex.getCause());     	
   }
  
}
