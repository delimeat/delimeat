package io.delimeat.rest;

import io.delimeat.util.jaxrs.CORSResponseFilter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JaxrsApplicationTest {

	private JaxrsApplication application;
	
	@Before
	public void setUp() throws Exception {
		application = new JaxrsApplication();
	}
	
	@Test
	public void classesTest(){
		Assert.assertEquals(4, application.getClasses().size());
		Assert.assertTrue(application.getClasses().contains(ShowResource.class));
		Assert.assertTrue(application.getClasses().contains(ConfigResource.class));
		Assert.assertTrue(application.getClasses().contains(GuideResource.class));
		Assert.assertTrue(application.getClasses().contains(CORSResponseFilter.class));
	}
	
	@Test
	public void singletonsTest(){
		Assert.assertEquals(1, application.getSingletons().size());
	}
	
	@Test
	public void propertiesTest(){
		Assert.assertEquals(1, application.getProperties().size());
		Assert.assertTrue(application.getProperties().containsKey("jersey.config.server.tracing.type"));
		Assert.assertEquals("ALL", application.getProperties().get("jersey.config.server.tracing.type"));
	}

}
