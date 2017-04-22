package io.delimeat.torrent.exception;

import org.junit.Assert;
import org.junit.Test;

import io.delimeat.torrent.exception.TorrentException;

public class TorrentExceptionTest {
  
  	@Test
  	public void messageConstructorTest(){
     	TorrentException ex = new TorrentException("TEST");
     	Assert.assertEquals("TEST", ex.getMessage());
   }
  	
  	@Test
  	public void causeConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	TorrentException ex = new TorrentException(throwable);
     	Assert.assertEquals(throwable, ex.getCause());
   }
  	
  	@Test
  	public void messageCauseConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	TorrentException ex = new TorrentException("TEST", throwable);
     	Assert.assertEquals("TEST", ex.getMessage());
     	Assert.assertEquals(throwable, ex.getCause());     	
   }
}
