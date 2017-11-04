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
package io.delimeat.feed;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import io.delimeat.feed.entity.FeedResult;
import io.delimeat.feed.entity.FeedSource;
import io.delimeat.feed.exception.FeedContentTypeException;
import io.delimeat.feed.exception.FeedException;
import io.delimeat.feed.exception.FeedResponseBodyException;
import io.delimeat.feed.exception.FeedResponseException;
import io.delimeat.feed.exception.FeedTimeoutException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okhttp3.mockwebserver.SocketPolicy;

public class SkyTorrentsFeedDataSource_ImplTest {

	private static final int PORT = 8089;
	private static MockWebServer mockedServer = new MockWebServer();
  
	private SkyTorrentsFeedDataSource_Impl dataSource;
  
	@BeforeClass
	public static void beforeClass() throws IOException{
		mockedServer.start(PORT);
	}
	
	@AfterClass
	public static void tearDown() throws IOException{
		mockedServer.shutdown();
	}
	
	@Before
	public void setUp() throws URISyntaxException {
		dataSource = new SkyTorrentsFeedDataSource_Impl();
	}

	@Test
	public void feedSourceTest() throws Exception {
		Assert.assertEquals(FeedSource.SKYTORRENTS, dataSource.getFeedSource());
	}
	
	@Test
	public void baseUriTest(){
		Assert.assertNull(dataSource.getBaseUri());
		dataSource.setBaseUri("http://localhost:8089");
		Assert.assertEquals("http://localhost:8089", dataSource.getBaseUri());
	}
	
	@Test
	public void toStringTest(){
		Assert.assertEquals("SkyTorrentsFeedDataSource_Impl [feedSource=SKYTORRENTS, properties={eclipselink.json.include-root=false, eclipselink.oxm.metadata-source=oxm/feed-skytorrents-oxm.xml, eclipselink.media-type=application/xml}, headers{Accept=text/xml}]", dataSource.toString());
	}
	
	@Test
	public void readTest() throws Exception{    	
     	String responseBody = "<?xml version='1.0' encoding='UTF-8'?>"
     			+ "<rss><channel><item>"
     			+ "<title><![CDATA[title]]></title>"
     			+ "<link><![CDATA[torrentUrl]]></link>"
     			+ "<description><![CDATA[2 Seeders, 3 Leechers 168 MB]]></description>"
     			+ "</item></channel></rss>";
     
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(200)
			    .addHeader("Content-Type", "text/xml")
			    .setBody(responseBody);
		
		mockedServer.enqueue(mockResponse);

		dataSource.setBaseUri("http://localhost:8089");
		
		List<FeedResult> results = dataSource.read("title");
		RecordedRequest request = mockedServer.takeRequest();
		Assert.assertEquals("/rss/all/ad/1/title", request.getPath());
		Assert.assertEquals("text/xml", request.getHeader("Accept"));
		
     	Assert.assertNotNull(results);
     	Assert.assertEquals(1, results.size());
     	Assert.assertEquals(FeedSource.SKYTORRENTS, results.get(0).getSource());
     	Assert.assertEquals("title",results.get(0).getTitle());
     	Assert.assertEquals("torrentUrl",results.get(0).getTorrentURL());
	}
  
	@Test
	public void readResponseExceptionTest() throws Exception {

		MockResponse mockResponse = new MockResponse()
				.setResponseCode(500);
		
		mockedServer.enqueue(mockResponse);

		dataSource.setBaseUri("http://localhost:8089");
		
		try{
			dataSource.read("title");
		} catch(FeedResponseException ex){
			RecordedRequest request = mockedServer.takeRequest();
			Assert.assertEquals("/rss/all/ad/1/title", request.getPath());
			Assert.assertEquals("text/xml", request.getHeader("Accept"));
			return;
		}
		Assert.fail();
	}
	
	@Test
	public void readContentTypeExceptionTest() throws Exception {
     	String responseBody = "<?xml version='1.0' encoding='UTF-8'?>"
     			+ "<rss><channel><item>"
     			+ "<title><![CDATA[title]]></title>"
     			+ "<link><![CDATA[magnet:?xt=urn:btih:ABCDEFGHIJKLMNOPQRSTUVWZY1234567890INFO&tr=udp://tracker.coppersurfer.tk:6969/announce]]></link>"
     			+ "<description><![CDATA[2 Seeders, 3 Leechers 168 MB]]></description>"
     			+ "</item></channel></rss>";
     	
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(200)
			    .addHeader("Content-Type", "application/json")
			    .setBody(responseBody);
		
		mockedServer.enqueue(mockResponse);

		dataSource.setBaseUri("http://localhost:8089");
		
		try{
			dataSource.read("title");
		} catch(FeedContentTypeException ex){
			RecordedRequest request = mockedServer.takeRequest();
			Assert.assertEquals("/rss/all/ad/1/title", request.getPath());
			Assert.assertEquals("text/xml", request.getHeader("Accept"));
			return;
		}
	}
	
	@Test
	public void readTimeoutExceptionTest() throws Exception {
     	
		MockResponse mockResponse = new MockResponse()
			    .setSocketPolicy(SocketPolicy.NO_RESPONSE);
		
		mockedServer.enqueue(mockResponse);

		dataSource.setBaseUri("http://localhost:8089");
		
		try{
			dataSource.read("title");
		}catch(FeedTimeoutException ex){
			RecordedRequest request = mockedServer.takeRequest();
			Assert.assertEquals("/rss/all/ad/1/title", request.getPath());
			Assert.assertEquals("text/xml", request.getHeader("Accept"));
			return;
		}
		Assert.fail();
	}
  
	@Test
	public void readResponseBodyExceptionTest() throws Exception {
		
		MockResponse mockResponse = new MockResponse()
				.setResponseCode(200)
			    .addHeader("Content-Type", "text/xml")
			    .setBody("X");
		
		mockedServer.enqueue(mockResponse);

		dataSource.setBaseUri("http://localhost:8089");
		
		try{
			dataSource.read("title");
		} catch(FeedResponseBodyException ex){
			RecordedRequest request = mockedServer.takeRequest();
			Assert.assertEquals("/rss/all/ad/1/title", request.getPath());
			Assert.assertEquals("text/xml", request.getHeader("Accept"));
			return;
		}
		Assert.fail();
	}
	
	@Test
	public void readProcessingExceptionTest() throws Exception {

		dataSource.setBaseUri("JIBBERISH");
		
		try{
			dataSource.read("title");
		}catch(FeedException ex){
			return;
		}
		Assert.fail();
	}
}
