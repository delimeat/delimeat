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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

public class TorrentDownloadsFeedDataSource_ImplTest {

	private static final int PORT = 8089;
	private static MockWebServer mockedServer = new MockWebServer();

	private TorrentDownloadsFeedDataSource_Impl dataSource;

	@BeforeAll
	public static void beforeClass() throws IOException {
		mockedServer.start(PORT);
	}

	@AfterAll
	public static void tearDown() throws IOException {
		mockedServer.shutdown();
	}

	@BeforeEach
	public void setUp() throws URISyntaxException {
		dataSource = new TorrentDownloadsFeedDataSource_Impl();
	}

	@Test
	public void feedSourceTest() throws Exception {
		Assertions.assertEquals(FeedSource.TORRENTDOWNLOADS, dataSource.getFeedSource());
	}

	@Test
	public void baseUriTest() {
		Assertions.assertNull(dataSource.getBaseUri());
		dataSource.setBaseUri("http://localhost:8089");
		Assertions.assertEquals("http://localhost:8089", dataSource.getBaseUri());
	}

	@Test
	public void toStringTest() {
		Assertions.assertEquals(
				"TorrentDownloadsFeedDataSource_Impl [feedSource=TORRENTDOWNLOADS, properties={eclipselink.json.include-root=false, eclipselink.oxm.metadata-source=oxm/feed-torrentdownloads-oxm.xml, eclipselink.media-type=application/xml}, headers{Accept=text/html}]",
				dataSource.toString());
	}

	@Test
	public void readTest() throws Exception {

		String responseBody = "<?xml version='1.0' encoding='UTF-8'?>" + "<rss><channel><item>"
				+ "<title><![CDATA[title]]></title>" + "<info_hash>INFO_HASH</info_hash>"
				+ "<size>9223372036854775807</size>" + "<seeders>1</seeders>" + "<leechers>1000</leechers>"
				+ "</item></channel></rss>";

		MockResponse mockResponse = new MockResponse().setResponseCode(200).addHeader("Content-Type", "text/html")
				.setBody(responseBody);

		mockedServer.enqueue(mockResponse);

		dataSource.setBaseUri("http://localhost:8089");

		List<FeedResult> results = dataSource.read("title");
		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/rss.xml?type=search&search=title", request.getPath());
		Assertions.assertEquals("text/html", request.getHeader("Accept"));

		Assertions.assertNotNull(results);
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals(FeedSource.TORRENTDOWNLOADS, results.get(0).getSource());
		Assertions.assertEquals("title", results.get(0).getTitle());
		Assertions.assertNull(results.get(0).getTorrentURL());
		Assertions.assertEquals("INFO_HASH", results.get(0).getInfoHashHex());
		Assertions.assertEquals(Long.MAX_VALUE, results.get(0).getContentLength());
		Assertions.assertEquals(1, results.get(0).getSeeders());
		Assertions.assertEquals(1000, results.get(0).getLeechers());

	}

	@Test
	public void readNoSeedersTest() throws Exception {

		String responseBody = "<?xml version='1.0' encoding='UTF-8'?>" + "<rss><channel><item>"
				+ "<title><![CDATA[title]]></title>" + "<info_hash>INFO_HASH</info_hash>"
				+ "<size>9223372036854775807</size>" + "<seeders/>" + "<leechers>1000</leechers>"
				+ "</item></channel></rss>";

		MockResponse mockResponse = new MockResponse().setResponseCode(200).addHeader("Content-Type", "text/html")
				.setBody(responseBody);

		mockedServer.enqueue(mockResponse);

		dataSource.setBaseUri("http://localhost:8089");

		List<FeedResult> results = dataSource.read("title");
		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/rss.xml?type=search&search=title", request.getPath());
		Assertions.assertEquals("text/html", request.getHeader("Accept"));

		Assertions.assertNotNull(results);
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals("title", results.get(0).getTitle());
		Assertions.assertNull(results.get(0).getTorrentURL());
		Assertions.assertEquals("INFO_HASH", results.get(0).getInfoHashHex());
		Assertions.assertEquals(Long.MAX_VALUE, results.get(0).getContentLength());
		Assertions.assertEquals(0, results.get(0).getSeeders());
		Assertions.assertEquals(1000, results.get(0).getLeechers());
	}

