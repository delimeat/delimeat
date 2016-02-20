package io.delimeat.rest;

import io.delimeat.core.config.Config;
import io.delimeat.core.config.ConfigException;
import io.delimeat.core.service.ConfigService;
import io.delimeat.util.jaxrs.AddETagResponseFilter;

import java.io.IOException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class ConfigResourceTest extends JerseyTest {

	private ConfigService mockedConfigService = Mockito.mock(ConfigService.class);

	@Override
	protected Application configure() {

		ResourceConfig config = new ResourceConfig();
		config.register(ConfigResource.class);
     	config.register(AddETagResponseFilter.class);
		config.register(new AbstractBinder() {

			@Override
			protected void configure() {
				bind(mockedConfigService).to(ConfigService.class);
			}

		});
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		return config;
	}

	@Test
	public void readNoEtagTest() throws IOException, Exception {
		Config expectedConfig = new Config();
		expectedConfig.getIgnoredFileTypes().add("FILETYPE");
		expectedConfig.setIgnoreFolders(false);
		expectedConfig.setOutputDirectory("OUTPUTDIR");
		expectedConfig.setPreferFiles(true);
		expectedConfig.setSearchInterval(Integer.MAX_VALUE);
		Mockito.when(mockedConfigService.read()).thenReturn(expectedConfig);

		Response response = target("config").request().get();
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("application/json", response.getHeaderString("Content-Type"));
          	
     	EntityTag etag = new EntityTag(Integer.toString(expectedConfig.hashCode()));
     	Assert.assertEquals("\""+etag.getValue() +"\"", response.getHeaderString("ETag"));
     
		Config actualConfig = response.readEntity(Config.class);
		Assert.assertEquals(1, actualConfig.getIgnoredFileTypes().size());
		Assert.assertEquals("FILETYPE", actualConfig.getIgnoredFileTypes().get(0));
		Assert.assertFalse(actualConfig.isIgnoreFolders());
		Assert.assertEquals("OUTPUTDIR", actualConfig.getOutputDirectory());
		Assert.assertTrue(actualConfig.isPreferFiles());
		Assert.assertEquals(Integer.MAX_VALUE, actualConfig.getSearchInterval());
	}

	@Test
	public void readNoMatchingEtagTest() throws IOException, Exception {
		Config expectedConfig = new Config();
		expectedConfig.getIgnoredFileTypes().add("FILETYPE");
		expectedConfig.setIgnoreFolders(false);
		expectedConfig.setOutputDirectory("OUTPUTDIR");
		expectedConfig.setPreferFiles(true);
		expectedConfig.setSearchInterval(Integer.MAX_VALUE);
		Mockito.when(mockedConfigService.read()).thenReturn(expectedConfig);

		Response response = target("config").request().header("If-None-Match", "\"INVALID_ETAG\"").get();
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("application/json", response.getHeaderString("Content-Type"));
          	
     	EntityTag etag = new EntityTag(Integer.toString(expectedConfig.hashCode()));
     	Assert.assertEquals("\""+etag.getValue() +"\"", response.getHeaderString("ETag"));
     
		Config actualConfig = response.readEntity(Config.class);
		Assert.assertEquals(1, actualConfig.getIgnoredFileTypes().size());
		Assert.assertEquals("FILETYPE", actualConfig.getIgnoredFileTypes().get(0));
		Assert.assertFalse(actualConfig.isIgnoreFolders());
		Assert.assertEquals("OUTPUTDIR", actualConfig.getOutputDirectory());
		Assert.assertTrue(actualConfig.isPreferFiles());
		Assert.assertEquals(Integer.MAX_VALUE, actualConfig.getSearchInterval());
	}
  

	@Test
	public void readMatchingEtagTest() throws IOException, Exception {
		Config expectedConfig = new Config();
		expectedConfig.getIgnoredFileTypes().add("FILETYPE");
		expectedConfig.setIgnoreFolders(false);
		expectedConfig.setOutputDirectory("OUTPUTDIR");
		expectedConfig.setPreferFiles(true);
		expectedConfig.setSearchInterval(Integer.MAX_VALUE);
		Mockito.when(mockedConfigService.read()).thenReturn(expectedConfig);

     	EntityTag etag = new EntityTag(Integer.toString(expectedConfig.hashCode()));

		Response response = target("config").request().header("If-None-Match", "\""+etag.getValue() +"\"").get();
		Assert.assertEquals(304, response.getStatus());
          	
     	Assert.assertEquals("\""+etag.getValue() +"\"", response.getHeaderString("ETag"));
	}
  
	@Test
	public void updateTest() throws ConfigException {
		Config expectedConfig = new Config();
		expectedConfig.getIgnoredFileTypes().add("FILETYPE");
		expectedConfig.setIgnoreFolders(false);
		expectedConfig.setOutputDirectory("OUTPUTDIR");
		expectedConfig.setPreferFiles(true);
		expectedConfig.setSearchInterval(Integer.MAX_VALUE);
		Mockito.when(mockedConfigService.update(Mockito.any(Config.class))).thenReturn(expectedConfig);

		Response response = target("config").request().put(Entity.entity(expectedConfig, MediaType.APPLICATION_JSON_TYPE));
		Assert.assertEquals(200, response.getStatus());
		Assert.assertEquals("application/json", response.getHeaderString("Content-Type"));
          	
     	EntityTag etag = new EntityTag(Integer.toString(expectedConfig.hashCode()));
     	Assert.assertEquals("\""+etag.getValue() +"\"", response.getHeaderString("ETag"));
     
		Config actualConfig = response.readEntity(Config.class);
		Assert.assertEquals(1, actualConfig.getIgnoredFileTypes().size());
		Assert.assertEquals("FILETYPE", actualConfig.getIgnoredFileTypes().get(0));
		Assert.assertFalse(actualConfig.isIgnoreFolders());
		Assert.assertEquals("OUTPUTDIR", actualConfig.getOutputDirectory());
		Assert.assertTrue(actualConfig.isPreferFiles());
		Assert.assertEquals(Integer.MAX_VALUE, actualConfig.getSearchInterval());
	}
}
