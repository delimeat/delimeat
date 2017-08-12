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

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import java.net.URISyntaxException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedSource;
import io.delimeat.feed.exception.FeedException;

public class SkyTorrentsFeedDataSource_ImplTest {

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);
  
	private SkyTorrentsFeedDataSource_Impl dataSource;
  
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
     
		stubFor(get(urlPathEqualTo("/rss/all/ad/1/title"))
				.withHeader("Accept", equalTo("text/xml"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "text/xml")
							.withBody(responseBody)));

		dataSource.setBaseUri("http://localhost:8089");
		
		List<FeedResult> results = dataSource.read("title");
     	Assert.assertNotNull(results);
     	Assert.assertEquals(1, results.size());
     	Assert.assertEquals("title",results.get(0).getTitle());
     	Assert.assertEquals("torrentUrl",results.get(0).getTorrentURL());
     	//TODO add back when adapter is ready
     	//Assert.assertEquals(Long.MAX_VALUE,results.get(0).getContentLength());

	}
  
	@Test(expected=FeedException.class)
	public void readExceptionTest() throws Exception {

		stubFor(get(urlPathEqualTo("/rss/all/ad/1/title"))
				.withHeader("Accept", equalTo("text/xml"))
				.willReturn(aResponse()
							.withStatus(500)
							.withHeader("Content-Type","text/xml")));

		dataSource.setBaseUri("http://localhost:8089");
		
		dataSource.read("title");
		Assert.fail();
	}
	
	@Test(expected=FeedException.class)
	public void readContentTypeExceptionTest() throws Exception {
     	String responseBody = "<?xml version='1.0' encoding='UTF-8'?>"
     			+ "<rss><channel><item>"
     			+ "<title><![CDATA[title]]></title>"
     			+ "<link><![CDATA[torrentUrl]]></link>"
     			+ "<description><![CDATA[2 Seeders, 3 Leechers 168 MB]]></description>"
     			+ "</item></channel></rss>";
     	
		stubFor(get(urlPathEqualTo("/rss/all/ad/1/title"))
				.withHeader("Accept", equalTo("text/xml"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "application/json")
							.withBody(responseBody)));

		dataSource.setBaseUri("http://localhost:8089");
		
		dataSource.read("title");
		Assert.fail();
	}
  
	@Test(expected=FeedException.class)
	public void readProcessingExceptionTest() throws Exception {

		stubFor(get(urlPathEqualTo("/rss/all/ad/1/title"))
				.withHeader("Accept", equalTo("text/xml"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type","text/xml")
                     .withFixedDelay(2000)));

		dataSource.setBaseUri("http://localhost:8089");
		
		dataSource.read("title");
		Assert.fail();
	}
}
