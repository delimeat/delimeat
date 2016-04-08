package io.delimeat.core.config;

import org.junit.Assert;
import org.junit.Test;

public class ConfigExceptionTest {
  
  	@Test
  	public void messageConstructorTest(){
     	ConfigException ex = new ConfigException("TEST");
     	Assert.assertEquals("TEST", ex.getMessage());
   }
  	
  	@Test
  	public void causeConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	ConfigException ex = new ConfigException(throwable);
     	Assert.assertEquals(throwable, ex.getCause());
   }
  	
  	@Test
  	public void messageCauseConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	ConfigException ex = new ConfigException("TEST", throwable);
     	Assert.assertEquals("TEST", ex.getMessage());
     	Assert.assertEquals(throwable, ex.getCause());     	
   }
}
