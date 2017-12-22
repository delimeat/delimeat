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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.github.tomakehurst.wiremock.WireMockServer;

import io.delimeat.feed.entity.FeedResult;
import io.delimeat.feed.entity.FeedSource;

public class TorrentProjectFeedDataSourceIT {

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
		client.setFeedSource(FeedSource.TORRENTPROJECT);
		client.getMoxyProperties().put("eclipselink.media-type", "application/xml");
		client.getMoxyProperties().put("eclipselink.oxm.metadata-source", "oxm/feed-torrentproject-oxm.xml");
		
		client.setMediaType(MediaType.APPLICATION_XML_TYPE);
		
		client.setTargetFactory(new TorrentProjectTargetFactory_Impl());
		
		client.setBaseUri(new URI("http://localhost:8089"));		
	}
	
	@Disabled("disabled because the &tr= causes issues with SAX")
	@Test
    public void readTest() throws Exception {
		
		String responseBody = "<?xml version='1.0' encoding='UTF-8'?>" 
				+ "<rss><channel><item>"
				+ "<title><![CDATA[title]]></title>"
				+ "<enclosure url='magnet:?xt=urn:btih:df706cf16f45e8c0fd226223509c7e97b4ffec13&tr=udp://tracker.coppersurfer.tk:6969/announce' length='9223372036854775807' type='application/x-bittorrent' />"
				+ "</item></channel></rss>";
		
		server.stubFor(get(urlEqualTo("/rss/TITLE/"))
				.willReturn(aResponse()
						.withStatus(200)
						.withHeader("Content-Type", "application/xml")
						.withBody(responseBody)));

		List<FeedResult> results = client.read("TITLE");

		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals(FeedSource.TORRENTPROJECT, results.get(0).getSource());
		Assertions.assertEquals("title", results.get(0).getTitle());
		Assertions.assertEquals("magnet:?xt=urn:btih:df706cf16f45e8c0fd226223509c7e97b4ffec13&tr=udp://tracker.coppersurfer.tk:6969/announce",results.get(0).getTorrentURL());
		Assertions.assertEquals(Long.MAX_VALUE, results.get(0).getContentLength());
		Assertions.assertNull(results.get(0).getInfoHashHex());
		Assertions.assertEquals(0, results.get(0).getContentLength());
		Assertions.assertEquals(0, results.get(0).getSeeders());
		Assertions.assertEquals(0, results.get(0).getLeechers());
		
	}
}
