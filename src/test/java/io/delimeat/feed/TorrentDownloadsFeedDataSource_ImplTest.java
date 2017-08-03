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


public class TorrentDownloadsFeedDataSource_ImplTest {
	
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);
  
	private TorrentDownloadsFeedDataSource_Impl dataSource;

	@Before
	public void setUp() throws URISyntaxException {
		dataSource = new TorrentDownloadsFeedDataSource_Impl();
	}

	@Test
	public void feedSourceTest() throws Exception {
		Assert.assertEquals(FeedSource.TORRENTDOWNLOADS, dataSource.getFeedSource());
	}
  
	@Test
	public void readTest() throws Exception{
    	
     	String responseBody = "<?xml version='1.0' encoding='UTF-8'?>"
     			+ "<rss><channel><item>"
     			+ "<title><![CDATA[title]]></title>"
     			+ "<info_hash>INFO_HASH</info_hash>"
     			+ "<size>9223372036854775807</size>"
     			+ "<seeders>1</seeders>"
     			+ "<leechers>1000</leechers>"
     			+ "</item></channel></rss>";
     
		stubFor(get(urlPathEqualTo("/rss.xml"))
				.withQueryParam("type", equalTo("search"))
				.withQueryParam("search", equalTo("title"))
				.withHeader("Accept", equalTo("text/html"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "text/html")
							.withBody(responseBody)));


		dataSource.setBaseUri("http://localhost:8089");
		
		List<FeedResult> results = dataSource.read("title");
     	Assert.assertNotNull(results);
     	Assert.assertEquals(1, results.size());
     	Assert.assertEquals("title",results.get(0).getTitle());
     	Assert.assertEquals("http://itorrents.org/torrent/INFO_HASH.torrent",results.get(0).getTorrentURL());
     	Assert.assertEquals(Long.MAX_VALUE,results.get(0).getContentLength());
     	Assert.assertEquals(1, results.get(0).getSeeders());
     	Assert.assertEquals(1000, results.get(0).getLeechers());

	}
  
	@Test(expected=FeedException.class)
	public void readWebAppExceptionTest() throws Exception {

		stubFor(get(urlPathEqualTo("/rss.xml"))
				.withQueryParam("type", equalTo("search"))
				.withQueryParam("search", equalTo("title"))
				.withHeader("Accept", equalTo("text/html"))
				.willReturn(aResponse()
							.withStatus(500)
							.withHeader("Content-Type","text/html")));


		dataSource.setBaseUri("http://localhost:8089");
		
		dataSource.read("title");
		Assert.fail();
	}
  
	@Test(expected=FeedException.class)
	public void readProcessingExceptionTest() throws Exception {

		stubFor(get(urlPathEqualTo("/rss.xml"))
				.withQueryParam("type", equalTo("search"))
				.withQueryParam("search", equalTo("title"))
				.withHeader("Accept", equalTo("text/html"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type","text/html")
                     .withFixedDelay(2000)));


		dataSource.setBaseUri("http://localhost:8089");
		
		dataSource.read("title");
		Assert.fail();
	}
}
