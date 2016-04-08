package io.delimeat.core.processor.writer;

import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedException;
import io.delimeat.core.processor.writer.TorrentFileWriter_Impl;
import io.delimeat.util.UrlHandler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TorrentFileWriter_ImplTest {

	private TorrentFileWriter_Impl writer;
	
	@Before
	public void setUp(){
		writer = new TorrentFileWriter_Impl();
	}
	
	@Test
	public void urlHandlerTest(){
		Assert.assertNull(writer.getUrlHandler());
		UrlHandler mockedHandler=mock(UrlHandler.class);
		writer.setUrlHandler(mockedHandler);
		Assert.assertEquals(mockedHandler, writer.getUrlHandler());
	}
	
	@Test
	public void writeOutputDirTest() throws IOException, FeedException{
		Config config = new Config();
		config.setOutputDirectory("OUTPUT");
		
		UrlHandler handler = Mockito.mock(UrlHandler.class);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Mockito.when(handler.openOutput(new URL("file:OUTPUT/FILENAME"))).thenReturn(baos);
		writer.setUrlHandler(handler);
		
		writer.write("FILENAME", "BYTES".getBytes(), config);
		
		Assert.assertEquals("BYTES", baos.toString());
	}
  
	@Test(expected=FeedException.class)
	public void writeIOExceptionTest() throws IOException, FeedException{
		Config config = new Config();
		config.setOutputDirectory("OUTPUT");
		
		UrlHandler handler = Mockito.mock(UrlHandler.class);
		Mockito.when(handler.openOutput(new URL("file:OUTPUT/FILENAME"))).thenThrow(new IOException());
		writer.setUrlHandler(handler);
		
		writer.write("FILENAME", "BYTES".getBytes(), config);
	}
	
	@Test
	public void writeNullOutputDirTest() throws IOException, FeedException{
		Config config = new Config();
		config.setOutputDirectory(null);
		
		UrlHandler handler = Mockito.mock(UrlHandler.class);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String dir = System.getProperty("user.home");
		Mockito.when(handler.openOutput(new URL("file:"+dir+"/FILENAME"))).thenReturn(baos);
		writer.setUrlHandler(handler);
		
		writer.write("FILENAME", "BYTES".getBytes(), config);
		
		Assert.assertEquals("BYTES", baos.toString());
	}
	
	@Test
	public void writeEmptyOutputDirTest() throws IOException, FeedException{
		Config config = new Config();
		config.setOutputDirectory("");
		
		UrlHandler handler = Mockito.mock(UrlHandler.class);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String dir = System.getProperty("user.home");
		Mockito.when(handler.openOutput(new URL("file:"+dir+"/FILENAME"))).thenReturn(baos);
		writer.setUrlHandler(handler);
		
		writer.write("FILENAME", "BYTES".getBytes(), config);
		
		Assert.assertEquals("BYTES", baos.toString());
	}
	
}
