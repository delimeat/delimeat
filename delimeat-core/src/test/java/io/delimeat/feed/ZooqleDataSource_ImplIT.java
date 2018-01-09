package io.delimeat.feed;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

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

public class ZooqleDataSource_ImplIT {

	private static WireMockServer server = new WireMockServer(8089);

	private ZooqleDataSource_Impl client;
	
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
		client = new ZooqleDataSource_Impl();		
		client.setBaseUri(new URI("http://localhost:8089"));		
	}
	
	@Test
    public void readTest() throws Exception {
		
		String responseBody = "<?xml version='1.0' encoding='UTF-8'?>"
				+ "<rss xmlns:torrent=\"https://zooqle.com/xmlns/0.1/index.xmlns\"><channel><item>"
				+ "<title><![CDATA[title]]></title>" 
				+ "<torrent:seeds>100</torrent:seeds>"
				+ "<torrent:peers>200</torrent:peers>"
				+ "<torrent:infoHash>67A8DBA7DBF53CD815A4632F4CB1DC870114EDD7</torrent:infoHash>"
				+ "<enclosure url='torrentUrl' length='9223372036854775807' type='application/x-bittorrent' />"
				+ "</item></channel></rss>";
		
		server.stubFor(get(urlPathEqualTo("/search"))
				//TODO encoding issue
				//.withQueryParam("q", matching("TITLE%2Bafter%3A60%2Bcategory%3ATV"))
				.withQueryParam("fmt", matching("rss"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/xml")
						.withBody(responseBody)));

		List<FeedResult> results = client.read("TITLE");

		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals(FeedSource.ZOOQLE, results.get(0).getSource());
		Assertions.assertEquals("title", results.get(0).getTitle());
		Assertions.assertEquals("torrentUrl", results.get(0).getTorrentURL());
		Assertions.assertEquals(Long.MAX_VALUE, results.get(0).getContentLength());
		Assertions.assertEquals(100, results.get(0).getSeeders());
		Assertions.assertEquals(200, results.get(0).getLeechers());
		Assertions.assertEquals("67A8DBA7DBF53CD815A4632F4CB1DC870114EDD7", results.get(0).getInfoHashHex());
		
	}
}
