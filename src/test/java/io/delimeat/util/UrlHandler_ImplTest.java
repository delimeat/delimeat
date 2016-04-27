package io.delimeat.util;

import io.delimeat.util.UrlHandler_Impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

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
  
	@Test  
  	public void openInputUrlConnectionGZIP() throws IOException{
   	URLConnection connection = Mockito.mock(URLConnection.class);
     	Mockito.when(connection.getContentEncoding()).thenReturn("gzip");
     	byte magicLeft = (byte)(GZIPInputStream.GZIP_MAGIC & 0xff);
     	byte magicRight = (byte)((GZIPInputStream.GZIP_MAGIC >> 8) & 0xff);
     	byte[] bytes = new byte[]{magicLeft,magicRight,8,0,0,0,0,0,0,0};
     	ByteArrayInputStream input = new ByteArrayInputStream(bytes);
     	Mockito.when(connection.getInputStream()).thenReturn(input);
     
     	InputStream returnedInput = handler.openInput(connection);
     	Assert.assertTrue(returnedInput instanceof GZIPInputStream); 
   }

	@Test
  	public void openInputUrlConnectionDeflate() throws IOException{
   	URLConnection connection = Mockito.mock(URLConnection.class);
     	Mockito.when(connection.getContentEncoding()).thenReturn("deflate");
     	InputStream input = Mockito.mock(InputStream.class);
     	Mockito.when(connection.getInputStream()).thenReturn(input);
     
     	InputStream returnedInput = handler.openInput(connection);
     	Assert.assertTrue(returnedInput instanceof InflaterInputStream); 
   }

	@Test
  	public void openInputUrlConnectionNull() throws IOException{
   	URLConnection connection = Mockito.mock(URLConnection.class);
     	Mockito.when(connection.getContentEncoding()).thenReturn(null);
     	InputStream input = Mockito.mock(InputStream.class);
     	Mockito.when(connection.getInputStream()).thenReturn(input);
     
     	InputStream returnedInput = handler.openInput(connection);
     	Assert.assertEquals(input, returnedInput); 
   }
 
	@Test
  	public void openInputUrlConnectionNotDeflateOrGZIP() throws IOException{
   	URLConnection connection = Mockito.mock(URLConnection.class);
     	Mockito.when(connection.getContentEncoding()).thenReturn("JIbberIsh");
     	InputStream input = Mockito.mock(InputStream.class);
     	Mockito.when(connection.getInputStream()).thenReturn(input);
     
     	InputStream returnedInput = handler.openInput(connection);
     	Assert.assertEquals(input, returnedInput); 
   }
  
	@Test  
  	public void openUrlConnectionNoHeadersTest() throws IOException{
     final URLConnection connection = Mockito.mock(URLConnection.class);
     URLStreamHandler stubUrlHandler = new URLStreamHandler() {
       @Override
       protected URLConnection openConnection(URL u) throws IOException {
         return connection;
       }            
     };
     URL url = new URL("foo", "bar", 99, "/foobar", stubUrlHandler);
     
     URLConnection returnedConnection = handler.openUrlConnection(url);
     
     Assert.assertEquals(connection, returnedConnection);
     
     Mockito.verify(connection,Mockito.times(1)).setConnectTimeout(Mockito.anyInt());
     Mockito.verify(connection,Mockito.times(1)).setRequestProperty("user-agent",UrlHandler_Impl.DEFAULT_USER_AGENT );
   }

	@Test  
  	public void openUrlConnectionEmptyHeadersTest() throws IOException{
     final URLConnection connection = Mockito.mock(URLConnection.class);
     URLStreamHandler stubUrlHandler = new URLStreamHandler() {
       @Override
       protected URLConnection openConnection(URL u) throws IOException {
         return connection;
       }            
     };
     URL url = new URL("foo", "bar", 99, "/foobar", stubUrlHandler);
     Map<String,String> headers = new HashMap<String,String>();
     URLConnection returnedConnection = handler.openUrlConnection(url,headers);
     
     Assert.assertEquals(connection, returnedConnection);
     
     Mockito.verify(connection,Mockito.times(1)).setConnectTimeout(Mockito.anyInt());
     Mockito.verify(connection,Mockito.times(1)).setRequestProperty("user-agent",UrlHandler_Impl.DEFAULT_USER_AGENT );
   }
  
	@Test  
  	public void openUrlConnectionUserAgentHeadersTest() throws IOException{
     final URLConnection connection = Mockito.mock(URLConnection.class);
     URLStreamHandler stubUrlHandler = new URLStreamHandler() {
       @Override
       protected URLConnection openConnection(URL u) throws IOException {
         return connection;
       }            
     };
     URL url = new URL("foo", "bar", 99, "/foobar", stubUrlHandler);
     Map<String,String> headers = new HashMap<String,String>();
     headers.put("user-agent","USERAGENTVALUE");
     URLConnection returnedConnection = handler.openUrlConnection(url,headers);
     
     Assert.assertEquals(connection, returnedConnection);
     
     Mockito.verify(connection,Mockito.times(1)).setConnectTimeout(Mockito.anyInt());
     Mockito.verify(connection,Mockito.times(1)).setRequestProperty("user-agent","USERAGENTVALUE" );
   }
  
	@Test  
  	public void openUrlConnectionNoUserAgentHeadersTest() throws IOException{
     final URLConnection connection = Mockito.mock(URLConnection.class);
     URLStreamHandler stubUrlHandler = new URLStreamHandler() {
       @Override
       protected URLConnection openConnection(URL u) throws IOException {
         return connection;
       }            
     };
     URL url = new URL("foo", "bar", 99, "/foobar", stubUrlHandler);
     Map<String,String> headers = new HashMap<String,String>();
     headers.put("RANDOM","VALUE");
     URLConnection returnedConnection = handler.openUrlConnection(url,headers);
     
     Assert.assertEquals(connection, returnedConnection);
     
     Mockito.verify(connection,Mockito.times(1)).setConnectTimeout(Mockito.anyInt());
     Mockito.verify(connection,Mockito.times(1)).setRequestProperty("RANDOM","VALUE" );
     Mockito.verify(connection,Mockito.times(1)).setRequestProperty("user-agent",UrlHandler_Impl.DEFAULT_USER_AGENT  );
   }
  
	@Test  
  	public void openUrlConnectionNullHeadersTest() throws IOException{
     final URLConnection connection = Mockito.mock(URLConnection.class);
     URLStreamHandler stubUrlHandler = new URLStreamHandler() {
       @Override
       protected URLConnection openConnection(URL u) throws IOException {
         return connection;
       }            
     };
     URL url = new URL("foo", "bar", 99, "/foobar", stubUrlHandler);

     URLConnection returnedConnection = handler.openUrlConnection(url,null);
     
     Assert.assertEquals(connection, returnedConnection);
     Mockito.verify(connection,Mockito.times(1)).setRequestProperty("user-agent", UrlHandler_Impl.DEFAULT_USER_AGENT );
   }
  
	@Test
  	public void openInputNoHeadersTest() throws IOException{
     final URLConnection connection = Mockito.mock(URLConnection.class);
     URLStreamHandler stubUrlHandler = new URLStreamHandler() {
       @Override
       protected URLConnection openConnection(URL u) throws IOException {
         return connection;
       }            
     };
     URL url = new URL("foo", "bar", 99, "/foobar", stubUrlHandler);
     Mockito.when(connection.getContentEncoding()).thenReturn(null);
     InputStream input = Mockito.mock(InputStream.class);
     Mockito.when(connection.getInputStream()).thenReturn(input);

     InputStream returnedInput = handler.openInput(url);
     Assert.assertEquals(input, returnedInput); 
     
     Mockito.verify(connection,Mockito.times(1)).setConnectTimeout(Mockito.anyInt());
     Mockito.verify(connection,Mockito.times(1)).setRequestProperty("user-agent", UrlHandler_Impl.DEFAULT_USER_AGENT );

   }
  
	@Test
  	public void openInputNullHeadersTest() throws IOException{
     final URLConnection connection = Mockito.mock(URLConnection.class);
     URLStreamHandler stubUrlHandler = new URLStreamHandler() {
       @Override
       protected URLConnection openConnection(URL u) throws IOException {
         return connection;
       }            
     };
     URL url = new URL("foo", "bar", 99, "/foobar", stubUrlHandler);
     Mockito.when(connection.getContentEncoding()).thenReturn(null);
     InputStream input = Mockito.mock(InputStream.class);
     Mockito.when(connection.getInputStream()).thenReturn(input);

     InputStream returnedInput = handler.openInput(url,null);
     Assert.assertEquals(input, returnedInput); 
     
     Mockito.verify(connection,Mockito.times(1)).setConnectTimeout(Mockito.anyInt());
     Mockito.verify(connection,Mockito.times(1)).setRequestProperty("user-agent", UrlHandler_Impl.DEFAULT_USER_AGENT );
   }
  

	@Test
  	public void openInputEmptyHeadersTest() throws IOException{
     final URLConnection connection = Mockito.mock(URLConnection.class);
     URLStreamHandler stubUrlHandler = new URLStreamHandler() {
       @Override
       protected URLConnection openConnection(URL u) throws IOException {
         return connection;
       }            
     };
     URL url = new URL("foo", "bar", 99, "/foobar", stubUrlHandler);
     Mockito.when(connection.getContentEncoding()).thenReturn(null);
     InputStream input = Mockito.mock(InputStream.class);
     Mockito.when(connection.getInputStream()).thenReturn(input);

     Map<String,String> headers = new HashMap<String,String>();
     
     InputStream returnedInput = handler.openInput(url,headers);
     Assert.assertEquals(input, returnedInput); 
     
     Mockito.verify(connection,Mockito.times(1)).setConnectTimeout(Mockito.anyInt());
     Mockito.verify(connection,Mockito.times(1)).setRequestProperty("user-agent", UrlHandler_Impl.DEFAULT_USER_AGENT );
   }
  
	@Test
  	public void openInputUserAgentHeadersTest() throws IOException{
     final URLConnection connection = Mockito.mock(URLConnection.class);
     URLStreamHandler stubUrlHandler = new URLStreamHandler() {
       @Override
       protected URLConnection openConnection(URL u) throws IOException {
         return connection;
       }            
     };
     URL url = new URL("foo", "bar", 99, "/foobar", stubUrlHandler);
     Mockito.when(connection.getContentEncoding()).thenReturn(null);
     InputStream input = Mockito.mock(InputStream.class);
     Mockito.when(connection.getInputStream()).thenReturn(input);

     Map<String,String> headers = new HashMap<String,String>();
     headers.put("user-agent","USERAGENTVALUE");
     
     InputStream returnedInput = handler.openInput(url,headers);
     Assert.assertEquals(input, returnedInput); 
     
     Mockito.verify(connection,Mockito.times(1)).setConnectTimeout(Mockito.anyInt());
     Mockito.verify(connection,Mockito.times(1)).setRequestProperty("user-agent", "USERAGENTVALUE" );
   }
  
	@Test
  	public void openInputNoUserAgentHeadersTest() throws IOException{
     final URLConnection connection = Mockito.mock(URLConnection.class);
     URLStreamHandler stubUrlHandler = new URLStreamHandler() {
       @Override
       protected URLConnection openConnection(URL u) throws IOException {
         return connection;
       }            
     };
     URL url = new URL("foo", "bar", 99, "/foobar", stubUrlHandler);
     Mockito.when(connection.getContentEncoding()).thenReturn(null);
     InputStream input = Mockito.mock(InputStream.class);
     Mockito.when(connection.getInputStream()).thenReturn(input);

     Map<String,String> headers = new HashMap<String,String>();
     headers.put("RANDOM","VALUE");
     
     InputStream returnedInput = handler.openInput(url,headers);
     Assert.assertEquals(input, returnedInput); 
     
     Mockito.verify(connection,Mockito.times(1)).setConnectTimeout(Mockito.anyInt());
     Mockito.verify(connection,Mockito.times(1)).setRequestProperty("RANDOM", "VALUE" );
     Mockito.verify(connection,Mockito.times(1)).setRequestProperty("user-agent", UrlHandler_Impl.DEFAULT_USER_AGENT );

   }
  
  	@Test
  	public void openOutputNotFileProtocolTest() throws IOException{
     final URLConnection connection = Mockito.mock(URLConnection.class);
     URLStreamHandler stubUrlHandler = new URLStreamHandler() {
       @Override
       protected URLConnection openConnection(URL u) throws IOException {
         return connection;
       }            
     };
     URL url = new URL("foo", "bar", 99, "/foobar", stubUrlHandler);
     Mockito.when(connection.getContentEncoding()).thenReturn(null);
     OutputStream output = Mockito.mock(OutputStream.class);
     Mockito.when(connection.getOutputStream()).thenReturn(output);
     
     OutputStream returnedOutput = handler.openOutput(url);
     Assert.assertTrue(returnedOutput instanceof BufferedOutputStream); 
     
     Mockito.verify(connection,Mockito.times(1)).setConnectTimeout(Mockito.anyInt());
     Mockito.verify(connection,Mockito.times(1)).setRequestProperty("user-agent", UrlHandler_Impl.DEFAULT_USER_AGENT );
   }
  
  	@Test
  	public void openOutputFileProtocolTest() throws IOException{
     	URL url = ClassLoader.getSystemClassLoader().getResource("log4j2.xml");
     	OutputStream returnedOutput = handler.openOutput(url);
		Assert.assertTrue(returnedOutput instanceof BufferedOutputStream);
     	returnedOutput.close();
   }
  
  	@Test
  	public void openOutputFileNotExistsNoFolderTest()throws Exception{
     URL url = new URL("file:tmp.out");
     OutputStream returnedOutput = handler.openOutput(url);
     Assert.assertTrue(returnedOutput instanceof BufferedOutputStream);
     returnedOutput.close();
     File file = new File(url.getFile());
     file.delete();
   }
  
  	@Test
  	public void openOutputFileNotExistsWithFolderTest()throws Exception{
     URL url = new URL("file:folder/tmp.out");
     OutputStream returnedOutput = handler.openOutput(url);
     Assert.assertTrue(returnedOutput instanceof BufferedOutputStream);
     returnedOutput.close();
     File file = new File(url.getFile());
     file.delete();
     file.getParentFile().delete();
   }
}
