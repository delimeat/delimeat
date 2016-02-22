package io.delimeat.core.guide;

import io.delimeat.util.jaxrs.CustomMOXyJsonProvider;
import io.delimeat.util.jaxrs.JaxbContextResolver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.client.HttpUrlConnectorProvider.ConnectionFactory;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

public class TvMazeJaxrsGuideDao_ImplTest {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

  	private class ErrorJsonGenerator {

		private StringBuffer xml;

		public ErrorJsonGenerator(String message) {
			xml = new StringBuffer();
			xml.append("{");
			xml.append("\"name\":\"" + message + "\"");
		}

		public String toString() {
			return xml.toString() + "}";
		}

		public InputStream generate() throws Exception {
			return new ByteArrayInputStream(this.toString().getBytes("UTF-8"));
		}
	}
  
	private class EpisodesJsonGenerator {
		private StringBuffer xml;
		private boolean first = true;

		public EpisodesJsonGenerator() {
			xml = new StringBuffer();
			xml.append("[");

		}

		public void addEpisode(Integer absoluteNumber, Integer airedEpisodeNumber, Integer airedSeason,
				String episodeName, String firstAired) {
			if (!first) {
				xml.append(",");
			}
			xml.append("{");
			xml.append("\"absoluteNumber\":" + absoluteNumber + ",");
			xml.append("\"number\":" + airedEpisodeNumber + ",");
			xml.append("\"season\":\"" + airedSeason + "\",");
			xml.append("\"name\":\"" + episodeName + "\",");
			xml.append("\"airdate\":\"" + firstAired + "\"");
			xml.append("}");
			first = false;
		}

		public String toString() {
			return xml.toString() + "]";
		}

		public InputStream generate() throws Exception {
			return new ByteArrayInputStream(this.toString().getBytes("UTF-8"));
		}
	}

	private class InfoJsonGenerator {
		private StringBuffer xml;

		public InfoJsonGenerator(String description, String guideid, String title, String runtime, String tvdbId,
				String tvrageId, List<String> genres, String status, String timezone, String time, List<String> days) {
			xml = new StringBuffer();
			xml.append("{");
			xml.append("\"summary\":\"" + description + "\",");
			xml.append("\"id\":\"" + guideid + "\",");
			xml.append("\"name\":\"" + title + "\",");
			xml.append("\"externals\":{");
        	xml.append("\"tvrage\":" + tvrageId+ ",");
			xml.append("\"thetvdb\":" + tvdbId );
			xml.append("},");
			xml.append("\"genres\":[");
			boolean firstGenre = true;
			for (String genre : genres) {
				if (!firstGenre) {
					xml.append(",");
				}
				xml.append("\"" + genre + "\"");
				firstGenre = false;
			}
			xml.append("],");
			xml.append("\"status\":\"" + status + "\",");
			xml.append("\"runtime\":" + runtime + ",");
			xml.append("\"network\":{\"country\":{\"timezone\":\"" + timezone + "\"}},");
			xml.append("\"schedule\":{\"time\":\"" + time + "\",");
			xml.append("\"days\":[");
			boolean firstDay = true;
			for (String day : days) {
				if (!firstDay) {
					xml.append(",");
				}
				xml.append("\"" + day + "\"");
				firstDay = false;
			}
			xml.append("]");
			xml.append("}");
		}

		public String toString() {
			return xml.toString() + "}";
		}

		public InputStream generate() throws Exception {
			return new ByteArrayInputStream(this.toString().getBytes("UTF-8"));
		}
	}

	private class SearchJsonGenerator {
		private StringBuffer xml;

		public SearchJsonGenerator() {
			xml = new StringBuffer();
			xml.append("[");
		}

