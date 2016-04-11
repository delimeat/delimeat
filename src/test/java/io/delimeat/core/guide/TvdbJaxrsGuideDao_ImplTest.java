package io.delimeat.core.guide;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.NotAuthorizedException;
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

import io.delimeat.util.jaxrs.JaxbContextResolver;

public class TvdbJaxrsGuideDao_ImplTest {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	private class ErrorJsonGenerator {

		private StringBuffer xml;

		public ErrorJsonGenerator(String message) {
			xml = new StringBuffer();
			xml.append("{");
			xml.append("\"Error\":\"" + message + "\"");
		}

		public String toString() {
			return xml.toString() + "}";
		}

		public InputStream generate() throws Exception {
			return new ByteArrayInputStream(this.toString().getBytes("UTF-8"));
		}
	}
  
	private class TokenJsonGenerator {

		private StringBuffer xml;

		public TokenJsonGenerator(String token) {
			xml = new StringBuffer();
			xml.append("{");
			xml.append("\"token\":\"" + token + "\"");
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
			xml.append("{\"data\": [");
		}

		public void addSeries(String description, String guideid, String title, String firstaired) {
			xml.append("{");
			xml.append("\"overview\":\"" + description + "\",");
			xml.append("\"id\":\"" + guideid + "\",");
			xml.append("\"seriesName\":\"" + title + "\",");
			xml.append("\"firstAired\":\"" + firstaired + "\"");
			xml.append("}");
		}

		public String toString() {
			return xml.toString() + "]}";
		}

		public InputStream generate() throws Exception {
			return new ByteArrayInputStream(this.toString().getBytes("UTF-8"));
		}
	}

	private class InfoJsonGenerator {
		private StringBuffer xml;

		public InfoJsonGenerator(String description, String guideid, String title, String status, String network,
				String runtime, String airsDayOfWeek, String airsTime, String imdbId, List<String> genres, String firstAired,
				long lastUpdated) {
			xml = new StringBuffer();
			xml.append("{\"data\": {");
			xml.append("\"overview\":\"" + description + "\",");
			xml.append("\"id\":\"" + guideid + "\",");
			xml.append("\"seriesName\":\"" + title + "\",");
			xml.append("\"firstAired\":\"" + firstAired + "\",");
			xml.append("\"status\":\"" + status + "\",");
			xml.append("\"network\":\"" + network + "\",");
			xml.append("\"runtime\":\"" + runtime + "\",");
			xml.append("\"airsDayOfWeek\":\"" + airsDayOfWeek + "\",");
			xml.append("\"airsTime\":\"" + airsTime + "\",");
			xml.append("\"imdbId\":\"" + imdbId + "\",");
			xml.append("\"genre\":[");
			boolean first = true;
			for (String genre : genres) {
				if (!first) {
					xml.append(",");
				}
				xml.append("\"" + genre + "\"");
				first = false;
			}
			xml.append("],");
			xml.append("\"lastUpdated\":" + lastUpdated + "");
			xml.append("}");
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

		public EpisodesJsonGenerator(Integer first, Integer last, Integer next, Integer previous) {
			xml = new StringBuffer();
			xml.append("{\"links\": {");
			xml.append("\"first\": " + first + ",");
			xml.append("\"last\": " + last + ",");
			xml.append("\"next\": " + next + ",");
			xml.append("\"prev\": " + previous + "},");
			xml.append("\"data\": [");

		}

		public void addEpisode(Integer absoluteNumber, Integer airedEpisodeNumber, Integer airedSeason,
				String episodeName, String firstAired) {
			xml.append("{");
			xml.append("\"absoluteNumber\":" + absoluteNumber + ",");
			xml.append("\"airedEpisodeNumber\":" + airedEpisodeNumber + ",");
			xml.append("\"airedSeason\":\"" + airedSeason + "\",");
			xml.append("\"episodeName\":\"" + episodeName + "\",");
			xml.append("\"firstAired\":\"" + firstAired + "\"");
			xml.append("}");
		}

		public String toString() {
			return xml.toString() + "]}";
		}

		public InputStream generate() throws Exception {
			return new ByteArrayInputStream(this.toString().getBytes("UTF-8"));
		}
	}

	private static final String METADATA = "META-INF/oxm/guide-tvdb-oxm.xml";
	
	private TvdbJaxrsGuideDao_Impl dao;

