package io.delimeat.core.feed;

import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import io.delimeat.core.config.Config;
import io.delimeat.core.torrent.Torrent;
import io.delimeat.core.torrent.TorrentInfo;
import io.delimeat.util.UrlHandler;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class FeedResultFileWriter_ImplTest {

	private FeedResultFileWriter_Impl writer;
	
	@Before
	public void setUp(){
		writer = new FeedResultFileWriter_Impl();
	}
	
	@Test
	public void urlHandlerTest(){
		Assert.assertNull(writer.getUrlHandler());
		UrlHandler mockedHandler=mock(UrlHandler.class);
		writer.setUrlHandler(mockedHandler);
		Assert.assertEquals(mockedHandler, writer.getUrlHandler());
	}
	
	@Test
	public void writeTest() throws IOException, FeedException{
		Torrent torrent = new Torrent();
		TorrentInfo info = new TorrentInfo();
		info.setName("FILE_NAME");
		torrent.setInfo(info);
		torrent.setBytes("BYTES".getBytes());
		
		Config config = new Config();
		config.setOutputDirectory("OUTPUT");
		
		UrlHandler handler = Mockito.mock(UrlHandler.class);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Mockito.when(handler.openOutput(Mockito.any(URL.class))).thenReturn(baos);
		writer.setUrlHandler(handler);
		
		writer.write(torrent, config);
		
		Assert.assertEquals("BYTES", baos.toString());
		
	}
}
