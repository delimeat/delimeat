/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.delimeat.torrent;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import io.delimeat.torrent.domain.Torrent;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.TorrentNotFoundException;
import io.delimeat.torrent.exception.TorrentResponseBodyException;
import io.delimeat.torrent.exception.TorrentResponseException;
import io.delimeat.torrent.exception.TorrentTimeoutException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.mockwebserver.SocketPolicy;
import okio.Buffer;

public class MagnetTorrentReader_ImplTest {

	private static final int PORT = 8089;
	private static MockWebServer mockedServer = new MockWebServer();
	
	private String sep = System.getProperty("file.separator");
	private MagnetTorrentReader_Impl reader;
	
	@BeforeClass
	public static void beforeClass() throws IOException{
		mockedServer.start(PORT);
	}
	
	@AfterClass
	public static void tearDown() throws IOException{
		mockedServer.shutdown();
	}
	
	@Before
	public void setUp(){
		reader = new MagnetTorrentReader_Impl();
	}
	
	@Test
	public void supportedProtocolTest(){
		Assert.assertEquals(Arrays.asList("MAGNET"),reader.getSupportedProtocols());
	}
	
	@Test
	public void downloadUriTemplateTest(){
		Assert.assertNull(reader.getDownloadUriTemplate());
		reader.setDownloadUriTemplate("template");
		Assert.assertEquals("template", reader.getDownloadUriTemplate());
	}
	
	@Test
	public void readTest() throws Exception {
		String bytesVal = "d8:announce9:TRACKER_113:announce-listll11:1_tracker_111:1_tracker_2el11:2_tracker_111:2_tracker_2ee4:infod5:filesld6:lengthi1234e4:pathl8:1_part_111:1_file_nameeed6:lengthi56789e4:pathl8:2_part_111:2_file_nameeee6:lengthi987654321e4:name4:NAMEee";
		
		Buffer buffer = new Buffer();
		buffer.write(bytesVal.getBytes());
		
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(200)
			    .addHeader("Content-Type", "application/x-bittorrent")
			    .setBody(buffer);
		
		mockedServer.enqueue(mockResponse);
		
		reader.setDownloadUriTemplate("http://localhost:8089/%s");
		
		Torrent torrent = reader.read(new URI("magnet:?xt=urn:btih:df706cf16f45e8c0fd226223509c7e97b4ffec13&tr=udp://tracker.coppersurfer.tk:6969/announce"));
		
		RecordedRequest request = mockedServer.takeRequest();
		Assert.assertEquals("/DF706CF16F45E8C0FD226223509C7E97B4FFEC13", request.getPath());
		Assert.assertEquals("application/x-bittorrent", request.getHeader("Accept"));
		Assert.assertEquals("http://localhost:8089/DF706CF16F45E8C0FD226223509C7E97B4FFEC13", request.getHeader("Referer"));
		
		Assert.assertEquals("TRACKER_1", torrent.getTracker());
		Assert.assertEquals(4, torrent.getTrackers().size());
		Assert.assertEquals("1_tracker_1", torrent.getTrackers().get(0));
		Assert.assertEquals("1_tracker_2", torrent.getTrackers().get(1));
		Assert.assertEquals("2_tracker_1", torrent.getTrackers().get(2));
		Assert.assertEquals("2_tracker_2", torrent.getTrackers().get(3));
		Assert.assertEquals(987654321L, torrent.getInfo().getLength());
		Assert.assertEquals("NAME", torrent.getInfo().getName());
		Assert.assertEquals(2, torrent.getInfo().getFiles().size());
		Assert.assertEquals("1_part_1" + sep + "1_file_name", torrent.getInfo().getFiles().get(0).getName());
		Assert.assertEquals(1234, torrent.getInfo().getFiles().get(0).getLength());
		Assert.assertEquals("2_part_1" + sep + "2_file_name", torrent.getInfo().getFiles().get(1).getName());
		Assert.assertEquals(56789, torrent.getInfo().getFiles().get(1).getLength());
		Assert.assertEquals("ab835ef1b726e2aa4d1c6df6b91278d651b228a7", torrent.getInfo().getInfoHash().getHex());
		Assert.assertEquals(bytesVal, new String(torrent.getBytes()));	
	}