		public void addSeries(String description, String guideid, String title, String firstaired, String tvdbId,
				String tvrageId) {
			xml.append("{\"show\":{");
			xml.append("\"summary\":\"" + description + "\",");
			xml.append("\"id\":\"" + guideid + "\",");
			xml.append("\"name\":\"" + title + "\",");
			xml.append("\"externals\":{");
  			xml.append("\"tvrage\":" + tvrageId + ",");
			xml.append("\"thetvdb\":" + tvdbId);
			xml.append("},");
			xml.append("\"premiered\":\"" + firstaired + "\"");
			xml.append("}}");
		}

		public String toString() {
			return xml.toString() + "]";
		}

		public InputStream generate() throws Exception {
			return new ByteArrayInputStream(this.toString().getBytes("UTF-8"));
		}
	}

	private static final String METADATA = "META-INF/oxm/guide-tvmaze-oxm.xml";
	
	private TvMazeJaxrsGuideDao_Impl dao;

	@Before
	public void setUp() throws Exception {
		dao = new TvMazeJaxrsGuideDao_Impl();
	}

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
		JAXBContext jc = JAXBContext.newInstance(
				new Class[] { GuideError.class, GuideEpisode.class, GuideInfo.class, GuideSearchResult.class }, properties);
		
      JaxbContextResolver resolver = new JaxbContextResolver();
		resolver.setContext(jc);
		resolver.getClasses().add(GuideEpisode.class);
		resolver.getClasses().add(GuideInfo.class);
		resolver.getClasses().add(GuideSearchResult.class);
      resolver.getClasses().add(GuideError.class);
		
      clientConfig.register(resolver);
		clientConfig.register(CustomMOXyJsonProvider.class);
		clientConfig.property("jersey.config.disableMoxyJson", "true");

		return ClientBuilder.newClient(clientConfig);
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
	public void infoTest() throws IOException, Exception {
		List<String> genres = new ArrayList<String>();
		genres.add("GENRE1");
		genres.add("GENRE2");
		List<String> days = new ArrayList<String>();
		days.add("Monday");
		days.add("Friday");
		InfoJsonGenerator generator = new InfoJsonGenerator("DESCRIPTION", "GUIDEID", "TITLE", "60", "123", "432",
				genres, "Ended", "CBS", "20:00", days);

		dao.setClient(prepareClient(new InputStream[] { generator.generate() }, new Integer[] { 200 }));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://test.com"));

		GuideInfo info = dao.info("ID");
		Assert.assertEquals("DESCRIPTION", info.getDescription());
		Assert.assertEquals("TITLE", info.getTitle());
		Assert.assertEquals(60, info.getRunningTime());
		Assert.assertFalse(info.isAiring());
		Assert.assertEquals("CBS", info.getTimezone());
		Assert.assertEquals("GUIDEID", info.getGuideId());
		Assert.assertEquals(72000000, info.getAirTime());
		Assert.assertEquals(2, info.getGenres().size());
		Assert.assertEquals("GENRE1", info.getGenres().get(0));
		Assert.assertEquals("GENRE2", info.getGenres().get(1));

		Assert.assertEquals(2, info.getAirDays().size());
		Assert.assertEquals(AiringDay.MONDAY, info.getAirDays().get(0));
		Assert.assertEquals(AiringDay.FRIDAY, info.getAirDays().get(1));

	}
  