	@Test
	public void readNoLeechersTest() throws Exception {

		String responseBody = "<?xml version='1.0' encoding='UTF-8'?>" + "<rss><channel><item>"
				+ "<title><![CDATA[title]]></title>" + "<info_hash>INFO_HASH</info_hash>"
				+ "<size>9223372036854775807</size>" + "<seeders>1</seeders>" + "<leechers/>"
				+ "</item></channel></rss>";

		MockResponse mockResponse = new MockResponse().setResponseCode(200).addHeader("Content-Type", "text/html")
				.setBody(responseBody);

		mockedServer.enqueue(mockResponse);

		dataSource.setBaseUri("http://localhost:8089");

		List<FeedResult> results = dataSource.read("title");
		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/rss.xml?type=search&search=title", request.getPath());
		Assertions.assertEquals("text/html", request.getHeader("Accept"));

		Assertions.assertNotNull(results);
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals("title", results.get(0).getTitle());
		Assertions.assertNull(results.get(0).getTorrentURL());
		Assertions.assertEquals("INFO_HASH", results.get(0).getInfoHashHex());
		Assertions.assertEquals(Long.MAX_VALUE, results.get(0).getContentLength());
		Assertions.assertEquals(1, results.get(0).getSeeders());
		Assertions.assertEquals(0, results.get(0).getLeechers());
	}

	@Test
	public void readResponseExceptionTest() throws Exception {

		MockResponse mockResponse = new MockResponse().setResponseCode(500);

		mockedServer.enqueue(mockResponse);

		dataSource.setBaseUri("http://localhost:8089");

		FeedResponseException ex = Assertions.assertThrows(FeedResponseException.class, () -> {
			dataSource.read("title");
		});

		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/rss.xml?type=search&search=title", request.getPath());
		Assertions.assertEquals("text/html", request.getHeader("Accept"));
		Assertions.assertEquals(
				"HTTP response code 500 with message \"Server Error\" for url http://localhost:8089/rss.xml?type=search&search=title",
				ex.getMessage());
	}

	@Test
	public void readContentTypeExceptionTest() throws Exception {
		String responseBody = "<?xml version='1.0' encoding='UTF-8'?>" + "<rss><channel><item>"
				+ "<title><![CDATA[title]]></title>" + "<info_hash>INFO_HASH</info_hash>"
				+ "<size>9223372036854775807</size>" + "<seeders>1</seeders>" + "<leechers>1000</leechers>"
				+ "</item></channel></rss>";

		MockResponse mockResponse = new MockResponse().setResponseCode(200)
				.addHeader("Content-Type", "application/json").setBody(responseBody);

		mockedServer.enqueue(mockResponse);

		dataSource.setBaseUri("http://localhost:8089");

		FeedContentTypeException ex = Assertions.assertThrows(FeedContentTypeException.class, () -> {
			dataSource.read("title");
		});

		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/rss.xml?type=search&search=title", request.getPath());
		Assertions.assertEquals("text/html", request.getHeader("Accept"));
		Assertions.assertEquals(
				"Expected Content-Type text/html received application/json for url http://localhost:8089/rss.xml?type=search&search=title \n<?xml version='1.0' encoding='UTF-8'?><rss><channel><item><title><![CDATA[title]]></title><info_hash>INFO_HASH</info_hash><size>9223372036854775807</size><seeders>1</seeders><leechers>1000</leechers></item></channel></rss>",
				ex.getMessage());
	}

	@Test
	public void readTimeoutExceptionTest() throws Exception {

		MockResponse mockResponse = new MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE);

		mockedServer.enqueue(mockResponse);

		dataSource.setBaseUri("http://localhost:8089");

		FeedTimeoutException ex = Assertions.assertThrows(FeedTimeoutException.class, () -> {
			dataSource.read("title");
		});

		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/rss.xml?type=search&search=title", request.getPath());
		Assertions.assertEquals("text/html", request.getHeader("Accept"));
		Assertions.assertEquals("Timeout for http://localhost:8089/rss.xml?type=search&search=title", ex.getMessage());

	}

	@Test
	public void readResponseBodyExceptionTest() throws Exception {

		MockResponse mockResponse = new MockResponse().setResponseCode(200).addHeader("Content-Type", "text/html")
				.setBody("X");

		mockedServer.enqueue(mockResponse);

		dataSource.setBaseUri("http://localhost:8089");

		FeedResponseBodyException ex = Assertions.assertThrows(FeedResponseBodyException.class, () -> {
			dataSource.read("title");
		});

		RecordedRequest request = mockedServer.takeRequest();
		Assertions.assertEquals("/rss.xml?type=search&search=title", request.getPath());
		Assertions.assertEquals("text/html", request.getHeader("Accept"));
		Assertions.assertEquals(
				"Unable to parse response for url http://localhost:8089/rss.xml?type=search&search=title \nX",
				ex.getMessage());
	}

	@Test
	public void readProcessingExceptionTest() throws Exception {

		dataSource.setBaseUri("JIBBERISH");

		FeedException ex = Assertions.assertThrows(FeedException.class, () -> {
			dataSource.read("title");
		});

		Assertions.assertEquals(
				"java.net.MalformedURLException: no protocol: JIBBERISH/rss.xml?type=search&search=title",
				ex.getMessage());
	}
}
