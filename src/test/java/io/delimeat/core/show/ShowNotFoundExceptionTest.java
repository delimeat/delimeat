package io.delimeat.core.show;

import org.junit.Assert;
import org.junit.Test;

public class ShowNotFoundExceptionTest {
      
  	@Test
  	public void messageConstructorTest(){
     	ShowNotFoundException ex = new ShowNotFoundException("TEST");
     	Assert.assertEquals("TEST", ex.getMessage());
   }
  	
  	@Test
  	public void causeConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	ShowNotFoundException ex = new ShowNotFoundException(throwable);
     	Assert.assertEquals(throwable, ex.getCause());
   }
  	
  	@Test
  	public void messageCauseConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	ShowNotFoundException ex = new ShowNotFoundException("TEST", throwable);
     	Assert.assertEquals("TEST", ex.getMessage());
     	Assert.assertEquals(throwable, ex.getCause());     	
   }
}
