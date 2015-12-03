package io.delimeat.rest;

import io.delimeat.core.guide.GuideIdentifier;
import io.delimeat.core.guide.GuideSearchResult;
import io.delimeat.core.guide.GuideSource;
import io.delimeat.core.service.GuideService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Application;
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
	public void searchTest() throws IOException, Exception {
		GuideSearchResult result = new GuideSearchResult();
		result.setDescription("DESCRIPTION");
		result.setFirstAired(SDF.parse("2015-12-01"));
		GuideIdentifier id = new GuideIdentifier();
		id.setSource(GuideSource.TVMAZE);
		id.setValue("VALUE");
		result.getGuideIds().add(id);
		result.setTitle("TITLE");
		List<GuideSearchResult> expectedResults = new ArrayList<GuideSearchResult>();
		expectedResults.add(result);

		Mockito.when(mockedGuideService.readLike(Mockito.anyString())).thenReturn(expectedResults);

		Response response = target("guide").path("search").path("title").request().get();
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("application/json",response.getHeaderString("Content-Type"));

		List<GuideSearchResult> actualResults = response.readEntity(new GenericType<List<GuideSearchResult>>() {});
		Assert.assertEquals(1, actualResults.size());
		Assert.assertEquals("DESCRIPTION", actualResults.get(0).getDescription());
		Assert.assertEquals("2015-12-01",SDF.format(actualResults.get(0).getFirstAired()));
		Assert.assertEquals(1, actualResults.get(0).getGuideIds().size());
		Assert.assertEquals(GuideSource.TVMAZE, actualResults.get(0).getGuideIds().get(0).getSource());
		Assert.assertEquals("VALUE", actualResults.get(0).getGuideIds().get(0).getValue());
		Assert.assertEquals("TITLE", actualResults.get(0).getTitle());
	}
}
