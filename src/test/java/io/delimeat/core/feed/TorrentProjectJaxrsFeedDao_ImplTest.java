package io.delimeat.core.feed;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

public class TorrentProjectJaxrsFeedDao_ImplTest {

	private class ItemEntityGenerator {

		private StringBuffer xml;

		public ItemEntityGenerator() {
			xml = new StringBuffer();
        	xml.append("<?xml version='1.0' encoding='UTF-8'?>");
			xml.append("<rss><channel>");
		}
     	public void addItem(String title, String torrentUrl, long length){
			xml.append("<item>");
        	xml.append("<title><![CDATA["+title+"]]></title>");
			xml.append("<enclosure url='"+torrentUrl+"' length='"+length+"' type='application/x-bittorrent' />");
			xml.append("</item>");        

      }

		public String toString() {
			return xml.toString() + "</channel></rss>";
		}

	}
  
	private static final String METADATA = "META-INF/oxm/feed-torrentproject-oxm.xml";
	
  	private static Client client;

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);
  
	private TorrentProjectJaxrsFeedDao_Impl dao;
	

	@BeforeClass
	public static void setUpClass() throws Exception {
		final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(JAXBContextProperties.OXM_METADATA_SOURCE,Arrays.asList(METADATA));
      MoxyXmlFeature feature = new MoxyXmlFeature(properties, classLoader, true, FeedResult.class, FeedSearch.class);
		Logger LOGGER = Logger.getLogger(LoggingFeature.class.getName());

		ClientConfig configuration = new ClientConfig()
										.register(feature)
										.register(new LoggingFeature(LOGGER))
        								.property(ClientProperties.CONNECT_TIMEOUT, 1000)
    									.property(ClientProperties.READ_TIMEOUT,    1000);
				
		client = JerseyClientBuilder.createClient(configuration);
	}
  
	@Before
	public void setUp() {
		dao = new TorrentProjectJaxrsFeedDao_Impl();
	}
	

	@Test
	public void encodingTest() {
		Assert.assertEquals("UTF-8",dao.getEncoding());
		dao.setEncoding("ENCODING");
		Assert.assertEquals("ENCODING", dao.getEncoding());
	}
  
	@Test
	public void mediaTypeTest() throws Exception {
		Assert.assertNull(dao.getMediaType());
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		Assert.assertEquals(MediaType.APPLICATION_JSON_TYPE, dao.getMediaType());
	}

	@Test
	public void baseUriTest() throws Exception {
		Assert.assertNull(dao.getBaseUri());
		URI uri = new URI("http://test.com");
		dao.setBaseUri(uri);
		Assert.assertEquals(uri, dao.getBaseUri());
	}

	@Test
	public void feedSourceTest() throws Exception {
		Assert.assertEquals(FeedSource.TORRENTPROJECT, dao.getFeedSource());
	}
  
	@Test
	public void readTest() throws Exception{
		ItemEntityGenerator response = new ItemEntityGenerator();
     	response.addItem("title", "torrentUrl", Long.MAX_VALUE);
     
		stubFor(get(urlEqualTo("/?s=title&out=rss"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type", "application/xml")
							.withBody(response.toString())));

		
		dao.setClient(client);
		dao.setMediaType(MediaType.APPLICATION_XML_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
		
		List<FeedResult> results = dao.read("title");
     	Assert.assertNotNull(results);
     	Assert.assertEquals(1, results.size());
     	Assert.assertEquals("title",results.get(0).getTitle());
     	Assert.assertEquals("torrentUrl",results.get(0).getTorrentURL());
     	Assert.assertEquals(Long.MAX_VALUE,results.get(0).getContentLength());

	}

	@Test(expected=RuntimeException.class)
	public void readUnsupportedEncodingTest() throws Exception {
		dao.setEncoding("JIBBERISH");
		
		dao.read("TITLE");
	}
  
	@Test(expected=FeedException.class)
	public void readWebAppExceptionTest() throws Exception {

		stubFor(get(urlEqualTo("/?s=title&out=rss"))
				.willReturn(aResponse()
							.withStatus(500)
							.withHeader("Content-Type","application/xml")));

		dao.setMediaType(MediaType.APPLICATION_XML_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
		dao.setClient(client);

		dao.read("title");
		Assert.fail();
	}
  
	@Test(expected=FeedException.class)
	public void readProcessingExceptionTest() throws Exception {

		stubFor(get(urlEqualTo("/?s=title&out=rss"))
				.willReturn(aResponse()
							.withStatus(200)
							.withHeader("Content-Type","application/xml")
                     .withFixedDelay(2000)));

		dao.setMediaType(MediaType.APPLICATION_XML_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
		dao.setClient(client);

		dao.read("title");
		Assert.fail();
	}

}
