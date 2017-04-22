package io.delimeat.torrent.bencode;

import org.junit.Assert;
import org.junit.Test;

import io.delimeat.torrent.bencode.BencodeException;

public class BencodeExceptionTest {
  
  	@Test
  	public void messageConstructorTest(){
     	BencodeException ex = new BencodeException("TEST");
     	Assert.assertEquals("TEST", ex.getMessage());
   }
  	
  	@Test
  	public void causeConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	BencodeException ex = new BencodeException(throwable);
     	Assert.assertEquals(throwable, ex.getCause());
   }
  	
  	@Test
  	public void messageCauseConstructorTest(){
     	Throwable throwable = new Throwable("THROWABLE");
     	BencodeException ex = new BencodeException("TEST", throwable);
     	Assert.assertEquals("TEST", ex.getMessage());
     	Assert.assertEquals(throwable, ex.getCause());     	
   }
}