	@Before
	public void setUp() throws Exception {
		dao = new TvdbJaxrsGuideDao_Impl();
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
		JAXBContext jc = JAXBContext.newInstance(new Class[] { TvdbApiKey.class, TvdbToken.class,
				GuideError.class, GuideSearchResult.class, GuideInfo.class, TvdbEpisodes.class, GuideEpisode.class },
				properties);

		JaxbContextResolver resolver = new JaxbContextResolver();
		resolver.setContext(jc);
		resolver.getClasses().add(TvdbApiKey.class);
		resolver.getClasses().add(TvdbToken.class);
		resolver.getClasses().add(GuideError.class);
		resolver.getClasses().add(GuideSearchResult.class);
		resolver.getClasses().add(GuideSearch.class);
		resolver.getClasses().add(GuideInfo.class);
		resolver.getClasses().add(TvdbEpisodes.class);
		resolver.getClasses().add(GuideEpisode.class);
		clientConfig.register(resolver);

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
	public void encodingTest() {
		Assert.assertEquals("UTF-8",dao.getEncoding());
		dao.setEncoding("ENCODING");
		Assert.assertEquals("ENCODING", dao.getEncoding());
	}
  
  	@Test
  	public void guideSourceTest(){
     	Assert.assertEquals(GuideSource.TVDB, dao.getGuideSource());
   }

	@Test
	public void apikeyTest() {
		Assert.assertNull(dao.getApiKey());
		dao.setApiKey("VALUE");
		Assert.assertEquals("VALUE", dao.getApiKey());
	}

	@Test
	public void validPeriodInMsTest() {
		Assert.assertEquals(0, dao.getValidPeriodInMs());
		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		Assert.assertEquals(Integer.MAX_VALUE, dao.getValidPeriodInMs());
	}

	@Test
	public void loginTest() throws Exception {
		TokenJsonGenerator generator = new TokenJsonGenerator("TOKEN");

		dao.setClient(prepareClient(new InputStream[] { generator.generate() }, new Integer[] { 200 }));

		dao.setBaseUri(new URI("http://test.com"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);

		TvdbToken token = dao.login("APIKEY");
		Assert.assertNotNull(token);
		Assert.assertEquals("TOKEN", token.getValue());
	}
  
	@Test(expected=GuideNotAuthorisedException.class)
	public void loginNotAuthorisedTest()  throws Exception {
		ErrorJsonGenerator generator = new ErrorJsonGenerator("THIS IS AN ERROR");

		dao.setClient(prepareClient(new InputStream[] { generator.generate() }, new Integer[] { 401 }));

		dao.setBaseUri(new URI("http://test.com"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);

      try{
			dao.login("APIKEY");
      }catch(GuideNotAuthorisedException ex){
      	Assert.assertEquals("THIS IS AN ERROR", ex.getMessage());
         Assert.assertEquals(NotAuthorizedException.class,ex.getCause().getClass());
         throw ex;
      }
	}
	@Test(expected=GuideException.class)
	public void loginExceptionTest() throws Exception {

      dao.setClient(prepareClient(new InputStream[] { null }, new Integer[] { 500 }));

		dao.setBaseUri(new URI("http://test.com"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);

      try{
			dao.login("APIKEY");
      }catch(GuideException ex){
         Assert.assertTrue(WebApplicationException.class.isAssignableFrom(ex.getCause().getClass()));
         throw ex;
      }
	}

	@Test
	public void refreshTokenTest() throws Exception {
		TokenJsonGenerator generator = new TokenJsonGenerator("NEW_TOKEN");
		dao.setClient(prepareClient(new InputStream[] { generator.generate() }, new Integer[] { 200 }));
		dao.setBaseUri(new URI("http://test.com"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);

		TvdbToken oldToken = new TvdbToken();
		oldToken.setValue("OLD_TOKEN");

		TvdbToken newToken = dao.refreshToken(oldToken);
		Assert.assertNotNull(newToken);
		Assert.assertEquals("NEW_TOKEN", newToken.getValue());
	}
  
	@Test(expected=GuideNotAuthorisedException.class)
	public void refreshTokenNotAuthorisedTest() throws Exception {
		ErrorJsonGenerator generator = new ErrorJsonGenerator("THIS IS AN ERROR");

		dao.setClient(prepareClient(new InputStream[] { generator.generate() }, new Integer[] { 401 }));

		dao.setBaseUri(new URI("http://test.com"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);

      TvdbToken oldToken = new TvdbToken();
      oldToken.setValue("OLD_TOKEN");

      try{
        dao.refreshToken(oldToken);
      }catch(GuideNotAuthorisedException ex){
      	Assert.assertEquals("THIS IS AN ERROR", ex.getMessage());
         Assert.assertEquals(NotAuthorizedException.class,ex.getCause().getClass());
         throw ex;
      }
	}
  
	@Test(expected=GuideException.class)
	public void refreshTokenExceptionTest() throws Exception {

      dao.setClient(prepareClient(new InputStream[] { null }, new Integer[] { 500 }));

		dao.setBaseUri(new URI("http://test.com"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
     
	   TvdbToken oldToken = new TvdbToken();
      oldToken.setValue("OLD_TOKEN");
     
      try{
        dao.refreshToken(oldToken);
      }catch(GuideException ex){
         Assert.assertTrue(WebApplicationException.class.isAssignableFrom(ex.getCause().getClass()));
         throw ex;
      }
	}

	@Test
	public void tokenUseExisting() throws Exception {
		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);
		TvdbToken returnToken = dao.getToken();
		Assert.assertEquals(token, returnToken);
	}

	@Test
	public void tokenLogin() throws Exception {
		TokenJsonGenerator generator = new TokenJsonGenerator("TOKEN");
		dao.setClient(prepareClient(new InputStream[] { generator.generate() }, new Integer[] { 200 }));
		dao.setBaseUri(new URI("http://test.com"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);

		dao.setApiKey("APIKEY");

		TvdbToken token = dao.getToken();
		Assert.assertNotNull(token);
		Assert.assertEquals("TOKEN", token.getValue());
	}

	@Test
	public void tokenRefreshTest() throws Exception {
		TokenJsonGenerator generator = new TokenJsonGenerator("NEW_TOKEN");
		dao.setClient(prepareClient(new InputStream[] { generator.generate() }, new Integer[] { 200 }));
		dao.setBaseUri(new URI("http://test.com"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);

		TvdbToken oldToken = new TvdbToken();
		oldToken.setValue("OLD_TOKEN");
		dao.setToken(oldToken);

		dao.setValidPeriodInMs(100000);

		TvdbToken newToken = dao.getToken();
		Assert.assertNotNull(newToken);
		Assert.assertEquals("NEW_TOKEN", newToken.getValue());
	}

	@Test
	public void searchTest() throws Exception {
		SearchJsonGenerator generator = new SearchJsonGenerator();
		generator.addSeries("DESCRIPTION", "GUIDEID", "TITLE", "2005-03-11");
		dao.setClient(prepareClient(new InputStream[] { generator.generate() }, new Integer[] { 200 }));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://test.com"));

		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);

		List<GuideSearchResult> results = dao.search("TITLE");
		Assert.assertNotNull(results);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals("GUIDEID", results.get(0).getGuideId());
		Assert.assertEquals("DESCRIPTION", results.get(0).getDescription());
		Assert.assertEquals("TITLE", results.get(0).getTitle());
		Assert.assertEquals("2005-03-11", SDF.format(results.get(0).getFirstAired()));
	}
  

	@Test(expected=GuideNotAuthorisedException.class)
	public void searchNotAuthorisedTest() throws Exception {
		ErrorJsonGenerator generator = new ErrorJsonGenerator("THIS IS AN ERROR");

		dao.setClient(prepareClient(new InputStream[] { generator.generate() }, new Integer[] { 401 }));

		dao.setBaseUri(new URI("http://test.com"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);

		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);

      try{
        dao.search("TITLE");
      }catch(GuideNotAuthorisedException ex){
      	Assert.assertEquals("THIS IS AN ERROR", ex.getMessage());
         Assert.assertEquals(NotAuthorizedException.class,ex.getCause().getClass());
         throw ex;
      }
	}
	
  @Test(expected=GuideNotFoundException.class)
	public void searchNotFoundTest() throws Exception {
		ErrorJsonGenerator generator = new ErrorJsonGenerator("THIS IS AN ERROR");

		dao.setClient(prepareClient(new InputStream[] { generator.generate() }, new Integer[] { 404 }));

		dao.setBaseUri(new URI("http://test.com"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);

		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);

      try{
        dao.search("TITLE");
      }catch(GuideNotFoundException ex){
      	Assert.assertEquals("THIS IS AN ERROR", ex.getMessage());
         Assert.assertEquals(NotFoundException.class,ex.getCause().getClass());
         throw ex;
      }
	}
  
	@Test(expected=GuideException.class)
	public void searchExceptionTest() throws Exception {

      dao.setClient(prepareClient(new InputStream[] { null }, new Integer[] { 500 }));

		dao.setBaseUri(new URI("http://test.com"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);

		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);
     
      try{
        dao.search("TITLE");
      }catch(GuideException ex){
         Assert.assertTrue(WebApplicationException.class.isAssignableFrom(ex.getCause().getClass()));
         throw ex;
      }
	}
  
	@Test(expected=RuntimeException.class)
	public void searchUnsupportedEncodingTest() throws Exception {
		dao.setEncoding("JIBBERISH");
		
		dao.search("TITLE");
	}
  
	@Test
	public void infoTest() throws Exception {
		InfoJsonGenerator generator = new InfoJsonGenerator("DESCRIPTION", "GUIDEID", "TITLE", "Ended", "CBS", "45",
				"Monday", "8:00pm", "IMDB", Arrays.asList("GENRE"),"2015-12-28",1454486401);
		dao.setClient(prepareClient(new InputStream[] { generator.generate() }, new Integer[] { 200 }));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://test.com"));

		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);

		GuideInfo info = dao.info("ID");
		Assert.assertNotNull(info);
		Assert.assertEquals("GUIDEID", info.getGuideId());
		Assert.assertEquals("DESCRIPTION", info.getDescription());
		Assert.assertEquals("TITLE", info.getTitle());
		Assert.assertEquals("2015-12-28", SDF.format(info.getFirstAired()));
		Assert.assertEquals(1, info.getAirDays().size());
		Assert.assertEquals(AiringDay.MONDAY, info.getAirDays().get(0));
		Assert.assertFalse(info.isAiring());
		Assert.assertEquals(72000000, info.getAirTime());
		Assert.assertEquals(1, info.getGenres().size());
		Assert.assertEquals("GENRE", info.getGenres().get(0));
		Assert.assertEquals("PST", info.getTimezone());
		Assert.assertEquals(45, info.getRunningTime());
		Assert.assertEquals("2016-02-03",SDF.format(info.getLastUpdated()));
	}

@Test(expected=GuideNotAuthorisedException.class)
	public void infoNotAuthorisedTest() throws Exception {
		ErrorJsonGenerator generator = new ErrorJsonGenerator("THIS IS AN ERROR");

		dao.setClient(prepareClient(new InputStream[] { generator.generate() }, new Integer[] { 401 }));

		dao.setBaseUri(new URI("http://test.com"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);

		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);

      try{
        dao.info("TITLE");
      }catch(GuideNotAuthorisedException ex){
      	Assert.assertEquals("THIS IS AN ERROR", ex.getMessage());
         Assert.assertEquals(NotAuthorizedException.class,ex.getCause().getClass());
         throw ex;
      }
	}
	
  @Test(expected=GuideNotFoundException.class)
	public void infoNotFoundTest() throws Exception {
		ErrorJsonGenerator generator = new ErrorJsonGenerator("THIS IS AN ERROR");

		dao.setClient(prepareClient(new InputStream[] { generator.generate() }, new Integer[] { 404 }));

		dao.setBaseUri(new URI("http://test.com"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);

		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);

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

		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);
     
      try{
        dao.info("TITLE");
      }catch(GuideException ex){
         Assert.assertTrue(WebApplicationException.class.isAssignableFrom(ex.getCause().getClass()));
         throw ex;
      }
	}
  
	@Test(expected=RuntimeException.class)
	public void infoUnsupportedEncodingTest() throws Exception {
		dao.setEncoding("JIBBERISH");
		
		dao.info("TITLE");
	}
  
	@Test
	public void episodesTest() throws Exception {
		EpisodesJsonGenerator generator = new EpisodesJsonGenerator(null, 0, 0, 0);
		generator.addEpisode(0, Integer.MIN_VALUE, Integer.MAX_VALUE, "TITLE", "2015-09-29");
		dao.setClient(prepareClient(new InputStream[] { generator.generate() }, new Integer[] { 200 }));

		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://test.com"));

		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);

		TvdbEpisodes result = dao.episodes("ID", 1);
		Assert.assertNotNull(result);
		Assert.assertNull(result.getFirst());
		Assert.assertEquals(0, result.getLast().intValue());
		Assert.assertEquals(0, result.getNext().intValue());
		Assert.assertEquals(0, result.getPrevious().intValue());
		Assert.assertNotNull(result.getEpisodes());
		Assert.assertEquals(1, result.getEpisodes().size());
		Assert.assertEquals(0, result.getEpisodes().get(0).getProductionNum().intValue());
		Assert.assertEquals(Integer.MIN_VALUE, result.getEpisodes().get(0).getEpisodeNum().intValue());
		Assert.assertEquals(Integer.MAX_VALUE, result.getEpisodes().get(0).getSeasonNum().intValue());
		Assert.assertEquals("TITLE", result.getEpisodes().get(0).getTitle());
		Assert.assertEquals("2015-09-29", SDF.format(result.getEpisodes().get(0).getAirDate()));
	}


@Test(expected=GuideNotAuthorisedException.class)
	public void episodesNotAuthorisedTest() throws Exception {
		ErrorJsonGenerator generator = new ErrorJsonGenerator("THIS IS AN ERROR");

		dao.setClient(prepareClient(new InputStream[] { generator.generate() }, new Integer[] { 401 }));

		dao.setBaseUri(new URI("http://test.com"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);

		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);

      try{
         dao.episodes("GUIDEID",1);
      }catch(GuideNotAuthorisedException ex){
      	Assert.assertEquals("THIS IS AN ERROR", ex.getMessage());
         Assert.assertEquals(NotAuthorizedException.class,ex.getCause().getClass());
         throw ex;
      }
	}
	
  @Test(expected=GuideNotFoundException.class)
	public void episodesNotFoundTest() throws Exception {
		ErrorJsonGenerator generator = new ErrorJsonGenerator("THIS IS AN ERROR");

		dao.setClient(prepareClient(new InputStream[] { generator.generate() }, new Integer[] { 404 }));

		dao.setBaseUri(new URI("http://test.com"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);

		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);

      try{
         dao.episodes("GUIDEID",1);
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

		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);
     
      try{
         dao.episodes("GUIDEID",1);
      }catch(GuideException ex){
         Assert.assertTrue(WebApplicationException.class.isAssignableFrom(ex.getCause().getClass()));
         throw ex;
      }
	}
	@Test
	public void episodeListNoNextTest() throws Exception {
		EpisodesJsonGenerator generator = new EpisodesJsonGenerator(1, 1, 0, 0);
		generator.addEpisode(0, Integer.MIN_VALUE, Integer.MAX_VALUE, "TITLE", "2015-09-29");
		dao.setClient(prepareClient(new InputStream[] { generator.generate() }, new Integer[] { 200 }));

		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://test.com"));

		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);

		List<GuideEpisode> episodes = dao.episodes("ID");
		Assert.assertNotNull(episodes);
		Assert.assertEquals(1, episodes.size());
		Assert.assertEquals(0, episodes.get(0).getProductionNum().intValue());
		Assert.assertEquals(Integer.MIN_VALUE, episodes.get(0).getEpisodeNum().intValue());
		Assert.assertEquals(Integer.MAX_VALUE, episodes.get(0).getSeasonNum().intValue());
		Assert.assertEquals("TITLE", episodes.get(0).getTitle());
		Assert.assertEquals("2015-09-29", SDF.format(episodes.get(0).getAirDate()));
	}

	@Test
	public void episodeListHasNextTest() throws Exception {
		EpisodesJsonGenerator generator = new EpisodesJsonGenerator(1, 2, 2, 0);
		generator.addEpisode(0, Integer.MIN_VALUE, Integer.MAX_VALUE, "TITLE", "2015-09-29");

		EpisodesJsonGenerator generatorTwo = new EpisodesJsonGenerator(2, 2, 0, 1);
		generatorTwo.addEpisode(0, Integer.MAX_VALUE, Integer.MIN_VALUE, "TITLE_TWO", "2005-09-29");

		dao.setClient(prepareClient(new InputStream[] { generator.generate(), generatorTwo.generate() },
				new Integer[] { 200, 200 }));

		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://test.com"));

		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);

		List<GuideEpisode> episodes = dao.episodes("ID");
		Assert.assertNotNull(episodes);
		Assert.assertEquals(2, episodes.size());
		Assert.assertEquals(0, episodes.get(0).getProductionNum().intValue());
		Assert.assertEquals(Integer.MIN_VALUE, episodes.get(0).getEpisodeNum().intValue());
		Assert.assertEquals(Integer.MAX_VALUE, episodes.get(0).getSeasonNum().intValue());
		Assert.assertEquals("TITLE", episodes.get(0).getTitle());
		Assert.assertEquals("2015-09-29", SDF.format(episodes.get(0).getAirDate()));
		Assert.assertEquals(0, episodes.get(1).getProductionNum().intValue());
		Assert.assertEquals(Integer.MAX_VALUE, episodes.get(1).getEpisodeNum().intValue());
		Assert.assertEquals(Integer.MIN_VALUE, episodes.get(1).getSeasonNum().intValue());
		Assert.assertEquals("TITLE_TWO", episodes.get(1).getTitle());
		Assert.assertEquals("2005-09-29", SDF.format(episodes.get(1).getAirDate()));
	}

	@Test(expected=RuntimeException.class)
	public void episodesUnsupportedEncodingTest() throws Exception {
		dao.setEncoding("JIBBERISH");
		
		dao.episodes("TITLE");
	}
}