  @Test(expected=GuideNotFoundException.class)
	public void infoNotFoundTest() throws Exception {
		ErrorJsonGenerator generator = new ErrorJsonGenerator("THIS IS AN ERROR");

		dao.setClient(prepareClient(new InputStream[] { generator.generate() }, new Integer[] { 404 }));

		dao.setBaseUri(new URI("http://test.com"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);

      try{
        dao.info("TITLE");
      }catch(GuideNotFoundException ex){
      	Assert.assertEquals("THIS IS AN ERROR", ex.getMessage());
         Assert.assertEquals(NotFoundException.class,ex.getCause().getClass());
         throw ex;
      }
	}
  
	@Test(expected=GuideException.class)
	public void infoExceptionTest() throws Exception {

      dao.setClient(prepareClient(new InputStream[] { null }, new Integer[] { 500 }));

		dao.setBaseUri(new URI("http://test.com"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
     
      try{
        dao.info("TITLE");
      }catch(GuideException ex){
         Assert.assertTrue(WebApplicationException.class.isAssignableFrom(ex.getCause().getClass()));
         throw ex;
      }
	}

	@Test
	public void episodesTest() throws Exception {
		EpisodesJsonGenerator generator = new EpisodesJsonGenerator();
		generator.addEpisode(0, Integer.MIN_VALUE, Integer.MAX_VALUE, "TITLE", "2015-09-29");
		generator.addEpisode(1, Integer.MIN_VALUE, Integer.MAX_VALUE, "TITLE", "2015-09-30");

		dao.setClient(prepareClient(new InputStream[] { generator.generate() }, new Integer[] { 200 }));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://test.com"));

		List<GuideEpisode> result = dao.episodes("ID");
		Assert.assertNotNull(result);
		Assert.assertEquals(2, result.size());
		Assert.assertEquals(0, result.get(0).getProductionNum().intValue());
		Assert.assertEquals(Integer.MIN_VALUE, result.get(0).getEpisodeNum().intValue());
		Assert.assertEquals(Integer.MAX_VALUE, result.get(0).getSeasonNum().intValue());
		Assert.assertEquals("TITLE", result.get(0).getTitle());
		Assert.assertEquals("2015-09-29", SDF.format(result.get(0).getAirDate()));
	}
  
	@Test(expected=GuideNotFoundException.class)
	public void episodesNotFoundTest() throws Exception {
		ErrorJsonGenerator generator = new ErrorJsonGenerator("THIS IS AN ERROR");

		dao.setClient(prepareClient(new InputStream[] { generator.generate() }, new Integer[] { 404 }));

		dao.setBaseUri(new URI("http://test.com"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);

      try{
         dao.episodes("GUIDEID");
      }catch(GuideNotFoundException ex){
      	Assert.assertEquals("THIS IS AN ERROR", ex.getMessage());
         Assert.assertEquals(NotFoundException.class,ex.getCause().getClass());
         throw ex;
      }
	}
  
	@Test(expected=GuideException.class)
	public void episodesExceptionTest() throws Exception {

      dao.setClient(prepareClient(new InputStream[] { null }, new Integer[] { 500 }));

		dao.setBaseUri(new URI("http://test.com"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
      try{
         dao.episodes("GUIDEID");
      }catch(GuideException ex){
         Assert.assertTrue(WebApplicationException.class.isAssignableFrom(ex.getCause().getClass()));
         throw ex;
      }
	}

	@Test
	public void searchTest() throws IOException, Exception {
		SearchJsonGenerator generator = new SearchJsonGenerator();
		generator.addSeries("DESCRIPTION", "GUIDEID", "TITLE", "2015-09-29", "123", "432");

		dao.setClient(prepareClient(new InputStream[] { generator.generate() }, new Integer[] { 200 }));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://test.com"));

		List<GuideSearchResult> results = dao.search("TITLE");
		Assert.assertNotNull(results);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals("GUIDEID", results.get(0).getGuideId());
		Assert.assertEquals("DESCRIPTION", results.get(0).getDescription());
		Assert.assertEquals("TITLE", results.get(0).getTitle());
		Assert.assertEquals("2015-09-29", SDF.format(results.get(0).getFirstAired()));
	}
  
	@Test(expected=GuideException.class)
	public void searchExceptionTest() throws Exception {

      dao.setClient(prepareClient(new InputStream[] { null }, new Integer[] { 500 }));

		dao.setBaseUri(new URI("http://test.com"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
     
      try{
        dao.search("TITLE");
      }catch(GuideException ex){
         Assert.assertTrue(WebApplicationException.class.isAssignableFrom(ex.getCause().getClass()));
         throw ex;
      }
	}
}
