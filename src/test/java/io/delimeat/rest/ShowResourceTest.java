package io.delimeat.rest;

import io.delimeat.core.service.ShowService;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowType;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericType;
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

public class ShowResourceTest extends JerseyTest {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
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

	private Show createShow() throws Exception{
		Show show = new Show(1L, 9900000, "TIMEZONE", "GUIDE_ID", "TITLE", true, ShowType.MINI_SERIES, SDF.parse("1970-01-01"), SDF.parse("1970-01-02"), SDF.parse("1970-01-03"), SDF.parse("1970-01-04"), true, true, Integer.MIN_VALUE, Integer.MAX_VALUE, 99);

		Episode nextEp = new Episode(Long.MIN_VALUE, "NEXT_EP_TITLE", SDF.parse("1970-01-05"), 2, 101, false, 99, null);
		show.setNextEpisode(nextEp);

		Episode prevEp = new Episode(Long.MIN_VALUE, "PREV_EP_TITLE", SDF.parse("1970-01-06"), 3, 102, true, 99, null);
		show.setPreviousEpisode(prevEp);

		return show;
	}

	@Test
	public void readAllTest() throws Exception {
		Show show = createShow();
		List<Show> shows = Arrays.asList(show);
		Mockito.when(mockedShowService.readAll()).thenReturn(shows);

		Response response = target("shows")
							.request()
							.get();

		Assert.assertEquals(Status.OK, response.getStatusInfo());
		Assert.assertTrue(response.hasEntity());
		Assert.assertEquals("application/json",response.getHeaderString("Content-Type"));

		List<Show> actualShows = response.readEntity(new GenericType<List<Show>>() {});
		Assert.assertNotNull(actualShows);
		Assert.assertEquals(1, actualShows.size());
		Assert.assertEquals(show.toString(), actualShows.get(0).toString());

		Mockito.verify(mockedShowService).readAll();
		Mockito.verifyNoMoreInteractions(mockedShowService);

	}

	@Test
	public void readTest() throws Exception {
		Show show = createShow();
		Mockito.when(mockedShowService.read(1L)).thenReturn(show);

		Response response = target("shows")
							.path("1")
							.request()
							.get();

		Assert.assertEquals(Status.OK, response.getStatusInfo());
		Assert.assertTrue(response.hasEntity());
		Assert.assertEquals("application/json",response.getHeaderString("Content-Type"));

		Show actualShow = response.readEntity(Show.class);
		Assert.assertEquals(show.toString(), actualShow.toString());

		Mockito.verify(mockedShowService).read(1l);
		Mockito.verifyNoMoreInteractions(mockedShowService);
	}

	@Test
	public void updateTest() throws Exception {
		Show show = createShow();
		Mockito.when(mockedShowService.update(Mockito.any(Show.class))).thenReturn(show);
		Mockito.when(mockedShowService.read(1L)).thenReturn(show);

		Response response = target("shows")
							.path("1")
							.request()
							.put(Entity.entity(new Show(), MediaType.APPLICATION_JSON_TYPE));

		Assert.assertEquals(Status.OK, response.getStatusInfo());
		Assert.assertTrue(response.hasEntity());
		Assert.assertEquals("application/json",response.getHeaderString("Content-Type"));

		Show actualShow = response.readEntity(Show.class);
		Assert.assertEquals(show.toString(), actualShow.toString());

		Mockito.verify(mockedShowService).update(Mockito.any(Show.class));
		Mockito.verifyNoMoreInteractions(mockedShowService);
	}

	@Test
	public void deleteTest() throws Exception {
		Response response = target("shows")
							.path("1")
							.request()
							.delete();
		
		Assert.assertEquals(204, response.getStatus());
		Assert.assertFalse(response.hasEntity());

		Mockito.verify(mockedShowService).delete(1l);
		Mockito.verifyNoMoreInteractions(mockedShowService);
	}

	@Test
	public void createTest() throws Exception {
		Show show = createShow();
		Mockito.when(mockedShowService.create(Mockito.any(Show.class))).thenReturn(show);

		Response response = target("shows")
							.request()
							.post(Entity.entity(show, MediaType.APPLICATION_JSON_TYPE));

		Assert.assertEquals(Status.OK, response.getStatusInfo());
		Assert.assertTrue(response.hasEntity());
		Assert.assertEquals("application/json",response.getHeaderString("Content-Type"));

		Show actualShow = response.readEntity(Show.class);
		Assert.assertEquals(show.toString(), actualShow.toString());

		Mockito.verify(mockedShowService).create(show);
		Mockito.verifyNoMoreInteractions(mockedShowService);

	}

	@Test
	public void readAllEpisodesTest() throws Exception {
		Episode ep = new Episode();
		List<Episode> episodes = Arrays.asList(ep);
		Mockito.when(mockedShowService.readAllEpisodes(1L))
				.thenReturn(episodes);

		Response response = target("shows")
							.path("1")
							.path("episodes")
							.request().get();

		Assert.assertEquals(Status.OK, response.getStatusInfo());
		Assert.assertTrue(response.hasEntity());
		Assert.assertEquals("application/json",response.getHeaderString("Content-Type"));

		List<Episode> actualEpisodes = response.readEntity(new GenericType<List<Episode>>() {});
		Assert.assertNotNull(actualEpisodes);
		Assert.assertEquals(1, actualEpisodes.size());
		Assert.assertEquals(ep.toString(), actualEpisodes.get(0).toString());

		Mockito.verify(mockedShowService).readAllEpisodes(1L);
		Mockito.verifyNoMoreInteractions(mockedShowService);
	}

	@Test
	public void showServiceTest() {
		ShowResource resource = new ShowResource();

		Assert.assertNull(resource.getShowService());
		resource.setShowService(mockedShowService);
		Assert.assertEquals(mockedShowService, resource.getShowService());
	}

	@Test
	public void generateEtagShowTest() throws Exception {
		Show show = createShow();
		Mockito.when(mockedShowService.read(1L)).thenReturn(show);

		ShowResource resource = new ShowResource();
		resource.setShowService(mockedShowService);

		EntityTag etag = resource.generateEtagShow(1L);
		Assert.assertEquals(new EntityTag("1091"), etag);

		Mockito.verify(mockedShowService).read(1l);
		Mockito.verifyNoMoreInteractions(mockedShowService);
	}

}
