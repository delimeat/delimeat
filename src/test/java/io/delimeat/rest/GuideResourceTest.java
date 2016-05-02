package io.delimeat.rest;

import io.delimeat.core.guide.AiringDay;
import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.core.guide.GuideInfo;
import io.delimeat.core.guide.GuideSearchResult;
import io.delimeat.core.service.GuideService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Application;
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

  	private GuideSearchResult createSearchResult() throws ParseException{
		GuideSearchResult result = new GuideSearchResult();
		result.setDescription("DESCRIPTION");
		result.setFirstAired(SDF.parse("2016-04-05"));
		result.setGuideId("VALUE");
		result.setTitle("TITLE");
     return result;
   }
  
  	public GuideInfo createInfo(){
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
     	return expectedInfo;
   }
  
  	private GuideEpisode createEpisode(){
		GuideEpisode expectedEp = new GuideEpisode();
		expectedEp.setAirDate(new Date(0));
		expectedEp.setEpisodeNum(1);
		expectedEp.setProductionNum(2);
		expectedEp.setSeasonNum(3);
		expectedEp.setTitle("TITLE");
     	return expectedEp;
   }
  
	@Test
	public void searchTest() throws IOException, Exception {
		GuideSearchResult result = createSearchResult();
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
		Assert.assertEquals(result, actualResults.get(0));
     
     	Mockito.verify(mockedGuideService).readLike("title");
     	Mockito.verifyNoMoreInteractions(mockedGuideService);
	}

	@Test
	public void infoTest()  throws IOException, Exception {
		GuideInfo expectedInfo = createInfo();
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
     	Assert.assertEquals(expectedInfo, actualInfo);
     
     	Mockito.verify(mockedGuideService).read("ID");
     	Mockito.verifyNoMoreInteractions(mockedGuideService);

	}
		
	@Test
	public void episodesTest() throws IOException, Exception{
		GuideEpisode expectedEp = createEpisode();
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
		Assert.assertEquals(expectedEp, actualResults.get(0));
     
		Mockito.verify(mockedGuideService).readEpisodes("ID");
     	Mockito.verifyNoMoreInteractions(mockedGuideService);

	}
}
