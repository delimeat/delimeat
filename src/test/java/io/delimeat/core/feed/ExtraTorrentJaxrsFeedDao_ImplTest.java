package io.delimeat.core.feed;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

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
  
	private static final String METADATA = "META-INF/oxm/feed-extratorrent-oxm.xml";
	
  	private static Client client;

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);
  
	private ExtraTorrentJaxrsFeedDao_Impl dao;
	

	@BeforeClass
	public static void setUpClass() throws Exception {
		final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, Arrays.asList(METADATA));
		MoxyXmlFeature feature = new MoxyXmlFeature(properties, classLoader, true, FeedResult.class, FeedSearch.class);
		Logger LOGGER = Logger.getLogger(LoggingFeature.class.getName());

		ClientConfig configuration = new ClientConfig()
										.register(feature)
										.register(new LoggingFeature(LOGGER));
				
		client = JerseyClientBuilder.createClient(configuration);
	}
  
	@Before
	public void setUp() {
		dao = new ExtraTorrentJaxrsFeedDao_Impl();
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

		
		dao.setClient(client);
		dao.setMediaType(MediaType.APPLICATION_XML_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
		
		List<FeedResult> results = dao.read("title");
     	Assert.assertNotNull(results);
     	Assert.assertEquals(1, results.size());
     	Assert.assertEquals("title",results.get(0).getTitle());
     	Assert.assertEquals("torrentUrl",results.get(0).getTorrentURL());
     	Assert.assertEquals(Long.MAX_VALUE,results.get(0).getContentLength());
     	Assert.assertEquals(1, results.get(0).getSeeders());
     	Assert.assertEquals(1000, results.get(0).getLeechers());

	}

	@Test(expected=RuntimeException.class)
	public void readUnsupportedEncodingTest() throws Exception {
		dao.setEncoding("JIBBERISH");
		
		dao.read("TITLE");
	}
  
	@Test
	public void readExceptionTest() throws Exception {

		stubFor(get(urlEqualTo("/?type=search&search=title"))
				.willReturn(aResponse()
							.withStatus(500)
							.withHeader("Content-Type","application/xml")));

		dao.setMediaType(MediaType.APPLICATION_XML_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
		dao.setClient(client);

		try {
			dao.read("title");
		} catch (FeedException ex) {
			Assert.assertTrue(WebApplicationException.class.isAssignableFrom(ex.getCause().getClass()));
			return;
		}
		Assert.fail();
	}
}
