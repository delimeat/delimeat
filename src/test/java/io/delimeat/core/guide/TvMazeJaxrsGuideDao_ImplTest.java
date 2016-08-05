package io.delimeat.core.guide;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import io.delimeat.util.jaxrs.CustomMOXyJsonProvider;
import io.delimeat.util.jaxrs.JaxbContextResolver;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Rule;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class TvMazeJaxrsGuideDao_ImplTest {

  	private class ErrorEntityGenerator {

		private StringBuffer xml;

		public ErrorEntityGenerator(String message) {
			xml = new StringBuffer();
			xml.append("{");
			xml.append("\"name\":\"" + message + "\"");
		}

		public String toString() {
			return xml.toString() + "}";
		}

	}
  
	private class EpisodesEntityGenerator {
		private StringBuffer xml;
		private boolean first = true;

		public EpisodesEntityGenerator() {
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

	}

	private class InfoEntityGenerator {
		private StringBuffer xml;

		public InfoEntityGenerator(String description, String guideid, String title, String runtime, String tvdbId,
				String tvrageId, List<String> genres, String status, String timezone, String time, List<String> days, Long lastUpdated) {
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
			xml.append("},");
			xml.append("\"updated\":" + lastUpdated + "");
		}

		public String toString() {
			return xml.toString() + "}";
		}

	}

	private class SearchEntityGenerator {
		private StringBuffer xml;

		public SearchEntityGenerator() {
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

	}

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	private static final String METADATA = "META-INF/oxm/guide-tvmaze-oxm.xml";
  
  	private static Client client;
  
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);
	
	private TvMazeJaxrsGuideDao_Impl dao;

  	@BeforeClass 
  	public static void setUpClass() throws Exception{
     	JaxbContextResolver resolver = new JaxbContextResolver();
     	resolver.setClasses(GuideSearchResult.class,GuideEpisode.class,GuideInfo.class);

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, Arrays.asList(METADATA));
     	properties.put(JAXBContextProperties.MEDIA_TYPE,"application/json");
     	properties.put(MarshallerProperties.JSON_INCLUDE_ROOT,false);
     	properties.put(Marshaller.JAXB_FORMATTED_OUTPUT, true);
     	resolver.setProperties(properties);
     	ClientConfig configuration = new ClientConfig();
     	configuration.register(resolver);
     	configuration.register(CustomMOXyJsonProvider.class);
     	Logger LOGGER = Logger.getLogger(LoggingFeature.class.getName());
     	configuration.register(new LoggingFeature(LOGGER));
     	configuration.property("jersey.config.disableMoxyJson", "true");
     	client  = JerseyClientBuilder.createClient(configuration);   
   }
  
	@Before
	public void setUp() throws Exception {
		dao = new TvMazeJaxrsGuideDao_Impl();
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
     	Assert.assertEquals(GuideSource.TVMAZE, dao.getGuideSource());
   }

	@Test
	public void infoTest() throws IOException, Exception {
		InfoEntityGenerator generator = new InfoEntityGenerator("DESCRIPTION", "GUIDEID", "TITLE", "60", "123", "432",
				Arrays.asList("GENRE1","GENRE2"), "Ended", "CBS", "20:00", Arrays.asList("Monday","Friday"), 1454486401L);

		stubFor(get(urlEqualTo("/shows/GUIDEID"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(generator.toString())));
     
     	dao.setClient(client);

		dao.setBaseUri(new URI("http://localhost:8089"));		
      dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);

		GuideInfo info = dao.info("GUIDEID");
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
  		Assert.assertEquals("2016-02-03", SDF.format(info.getLastUpdated()));

	}
  
  @Test(expected=GuideNotFoundException.class)
	public void infoNotFoundTest() throws Exception {
		ErrorEntityGenerator generator = new ErrorEntityGenerator("THIS IS AN ERROR");

		stubFor(get(urlEqualTo("/shows/GUIDEID"))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody(generator.toString())));
     
     	dao.setClient(client);

		dao.setBaseUri(new URI("http://localhost:8089"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);

      try{
        dao.info("GUIDEID");
      }catch(GuideNotFoundException ex){
      	Assert.assertEquals("THIS IS AN ERROR", ex.getMessage());
         Assert.assertEquals(NotFoundException.class,ex.getCause().getClass());
         throw ex;
      }
	}
  
	@Test(expected=GuideException.class)
	public void infoExceptionTest() throws Exception {
		stubFor(get(urlEqualTo("/shows/GUIDEID"))
            .willReturn(aResponse()
                .withStatus(500)
                .withHeader("Content-Type", "application/json")));
     
     	dao.setClient(client);

		dao.setBaseUri(new URI("http://localhost:8089"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
     
      dao.info("GUIDEID");
	}
  
	@Test(expected=RuntimeException.class)
	public void infoUnsupportedEncodingTest() throws Exception {
		dao.setEncoding("JIBBERISH");
		
		dao.info("GUIDEID");
	}

	@Test
	public void episodesTest() throws Exception {
		EpisodesEntityGenerator generator = new EpisodesEntityGenerator();
		generator.addEpisode(0, Integer.MIN_VALUE, Integer.MAX_VALUE, "TITLE", "2015-09-29");
		generator.addEpisode(1, Integer.MIN_VALUE, Integer.MAX_VALUE, "TITLE", "2015-09-30");

		stubFor(get(urlEqualTo("/shows/GUIDEID/episodes"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(generator.toString())));
     
     	dao.setClient(client);

		dao.setBaseUri(new URI("http://localhost:8089"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);

		List<GuideEpisode> result = dao.episodes("GUIDEID");
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
		ErrorEntityGenerator generator = new ErrorEntityGenerator("THIS IS AN ERROR");

		stubFor(get(urlEqualTo("/shows/GUIDEID/episodes"))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody(generator.toString())));
     
     	dao.setClient(client);

		dao.setBaseUri(new URI("http://localhost:8089"));
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

		stubFor(get(urlEqualTo("/shows/GUIDEID/episodes"))
            .willReturn(aResponse()
                .withStatus(500)
                .withHeader("Content-Type", "application/json")));
     
     	dao.setClient(client);

		dao.setBaseUri(new URI("http://localhost:8089"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
      
     	dao.episodes("GUIDEID");
	}

	@Test(expected=RuntimeException.class)
	public void episodesUnsupportedEncodingTest() throws Exception {
		dao.setEncoding("JIBBERISH");
		
		dao.episodes("TITLE");
	}
  
	@Test
	public void searchTestWireMock() throws IOException, Exception {
		SearchEntityGenerator generator = new SearchEntityGenerator();
		generator.addSeries("DESCRIPTION", "GUIDEID", "TITLE", "2015-09-29", "123", "432");
     
		stubFor(get(urlEqualTo("/search/shows?q=title"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(generator.toString())));
     
     	dao.setClient(client);
     	dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		dao.setBaseUri(new URI("http://localhost:8089"));

		List<GuideSearchResult> results = dao.search("title");
     	System.out.println(results);
		Assert.assertNotNull(results);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals("GUIDEID", results.get(0).getGuideId());
		Assert.assertEquals("DESCRIPTION", results.get(0).getDescription());
		Assert.assertEquals("TITLE", results.get(0).getTitle());
		Assert.assertEquals("2015-09-29", SDF.format(results.get(0).getFirstAired()));
     
	}
  
	@Test(expected=GuideException.class)
  	public void searchExceptionTest() throws Exception{
		stubFor(get(urlEqualTo("/search/shows?q=title"))
            .willReturn(aResponse()
                .withStatus(500)));
     
		dao.setBaseUri(new URI("http://localhost:8089"));
		dao.setMediaType(MediaType.APPLICATION_JSON_TYPE);
     
     	dao.setClient(client);
     	dao.search("title");
   }
  
	@Test(expected=RuntimeException.class)
	public void searchUnsupportedEncodingTest() throws Exception {
		dao.setEncoding("JIBBERISH");
		
		dao.search("TITLE");
	}
}
