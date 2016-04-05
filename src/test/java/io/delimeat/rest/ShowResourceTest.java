package io.delimeat.rest;

import io.delimeat.core.service.ShowService;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowType;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Entity;
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

public class ShowResourceTest extends JerseyTest{

	private ShowService mockedShowService = Mockito.mock(ShowService.class);

	@Override
	protected Application configure() {
		ResourceConfig config = new ResourceConfig();
		config.register(ShowResource.class);
		config.register(new AbstractBinder() {

			@Override
			protected void configure() {
				bind(mockedShowService).to(ShowService.class);
			}

		});

		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		return config;
	}
  
  	private Show createShow() {
		Show show = new Show();
		show.setAiring(true);
		show.setAirTime(9900000);
		show.setEnabled(false);
		show.setIncludeSpecials(true);
		show.setLastFeedUpdate(new Date(0));
		show.setLastGuideUpdate(new Date(0));
		show.setShowId(Long.MIN_VALUE);
		show.setShowType(ShowType.MINI_SERIES);
		show.setTimezone("TIMEZONE");
		show.setTitle("TITLE");
		show.setVersion(97);
		
		Episode nextEp = new Episode();
		nextEp.setAirDate(new Date(0));
		nextEp.setDoubleEp(true);
		nextEp.setEpisodeId(Long.MAX_VALUE);
		nextEp.setEpisodeNum(101);
		nextEp.setSeasonNum(2);
		nextEp.setShow(show);
		nextEp.setTitle("NEXT_EP_TITLE");
		nextEp.setVersion(3);
		show.setNextEpisode(nextEp);
		
		Episode prevEp = new Episode();
		prevEp.setAirDate(new Date(0));
		prevEp.setDoubleEp(true);
		prevEp.setEpisodeId(Long.MIN_VALUE);
		prevEp.setEpisodeNum(102);
		prevEp.setSeasonNum(3);
		prevEp.setShow(show);
		prevEp.setTitle("PREV_EP_TITLE");
		prevEp.setVersion(4);
		show.setPreviousEpisode(prevEp);		

		show.setGuideId("GUIDE_ID");
     	return show;
   }
	
	@Test
	public void readAllNoEtagTest() throws Exception{
		Show show = createShow();
		List<Show> shows = Arrays.asList(show);
		Mockito.when(mockedShowService.readAll()).thenReturn(shows);
		
		Response response = target("shows")
        								.request()
        								.get();
     
		Assert.assertEquals(Status.OK, response.getStatusInfo());
     	Assert.assertTrue(response.hasEntity());
		Assert.assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
		
		List<Show> actualShows = response.readEntity(new GenericType<List<Show>>() {});
		Assert.assertNotNull(actualShows);
		Assert.assertEquals(1, actualShows.size());
		Assert.assertEquals(show, actualShows.get(0));
     
     	Mockito.verify(mockedShowService).readAll();
	}
  
	@Test
	public void readAllMatchingEtagTest() throws Exception{
		Show show = createShow();		
		List<Show> shows = Arrays.asList(show);
		Mockito.when(mockedShowService.readAll()).thenReturn(shows);
		
     	EntityTag etag = new EntityTag(Integer.toString(shows.hashCode()));
     
		Response response = target("shows")
        								.request()
        								.header(HttpHeaders.IF_NONE_MATCH, etag)
        								.get();
     
		Assert.assertEquals(Status.NOT_MODIFIED, response.getStatusInfo());
     	Assert.assertFalse(response.hasEntity());
     	Assert.assertEquals(etag.toString(), response.getHeaderString(HttpHeaders.ETAG)); 
     
     	Mockito.verify(mockedShowService).readAll();
	}
  
	@Test
	public void readAllNotMatchingEtagTest() throws Exception{
		Show show = createShow();
		List<Show> shows = Arrays.asList(show);
		Mockito.when(mockedShowService.readAll()).thenReturn(shows);
     
     	EntityTag etag = new EntityTag("INVALID_ETAG");

		Response response = target("shows")
        								.request()
        								.header(HttpHeaders.IF_NONE_MATCH, etag)
        								.get();
     
		Assert.assertEquals(Status.OK, response.getStatusInfo());
     	Assert.assertTrue(response.hasEntity());
		Assert.assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
		
		List<Show> actualShows = response.readEntity(new GenericType<List<Show>>() {});
		Assert.assertNotNull(actualShows);
		Assert.assertEquals(1, actualShows.size());
		Assert.assertEquals(show, actualShows.get(0));
     
     	Mockito.verify(mockedShowService).readAll();
	}
  
