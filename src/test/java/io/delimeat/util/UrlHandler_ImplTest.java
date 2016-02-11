package io.delimeat.util;

import io.delimeat.util.UrlHandler_Impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

//TODO add additional tests
public class UrlHandler_ImplTest {

	private UrlHandler_Impl handler;

	@Before
	public void setUp() {
		handler = new UrlHandler_Impl();
	}

	@Test
	public void timeoutTest() {
		Assert.assertEquals(0, handler.getTimeout());
		handler.setTimeout(Integer.MAX_VALUE);
		Assert.assertEquals(Integer.MAX_VALUE, handler.getTimeout());
	}
  
  	public void openInputUrlConnectionGZIP() throws IOException{
   	URLConnection connection = Mockito.mock(URLConnection.class);
     	Mockito.when(connection.getContentEncoding()).thenReturn("gzip");
     	InputStream input = Mockito.mock(InputStream.class);
     	Mockito.when(connection.getInputStream()).thenReturn(input);
     
     	InputStream returnedInput = handler.openInput(connection);
     	Assert.assertTrue(returnedInput instanceof GZIPInputStream); 
   }
  
  	public void openInputUrlConnectionDeflate() throws IOException{
   	URLConnection connection = Mockito.mock(URLConnection.class);
     	Mockito.when(connection.getContentEncoding()).thenReturn("deflate");
     	InputStream input = Mockito.mock(InputStream.class);
     	Mockito.when(connection.getInputStream()).thenReturn(input);
     
     	InputStream returnedInput = handler.openInput(connection);
     	Assert.assertTrue(returnedInput instanceof InflaterInputStream); 
   }
  
  	public void openInputUrlConnectionNull() throws IOException{
   	URLConnection connection = Mockito.mock(URLConnection.class);
     	Mockito.when(connection.getContentEncoding()).thenReturn(null);
     	InputStream input = Mockito.mock(InputStream.class);
     	Mockito.when(connection.getInputStream()).thenReturn(input);
     
     	InputStream returnedInput = handler.openInput(connection);
     	Assert.assertEquals(input, returnedInput); 
   }
  
  	public void openInputUrlConnectionNotDeflateOrGZIP() throws IOException{
   	URLConnection connection = Mockito.mock(URLConnection.class);
     	Mockito.when(connection.getContentEncoding()).thenReturn("JIbberIsh");
     	InputStream input = Mockito.mock(InputStream.class);
     	Mockito.when(connection.getInputStream()).thenReturn(input);
     
     	InputStream returnedInput = handler.openInput(connection);
     	Assert.assertEquals(input, returnedInput); 
   }
}
