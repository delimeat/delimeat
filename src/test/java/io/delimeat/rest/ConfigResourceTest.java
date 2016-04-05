package io.delimeat.rest;

import io.delimeat.core.config.Config;
import io.delimeat.core.config.ConfigException;
import io.delimeat.core.service.ConfigService;

import java.io.IOException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.EntityTag;
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

public class ConfigResourceTest extends JerseyTest {

	private ConfigService mockedConfigService = Mockito.mock(ConfigService.class);

	@Override
	protected Application configure() {

		ResourceConfig config = new ResourceConfig();
		config.register(ConfigResource.class);
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

  	private Config createConfig(){
		Config config = new Config();
		config.getIgnoredFileTypes().add("FILETYPE");
		config.setIgnoreFolders(false);
		config.setOutputDirectory("OUTPUTDIR");
		config.setPreferFiles(true);
		config.setSearchInterval(Integer.MAX_VALUE);
     	return config;
   }
	@Test
	public void readNoEtagTest() throws IOException, Exception {
		Config expectedConfig = createConfig();
		Mockito.when(mockedConfigService.read()).thenReturn(expectedConfig);

		Response response = target("config")
        								.request()
        								.get();
     
		Assert.assertEquals(Status.OK, response.getStatusInfo());
     	Assert.assertTrue(response.hasEntity());
		Assert.assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
     
		Config actualConfig = response.readEntity(Config.class);
     	Assert.assertEquals(expectedConfig, actualConfig);
     
     	Mockito.verify(mockedConfigService).read();
	}

	@Test
	public void readNoMatchingEtagTest() throws IOException, Exception {
		Config expectedConfig = createConfig();
		Mockito.when(mockedConfigService.read()).thenReturn(expectedConfig);

     	EntityTag etag = new EntityTag("INVALID_ETAG");
     
		Response response = target("config")
        								.request()
        								.header(HttpHeaders.IF_NONE_MATCH, etag)
        								.get();
     
		Assert.assertEquals(Status.OK, response.getStatusInfo());
     	Assert.assertTrue(response.hasEntity());
		Assert.assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
          	    
		Config actualConfig = response.readEntity(Config.class);
     	Assert.assertEquals(expectedConfig, actualConfig);
     
     	Mockito.verify(mockedConfigService).read();
	}
  

	@Test
	public void readMatchingEtagTest() throws IOException, Exception {
		Config expectedConfig = createConfig();
		Mockito.when(mockedConfigService.read()).thenReturn(expectedConfig);

     	EntityTag etag = new EntityTag(Integer.toString(expectedConfig.hashCode()));

		Response response = target("config")
        								.request()
        								.header(HttpHeaders.IF_NONE_MATCH, etag)
        								.get();
     
		Assert.assertEquals(Status.NOT_MODIFIED, response.getStatusInfo());
     	Assert.assertFalse(response.hasEntity());     
     	Assert.assertEquals(etag.toString(), response.getHeaderString(HttpHeaders.ETAG));
          
     	Mockito.verify(mockedConfigService).read();
	}
  
	@Test
	public void updateNoEtagTest() throws ConfigException {
		Config expectedConfig = createConfig();
		Mockito.when(mockedConfigService.update(Mockito.any(Config.class))).thenReturn(expectedConfig);
     	Mockito.when(mockedConfigService.read()).thenReturn(expectedConfig);

		Response response = target("config")
        								.request()
        								.put(Entity.entity(new Config(), MediaType.APPLICATION_JSON_TYPE));
     
		Assert.assertEquals(Status.OK, response.getStatusInfo());
     	Assert.assertTrue(response.hasEntity());
		Assert.assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
     
		Config actualConfig = response.readEntity(Config.class);
     	Assert.assertEquals(expectedConfig, actualConfig);
     
     	Mockito.verify(mockedConfigService).read();
     	Mockito.verify(mockedConfigService).update(Mockito.any(Config.class));
	}
  
  
	@Test
	public void updateNoMatchingEtagTest() throws ConfigException {
		Config expectedConfig = createConfig();
     	Mockito.when(mockedConfigService.read()).thenReturn(expectedConfig);

     	EntityTag etag = new EntityTag("INVALID_ETAG");

		Response response = target("config")
        							.request()
        							.header(HttpHeaders.IF_MATCH, etag)
        							.put(Entity.entity(expectedConfig, MediaType.APPLICATION_JSON_TYPE));
     
		Assert.assertEquals(Status.PRECONDITION_FAILED, response.getStatusInfo());
     	Assert.assertFalse(response.hasEntity());
        
     	Mockito.verify(mockedConfigService).read();
     	Mockito.verify(mockedConfigService, Mockito.times(0)).update(Mockito.any(Config.class));

	}
  
	@Test
	public void updateMatchingEtagTest() throws ConfigException {
		Config expectedConfig = createConfig();
		Mockito.when(mockedConfigService.update(Mockito.any(Config.class))).thenReturn(expectedConfig);
     	Mockito.when(mockedConfigService.read()).thenReturn(expectedConfig);

     	EntityTag etag = new EntityTag(Integer.toString(expectedConfig.hashCode()));
     
		Response response = target("config")
        							.request()
        							.header(HttpHeaders.IF_MATCH, etag)
        							.put(Entity.entity(expectedConfig, MediaType.APPLICATION_JSON_TYPE));
     
		Assert.assertEquals(Status.OK, response.getStatusInfo());
     	Assert.assertTrue(response.hasEntity());
		Assert.assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
     
		Config actualConfig = response.readEntity(Config.class);
     	Assert.assertEquals(expectedConfig, actualConfig);
     
     	Mockito.verify(mockedConfigService).read();
     	Mockito.verify(mockedConfigService).update(Mockito.any(Config.class));
	}
}