	@Test
	public void readNoEtagTest() throws Exception{
		Show show = createShow();
		Mockito.when(mockedShowService.read(1L)).thenReturn(show);
		
		Response response = target("shows")
        								.path("1")
        								.request()
        								.get();
     
		Assert.assertEquals(Status.OK, response.getStatusInfo());
     	Assert.assertTrue(response.hasEntity());
		Assert.assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
		
		Show actualShow = response.readEntity(Show.class);
		Assert.assertEquals(show, actualShow);
     
     	Mockito.verify(mockedShowService).read(1l);
	}
    
	@Test
	public void readNoMatchingEtagTest() throws Exception{
		Show show = createShow();
		Mockito.when(mockedShowService.read(1L)).thenReturn(show);
		  
     	EntityTag etag = new EntityTag("INVALID_ETAG");

		Response response = target("shows")
        								.path("1")	
        								.request()
        								.header(HttpHeaders.IF_NONE_MATCH, etag)
        								.get();
     
		Assert.assertEquals(Status.OK, response.getStatusInfo());
     	Assert.assertTrue(response.hasEntity());
		Assert.assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
		
		Show actualShow = response.readEntity(Show.class);
		Assert.assertEquals(show, actualShow);	

     Mockito.verify(mockedShowService).read(1l);
	}

	@Test
	public void readMatchingEtagTest() throws Exception{
		Show show = createShow();
		Mockito.when(mockedShowService.read(1L)).thenReturn(show);
		  
     	EntityTag etag = new EntityTag(Integer.toString(show.hashCode()));
     
		Response response = target("shows")
        								.path("1")
        								.request()
        								.header(HttpHeaders.IF_NONE_MATCH, etag)
        								.get();
     
		Assert.assertEquals(Status.NOT_MODIFIED, response.getStatusInfo());
     	Assert.assertFalse(response.hasEntity());
     	Assert.assertEquals(etag.toString(), response.getHeaderString(HttpHeaders.ETAG));  
     
     	Mockito.verify(mockedShowService).read(1l);
	}
  
	@Test
	public void updateNoEtagTest() throws Exception{
		Show show = createShow();
		Mockito.when(mockedShowService.update(Mockito.any(Show.class))).thenReturn(show);
     	Mockito.when(mockedShowService.read(1L)).thenReturn(show);
			
		Response response = target("shows")
        								.path("1")
        								.request()
        								.put(Entity.entity(new Show(), MediaType.APPLICATION_JSON_TYPE));
     
		Assert.assertEquals(Status.OK, response.getStatusInfo());
     	Assert.assertTrue(response.hasEntity());
		Assert.assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
     
		Show actualShow = response.readEntity(Show.class);
     	Assert.assertEquals(show, actualShow);
     
     	Mockito.verify(mockedShowService).read(1l);
     	Mockito.verify(mockedShowService).update(Mockito.any(Show.class));
	}
	  
  	@Test
  	public void updateNoMatchingEtagTest() throws Exception{
		Show show = createShow();
     	Mockito.when(mockedShowService.read(1L)).thenReturn(show);
			
		EntityTag etag = new EntityTag("INVALID_ETAG");
     
		Response response = target("shows")
        								.path("1")
        								.request()
        								.header(HttpHeaders.IF_MATCH, etag)
        								.put(Entity.entity(new Show(), MediaType.APPLICATION_JSON_TYPE));
     
		Assert.assertEquals(Status.PRECONDITION_FAILED, response.getStatusInfo());
     	Assert.assertFalse(response.hasEntity());
     
     	Mockito.verify(mockedShowService).read(1l);
     	Mockito.verify(mockedShowService, Mockito.times(0)).update(Mockito.any(Show.class));
   }
  
