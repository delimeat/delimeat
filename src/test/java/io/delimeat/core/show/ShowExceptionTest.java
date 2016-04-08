package io.delimeat.core.show;

import org.junit.Assert;
import org.junit.Test;

public class ShowExceptionTest {
      
  	@Test
  	public void messageConstructorTest(){
     	ShowException ex = new ShowException("TEST");
     	Assert.assertEquals("TEST", ex.getMessage());
   }
  	
  	@Test
  	public void causeConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	ShowException ex = new ShowException(throwable);
     	Assert.assertEquals(throwable, ex.getCause());
   }
  	
  	@Test
  	public void messageCauseConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	ShowException ex = new ShowException("TEST", throwable);
     	Assert.assertEquals("TEST", ex.getMessage());
     	Assert.assertEquals(throwable, ex.getCause());     	
   }
  

}