	@Test
	public void readTimeoutExceptionTest() throws Exception {	
		
		MockResponse mockResponse = new MockResponse()
				.setSocketPolicy(SocketPolicy.NO_RESPONSE);
		
		mockedServer.enqueue(mockResponse);

		reader.setDownloadUriTemplate("http://localhost:8089/%s");

		try{
			reader.read(new URI("magnet:?xt=urn:btih:df706cf16f45e8c0fd226223509c7e97b4ffec13&tr=udp://tracker.coppersurfer.tk:6969/announce"));
		}catch(TorrentTimeoutException ex){
			RecordedRequest request = mockedServer.takeRequest();
			Assert.assertEquals("/DF706CF16F45E8C0FD226223509C7E97B4FFEC13", request.getPath());
			Assert.assertEquals("application/x-bittorrent", request.getHeader("Accept"));
			Assert.assertEquals("http://localhost:8089/DF706CF16F45E8C0FD226223509C7E97B4FFEC13", request.getHeader("Referer"));
			
			return;
		}
		Assert.fail();
	}

  	@Test
	public void readNotFoundExceptionTest() throws Exception {
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(404)
			    .addHeader("Content-Type", "application/x-bittorrent");
		
		mockedServer.enqueue(mockResponse);
		
		reader.setDownloadUriTemplate("http://localhost:8089/%s");

		try{
			reader.read(new URI("magnet:?xt=urn:btih:df706cf16f45e8c0fd226223509c7e97b4ffec13&tr=udp://tracker.coppersurfer.tk:6969/announce"));
		}catch(TorrentNotFoundException ex){
			RecordedRequest request = mockedServer.takeRequest();
			Assert.assertEquals("/DF706CF16F45E8C0FD226223509C7E97B4FFEC13", request.getPath());
			Assert.assertEquals("application/x-bittorrent", request.getHeader("Accept"));
			Assert.assertEquals("http://localhost:8089/DF706CF16F45E8C0FD226223509C7E97B4FFEC13", request.getHeader("Referer"));
			
			return;
		}
		Assert.fail();
	}
  	
  	@Test
	public void readResponseExceptionTest() throws Exception {
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(500)
			    .addHeader("Content-Type", "application/x-bittorrent");
		
		mockedServer.enqueue(mockResponse);
		
		reader.setDownloadUriTemplate("http://localhost:8089/%s");

		try{
			reader.read(new URI("magnet:?xt=urn:btih:df706cf16f45e8c0fd226223509c7e97b4ffec13&tr=udp://tracker.coppersurfer.tk:6969/announce"));
		}catch(TorrentResponseException ex){
			RecordedRequest request = mockedServer.takeRequest();
			Assert.assertEquals("/DF706CF16F45E8C0FD226223509C7E97B4FFEC13", request.getPath());
			Assert.assertEquals("application/x-bittorrent", request.getHeader("Accept"));
			Assert.assertEquals("http://localhost:8089/DF706CF16F45E8C0FD226223509C7E97B4FFEC13", request.getHeader("Referer"));
			
			return;
		}
		Assert.fail();
	}

  	@Test
	public void readResponseBodyExceptionTest() throws Exception {
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(200)
			    .addHeader("Content-Type", "application/x-bittorrent")
			    .setBody("X");
		
		mockedServer.enqueue(mockResponse);
		
		reader.setDownloadUriTemplate("http://localhost:8089/%s");

		try{
			reader.read(new URI("magnet:?xt=urn:btih:df706cf16f45e8c0fd226223509c7e97b4ffec13&tr=udp://tracker.coppersurfer.tk:6969/announce"));
		}catch(TorrentResponseBodyException ex){
			RecordedRequest request = mockedServer.takeRequest();
			Assert.assertEquals("/DF706CF16F45E8C0FD226223509C7E97B4FFEC13", request.getPath());
			Assert.assertEquals("application/x-bittorrent", request.getHeader("Accept"));
			Assert.assertEquals("http://localhost:8089/DF706CF16F45E8C0FD226223509C7E97B4FFEC13", request.getHeader("Referer"));
			
			return;
		}
		Assert.fail();
	}
  
	@Test(expected = TorrentException.class)
	public void readUnsupportedProtocalTest() throws Exception {
		reader.read(new URI("udp://read.com:8080"));
	}
}
