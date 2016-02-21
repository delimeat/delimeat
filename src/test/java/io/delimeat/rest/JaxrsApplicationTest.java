package io.delimeat.rest;

import io.delimeat.util.jaxrs.AddETagResponseFilter;
import io.delimeat.util.jaxrs.CORSResponseFilter;
import io.delimeat.util.jaxrs.GenericExceptionMapper;
import io.delimeat.util.jaxrs.GuideExceptionMapper;
import io.delimeat.util.jaxrs.ShowExceptionMapper;
import io.delimeat.util.jaxrs.WebApplicationExceptionMapper;

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
		Assert.assertEquals(9, application.getClasses().size());
		Assert.assertTrue(application.getClasses().contains(ShowResource.class));
		Assert.assertTrue(application.getClasses().contains(ConfigResource.class));
		Assert.assertTrue(application.getClasses().contains(GuideResource.class));
		Assert.assertTrue(application.getClasses().contains(CORSResponseFilter.class));
		Assert.assertTrue(application.getClasses().contains(GenericExceptionMapper.class));
		Assert.assertTrue(application.getClasses().contains(GuideExceptionMapper.class));
		Assert.assertTrue(application.getClasses().contains(ShowExceptionMapper.class));
		Assert.assertTrue(application.getClasses().contains(AddETagResponseFilter.class));
		Assert.assertTrue(application.getClasses().contains(WebApplicationExceptionMapper.class));     
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
