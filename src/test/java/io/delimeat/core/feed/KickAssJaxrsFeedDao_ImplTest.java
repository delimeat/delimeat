package io.delimeat.core.feed;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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
	
	private static final String METADATA = "META-INF/oxm/feed-kat-oxm.xml";

  	private static Client client;

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);
  
	private KickAssJaxrsFeedDao_Impl dao;
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		final ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(JAXBContextProperties.OXM_METADATA_SOURCE,Arrays.asList(METADATA));
      MoxyXmlFeature feature = new MoxyXmlFeature(properties, classLoader, true, FeedResult.class, FeedSearch.class);
		Logger LOGGER = Logger.getLogger(LoggingFeature.class.getName());

		ClientConfig configuration = new ClientConfig()
										.register(feature)
										.register(new LoggingFeature(LOGGER));
				
		client = JerseyClientBuilder.createClient(configuration);
	}
  
	@Before
	public void setUp() throws Exception {
		dao = new KickAssJaxrsFeedDao_Impl();
	}
	
  	/*
	private Client prepareClient(InputStream[] inputs, Integer[] responseCodes) throws IOException, JAXBException {

		ClientConfig clientConfig = new ClientConfig();

		HttpURLConnection mockedUrlConnection = Mockito.mock(HttpURLConnection.class);
		// register each input stream to return
		OngoingStubbing<InputStream> inputStub = null;
		for (InputStream input : inputs) {
			if (inputStub == null) {
				inputStub = Mockito.when(mockedUrlConnection.getInputStream());
			}
			inputStub = inputStub.thenReturn(input);
		}
     
		OngoingStubbing<InputStream> errorStub = null;
		for (InputStream input : inputs) {
			if (errorStub == null) {
				errorStub = Mockito.when(mockedUrlConnection.getErrorStream());
			}
			errorStub = errorStub.thenReturn(input);
		}   
     
		Mockito.when(mockedUrlConnection.getURL()).thenReturn(new URL("http://test.com"));

		// register each response code to return
		OngoingStubbing<Integer> responseCodeStub = null;
		for (Integer responseCode : responseCodes) {
			if (responseCodeStub == null) {
				responseCodeStub = Mockito.when(mockedUrlConnection.getResponseCode());
			}
			responseCodeStub = responseCodeStub.thenReturn(responseCode);
		}

		// register the header values to return
		HashMap<String, List<String>> headers = new HashMap<String, List<String>>();
		headers.put("Content-Type", Arrays.asList(new String[] { "application/json" }));
		Mockito.when(mockedUrlConnection.getHeaderFields()).thenReturn(headers);
		ConnectionFactory mockedConnectionFactory = Mockito.mock(ConnectionFactory.class);
		Mockito.when(mockedConnectionFactory.getConnection(Mockito.any(URL.class))).thenReturn(mockedUrlConnection);

		HttpUrlConnectorProvider connectorProvider = new HttpUrlConnectorProvider();
		connectorProvider.connectionFactory(mockedConnectionFactory);
		clientConfig.connectorProvider(connectorProvider);

		// add logging of the client
		Logger logger = Logger.getLogger(this.getClass().getName());
		clientConfig.register(new LoggingFilter(logger, true));

		// register the context provider to use mapping
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, METADATA);
		JAXBContext jc = JAXBContext.newInstance(new Class[] { FeedResult.class, FeedSearch.class },
				properties);

		JaxbContextResolver resolver = new JaxbContextResolver();
		resolver.setContext(jc);
		resolver.getClasses().add(FeedResult.class);
		resolver.getClasses().add(FeedSearch.class);
		clientConfig.register(resolver);

		return ClientBuilder.newClient(clientConfig);
	}
   */

	@Test
	public void encodingTest() {
		Assert.assertEquals("UTF-8",dao.getEncoding());
		dao.setEncoding("ENCODING");
		Assert.assertEquals("ENCODING", dao.getEncoding());
	}
  
	@Test
	public void mediaTypeTest() throws URISyntaxException {
		Assert.assertNull(dao.getMediaType());
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		Assert.assertEquals(MediaType.APPLICATION_JSON_TYPE, dao.getMediaType());
	}

	@Test
	public void baseUriTest() throws URISyntaxException {
		Assert.assertNull(dao.getBaseUri());
		URI uri = new URI("http://test.com");
		dao.setBaseUri(uri);
		Assert.assertEquals(uri, dao.getBaseUri());
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

		
		dao.setClient(client);
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
		
		List<FeedResult> results = dao.read("title");
     	Assert.assertNotNull(results);
     	Assert.assertEquals(1, results.size());
     	Assert.assertEquals("title",results.get(0).getTitle());
     	Assert.assertEquals("http://test.com",results.get(0).getTorrentURL());
     	Assert.assertEquals(100,results.get(0).getContentLength());
     	Assert.assertEquals(50,results.get(0).getSeeders());
     	Assert.assertEquals(30,results.get(0).getLeechers());

	}

	@Test(expected=RuntimeException.class)
	public void readUnsupportedEncodingTest() throws Exception {
		dao.setEncoding("JIBBERISH");
		
		dao.read("TITLE");
	}
  
	@Test(expected=FeedException.class)
	public void readExceptionTest() throws Exception {

		stubFor(get(urlEqualTo("/?q=title+category%3Atv"))
				.willReturn(aResponse()
							.withStatus(500)
							.withHeader("Content-Type","application/json")));

		dao.setMediaType(MediaType.APPLICATION_XML_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
		dao.setClient(client);

		dao.read("title");
		Assert.fail();
	}

}
