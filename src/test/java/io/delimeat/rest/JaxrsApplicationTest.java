package io.delimeat.rest;

import io.delimeat.rest.util.jaxrs.ETagRequestFilter;
import io.delimeat.rest.util.jaxrs.ETagResponseFilter;
import io.delimeat.rest.util.jaxrs.GenericExceptionMapper;
import io.delimeat.rest.util.jaxrs.GuideExceptionMapper;
import io.delimeat.rest.util.jaxrs.ShowExceptionMapper;
import io.delimeat.rest.util.jaxrs.WebApplicationExceptionMapper;

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
		Assert.assertEquals(10, application.getClasses().size());
		Assert.assertTrue(application.getClasses().contains(ShowResource.class));
		Assert.assertTrue(application.getClasses().contains(ConfigResource.class));
		Assert.assertTrue(application.getClasses().contains(GuideResource.class));
		Assert.assertTrue(application.getClasses().contains(EpisodeResource.class));
		Assert.assertTrue(application.getClasses().contains(GenericExceptionMapper.class));
		Assert.assertTrue(application.getClasses().contains(GuideExceptionMapper.class));
		Assert.assertTrue(application.getClasses().contains(ShowExceptionMapper.class));
		Assert.assertTrue(application.getClasses().contains(ETagResponseFilter.class));
  		Assert.assertTrue(application.getClasses().contains(ETagRequestFilter.class));
		Assert.assertTrue(application.getClasses().contains(WebApplicationExceptionMapper.class));     
	}
	
	@Test
	public void singletonsTest(){
		Assert.assertEquals(2, application.getSingletons().size());
	}
}
