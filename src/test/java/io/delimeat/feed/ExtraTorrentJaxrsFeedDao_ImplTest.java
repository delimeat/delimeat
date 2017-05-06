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
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import java.net.URI;
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

public class ExtraTorrentJaxrsFeedDao_ImplTest {
	
	private class ItemEntityGenerator {

		private StringBuffer xml;

		public ItemEntityGenerator() {
			xml = new StringBuffer();
        	xml.append("<?xml version='1.0' encoding='UTF-8'?>");
			xml.append("<rss><channel>");
		}
     	public void addItem(String title, String torrentUrl, long length,long seeders, long leechers){
			xml.append("<item>");
        	xml.append("<title><![CDATA["+title+"]]></title>");
			xml.append("<enclosure url='"+torrentUrl+"' length='"+length+"' type='application/x-bittorrent' />");
        	xml.append("<seeders>"+ seeders + "</seeders>");
        	xml.append("<leechers>" + leechers + "</leechers>");			
			xml.append("</item>");        

      }

		public String toString() {
			return xml.toString() + "</channel></rss>";
		}

	}

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);
  
	private ExtraTorrentJaxrsFeedDao_Impl dao;
  
	@Before
	public void setUp() throws URISyntaxException {
		dao = new ExtraTorrentJaxrsFeedDao_Impl(new URI("http://localhost:8089"));
	}

	@Test
	public void feedSourceTest() throws Exception {
		Assert.assertEquals(FeedSource.EXTRATORRENT, dao.getFeedSource());
	}
  
	@Test
	public void readTest() throws Exception{
		ItemEntityGenerator response = new ItemEntityGenerator();
     	response.addItem("title", "torrentUrl", Long.MAX_VALUE, 1, 1000);
     
		stubFor(get(urlEqualTo("/?type=search&search=title"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "application/xml")
							.withBody(response.toString())));
		
		List<FeedResult> results = dao.read("title");
     	Assert.assertNotNull(results);
     	Assert.assertEquals(1, results.size());
     	Assert.assertEquals("title",results.get(0).getTitle());
     	Assert.assertEquals("torrentUrl",results.get(0).getTorrentURL());
     	Assert.assertEquals(Long.MAX_VALUE,results.get(0).getContentLength());
     	Assert.assertEquals(1, results.get(0).getSeeders());
     	Assert.assertEquals(1000, results.get(0).getLeechers());

	}
  
	@Test(expected=FeedException.class)
	public void readWebAppExceptionTest() throws Exception {

		stubFor(get(urlEqualTo("/?type=search&search=title"))
				.willReturn(aResponse()
							.withStatus(500)
							.withHeader("Content-Type","application/xml")));


		dao.read("title");
		Assert.fail();
	}
  
	@Test(expected=FeedException.class)
	public void readProcessingExceptionTest() throws Exception {

		stubFor(get(urlEqualTo("/?type=search&search=title"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type","application/xml")
                     .withFixedDelay(2000)));

		dao.read("title");
		Assert.fail();
	}
}
