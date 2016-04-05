package io.delimeat.rest;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.EncodingFilter;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.delimeat.core.service.ProcessorService;

public class ProcessorResourceTest extends JerseyTest {
  
  	private ProcessorService mockedService = Mockito.mock(ProcessorService.class);

  	@Override
	protected Application configure() {
		ResourceConfig config = new ResourceConfig();
		config.register(ProcessorResource.class);
		config.register(new AbstractBinder() {

			@Override
			protected void configure() {
				bind(mockedService).to(ProcessorService.class);
			}

		});
		EncodingFilter.enableFor(config, GZipEncoder.class);

		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		return config;
	}
  
	@Test
	public void processAllGuideTest() throws Exception{
	
		Response response = target("process/guide")
        							.request()
        							.head();
     
		Assert.assertEquals(Status.NO_CONTENT, response.getStatusInfo());
     	Assert.assertFalse(response.hasEntity());
		
     	Mockito.verify(mockedService,Mockito.times(1)).processAllGuideUpdates();
	}
  
	@Test
	public void processAllFeedTest() throws Exception{
	
		Response response = target("process/feed")
        								.request()
        								.head();
     
		Assert.assertEquals(Status.NO_CONTENT, response.getStatusInfo());
     	Assert.assertFalse(response.hasEntity());

     	Mockito.verify(mockedService,Mockito.times(1)).processAllFeedUpdates();
	}
}
