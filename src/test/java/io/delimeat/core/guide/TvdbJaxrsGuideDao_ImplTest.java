package io.delimeat.core.guide;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import java.net.URI;
import java.net.URISyntaxException;
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
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.delimeat.util.jaxrs.CustomMOXyJsonProvider;
import io.delimeat.util.jaxrs.JaxbContextResolver;

public class TvdbJaxrsGuideDao_ImplTest {

	private class ErrorEntityGenerator {

		private StringBuffer xml;

		public ErrorEntityGenerator(String message) {
			xml = new StringBuffer();
			xml.append("{");
			xml.append("\"Error\":\"" + message + "\"");
		}

		public String toString() {
			return xml.toString() + "}";
		}

	}
  
	private class APIKeyEntityGenerator {

		private StringBuffer xml;

		public APIKeyEntityGenerator(String message) {
			xml = new StringBuffer();
			xml.append("{");
			xml.append("\"apikey\":\"" + message + "\"");
		}

		public String toString() {
			return xml.toString() + "}";
		}
     
	}
  
	private class TokenEntityGenerator {

		private StringBuffer xml;

		public TokenEntityGenerator(String token) {
			xml = new StringBuffer();
			xml.append("{");
			xml.append("\"token\":\"" + token + "\"");
		}

		public String toString() {
			return xml.toString() + "}";
		}
	}

	private class SearchEntityGenerator {
		private StringBuffer xml;

		public SearchEntityGenerator() {
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

	}

	private class InfoEntityGenerator {
		private StringBuffer xml;

		public InfoEntityGenerator(String description, String guideid, String title, String status, String network,
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

	}

	private class EpisodesEntityGenerator {
		private StringBuffer xml;

		public EpisodesEntityGenerator(Integer first, Integer last, Integer next, Integer previous) {
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

	}

  	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	private static final String METADATA = "META-INF/oxm/guide-tvdb-oxm.xml";
	
  	private static Client client;
  
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);
  
	private TvdbJaxrsGuideDao_Impl dao;

  	@BeforeClass 
  	public static void setUpClass() throws Exception{
     	JaxbContextResolver resolver = new JaxbContextResolver();
     	resolver.getClasses().add(GuideSearchResult.class);
     	resolver.getClasses().add(GuideEpisode.class);
     	resolver.getClasses().add(GuideInfo.class);
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, Arrays.asList(METADATA));
     	properties.put(JAXBContextProperties.MEDIA_TYPE,"application/json");
     	properties.put(MarshallerProperties.JSON_INCLUDE_ROOT,false);
     	properties.put(Marshaller.JAXB_FORMATTED_OUTPUT, true);
     	JAXBContext context = JAXBContext.newInstance(new Class[]{TvdbApiKey.class, TvdbToken.class,
				GuideError.class, GuideSearchResult.class, GuideInfo.class, TvdbEpisodes.class, GuideEpisode.class}, properties);
     	resolver.setContext(context);
     	ClientConfig configuration = new ClientConfig();
     	configuration.register(resolver);
     	configuration.register(CustomMOXyJsonProvider.class);
     	Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());
     	configuration.register(new LoggingFilter(LOGGER, true));
     	configuration.property("jersey.config.disableMoxyJson", "true");
     	client  = JerseyClientBuilder.createClient(configuration);   
   }
  
	@Before
	public void setUp() throws Exception {
		dao = new TvdbJaxrsGuideDao_Impl();
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
     	APIKeyEntityGenerator request = new APIKeyEntityGenerator("APIKEY");
		TokenEntityGenerator response = new TokenEntityGenerator("TOKEN");

     stubFor(post(urlEqualTo("/login"))
            .withRequestBody(equalToJson(request.toString()))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(response.toString())));
     