  	@Test
  	public void updateMatchingEtagTest() throws Exception{
		Show show = createShow();
		Mockito.when(mockedShowService.update(Mockito.any(Show.class))).thenReturn(show);
     	Mockito.when(mockedShowService.read(1L)).thenReturn(show);
			
     	EntityTag etag = new EntityTag(Integer.toString(show.hashCode()));
     
		Response response = target("shows")
        								.path("1")
        								.request()
        								.header(HttpHeaders.IF_MATCH, etag)
        								.put(Entity.entity(new Show(), MediaType.APPLICATION_JSON_TYPE));
     
		Assert.assertEquals(Status.OK, response.getStatusInfo());
     	Assert.assertTrue(response.hasEntity());
		Assert.assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
     
		Show actualShow = response.readEntity(Show.class);
     	Assert.assertEquals(show, actualShow);
     
     	Mockito.verify(mockedShowService).read(1l);
     	Mockito.verify(mockedShowService).update(Mockito.any(Show.class));

   }
  
	@Test
	public void deleteTest() throws Exception{
		Response response = target("shows").path("1").request().delete();
		Assert.assertEquals(204, response.getStatus());
		
		Mockito.verify(mockedShowService).delete(1l);
	}

	@Test
	public void createTest() throws Exception{
		Show show = createShow();
		Mockito.when(mockedShowService.create(Mockito.any(Show.class))).thenReturn(show);
			
		Response response = target("shows")
        								.request()
        								.post(Entity.entity(new Show(), MediaType.APPLICATION_JSON_TYPE));
     
		Assert.assertEquals(Status.OK, response.getStatusInfo());
     	Assert.assertTrue(response.hasEntity());
		Assert.assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
     
		Show actualShow = response.readEntity(Show.class);
     	Assert.assertEquals(show, actualShow);
     
     	Mockito.verify(mockedShowService).create(Mockito.any(Show.class));	
	}
  
  	@Test
  	public void readAllEpisodesNoEtagTest() throws Exception{
     Episode ep = new Episode();
     List<Episode> episodes = Arrays.asList(ep);
     Mockito.when(mockedShowService.readAllEpisodes(1L)).thenReturn(episodes);

     Response response = target("shows")
                             .path("1")
                             .path("episodes")
                             .request()
                             .get();

     Assert.assertEquals(Status.OK, response.getStatusInfo());
     Assert.assertTrue(response.hasEntity());
     Assert.assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));

     List<Episode> actualEpisodes = response.readEntity(new GenericType<List<Episode>>() {});
     Assert.assertNotNull(actualEpisodes);
     Assert.assertEquals(1, actualEpisodes.size());
     Assert.assertEquals(ep, actualEpisodes.get(0));
     
     Mockito.verify(mockedShowService).readAllEpisodes(1L);	
   }
  
  	@Test
  	public void readAllEpisodesNotMatchingEtagTest() throws Exception{
     Episode ep = new Episode();
     List<Episode> episodes = Arrays.asList(ep);
     Mockito.when(mockedShowService.readAllEpisodes(Mockito.any(Long.class))).thenReturn(episodes);

     EntityTag etag = new EntityTag("INVALID_ETAG");

     Response response = target("shows")
                             .path("1")
                             .path("episodes")
                             .request()
                             .header(HttpHeaders.IF_NONE_MATCH, etag)
                             .get();

     Assert.assertEquals(Status.OK, response.getStatusInfo());
     Assert.assertTrue(response.hasEntity());
     Assert.assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));

     List<Episode> actualEpisodes = response.readEntity(new GenericType<List<Episode>>() {});
     Assert.assertNotNull(actualEpisodes);
     Assert.assertEquals(1, actualEpisodes.size());
     Assert.assertEquals(ep, actualEpisodes.get(0));

     Mockito.verify(mockedShowService).readAllEpisodes(1L);
   }
  
    @Test
    public void readAllEpisodesMatchingEtagTest() throws Exception{
      Episode ep = new Episode();
      List<Episode> episodes = Arrays.asList(ep);
      Mockito.when(mockedShowService.readAllEpisodes(Mockito.any(Long.class))).thenReturn(episodes);

     	EntityTag etag = new EntityTag(Integer.toString(episodes.hashCode()));

      Response response = target("shows")
                            .path("1")
                            .path("episodes")
                            .request()
                            .header(HttpHeaders.IF_NONE_MATCH, etag)
                            .get();

      Assert.assertEquals(Status.NOT_MODIFIED, response.getStatusInfo());
      Assert.assertFalse(response.hasEntity());
     	Assert.assertEquals(etag.toString(), response.getHeaderString(HttpHeaders.ETAG)); 

      Mockito.verify(mockedShowService).readAllEpisodes(1L);
    }
}
