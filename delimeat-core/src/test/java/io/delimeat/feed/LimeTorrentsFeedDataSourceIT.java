package io.delimeat.feed;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.WireMockServer;

import io.delimeat.feed.entity.FeedResult;
import io.delimeat.feed.entity.FeedSource;

public class LimeTorrentsFeedDataSourceIT {

	private static WireMockServer server = new WireMockServer(8089);
	
	private JaxrsFeedDataSource_Impl client;
	
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
		client = new JaxrsFeedDataSource_Impl();
		client.setFeedSource(FeedSource.LIMETORRENTS);
		client.getMoxyProperties().put("eclipselink.media-type", "application/xml");
		client.getMoxyProperties().put("eclipselink.oxm.metadata-source", "oxm/feed-limetorrents-oxm.xml");
		
		client.setMediaType(MediaType.APPLICATION_XML_TYPE);
		
		client.setTargetFactory(new LimeTorrentsTargetFactory_Impl());
		
		client.setBaseUri(new URI("http://localhost:8089"));		
	}
	
	@Test
    public void readTest() throws Exception {
		
		String responseBody = "<?xml version='1.0' encoding='UTF-8'?>" 
				+ "<rss><channel><item>"
				+ "<title><![CDATA[title]]></title><enclosure url='torrentUrl' type='application/x-bittorrent' />"
				+ "<size>9223372036854775807</size>" 
				+ "</item></channel></rss>";
		
		server.stubFor(get(urlEqualTo("/searchrss/TITLE/"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/xml")
						.withBody(responseBody)));

		List<FeedResult> results = client.read("TITLE");

		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals(FeedSource.LIMETORRENTS, results.get(0).getSource());
		Assertions.assertEquals("title", results.get(0).getTitle());
		Assertions.assertEquals("torrentUrl", results.get(0).getTorrentURL());
		Assertions.assertNull(results.get(0).getInfoHashHex());
		Assertions.assertEquals(Long.MAX_VALUE, results.get(0).getContentLength());
		Assertions.assertEquals("title", results.get(0).getTitle());
		Assertions.assertEquals(0, results.get(0).getSeeders());
		Assertions.assertEquals(0, results.get(0).getLeechers());
		
	}
}
