package io.delimeat.core.torrent;

import org.junit.Assert;
import org.junit.Test;

public class UnhandledScrapeExceptionTest {
  
  	@Test
  	public void messageConstructorTest(){
     	UnhandledScrapeException ex = new UnhandledScrapeException("TEST");
     	Assert.assertEquals("TEST", ex.getMessage());
   }
  	
  	@Test
  	public void causeConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	UnhandledScrapeException ex = new UnhandledScrapeException(throwable);
     	Assert.assertEquals(throwable, ex.getCause());
   }
  	
  	@Test
  	public void messageCauseConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	UnhandledScrapeException ex = new UnhandledScrapeException("TEST", throwable);
     	Assert.assertEquals("TEST", ex.getMessage());
     	Assert.assertEquals(throwable, ex.getCause());     	
   }
  
}
