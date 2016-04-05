package io.delimeat.rest;

import io.delimeat.core.guide.AiringDay;
import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.core.guide.GuideInfo;
import io.delimeat.core.guide.GuideSearchResult;
import io.delimeat.core.service.GuideService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
		List<GuideSearchResult> expectedResults = Arrays.asList(result);
		Mockito.when(mockedGuideService.readLike("title")).thenReturn(expectedResults);

		Response response = target("guide")
        								.path("search")
        								.path("title")
        								.request()
        								.get();
     
		Assert.assertEquals(Status.OK, response.getStatusInfo());
     	Assert.assertTrue(response.hasEntity());
		Assert.assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
     	
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
		List<GuideSearchResult> expectedResults = Arrays.asList(result);
		Mockito.when(mockedGuideService.readLike("title")).thenReturn(expectedResults);

     	EntityTag etag = new EntityTag("INVALID_ETAG");
     
		Response response = target("guide")
        								.path("search")
        								.path("title")
        								.request()
        								.header(HttpHeaders.IF_NONE_MATCH,etag)
        								.get();
     
		Assert.assertEquals(Status.OK, response.getStatusInfo());
     	Assert.assertTrue(response.hasEntity());
		Assert.assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
     	
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
		List<GuideSearchResult> expectedResults = Arrays.asList(result);
		Mockito.when(mockedGuideService.readLike("title")).thenReturn(expectedResults);

     	EntityTag etag = new EntityTag(Integer.toString(expectedResults.hashCode()));

		Response response = target("guide")
        								.path("search")
        								.path("title")
        								.request()
        								.header(HttpHeaders.IF_NONE_MATCH, etag)
        								.get();
     
		Assert.assertEquals(Status.NOT_MODIFIED, response.getStatusInfo());
     	Assert.assertFalse(response.hasEntity());    
     	Assert.assertEquals(etag.toString(), response.getHeaderString(HttpHeaders.ETAG));
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
		Mockito.when(mockedGuideService.read("ID")).thenReturn(expectedInfo);

		Response response = target("guide")
        								.path("info")
        								.path("ID")
        								.request()
        								.get();
     
		Assert.assertEquals(Status.OK, response.getStatusInfo());
     	Assert.assertTrue(response.hasEntity());
		Assert.assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
		
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
		Mockito.when(mockedGuideService.read("ID")).thenReturn(expectedInfo);

     	EntityTag etag = new EntityTag("INVALID_ETAG");

		Response response = target("guide")
        								.path("info")
        								.path("ID")
        								.request()
        								.header(HttpHeaders.IF_NONE_MATCH,etag)
        								.get();
     
		Assert.assertEquals(Status.OK, response.getStatusInfo());
     	Assert.assertTrue(response.hasEntity());
		Assert.assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
		
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
		Mockito.when(mockedGuideService.read("ID")).thenReturn(expectedInfo);

     	EntityTag etag = new EntityTag(Integer.toString(expectedInfo.hashCode()));

		Response response = target("guide")
        								.path("info")
        								.path("ID")
        								.request()
        								.header(HttpHeaders.IF_NONE_MATCH,etag)
        								.get();
     
		Assert.assertEquals(Status.NOT_MODIFIED, response.getStatusInfo());
     	Assert.assertFalse(response.hasEntity());   
     	Assert.assertEquals(etag.toString(), response.getHeaderString(HttpHeaders.ETAG));
		
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
		Mockito.when(mockedGuideService.readEpisodes("ID")).thenReturn(expectedEps);
		
		Response response = target("guide")
        								.path("info")
        								.path("ID")
        								.path("episodes")
        								.request()
        								.get();
     
		Assert.assertEquals(Status.OK, response.getStatusInfo());
     	Assert.assertTrue(response.hasEntity());
		Assert.assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
     	
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
		Mockito.when(mockedGuideService.readEpisodes("ID")).thenReturn(expectedEps);
		
     	EntityTag etag = new EntityTag("INVALID_ETAG");
     
		Response response = target("guide")
        								.path("info")
        								.path("ID")
        								.path("episodes")
        								.request()
        								.header(HttpHeaders.IF_NONE_MATCH,etag)
        								.get();
     
		Assert.assertEquals(Status.OK, response.getStatusInfo());
     	Assert.assertTrue(response.hasEntity());
		Assert.assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
		
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
		Mockito.when(mockedGuideService.readEpisodes("ID")).thenReturn(expectedEps);
		
	 	EntityTag etag = new EntityTag(Integer.toString(expectedEps.hashCode()));

		Response response = target("guide")
        								.path("info")
        								.path("ID")
        								.path("episodes")
        								.request()
        								.header(HttpHeaders.IF_NONE_MATCH,etag)
        								.get();
     
		Assert.assertEquals(Status.NOT_MODIFIED, response.getStatusInfo());
      Assert.assertFalse(response.hasEntity());
	 	Assert.assertEquals(etag.toString(), response.getHeaderString(HttpHeaders.ETAG));
	}
}
