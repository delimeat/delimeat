package io.delimeat.rest;

import io.delimeat.core.guide.AiringDay;
import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.core.guide.GuideInfo;
import io.delimeat.core.guide.GuideSearchResult;
import io.delimeat.core.service.GuideService;
import io.delimeat.util.jaxrs.AddETagResponseFilter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class GuideResourceTest extends JerseyTest {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	private GuideService mockedGuideService = Mockito.mock(GuideService.class);

	@Override
	protected Application configure() {
		ResourceConfig config = new ResourceConfig();
		config.register(GuideResource.class);
     	config.register(AddETagResponseFilter.class);
		config.register(new AbstractBinder() {

			@Override
			protected void configure() {
				bind(mockedGuideService).to(GuideService.class);
			}

		});
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		return config;
	}

	@Test
	public void searchNoETagTest() throws IOException, Exception {
		GuideSearchResult result = new GuideSearchResult();
		result.setDescription("DESCRIPTION");
		result.setFirstAired(SDF.parse("2015-12-01"));
		result.setGuideId("VALUE");
		result.setTitle("TITLE");
		List<GuideSearchResult> expectedResults = new ArrayList<GuideSearchResult>();
		expectedResults.add(result);

		Mockito.when(mockedGuideService.readLike(Mockito.anyString())).thenReturn(expectedResults);

		Response response = target("guide").path("search").path("title").request().get();
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("application/json",response.getHeaderString("Content-Type"));

     	EntityTag etag = new EntityTag(Integer.toString(expectedResults.hashCode()));
     	Assert.assertEquals("\""+etag.getValue() +"\"", response.getHeaderString("ETag"));
     	
		List<GuideSearchResult> actualResults = response.readEntity(new GenericType<List<GuideSearchResult>>() {});
		Assert.assertEquals(1, actualResults.size());
		Assert.assertEquals("DESCRIPTION", actualResults.get(0).getDescription());
		Assert.assertEquals("2015-12-01",SDF.format(actualResults.get(0).getFirstAired()));
		Assert.assertEquals("VALUE", actualResults.get(0).getGuideId());
		Assert.assertEquals("TITLE", actualResults.get(0).getTitle());
	}

	@Test
	public void searchNoMatchingETagTest() throws IOException, Exception {
		GuideSearchResult result = new GuideSearchResult();
		result.setDescription("DESCRIPTION");
		result.setFirstAired(SDF.parse("2015-12-01"));
		result.setGuideId("VALUE");

		result.setTitle("TITLE");
		List<GuideSearchResult> expectedResults = new ArrayList<GuideSearchResult>();
		expectedResults.add(result);

		Mockito.when(mockedGuideService.readLike(Mockito.anyString())).thenReturn(expectedResults);

		Response response = target("guide").path("search").path("title").request().header("If-None-Match", "\"INVALID_ETAG\"").get();
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("application/json",response.getHeaderString("Content-Type"));

     	EntityTag etag = new EntityTag(Integer.toString(expectedResults.hashCode()));
     	Assert.assertEquals("\""+etag.getValue() +"\"", response.getHeaderString("ETag"));
     	
		List<GuideSearchResult> actualResults = response.readEntity(new GenericType<List<GuideSearchResult>>() {});
		Assert.assertEquals(1, actualResults.size());
		Assert.assertEquals("DESCRIPTION", actualResults.get(0).getDescription());
		Assert.assertEquals("2015-12-01",SDF.format(actualResults.get(0).getFirstAired()));
		Assert.assertEquals("VALUE", actualResults.get(0).getGuideId());
		Assert.assertEquals("TITLE", actualResults.get(0).getTitle());
	}
	

	@Test
	public void searchMatchingETagTest() throws IOException, Exception {
		GuideSearchResult result = new GuideSearchResult();
		result.setDescription("DESCRIPTION");
		result.setFirstAired(SDF.parse("2015-12-01"));
		result.setGuideId("VALUE");
		result.setTitle("TITLE");
		List<GuideSearchResult> expectedResults = new ArrayList<GuideSearchResult>();
		expectedResults.add(result);

		Mockito.when(mockedGuideService.readLike(Mockito.anyString())).thenReturn(expectedResults);

     	EntityTag etag = new EntityTag(Integer.toString(expectedResults.hashCode()));

		Response response = target("guide").path("search").path("title").request().header("If-None-Match", "\" "+ etag.getValue() +"\"").get();
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("application/json",response.getHeaderString("Content-Type"));

     	Assert.assertEquals("\""+etag.getValue() +"\"", response.getHeaderString("ETag"));
     	
		List<GuideSearchResult> actualResults = response.readEntity(new GenericType<List<GuideSearchResult>>() {});
		Assert.assertEquals(1, actualResults.size());
		Assert.assertEquals("DESCRIPTION", actualResults.get(0).getDescription());
		Assert.assertEquals("2015-12-01",SDF.format(actualResults.get(0).getFirstAired()));
		Assert.assertEquals("VALUE", actualResults.get(0).getGuideId());
		Assert.assertEquals("TITLE", actualResults.get(0).getTitle());
	}
	
	@Test
	public void infoNoETagTest()  throws IOException, Exception {
		GuideInfo expectedInfo = new GuideInfo();
		expectedInfo.getAirDays().add(AiringDay.FRIDAY);
		expectedInfo.setAiring(false);
		expectedInfo.setAirTime(9900000);
		expectedInfo.setDescription("DESCRIPTION");
		expectedInfo.getGenres().add("GENRE");
		expectedInfo.setGuideId("VALUE");
		expectedInfo.setTimezone("TIMEZONE");
		expectedInfo.setRunningTime(Integer.MIN_VALUE);
		expectedInfo.setTitle("TITLE");
		
		Mockito.when(mockedGuideService.read( Mockito.anyString())).thenReturn(expectedInfo);

		Response response = target("guide").path("info").path("ID").request().get();
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("application/json",response.getHeaderString("Content-Type"));

     	EntityTag etag = new EntityTag(Integer.toString(expectedInfo.hashCode()));
     	Assert.assertEquals("\""+etag.getValue() +"\"", response.getHeaderString("ETag"));
		
		GuideInfo actualInfo = response.readEntity(GuideInfo.class);
		Assert.assertNotNull(actualInfo.getAirDays());
		Assert.assertEquals(1, actualInfo.getAirDays().size());
		Assert.assertEquals(AiringDay.FRIDAY, actualInfo.getAirDays().get(0));
		Assert.assertFalse(actualInfo.isAiring());
		Assert.assertEquals(9900000, actualInfo.getAirTime());
		Assert.assertEquals("DESCRIPTION", actualInfo.getDescription());
		Assert.assertNotNull(actualInfo.getGenres());
		Assert.assertEquals(1, actualInfo.getGenres().size());
		Assert.assertEquals("GENRE", actualInfo.getGenres().get(0));
		Assert.assertEquals("VALUE", actualInfo.getGuideId());
		Assert.assertEquals("TIMEZONE", actualInfo.getTimezone());
		Assert.assertEquals(Integer.MIN_VALUE, actualInfo.getRunningTime());
		Assert.assertEquals("TITLE", actualInfo.getTitle());	
	}
	
	@Test
	public void infoNoMatchingETagTest()  throws IOException, Exception {
		GuideInfo expectedInfo = new GuideInfo();
		expectedInfo.getAirDays().add(AiringDay.FRIDAY);
		expectedInfo.setAiring(false);
		expectedInfo.setAirTime(9900000);
		expectedInfo.setDescription("DESCRIPTION");
		expectedInfo.getGenres().add("GENRE");
		expectedInfo.setGuideId("VALUE");
		expectedInfo.setTimezone("TIMEZONE");
		expectedInfo.setRunningTime(Integer.MIN_VALUE);
		expectedInfo.setTitle("TITLE");
		
		Mockito.when(mockedGuideService.read( Mockito.anyString())).thenReturn(expectedInfo);

		Response response = target("guide").path("info").path("ID").request().header("If-None-Match", "\"INVALID_ETAG\"").get();
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("application/json",response.getHeaderString("Content-Type"));

     	EntityTag etag = new EntityTag(Integer.toString(expectedInfo.hashCode()));
     	Assert.assertEquals("\""+etag.getValue() +"\"", response.getHeaderString("ETag"));
		
		GuideInfo actualInfo = response.readEntity(GuideInfo.class);
		Assert.assertNotNull(actualInfo.getAirDays());
		Assert.assertEquals(1, actualInfo.getAirDays().size());
		Assert.assertEquals(AiringDay.FRIDAY, actualInfo.getAirDays().get(0));
		Assert.assertFalse(actualInfo.isAiring());
		Assert.assertEquals(9900000, actualInfo.getAirTime());
		Assert.assertEquals("DESCRIPTION", actualInfo.getDescription());
		Assert.assertNotNull(actualInfo.getGenres());
		Assert.assertEquals(1, actualInfo.getGenres().size());
		Assert.assertEquals("GENRE", actualInfo.getGenres().get(0));
		Assert.assertEquals("VALUE", actualInfo.getGuideId());
		Assert.assertEquals("TIMEZONE", actualInfo.getTimezone());
		Assert.assertEquals(Integer.MIN_VALUE, actualInfo.getRunningTime());
		Assert.assertEquals("TITLE", actualInfo.getTitle());	
	}
	
	@Test
	public void infoMatchingETagTest()  throws IOException, Exception {
		GuideInfo expectedInfo = new GuideInfo();
		expectedInfo.getAirDays().add(AiringDay.FRIDAY);
		expectedInfo.setAiring(false);
		expectedInfo.setAirTime(9900000);
		expectedInfo.setDescription("DESCRIPTION");
		expectedInfo.getGenres().add("GENRE");
		expectedInfo.setGuideId("VALUE");
		expectedInfo.setTimezone("TIMEZONE");
		expectedInfo.setRunningTime(Integer.MIN_VALUE);
		expectedInfo.setTitle("TITLE");
		
		Mockito.when(mockedGuideService.read( Mockito.anyString())).thenReturn(expectedInfo);

     	EntityTag etag = new EntityTag(Integer.toString(expectedInfo.hashCode()));

		Response response = target("guide").path("info").path("ID").request().header("If-None-Match", "\" "+ etag.getValue() +"\"").get();
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("application/json",response.getHeaderString("Content-Type"));

     	Assert.assertEquals("\""+etag.getValue() +"\"", response.getHeaderString("ETag"));
		
		GuideInfo actualInfo = response.readEntity(GuideInfo.class);
		Assert.assertNotNull(actualInfo.getAirDays());
		Assert.assertEquals(1, actualInfo.getAirDays().size());
		Assert.assertEquals(AiringDay.FRIDAY, actualInfo.getAirDays().get(0));
		Assert.assertFalse(actualInfo.isAiring());
		Assert.assertEquals(9900000, actualInfo.getAirTime());
		Assert.assertEquals("DESCRIPTION", actualInfo.getDescription());
		Assert.assertNotNull(actualInfo.getGenres());
		Assert.assertEquals(1, actualInfo.getGenres().size());
		Assert.assertEquals("GENRE", actualInfo.getGenres().get(0));
		Assert.assertEquals("VALUE", actualInfo.getGuideId());
		Assert.assertEquals("TIMEZONE", actualInfo.getTimezone());
		Assert.assertEquals(Integer.MIN_VALUE, actualInfo.getRunningTime());
		Assert.assertEquals("TITLE", actualInfo.getTitle());	
	}
		
	@Test
	public void episodesNoETagTest() throws IOException, Exception{
		GuideEpisode expectedEp = new GuideEpisode();
		expectedEp.setAirDate(SDF.parse("2015-12-21"));
		expectedEp.setEpisodeNum(1);
		expectedEp.setProductionNum(2);
		expectedEp.setSeasonNum(3);
		expectedEp.setTitle("TITLE");
		List<GuideEpisode> expectedEps = Arrays.asList(expectedEp);

		Mockito.when(mockedGuideService.readEpisodes( Mockito.anyString())).thenReturn(expectedEps);
		
		Response response = target("guide").path("info").path("ID").path("episodes").request().get();
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("application/json",response.getHeaderString("Content-Type"));

     	EntityTag etag = new EntityTag(Integer.toString(expectedEps.hashCode()));
     	Assert.assertEquals("\""+etag.getValue() +"\"", response.getHeaderString("ETag"));
     	
		List<GuideEpisode> actualResults = response.readEntity(new GenericType<List<GuideEpisode>>() {});
		Assert.assertNotNull(actualResults);
		Assert.assertEquals(1, actualResults.size());
		Assert.assertEquals("2015-12-21", SDF.format(actualResults.get(0).getAirDate()));
		Assert.assertEquals(1, actualResults.get(0).getEpisodeNum().intValue());
		Assert.assertEquals(2, actualResults.get(0).getProductionNum().intValue());
		Assert.assertEquals(3, actualResults.get(0).getSeasonNum().intValue());
		Assert.assertEquals("TITLE", actualResults.get(0).getTitle());
	}
	
	@Test
	public void episodesNoMatchingETagTest() throws IOException, Exception{
		GuideEpisode expectedEp = new GuideEpisode();
		expectedEp.setAirDate(SDF.parse("2015-12-21"));
		expectedEp.setEpisodeNum(1);
		expectedEp.setProductionNum(2);
		expectedEp.setSeasonNum(3);
		expectedEp.setTitle("TITLE");
		List<GuideEpisode> expectedEps = Arrays.asList(expectedEp);
	
		Mockito.when(mockedGuideService.readEpisodes( Mockito.anyString())).thenReturn(expectedEps);
		
		Response response = target("guide").path("info").path("ID").path("episodes").request().header("If-None-Match", "\"INVALID_ETAG\"").get();
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("application/json",response.getHeaderString("Content-Type"));
	
	 	EntityTag etag = new EntityTag(Integer.toString(expectedEps.hashCode()));
	 	Assert.assertEquals("\""+etag.getValue() +"\"", response.getHeaderString("ETag"));
	 	
		List<GuideEpisode> actualResults = response.readEntity(new GenericType<List<GuideEpisode>>() {});
		Assert.assertNotNull(actualResults);
		Assert.assertEquals(1, actualResults.size());
		Assert.assertEquals("2015-12-21", SDF.format(actualResults.get(0).getAirDate()));
		Assert.assertEquals(1, actualResults.get(0).getEpisodeNum().intValue());
		Assert.assertEquals(2, actualResults.get(0).getProductionNum().intValue());
		Assert.assertEquals(3, actualResults.get(0).getSeasonNum().intValue());
		Assert.assertEquals("TITLE", actualResults.get(0).getTitle());
	}
	
	@Test
	public void episodesMatchingETagTest() throws IOException, Exception{
		GuideEpisode expectedEp = new GuideEpisode();
		expectedEp.setAirDate(SDF.parse("2015-12-21"));
		expectedEp.setEpisodeNum(1);
		expectedEp.setProductionNum(2);
		expectedEp.setSeasonNum(3);
		expectedEp.setTitle("TITLE");
		List<GuideEpisode> expectedEps = Arrays.asList(expectedEp);
	
		Mockito.when(mockedGuideService.readEpisodes( Mockito.anyString())).thenReturn(expectedEps);
		
	 	EntityTag etag = new EntityTag(Integer.toString(expectedEps.hashCode()));

		Response response = target("guide").path("info").path("ID").path("episodes").request().header("If-None-Match", "\" "+ etag.getValue() +"\"").get();
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("application/json",response.getHeaderString("Content-Type"));
	
	 	Assert.assertEquals("\""+etag.getValue() +"\"", response.getHeaderString("ETag"));
	 	
		List<GuideEpisode> actualResults = response.readEntity(new GenericType<List<GuideEpisode>>() {});
		Assert.assertNotNull(actualResults);
		Assert.assertEquals(1, actualResults.size());
		Assert.assertEquals("2015-12-21", SDF.format(actualResults.get(0).getAirDate()));
		Assert.assertEquals(1, actualResults.get(0).getEpisodeNum().intValue());
		Assert.assertEquals(2, actualResults.get(0).getProductionNum().intValue());
		Assert.assertEquals(3, actualResults.get(0).getSeasonNum().intValue());
		Assert.assertEquals("TITLE", actualResults.get(0).getTitle());
	}
}