		dao.setBaseUri(new URI("http://localhost:8089"));
     	dao.setClient(client);
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);

		TvdbToken token = dao.login("APIKEY");
		Assert.assertNotNull(token);
		Assert.assertEquals("TOKEN", token.getValue());
	}
  
	@Test
	public void loginNotAuthorisedTest()  throws Exception {
     	APIKeyEntityGenerator request = new APIKeyEntityGenerator("APIKEY");
		ErrorEntityGenerator response = new ErrorEntityGenerator("THIS IS AN ERROR");

     stubFor(post(urlEqualTo("/login"))
            .withRequestBody(equalToJson(request.toString()))
            .willReturn(aResponse()
                .withStatus(401)
                .withHeader("Content-Type", "application/json")
                .withBody(response.toString())));
     
		dao.setBaseUri(new URI("http://localhost:8089"));
     	dao.setClient(client);

		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);

      try{
			dao.login("APIKEY");
      }catch(GuideNotAuthorisedException ex){
      	Assert.assertEquals("THIS IS AN ERROR", ex.getMessage());
         Assert.assertEquals(NotAuthorizedException.class,ex.getCause().getClass());
         return;
      }
     	Assert.fail();
	}
  
	@Test
	public void loginExceptionTest() throws Exception {
     	APIKeyEntityGenerator request = new APIKeyEntityGenerator("APIKEY");

     stubFor(post(urlEqualTo("/login"))
            .withRequestBody(equalToJson(request.toString()))
            .willReturn(aResponse()
                .withStatus(500)
                .withHeader("Content-Type", "application/json")));
     
		dao.setBaseUri(new URI("http://localhost:8089"));
     	dao.setClient(client);
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);

      try{
			dao.login("APIKEY");
      }catch(GuideException ex){
         Assert.assertTrue(WebApplicationException.class.isAssignableFrom(ex.getCause().getClass()));
        	return;
      }
     	Assert.fail();
	}

	@Test
	public void refreshTokenTest() throws Exception {
		TokenEntityGenerator response = new TokenEntityGenerator("NEW_TOKEN");
     
     stubFor(get(urlEqualTo("/refresh_token"))
            .withHeader("Authorization", equalTo("Bearer OLD_TOKEN"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(response.toString())));
     
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
     	dao.setClient(client);
     
		TvdbToken oldToken = new TvdbToken();
		oldToken.setValue("OLD_TOKEN");

		TvdbToken newToken = dao.refreshToken(oldToken);
		Assert.assertNotNull(newToken);
		Assert.assertEquals("NEW_TOKEN", newToken.getValue());
	}
  
	@Test
	public void refreshTokenNotAuthorisedTest() throws Exception {
		ErrorEntityGenerator response = new ErrorEntityGenerator("THIS IS AN ERROR");
     
     stubFor(get(urlEqualTo("/refresh_token"))
            .withHeader("Authorization", equalTo("Bearer OLD_TOKEN"))
            .willReturn(aResponse()
                .withStatus(401)
                .withHeader("Content-Type", "application/json")
                .withBody(response.toString())));
     
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
     	dao.setClient(client);
     
      TvdbToken oldToken = new TvdbToken();
      oldToken.setValue("OLD_TOKEN");

      try{
        dao.refreshToken(oldToken);
      }catch(GuideNotAuthorisedException ex){
      	Assert.assertEquals("THIS IS AN ERROR", ex.getMessage());
         Assert.assertEquals(NotAuthorizedException.class,ex.getCause().getClass());
         return;
      }
     	Assert.fail();
	}
  
	@Test
	public void refreshTokenExceptionTest() throws Exception {
     
     stubFor(get(urlEqualTo("/refresh_token"))
            .withHeader("Authorization", equalTo("Bearer OLD_TOKEN"))
            .willReturn(aResponse()
                .withStatus(500)
                .withHeader("Content-Type", "application/json")));
     
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
     	dao.setClient(client);
     
	   TvdbToken oldToken = new TvdbToken();
      oldToken.setValue("OLD_TOKEN");
     
      try{
        dao.refreshToken(oldToken);
      }catch(GuideException ex){
         Assert.assertTrue(WebApplicationException.class.isAssignableFrom(ex.getCause().getClass()));
        	return;
      }
     	Assert.fail();
     	
	}

	@Test
	public void getTokenUseExistingTest() throws Exception {
		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);
		TvdbToken returnToken = dao.getToken();
		Assert.assertEquals(token, returnToken);
	}

	@Test
	public void getTokenLoginTest() throws Exception {
     	APIKeyEntityGenerator request = new APIKeyEntityGenerator("APIKEY");
		TokenEntityGenerator response = new TokenEntityGenerator("TOKEN");

     stubFor(post(urlEqualTo("/login"))
            .withRequestBody(equalToJson(request.toString()))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(response.toString())));
     
		dao.setBaseUri(new URI("http://localhost:8089"));
     	dao.setClient(client);
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);

		dao.setApiKey("APIKEY");

		TvdbToken token = dao.getToken();
		Assert.assertNotNull(token);
		Assert.assertEquals("TOKEN", token.getValue());
	}

	@Test
	public void getTokenRefreshTest() throws Exception {
		TokenEntityGenerator response = new TokenEntityGenerator("NEW_TOKEN");
     
     stubFor(get(urlEqualTo("/refresh_token"))
            .withHeader("Authorization", equalTo("Bearer OLD_TOKEN"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(response.toString())));
     
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
     	dao.setClient(client);

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
		SearchEntityGenerator response = new SearchEntityGenerator();
		response.addSeries("DESCRIPTION", "GUIDEID", "TITLE", "2005-03-11");
     
     stubFor(get(urlEqualTo("/search/series?name=TITLE"))
            .withHeader("Authorization", equalTo("Bearer TOKEN"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(response.toString())));
     
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
     	dao.setClient(client);

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
  

	@Test
	public void searchNotAuthorisedTest() throws Exception {
		ErrorEntityGenerator response = new ErrorEntityGenerator("THIS IS AN ERROR");

     stubFor(get(urlEqualTo("/search/series?name=TITLE"))
            .withHeader("Authorization", equalTo("Bearer TOKEN"))
            .willReturn(aResponse()
                .withStatus(401)
                .withHeader("Content-Type", "application/json")
                .withBody(response.toString())));
     
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
     	dao.setClient(client);

		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);

      try{
        dao.search("TITLE");
      }catch(GuideNotAuthorisedException ex){
      	Assert.assertEquals("THIS IS AN ERROR", ex.getMessage());
         Assert.assertEquals(NotAuthorizedException.class,ex.getCause().getClass());
        	return;
      }
     	Assert.fail();
	}
	
  	@Test
	public void searchNotFoundTest() throws Exception {
		ErrorEntityGenerator response = new ErrorEntityGenerator("THIS IS AN ERROR");
     
     stubFor(get(urlEqualTo("/search/series?name=TITLE"))
            .withHeader("Authorization", equalTo("Bearer TOKEN"))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody(response.toString())));
     
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
     	dao.setClient(client);

		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);

      try{
        dao.search("TITLE");
      }catch(GuideNotFoundException ex){
      	Assert.assertEquals("THIS IS AN ERROR", ex.getMessage());
         Assert.assertEquals(NotFoundException.class,ex.getCause().getClass());
         return;
      }
     Assert.fail();
	}
  
	@Test
	public void searchExceptionTest() throws Exception {
  
     stubFor(get(urlEqualTo("/search/series?name=TITLE"))
            .withHeader("Authorization", equalTo("Bearer TOKEN"))
            .willReturn(aResponse()
                .withStatus(500)
                .withHeader("Content-Type", "application/json")));
     
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
     	dao.setClient(client);

		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);
     
      try{
        dao.search("TITLE");
      }catch(GuideException ex){
         Assert.assertTrue(WebApplicationException.class.isAssignableFrom(ex.getCause().getClass()));
         return;
      }
     	Assert.fail();
	}
  
	@Test(expected=RuntimeException.class)
	public void searchUnsupportedEncodingTest() throws Exception {
		dao.setEncoding("JIBBERISH");
		
		dao.search("TITLE");
	}
  
	@Test
	public void infoTest() throws Exception {
		InfoEntityGenerator response = new InfoEntityGenerator("DESCRIPTION", "GUIDEID", "TITLE", "Ended", "CBS", "45",
				"Monday", "8:00pm", "IMDB", Arrays.asList("GENRE"),"2015-12-28",1454486401);
     
     stubFor(get(urlEqualTo("/series/GUIDEID"))
            .withHeader("Authorization", equalTo("Bearer TOKEN"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(response.toString())));
     
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
     	dao.setClient(client);
     
		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);

		GuideInfo info = dao.info("GUIDEID");
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

	@Test
	public void infoNotAuthorisedTest() throws Exception {
		ErrorEntityGenerator response = new ErrorEntityGenerator("THIS IS AN ERROR");
     
     stubFor(get(urlEqualTo("/series/GUIDEID"))
            .withHeader("Authorization", equalTo("Bearer TOKEN"))
            .willReturn(aResponse()
                .withStatus(401)
                .withHeader("Content-Type", "application/json")
                .withBody(response.toString())));
     
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
     	dao.setClient(client);
     
		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);

      try{
        dao.info("GUIDEID");
      }catch(GuideNotAuthorisedException ex){
      	Assert.assertEquals("THIS IS AN ERROR", ex.getMessage());
         Assert.assertEquals(NotAuthorizedException.class,ex.getCause().getClass());
         return;
      }
     	Assert.fail();
	}
	
  	@Test
	public void infoNotFoundTest() throws Exception {
		ErrorEntityGenerator response = new ErrorEntityGenerator("THIS IS AN ERROR");
     
     stubFor(get(urlEqualTo("/series/GUIDEID"))
            .withHeader("Authorization", equalTo("Bearer TOKEN"))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody(response.toString())));
     
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
     	dao.setClient(client);

		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);

      try{
        dao.info("GUIDEID");
      }catch(GuideNotFoundException ex){
      	Assert.assertEquals("THIS IS AN ERROR", ex.getMessage());
         Assert.assertEquals(NotFoundException.class,ex.getCause().getClass());
         return;
      }
     	Assert.fail();
	}
  
	@Test
	public void infoExceptionTest() throws Exception {
     
     stubFor(get(urlEqualTo("/series/GUIDEID"))
            .withHeader("Authorization", equalTo("Bearer TOKEN"))
            .willReturn(aResponse()
                .withStatus(500)
                .withHeader("Content-Type", "application/json")));
     
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
     	dao.setClient(client);

		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);
     
      try{
        dao.info("GUIDEID");
      }catch(GuideException ex){
         Assert.assertTrue(WebApplicationException.class.isAssignableFrom(ex.getCause().getClass()));
         return;
      }
     	Assert.fail();
	}
  
	@Test(expected=RuntimeException.class)
	public void infoUnsupportedEncodingTest() throws Exception {
		dao.setEncoding("JIBBERISH");
		
		dao.info("TITLE");
	}
  
	@Test
	public void episodesTest() throws Exception {
		EpisodesEntityGenerator response = new EpisodesEntityGenerator(null, 0, 0, 0);
		response.addEpisode(0, Integer.MIN_VALUE, Integer.MAX_VALUE, "TITLE", "2015-09-29");
     
     stubFor(get(urlEqualTo("/series/GUIDEID/episodes?page=1"))
            .withHeader("Authorization", equalTo("Bearer TOKEN"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(response.toString())));
     
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
     	dao.setClient(client);
     
		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);

		TvdbEpisodes result = dao.episodes("GUIDEID", 1);
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


	@Test
	public void episodesNotAuthorisedTest() throws Exception {
		ErrorEntityGenerator response = new ErrorEntityGenerator("THIS IS AN ERROR");
     
     stubFor(get(urlEqualTo("/series/GUIDEID/episodes?page=1"))
            .withHeader("Authorization", equalTo("Bearer TOKEN"))
            .willReturn(aResponse()
                .withStatus(401)
                .withHeader("Content-Type", "application/json")
                .withBody(response.toString())));
     
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
     	dao.setClient(client);
  
		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);

      try{
         dao.episodes("GUIDEID",1);
      }catch(GuideNotAuthorisedException ex){
      	Assert.assertEquals("THIS IS AN ERROR", ex.getMessage());
         Assert.assertEquals(NotAuthorizedException.class,ex.getCause().getClass());
         return;
      }
     	Assert.fail();
	}
	
  	@Test
	public void episodesNotFoundTest() throws Exception {
		ErrorEntityGenerator response = new ErrorEntityGenerator("THIS IS AN ERROR");
     
     stubFor(get(urlEqualTo("/series/GUIDEID/episodes?page=1"))
            .withHeader("Authorization", equalTo("Bearer TOKEN"))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody(response.toString())));
     
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
     	dao.setClient(client);
  
		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);

      try{
         dao.episodes("GUIDEID",1);
      }catch(GuideNotFoundException ex){
      	Assert.assertEquals("THIS IS AN ERROR", ex.getMessage());
         Assert.assertEquals(NotFoundException.class,ex.getCause().getClass());
         return;
      }
     	Assert.fail();
	}
  
	@Test
	public void episodesExceptionTest() throws Exception {
     
     stubFor(get(urlEqualTo("/series/GUIDEID/episodes?page=1"))
            .withHeader("Authorization", equalTo("Bearer TOKEN"))
            .willReturn(aResponse()
                .withStatus(500)
                .withHeader("Content-Type", "application/json")));
     
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
     	dao.setClient(client);
 
		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);
     
      try{
         dao.episodes("GUIDEID",1);
      }catch(GuideException ex){
         Assert.assertTrue(WebApplicationException.class.isAssignableFrom(ex.getCause().getClass()));
         return;
      }
     	Assert.fail();
	}
  
	@Test
	public void episodeListNoNextTest() throws Exception {
		EpisodesEntityGenerator response = new EpisodesEntityGenerator(1, 1, 0, 0);
		response.addEpisode(0, Integer.MIN_VALUE, Integer.MAX_VALUE, "TITLE", "2015-09-29");
     
     stubFor(get(urlEqualTo("/series/GUIDEID/episodes?page=1"))
            .withHeader("Authorization", equalTo("Bearer TOKEN"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(response.toString())));
     
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
     	dao.setClient(client);

		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);

		List<GuideEpisode> episodes = dao.episodes("GUIDEID");
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
		EpisodesEntityGenerator responseOne = new EpisodesEntityGenerator(1, 2, 2, 0);
		responseOne.addEpisode(0, Integer.MIN_VALUE, Integer.MAX_VALUE, "TITLE", "2015-09-29");

		EpisodesEntityGenerator responseTwo = new EpisodesEntityGenerator(2, 2, 0, 1);
		responseTwo.addEpisode(0, Integer.MAX_VALUE, Integer.MIN_VALUE, "TITLE_TWO", "2005-09-29");
    
     stubFor(get(urlEqualTo("/series/GUIDEID/episodes?page=1"))
            .withHeader("Authorization", equalTo("Bearer TOKEN"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(responseOne.toString())));
     
     stubFor(get(urlEqualTo("/series/GUIDEID/episodes?page=2"))
            .withHeader("Authorization", equalTo("Bearer TOKEN"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(responseTwo.toString())));
     
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));
     	dao.setClient(client);

		dao.setValidPeriodInMs(Integer.MAX_VALUE);
		TvdbToken token = new TvdbToken();
		token.setValue("TOKEN");
		dao.setToken(token);

		List<GuideEpisode> episodes = dao.episodes("GUIDEID");
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
