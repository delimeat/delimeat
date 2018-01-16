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
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.WireMockServer;

import io.delimeat.feed.entity.FeedResult;
import io.delimeat.feed.entity.FeedSource;

public class SkyTorrentsDataSource_ImplIT {

	private static WireMockServer server = new WireMockServer(8089);
	
	private SkyTorrentsDataSource_Impl client;
	
	@BeforeAll
	public static void setUpClass() {
		server.start();
	}
	
	@AfterAll
	public static void tearDownClass() {
		server.stop();
	}
	
	@BeforeEach
	public void setUp() throws URISyntaxException {
		client = new SkyTorrentsDataSource_Impl();		
		client.setBaseUri(new URI("http://localhost:8089"));		
	}
	
	@Test
    public void readTest() throws Exception {
		
		String responseBody = "<?xml version='1.0' encoding='UTF-8'?>" 
				+ "<rss><channel><item>"
				+ "<title><![CDATA[title]]></title>" 
				+ "<link><![CDATA[torrentUrl]]></link>"
				+ "<description><![CDATA[2 Seeders, 3 Leechers 168 MB]]></description>" 
				+ "</item></channel></rss>";
		
		server.stubFor(get(urlEqualTo("/rss/all/ad/1/TITLE"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/xml")
						.withBody(responseBody)));

		List<FeedResult> results = client.read("TITLE");

		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals(FeedSource.SKYTORRENTS, results.get(0).getSource());
		Assertions.assertEquals("title", results.get(0).getTitle());
		Assertions.assertEquals("torrentUrl", results.get(0).getTorrentURL());
		Assertions.assertNull(results.get(0).getInfoHashHex());
		Assertions.assertEquals(0, results.get(0).getContentLength());
		Assertions.assertEquals("title", results.get(0).getTitle());
		Assertions.assertEquals(0, results.get(0).getSeeders());
		Assertions.assertEquals(0, results.get(0).getLeechers());
		
	}
}
