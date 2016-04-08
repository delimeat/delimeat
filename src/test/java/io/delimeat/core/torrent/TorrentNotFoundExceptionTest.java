package io.delimeat.core.torrent;

import org.junit.Assert;
import org.junit.Test;

public class TorrentNotFoundExceptionTest {
  
  	@Test
  	public void messageConstructorTest(){
     	TorrentNotFoundException ex = new TorrentNotFoundException("TEST");
     	Assert.assertEquals("TEST", ex.getMessage());
   }
  	
  	@Test
  	public void causeConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	TorrentNotFoundException ex = new TorrentNotFoundException(throwable);
     	Assert.assertEquals(throwable, ex.getCause());
   }
  	
  	@Test
  	public void messageCauseConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	TorrentNotFoundException ex = new TorrentNotFoundException("TEST", throwable);
     	Assert.assertEquals("TEST", ex.getMessage());
     	Assert.assertEquals(throwable, ex.getCause());     	
   }
}
