package io.delimeat.rest;

import io.delimeat.core.guide.GuideSource;
import io.delimeat.core.service.ShowService;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowGuideSource;
import io.delimeat.core.show.ShowGuideSourcePK;
import io.delimeat.core.show.ShowType;
import io.delimeat.util.jaxrs.AddETagResponseFilter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.EncodingFilter;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class ShowResourceTest extends JerseyTest{

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	private ShowService mockedShowService = Mockito.mock(ShowService.class);

	@Override
	protected Application configure() {
		ResourceConfig config = new ResourceConfig();
		config.register(ShowResource.class);
  		config.register(AddETagResponseFilter.class);
		config.register(new AbstractBinder() {

			@Override
			protected void configure() {
				bind(mockedShowService).to(ShowService.class);
			}

		});
		EncodingFilter.enableFor(config, GZipEncoder.class);

		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		return config;
	}
	
	@Test
	public void readAllNoEtagTest() throws Exception{
		Show show = new Show();
		show.setAiring(true);
		show.setAirTime(9900000);
		show.setEnabled(false);
		show.setIncludeSpecials(true);
		show.setLastFeedUpdate(SDF.parse("2015-12-03"));
		show.setLastGuideUpdate(SDF.parse("2015-12-03"));
		show.setShowId(Long.MIN_VALUE);
		show.setShowType(ShowType.MINI_SERIES);
		show.setTimezone("TIMEZONE");
		show.setTitle("TITLE");
		show.setVersion(97);
		
		Episode nextEp = new Episode();
		nextEp.setAirDate(SDF.parse("2015-12-03"));
		nextEp.setDoubleEp(true);
		nextEp.setEpisodeId(Long.MAX_VALUE);
		nextEp.setEpisodeNum(101);
		nextEp.setSeasonNum(2);
		nextEp.setShow(show);
		nextEp.setTitle("NEXT_EP_TITLE");
		nextEp.setVersion(3);
		show.setNextEpisode(nextEp);
		
		Episode prevEp = new Episode();
		prevEp.setAirDate(SDF.parse("2015-12-04"));
		prevEp.setDoubleEp(true);
		prevEp.setEpisodeId(Long.MIN_VALUE);
		prevEp.setEpisodeNum(102);
		prevEp.setSeasonNum(3);
		prevEp.setShow(show);
		prevEp.setTitle("PREV_EP_TITLE");
		prevEp.setVersion(4);
		show.setPreviousEpisode(prevEp);		
		
		ShowGuideSource sgs = new ShowGuideSource();
		ShowGuideSourcePK pk = new ShowGuideSourcePK();
		pk.setGuideSource(GuideSource.IMDB);
		pk.setShowId(Long.MIN_VALUE);
		sgs.setId(pk);
		sgs.setGuideId("GUIDE_ID");
		sgs.setShow(show);
		sgs.setVersion(99);
		show.getGuideSources().add(sgs);
		
		List<Show> shows = new ArrayList<Show>();
		shows.add(show);
		Mockito.when(mockedShowService.readAll()).thenReturn(shows);
		
		Response response = target("shows").request().get();
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("application/json",response.getHeaderString("Content-Type"));
     	
     	EntityTag etag = ShowResource.createShowsEtag(shows);
     	Assert.assertEquals(etag.toString(), response.getHeaderString("ETag"));
		
		List<Show> actualShows = response.readEntity(new GenericType<List<Show>>() {});
		Assert.assertNotNull(actualShows);
		Assert.assertEquals(1, actualShows.size());
		Show actualShow = actualShows.get(0);
		Assert.assertTrue(actualShow.isAiring());
		Assert.assertEquals(9900000, actualShow.getAirTime());
		Assert.assertFalse(actualShow.isEnabled());
		Assert.assertTrue(actualShow.isIncludeSpecials());
		Assert.assertEquals("2015-12-03", SDF.format(actualShow.getLastFeedUpdate()));
		Assert.assertEquals("2015-12-03", SDF.format(actualShow.getLastGuideUpdate()));
		Assert.assertEquals(Long.MIN_VALUE, actualShow.getShowId());
		Assert.assertEquals(ShowType.MINI_SERIES, actualShow.getShowType());
		Assert.assertEquals("TIMEZONE", actualShow.getTimezone());
		Assert.assertEquals("TITLE", actualShow.getTitle());
		Assert.assertEquals(97, actualShow.getVersion());
		
		Assert.assertNotNull(actualShow.getNextEpisode());
		Episode actualNextEp = actualShow.getNextEpisode();
		Assert.assertEquals("2015-12-03", SDF.format(actualNextEp.getAirDate()));
		Assert.assertTrue(actualNextEp.isDoubleEp());
		Assert.assertEquals(Long.MAX_VALUE, actualNextEp.getEpisodeId());
		Assert.assertEquals(101, actualNextEp.getEpisodeNum());
		Assert.assertEquals(2, actualNextEp.getSeasonNum());
		Assert.assertNull(actualNextEp.getShow());
		Assert.assertEquals("NEXT_EP_TITLE", actualNextEp.getTitle());
		Assert.assertEquals(3, actualNextEp.getVersion());
		
		Assert.assertNotNull(actualShow.getPreviousEpisode());
		Episode actualPrevEp = actualShow.getPreviousEpisode();
		Assert.assertEquals("2015-12-04", SDF.format(actualPrevEp.getAirDate()));
		Assert.assertTrue(actualPrevEp.isDoubleEp());
		Assert.assertEquals(Long.MIN_VALUE, actualPrevEp.getEpisodeId());
		Assert.assertEquals(102, actualPrevEp.getEpisodeNum());
		Assert.assertEquals(3, actualPrevEp.getSeasonNum());
		Assert.assertNull(actualPrevEp.getShow());
		Assert.assertEquals("PREV_EP_TITLE", actualPrevEp.getTitle());
		Assert.assertEquals(4, actualPrevEp.getVersion());
		
		Assert.assertNotNull(actualShow.getGuideSources());
		Assert.assertEquals(1, actualShow.getGuideSources().size());
		ShowGuideSource actualSgs = actualShow.getGuideSources().get(0);
		Assert.assertEquals("GUIDE_ID", actualSgs.getGuideId());
		Assert.assertNotNull(actualSgs.getId());
		Assert.assertEquals(GuideSource.IMDB, sgs.getId().getGuideSource());
		Assert.assertEquals(Long.MIN_VALUE,sgs.getId().getShowId());
		Assert.assertNull(actualSgs.getShow());
		Assert.assertEquals(99, actualSgs.getVersion());
	}
  
	@Test
	public void readAllMatchingEtagTest() throws Exception{
		Show show = new Show();
		show.setAiring(true);
		show.setAirTime(9900000);
		show.setEnabled(false);
		show.setIncludeSpecials(true);
		show.setLastFeedUpdate(SDF.parse("2015-12-03"));
		show.setLastGuideUpdate(SDF.parse("2015-12-03"));
		show.setShowId(Long.MIN_VALUE);
		show.setShowType(ShowType.MINI_SERIES);
		show.setTimezone("TIMEZONE");
		show.setTitle("TITLE");
		show.setVersion(97);
		
		Episode nextEp = new Episode();
		nextEp.setAirDate(SDF.parse("2015-12-03"));
		nextEp.setDoubleEp(true);
		nextEp.setEpisodeId(Long.MAX_VALUE);
		nextEp.setEpisodeNum(101);
		nextEp.setSeasonNum(2);
		nextEp.setShow(show);
		nextEp.setTitle("NEXT_EP_TITLE");
		nextEp.setVersion(3);
		show.setNextEpisode(nextEp);
		
		Episode prevEp = new Episode();
		prevEp.setAirDate(SDF.parse("2015-12-04"));
		prevEp.setDoubleEp(true);
		prevEp.setEpisodeId(Long.MIN_VALUE);
		prevEp.setEpisodeNum(102);
		prevEp.setSeasonNum(3);
		prevEp.setShow(show);
		prevEp.setTitle("PREV_EP_TITLE");
		prevEp.setVersion(4);
		show.setPreviousEpisode(prevEp);		
		
		ShowGuideSource sgs = new ShowGuideSource();
		ShowGuideSourcePK pk = new ShowGuideSourcePK();
		pk.setGuideSource(GuideSource.IMDB);
		pk.setShowId(Long.MIN_VALUE);
		sgs.setId(pk);
		sgs.setGuideId("GUIDE_ID");
		sgs.setShow(show);
		sgs.setVersion(99);
		show.getGuideSources().add(sgs);
		
		List<Show> shows = new ArrayList<Show>();
		shows.add(show);
		Mockito.when(mockedShowService.readAll()).thenReturn(shows);
		
     	EntityTag etag = ShowResource.createShowsEtag(shows);
		Response response = target("shows").request().header("If-None-Match", "\""+etag.getValue()+"\"").get();
		Assert.assertEquals(304, response.getStatus());
     
     	Assert.assertEquals(etag.toString(), response.getHeaderString("ETag"));
	}
  
	@Test
	public void readAllNotMatchingEtagTest() throws Exception{
		Show show = new Show();
		show.setAiring(true);
		show.setAirTime(9900000);
		show.setEnabled(false);
		show.setIncludeSpecials(true);
		show.setLastFeedUpdate(SDF.parse("2015-12-03"));
		show.setLastGuideUpdate(SDF.parse("2015-12-03"));
		show.setShowId(Long.MIN_VALUE);
		show.setShowType(ShowType.MINI_SERIES);
		show.setTimezone("TIMEZONE");
		show.setTitle("TITLE");
		show.setVersion(97);
		
		Episode nextEp = new Episode();
		nextEp.setAirDate(SDF.parse("2015-12-03"));
		nextEp.setDoubleEp(true);
		nextEp.setEpisodeId(Long.MAX_VALUE);
		nextEp.setEpisodeNum(101);
		nextEp.setSeasonNum(2);
		nextEp.setShow(show);
		nextEp.setTitle("NEXT_EP_TITLE");
		nextEp.setVersion(3);
		show.setNextEpisode(nextEp);
		
		Episode prevEp = new Episode();
		prevEp.setAirDate(SDF.parse("2015-12-04"));
		prevEp.setDoubleEp(true);
		prevEp.setEpisodeId(Long.MIN_VALUE);
		prevEp.setEpisodeNum(102);
		prevEp.setSeasonNum(3);
		prevEp.setShow(show);
		prevEp.setTitle("PREV_EP_TITLE");
		prevEp.setVersion(4);
		show.setPreviousEpisode(prevEp);		
		
		ShowGuideSource sgs = new ShowGuideSource();
		ShowGuideSourcePK pk = new ShowGuideSourcePK();
		pk.setGuideSource(GuideSource.IMDB);
		pk.setShowId(Long.MIN_VALUE);
		sgs.setId(pk);
		sgs.setGuideId("GUIDE_ID");
		sgs.setShow(show);
		sgs.setVersion(99);
		show.getGuideSources().add(sgs);
		
		List<Show> shows = new ArrayList<Show>();
		shows.add(show);
		Mockito.when(mockedShowService.readAll()).thenReturn(shows);
		
		Response response = target("shows").request().header("If-None-Match", "\"INVALID_ETAG\"").get();
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("application/json",response.getHeaderString("Content-Type"));
		
     	
     	EntityTag etag = ShowResource.createShowsEtag(shows);
     	Assert.assertEquals(etag.toString(), response.getHeaderString("ETag"));
     
		List<Show> actualShows = response.readEntity(new GenericType<List<Show>>() {});
		Assert.assertNotNull(actualShows);
		Assert.assertEquals(1, actualShows.size());
		Show actualShow = actualShows.get(0);
		Assert.assertTrue(actualShow.isAiring());
		Assert.assertEquals(9900000, actualShow.getAirTime());
		Assert.assertFalse(actualShow.isEnabled());
		Assert.assertTrue(actualShow.isIncludeSpecials());
		Assert.assertEquals("2015-12-03", SDF.format(actualShow.getLastFeedUpdate()));
		Assert.assertEquals("2015-12-03", SDF.format(actualShow.getLastGuideUpdate()));
		Assert.assertEquals(Long.MIN_VALUE, actualShow.getShowId());
		Assert.assertEquals(ShowType.MINI_SERIES, actualShow.getShowType());
		Assert.assertEquals("TIMEZONE", actualShow.getTimezone());
		Assert.assertEquals("TITLE", actualShow.getTitle());
		Assert.assertEquals(97, actualShow.getVersion());
		
		Assert.assertNotNull(actualShow.getNextEpisode());
		Episode actualNextEp = actualShow.getNextEpisode();
		Assert.assertEquals("2015-12-03", SDF.format(actualNextEp.getAirDate()));
		Assert.assertTrue(actualNextEp.isDoubleEp());
		Assert.assertEquals(Long.MAX_VALUE, actualNextEp.getEpisodeId());
		Assert.assertEquals(101, actualNextEp.getEpisodeNum());
		Assert.assertEquals(2, actualNextEp.getSeasonNum());
		Assert.assertNull(actualNextEp.getShow());
		Assert.assertEquals("NEXT_EP_TITLE", actualNextEp.getTitle());
		Assert.assertEquals(3, actualNextEp.getVersion());
		
		Assert.assertNotNull(actualShow.getPreviousEpisode());
		Episode actualPrevEp = actualShow.getPreviousEpisode();
		Assert.assertEquals("2015-12-04", SDF.format(actualPrevEp.getAirDate()));
		Assert.assertTrue(actualPrevEp.isDoubleEp());
		Assert.assertEquals(Long.MIN_VALUE, actualPrevEp.getEpisodeId());
		Assert.assertEquals(102, actualPrevEp.getEpisodeNum());
		Assert.assertEquals(3, actualPrevEp.getSeasonNum());
		Assert.assertNull(actualPrevEp.getShow());
		Assert.assertEquals("PREV_EP_TITLE", actualPrevEp.getTitle());
		Assert.assertEquals(4, actualPrevEp.getVersion());
		
		Assert.assertNotNull(actualShow.getGuideSources());
		Assert.assertEquals(1, actualShow.getGuideSources().size());
		ShowGuideSource actualSgs = actualShow.getGuideSources().get(0);
		Assert.assertEquals("GUIDE_ID", actualSgs.getGuideId());
		Assert.assertNotNull(actualSgs.getId());
		Assert.assertEquals(GuideSource.IMDB, sgs.getId().getGuideSource());
		Assert.assertEquals(Long.MIN_VALUE,sgs.getId().getShowId());
		Assert.assertNull(actualSgs.getShow());
		Assert.assertEquals(99, actualSgs.getVersion());
	}
  
	@Test
	public void readNoEtagTest() throws Exception{
		Show show = new Show();
		show.setAiring(true);
		show.setAirTime(9900000);
		show.setEnabled(false);
		show.setIncludeSpecials(true);
		show.setLastFeedUpdate(SDF.parse("2015-12-03"));
		show.setLastGuideUpdate(SDF.parse("2015-12-03"));
		show.setShowId(Long.MIN_VALUE);
		show.setShowType(ShowType.MINI_SERIES);
		show.setTimezone("TIMEZONE");
		show.setTitle("TITLE");
		show.setVersion(97);
		
		Episode nextEp = new Episode();
		nextEp.setAirDate(SDF.parse("2015-12-03"));
		nextEp.setDoubleEp(true);
		nextEp.setEpisodeId(Long.MAX_VALUE);
		nextEp.setEpisodeNum(101);
		nextEp.setSeasonNum(2);
		nextEp.setShow(show);
		nextEp.setTitle("NEXT_EP_TITLE");
		nextEp.setVersion(3);
		show.setNextEpisode(nextEp);
		
		Episode prevEp = new Episode();
		prevEp.setAirDate(SDF.parse("2015-12-04"));
		prevEp.setDoubleEp(true);
		prevEp.setEpisodeId(Long.MIN_VALUE);
		prevEp.setEpisodeNum(102);
		prevEp.setSeasonNum(3);
		prevEp.setShow(show);
		prevEp.setTitle("PREV_EP_TITLE");
		prevEp.setVersion(4);
		show.setPreviousEpisode(prevEp);		
		
		ShowGuideSource sgs = new ShowGuideSource();
		ShowGuideSourcePK pk = new ShowGuideSourcePK();
		pk.setGuideSource(GuideSource.IMDB);
		pk.setShowId(Long.MIN_VALUE);
		sgs.setId(pk);
		sgs.setGuideId("GUIDE_ID");
		sgs.setShow(show);
		sgs.setVersion(99);
		show.getGuideSources().add(sgs);
		
		Mockito.when(mockedShowService.read(Mockito.anyLong())).thenReturn(show);
		
		Response response = target("shows").path("1").request().get();
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("application/json",response.getHeaderString("Content-Type"));
          	
     	EntityTag etag = ShowResource.createShowEtag(show);
     	Assert.assertEquals(etag.toString(), response.getHeaderString("ETag"));
		
		Show actualShow = response.readEntity(Show.class);
		Assert.assertTrue(actualShow.isAiring());
		Assert.assertEquals(9900000, actualShow.getAirTime());
		Assert.assertFalse(actualShow.isEnabled());
		Assert.assertTrue(actualShow.isIncludeSpecials());
		Assert.assertEquals("2015-12-03", SDF.format(actualShow.getLastFeedUpdate()));
		Assert.assertEquals("2015-12-03", SDF.format(actualShow.getLastGuideUpdate()));
		Assert.assertEquals(Long.MIN_VALUE, actualShow.getShowId());
		Assert.assertEquals(ShowType.MINI_SERIES, actualShow.getShowType());
		Assert.assertEquals("TIMEZONE", actualShow.getTimezone());
		Assert.assertEquals("TITLE", actualShow.getTitle());
		Assert.assertEquals(97, actualShow.getVersion());
		
		Assert.assertNotNull(actualShow.getNextEpisode());
		Episode actualNextEp = actualShow.getNextEpisode();
		Assert.assertEquals("2015-12-03", SDF.format(actualNextEp.getAirDate()));
		Assert.assertTrue(actualNextEp.isDoubleEp());
		Assert.assertEquals(Long.MAX_VALUE, actualNextEp.getEpisodeId());
		Assert.assertEquals(101, actualNextEp.getEpisodeNum());
		Assert.assertEquals(2, actualNextEp.getSeasonNum());
		Assert.assertNull(actualNextEp.getShow());
		Assert.assertEquals("NEXT_EP_TITLE", actualNextEp.getTitle());
		Assert.assertEquals(3, actualNextEp.getVersion());
		
		Assert.assertNotNull(actualShow.getPreviousEpisode());
		Episode actualPrevEp = actualShow.getPreviousEpisode();
		Assert.assertEquals("2015-12-04", SDF.format(actualPrevEp.getAirDate()));
		Assert.assertTrue(actualPrevEp.isDoubleEp());
		Assert.assertEquals(Long.MIN_VALUE, actualPrevEp.getEpisodeId());
		Assert.assertEquals(102, actualPrevEp.getEpisodeNum());
		Assert.assertEquals(3, actualPrevEp.getSeasonNum());
		Assert.assertNull(actualPrevEp.getShow());
		Assert.assertEquals("PREV_EP_TITLE", actualPrevEp.getTitle());
		Assert.assertEquals(4, actualPrevEp.getVersion());
		
		Assert.assertNotNull(actualShow.getGuideSources());
		Assert.assertEquals(1, actualShow.getGuideSources().size());
		ShowGuideSource actualSgs = actualShow.getGuideSources().get(0);
		Assert.assertEquals("GUIDE_ID", actualSgs.getGuideId());
		Assert.assertNotNull(actualSgs.getId());
		Assert.assertEquals(GuideSource.IMDB, sgs.getId().getGuideSource());
		Assert.assertEquals(Long.MIN_VALUE,sgs.getId().getShowId());
		Assert.assertNull(actualSgs.getShow());
		Assert.assertEquals(99, actualSgs.getVersion());	
	}
    
	@Test
	public void readNoMatchingEtagTest() throws Exception{
		Show show = new Show();
		show.setAiring(true);
		show.setAirTime(9900000);
		show.setEnabled(false);
		show.setIncludeSpecials(true);
		show.setLastFeedUpdate(SDF.parse("2015-12-03"));
		show.setLastGuideUpdate(SDF.parse("2015-12-03"));
		show.setShowId(Long.MIN_VALUE);
		show.setShowType(ShowType.MINI_SERIES);
		show.setTimezone("TIMEZONE");
		show.setTitle("TITLE");
		show.setVersion(97);
		
		Episode nextEp = new Episode();
		nextEp.setAirDate(SDF.parse("2015-12-03"));
		nextEp.setDoubleEp(true);
		nextEp.setEpisodeId(Long.MAX_VALUE);
		nextEp.setEpisodeNum(101);
		nextEp.setSeasonNum(2);
		nextEp.setShow(show);
		nextEp.setTitle("NEXT_EP_TITLE");
		nextEp.setVersion(3);
		show.setNextEpisode(nextEp);
		
		Episode prevEp = new Episode();
		prevEp.setAirDate(SDF.parse("2015-12-04"));
		prevEp.setDoubleEp(true);
		prevEp.setEpisodeId(Long.MIN_VALUE);
		prevEp.setEpisodeNum(102);
		prevEp.setSeasonNum(3);
		prevEp.setShow(show);
		prevEp.setTitle("PREV_EP_TITLE");
		prevEp.setVersion(4);
		show.setPreviousEpisode(prevEp);		
		
		ShowGuideSource sgs = new ShowGuideSource();
		ShowGuideSourcePK pk = new ShowGuideSourcePK();
		pk.setGuideSource(GuideSource.IMDB);
		pk.setShowId(Long.MIN_VALUE);
		sgs.setId(pk);
		sgs.setGuideId("GUIDE_ID");
		sgs.setShow(show);
		sgs.setVersion(99);
		show.getGuideSources().add(sgs);
		
		Mockito.when(mockedShowService.read(Mockito.anyLong())).thenReturn(show);
		  
		Response response = target("shows").path("1").request().header("If-None-Match", "\"INVALID_ETAG\"").get();
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("application/json",response.getHeaderString("Content-Type"));
          	
		EntityTag etag = ShowResource.createShowEtag(show);
     	Assert.assertEquals(etag.toString(), response.getHeaderString("ETag"));
		
		Show actualShow = response.readEntity(Show.class);
		Assert.assertTrue(actualShow.isAiring());
		Assert.assertEquals(9900000, actualShow.getAirTime());
		Assert.assertFalse(actualShow.isEnabled());
		Assert.assertTrue(actualShow.isIncludeSpecials());
		Assert.assertEquals("2015-12-03", SDF.format(actualShow.getLastFeedUpdate()));
		Assert.assertEquals("2015-12-03", SDF.format(actualShow.getLastGuideUpdate()));
		Assert.assertEquals(Long.MIN_VALUE, actualShow.getShowId());
		Assert.assertEquals(ShowType.MINI_SERIES, actualShow.getShowType());
		Assert.assertEquals("TIMEZONE", actualShow.getTimezone());
		Assert.assertEquals("TITLE", actualShow.getTitle());
		Assert.assertEquals(97, actualShow.getVersion());
		
		Assert.assertNotNull(actualShow.getNextEpisode());
		Episode actualNextEp = actualShow.getNextEpisode();
		Assert.assertEquals("2015-12-03", SDF.format(actualNextEp.getAirDate()));
		Assert.assertTrue(actualNextEp.isDoubleEp());
		Assert.assertEquals(Long.MAX_VALUE, actualNextEp.getEpisodeId());
		Assert.assertEquals(101, actualNextEp.getEpisodeNum());
		Assert.assertEquals(2, actualNextEp.getSeasonNum());
		Assert.assertNull(actualNextEp.getShow());
		Assert.assertEquals("NEXT_EP_TITLE", actualNextEp.getTitle());
		Assert.assertEquals(3, actualNextEp.getVersion());
		
		Assert.assertNotNull(actualShow.getPreviousEpisode());
		Episode actualPrevEp = actualShow.getPreviousEpisode();
		Assert.assertEquals("2015-12-04", SDF.format(actualPrevEp.getAirDate()));
		Assert.assertTrue(actualPrevEp.isDoubleEp());
		Assert.assertEquals(Long.MIN_VALUE, actualPrevEp.getEpisodeId());
		Assert.assertEquals(102, actualPrevEp.getEpisodeNum());
		Assert.assertEquals(3, actualPrevEp.getSeasonNum());
		Assert.assertNull(actualPrevEp.getShow());
		Assert.assertEquals("PREV_EP_TITLE", actualPrevEp.getTitle());
		Assert.assertEquals(4, actualPrevEp.getVersion());
		
		Assert.assertNotNull(actualShow.getGuideSources());
		Assert.assertEquals(1, actualShow.getGuideSources().size());
		ShowGuideSource actualSgs = actualShow.getGuideSources().get(0);
		Assert.assertEquals("GUIDE_ID", actualSgs.getGuideId());
		Assert.assertNotNull(actualSgs.getId());
		Assert.assertEquals(GuideSource.IMDB, sgs.getId().getGuideSource());
		Assert.assertEquals(Long.MIN_VALUE,sgs.getId().getShowId());
		Assert.assertNull(actualSgs.getShow());
		Assert.assertEquals(99, actualSgs.getVersion());	
	}

	@Test
	public void readMatchingEtagTest() throws Exception{
		Show show = new Show();
		show.setAiring(true);
		show.setAirTime(9900000);
		show.setEnabled(false);
		show.setIncludeSpecials(true);
		show.setLastFeedUpdate(SDF.parse("2015-12-03"));
		show.setLastGuideUpdate(SDF.parse("2015-12-03"));
		show.setShowId(Long.MIN_VALUE);
		show.setShowType(ShowType.MINI_SERIES);
		show.setTimezone("TIMEZONE");
		show.setTitle("TITLE");
		show.setVersion(97);
		
		Episode nextEp = new Episode();
		nextEp.setAirDate(SDF.parse("2015-12-03"));
		nextEp.setDoubleEp(true);
		nextEp.setEpisodeId(Long.MAX_VALUE);
		nextEp.setEpisodeNum(101);
		nextEp.setSeasonNum(2);
		nextEp.setShow(show);
		nextEp.setTitle("NEXT_EP_TITLE");
		nextEp.setVersion(3);
		show.setNextEpisode(nextEp);
		
		Episode prevEp = new Episode();
		prevEp.setAirDate(SDF.parse("2015-12-04"));
		prevEp.setDoubleEp(true);
		prevEp.setEpisodeId(Long.MIN_VALUE);
		prevEp.setEpisodeNum(102);
		prevEp.setSeasonNum(3);
		prevEp.setShow(show);
		prevEp.setTitle("PREV_EP_TITLE");
		prevEp.setVersion(4);
		show.setPreviousEpisode(prevEp);		
		
		ShowGuideSource sgs = new ShowGuideSource();
		ShowGuideSourcePK pk = new ShowGuideSourcePK();
		pk.setGuideSource(GuideSource.IMDB);
		pk.setShowId(Long.MIN_VALUE);
		sgs.setId(pk);
		sgs.setGuideId("GUIDE_ID");
		sgs.setShow(show);
		sgs.setVersion(99);
		show.getGuideSources().add(sgs);
		
		Mockito.when(mockedShowService.read(Mockito.anyLong())).thenReturn(show);
		  
     	EntityTag etag = ShowResource.createShowEtag(show);
     	System.out.println("TEST: "+ etag);
     
		Response response = target("shows").path("1").request().header("If-None-Match", "\""+ etag.getValue() +  "\"").get();
		Assert.assertEquals(304, response.getStatus());
          	
     	Assert.assertEquals(etag.toString(), response.getHeaderString("ETag"));
			
	}
  
	@Test
	public void updateTest() throws Exception{
		Show show = new Show();
		show.setAiring(true);
		show.setAirTime(9900000);
		show.setEnabled(false);
		show.setIncludeSpecials(true);
		show.setLastFeedUpdate(SDF.parse("2015-12-03"));
		show.setLastGuideUpdate(SDF.parse("2015-12-03"));
		show.setShowId(Long.MIN_VALUE);
		show.setShowType(ShowType.MINI_SERIES);
		show.setTimezone("TIMEZONE");
		show.setTitle("TITLE");
		show.setVersion(97);
		
		Episode nextEp = new Episode();
		nextEp.setAirDate(SDF.parse("2015-12-03"));
		nextEp.setDoubleEp(true);
		nextEp.setEpisodeId(Long.MAX_VALUE);
		nextEp.setEpisodeNum(101);
		nextEp.setSeasonNum(2);
		nextEp.setShow(show);
		nextEp.setTitle("NEXT_EP_TITLE");
		nextEp.setVersion(3);
		show.setNextEpisode(nextEp);
		
		Episode prevEp = new Episode();
		prevEp.setAirDate(SDF.parse("2015-12-04"));
		prevEp.setDoubleEp(true);
		prevEp.setEpisodeId(Long.MIN_VALUE);
		prevEp.setEpisodeNum(102);
		prevEp.setSeasonNum(3);
		prevEp.setShow(show);
		prevEp.setTitle("PREV_EP_TITLE");
		prevEp.setVersion(4);
		show.setPreviousEpisode(prevEp);		
		
		ShowGuideSource sgs = new ShowGuideSource();
		ShowGuideSourcePK pk = new ShowGuideSourcePK();
		pk.setGuideSource(GuideSource.IMDB);
		pk.setShowId(Long.MIN_VALUE);
		sgs.setId(pk);
		sgs.setGuideId("GUIDE_ID");
		sgs.setShow(show);
		sgs.setVersion(99);
		show.getGuideSources().add(sgs);
		
		Mockito.when(mockedShowService.update(Mockito.any(Show.class))).thenReturn(show);
			
		Response response = target("shows").path("1").request().put(Entity.entity(new Show(), MediaType.APPLICATION_JSON_TYPE));
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("application/json",response.getHeaderString("Content-Type"));
       	
     	EntityTag etag = ShowResource.createShowEtag(show);
     	Assert.assertEquals(etag.toString(), response.getHeaderString("ETag"));
     
		Show actualShow = response.readEntity(Show.class);
		Assert.assertTrue(actualShow.isAiring());
		Assert.assertEquals(9900000, actualShow.getAirTime());
		Assert.assertFalse(actualShow.isEnabled());
		Assert.assertTrue(actualShow.isIncludeSpecials());
		Assert.assertEquals("2015-12-03", SDF.format(actualShow.getLastFeedUpdate()));
		Assert.assertEquals("2015-12-03", SDF.format(actualShow.getLastGuideUpdate()));
		Assert.assertEquals(Long.MIN_VALUE, actualShow.getShowId());
		Assert.assertEquals(ShowType.MINI_SERIES, actualShow.getShowType());
		Assert.assertEquals("TIMEZONE", actualShow.getTimezone());
		Assert.assertEquals("TITLE", actualShow.getTitle());
		Assert.assertEquals(97, actualShow.getVersion());
		
		Assert.assertNotNull(actualShow.getNextEpisode());
		Episode actualNextEp = actualShow.getNextEpisode();
		Assert.assertEquals("2015-12-03", SDF.format(actualNextEp.getAirDate()));
		Assert.assertTrue(actualNextEp.isDoubleEp());
		Assert.assertEquals(Long.MAX_VALUE, actualNextEp.getEpisodeId());
		Assert.assertEquals(101, actualNextEp.getEpisodeNum());
		Assert.assertEquals(2, actualNextEp.getSeasonNum());
		Assert.assertNull(actualNextEp.getShow());
		Assert.assertEquals("NEXT_EP_TITLE", actualNextEp.getTitle());
		Assert.assertEquals(3, actualNextEp.getVersion());
		
		Assert.assertNotNull(actualShow.getPreviousEpisode());
		Episode actualPrevEp = actualShow.getPreviousEpisode();
		Assert.assertEquals("2015-12-04", SDF.format(actualPrevEp.getAirDate()));
		Assert.assertTrue(actualPrevEp.isDoubleEp());
		Assert.assertEquals(Long.MIN_VALUE, actualPrevEp.getEpisodeId());
		Assert.assertEquals(102, actualPrevEp.getEpisodeNum());
		Assert.assertEquals(3, actualPrevEp.getSeasonNum());
		Assert.assertNull(actualPrevEp.getShow());
		Assert.assertEquals("PREV_EP_TITLE", actualPrevEp.getTitle());
		Assert.assertEquals(4, actualPrevEp.getVersion());
		
		Assert.assertNotNull(actualShow.getGuideSources());
		Assert.assertEquals(1, actualShow.getGuideSources().size());
		ShowGuideSource actualSgs = actualShow.getGuideSources().get(0);
		Assert.assertEquals("GUIDE_ID", actualSgs.getGuideId());
		Assert.assertNotNull(actualSgs.getId());
		Assert.assertEquals(GuideSource.IMDB, sgs.getId().getGuideSource());
		Assert.assertEquals(Long.MIN_VALUE,sgs.getId().getShowId());
		Assert.assertNull(actualSgs.getShow());
		Assert.assertEquals(99, actualSgs.getVersion());	
	}
	
	@Test
	public void deleteTest() throws Exception{
		Response response = target("shows").path("1").request().delete();
		Assert.assertEquals(204, response.getStatus());
		
		Mockito.verify(mockedShowService).delete(1l);
	}

	@Test
	public void createTest() throws Exception{
		Show show = new Show();
		show.setAiring(true);
		show.setAirTime(9900000);
		show.setEnabled(false);
		show.setIncludeSpecials(true);
		show.setLastFeedUpdate(SDF.parse("2015-12-03"));
		show.setLastGuideUpdate(SDF.parse("2015-12-03"));
		show.setShowId(Long.MIN_VALUE);
		show.setShowType(ShowType.MINI_SERIES);
		show.setTimezone("TIMEZONE");
		show.setTitle("TITLE");
		show.setVersion(97);
		
		Episode nextEp = new Episode();
		nextEp.setAirDate(SDF.parse("2015-12-03"));
		nextEp.setDoubleEp(true);
		nextEp.setEpisodeId(Long.MAX_VALUE);
		nextEp.setEpisodeNum(101);
		nextEp.setSeasonNum(2);
		nextEp.setShow(show);
		nextEp.setTitle("NEXT_EP_TITLE");
		nextEp.setVersion(3);
		show.setNextEpisode(nextEp);
		
		Episode prevEp = new Episode();
		prevEp.setAirDate(SDF.parse("2015-12-04"));
		prevEp.setDoubleEp(true);
		prevEp.setEpisodeId(Long.MIN_VALUE);
		prevEp.setEpisodeNum(102);
		prevEp.setSeasonNum(3);
		prevEp.setShow(show);
		prevEp.setTitle("PREV_EP_TITLE");
		prevEp.setVersion(4);
		show.setPreviousEpisode(prevEp);		
		
		ShowGuideSource sgs = new ShowGuideSource();
		ShowGuideSourcePK pk = new ShowGuideSourcePK();
		pk.setGuideSource(GuideSource.IMDB);
		pk.setShowId(Long.MIN_VALUE);
		sgs.setId(pk);
		sgs.setGuideId("GUIDE_ID");
		sgs.setShow(show);
		sgs.setVersion(99);
		show.getGuideSources().add(sgs);
		
		Mockito.when(mockedShowService.create(Mockito.any(Show.class))).thenReturn(show);
			
		Response response = target("shows").request().post(Entity.entity(new Show(), MediaType.APPLICATION_JSON_TYPE));
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("application/json",response.getHeaderString("Content-Type"));
		          	
     	EntityTag etag = ShowResource.createShowEtag(show);
     	Assert.assertEquals(etag.toString(), response.getHeaderString("ETag"));
     
		Show actualShow = response.readEntity(Show.class);
		Assert.assertTrue(actualShow.isAiring());
		Assert.assertEquals(9900000, actualShow.getAirTime());
		Assert.assertFalse(actualShow.isEnabled());
		Assert.assertTrue(actualShow.isIncludeSpecials());
		Assert.assertEquals("2015-12-03", SDF.format(actualShow.getLastFeedUpdate()));
		Assert.assertEquals("2015-12-03", SDF.format(actualShow.getLastGuideUpdate()));
		Assert.assertEquals(Long.MIN_VALUE, actualShow.getShowId());
		Assert.assertEquals(ShowType.MINI_SERIES, actualShow.getShowType());
		Assert.assertEquals("TIMEZONE", actualShow.getTimezone());
		Assert.assertEquals("TITLE", actualShow.getTitle());
		Assert.assertEquals(97, actualShow.getVersion());
		
		Assert.assertNotNull(actualShow.getNextEpisode());
		Episode actualNextEp = actualShow.getNextEpisode();
		Assert.assertEquals("2015-12-03", SDF.format(actualNextEp.getAirDate()));
		Assert.assertTrue(actualNextEp.isDoubleEp());
		Assert.assertEquals(Long.MAX_VALUE, actualNextEp.getEpisodeId());
		Assert.assertEquals(101, actualNextEp.getEpisodeNum());
		Assert.assertEquals(2, actualNextEp.getSeasonNum());
		Assert.assertNull(actualNextEp.getShow());
		Assert.assertEquals("NEXT_EP_TITLE", actualNextEp.getTitle());
		Assert.assertEquals(3, actualNextEp.getVersion());
		
		Assert.assertNotNull(actualShow.getPreviousEpisode());
		Episode actualPrevEp = actualShow.getPreviousEpisode();
		Assert.assertEquals("2015-12-04", SDF.format(actualPrevEp.getAirDate()));
		Assert.assertTrue(actualPrevEp.isDoubleEp());
		Assert.assertEquals(Long.MIN_VALUE, actualPrevEp.getEpisodeId());
		Assert.assertEquals(102, actualPrevEp.getEpisodeNum());
		Assert.assertEquals(3, actualPrevEp.getSeasonNum());
		Assert.assertNull(actualPrevEp.getShow());
		Assert.assertEquals("PREV_EP_TITLE", actualPrevEp.getTitle());
		Assert.assertEquals(4, actualPrevEp.getVersion());
		
		Assert.assertNotNull(actualShow.getGuideSources());
		Assert.assertEquals(1, actualShow.getGuideSources().size());
		ShowGuideSource actualSgs = actualShow.getGuideSources().get(0);
		Assert.assertEquals("GUIDE_ID", actualSgs.getGuideId());
		Assert.assertNotNull(actualSgs.getId());
		Assert.assertEquals(GuideSource.IMDB, sgs.getId().getGuideSource());
		Assert.assertEquals(Long.MIN_VALUE,sgs.getId().getShowId());
		Assert.assertNull(actualSgs.getShow());
		Assert.assertEquals(99, actualSgs.getVersion());		
	}
  
  	@Test
  	public void createShowEtagTest(){
     	Show show = new Show();
		show.setShowId(Long.MIN_VALUE);
     	show.setVersion(97);
     	EntityTag etag = ShowResource.createShowEtag(show);
      Assert.assertEquals("cd5d8f931ff1c5fb10e215ed42d09d77b2ae0888",etag.getValue());
   }
  
  	@Test
  	public void createShowsEtagTest(){
     	Show show = new Show();
		show.setShowId(Long.MIN_VALUE);
     	show.setVersion(97);
     	EntityTag etag = ShowResource.createShowsEtag(Arrays.asList(show));
      Assert.assertEquals("e834bce83e9da3f05561ed380480bdad1d68e0ef",etag.getValue());
   }
}
