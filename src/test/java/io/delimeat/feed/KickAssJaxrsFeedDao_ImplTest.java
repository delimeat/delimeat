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
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedSource;
import io.delimeat.feed.exception.FeedException;

public class KickAssJaxrsFeedDao_ImplTest {

	private class ResultJsonGenerator {
		
		private StringBuffer xml;
		private boolean isFirst = true;
	
		public ResultJsonGenerator() {
			xml = new StringBuffer();
			xml.append("{\"list\": [");

		}

		public void addResult(String title, Integer length, Integer seeders, Integer leechers, String torrentUrl) {
			if(isFirst == false){
				xml.append(",");				
			}
			xml.append("{");
			xml.append("\"title\":\"" + title + "\",");
			xml.append("\"size\":" + length + ",");
			xml.append("\"seeds\":\"" + seeders + "\",");
			xml.append("\"leechs\":\"" + leechers + "\",");
			xml.append("\"torrentLink\":\"" + torrentUrl + "\"");
			xml.append("}");
			isFirst = false;
		}

		public String toString() {
			return xml.toString() + "]}";
		}
	}

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);
  
	private KickAssJaxrsFeedDao_Impl dao;

  
	@Before
	public void setUp() throws Exception {
		dao = new KickAssJaxrsFeedDao_Impl(new URI("http://localhost:8089"));
	}

	@Test
	public void feedSourceTest() throws Exception {
		Assert.assertEquals(FeedSource.KAT, dao.getFeedSource());
	}

	@Test
	public void readTest() throws Exception {
		ResultJsonGenerator response = new ResultJsonGenerator();
		response.addResult("title", 100, 50, 30, "http://test.com");
     
		stubFor(get(urlEqualTo("/?q=title+category%3Atv"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "application/json")
							.withBody(response.toString())));
				
		List<FeedResult> results = dao.read("title");
     	Assert.assertNotNull(results);
     	Assert.assertEquals(1, results.size());
     	Assert.assertEquals("title",results.get(0).getTitle());
     	Assert.assertEquals("http://test.com",results.get(0).getTorrentURL());
     	Assert.assertEquals(100,results.get(0).getContentLength());
     	Assert.assertEquals(50,results.get(0).getSeeders());
     	Assert.assertEquals(30,results.get(0).getLeechers());

	}
  
	@Test(expected=FeedException.class)
	public void readWebAppExceptionTest() throws Exception {

		stubFor(get(urlEqualTo("/?q=title+category%3Atv"))
				.willReturn(aResponse()
							.withStatus(500)
							.withHeader("Content-Type","application/json")));

		dao.read("title");
		Assert.fail();
	}
  
	@Test(expected=FeedException.class)
	public void readProcessingExceptionTest() throws Exception {

		stubFor(get(urlEqualTo("/?q=title+category%3Atv"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type","application/json")
                     .withFixedDelay(2000)));


		dao.read("title");
		Assert.fail();
	}

}
