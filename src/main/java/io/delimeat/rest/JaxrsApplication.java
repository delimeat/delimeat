package io.delimeat.rest;

import io.delimeat.util.jaxrs.ETagRequestFilter;
import io.delimeat.util.jaxrs.ETagResponseFilter;
import io.delimeat.util.jaxrs.GenericExceptionMapper;
import io.delimeat.util.jaxrs.GuideExceptionMapper;
import io.delimeat.util.jaxrs.ShowExceptionMapper;
import io.delimeat.util.jaxrs.WebApplicationExceptionMapper;

import java.util.logging.Logger;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("api")
public class JaxrsApplication extends ResourceConfig {

	private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());

	public JaxrsApplication(){
		register(ShowResource.class);
		register(GuideResource.class);
		register(ConfigResource.class);
		register(new LoggingFilter(LOGGER, true));
		register(ETagResponseFilter.class); 
     	register(ETagRequestFilter.class);     
		register(ShowExceptionMapper.class);
		register(GuideExceptionMapper.class);
		register(WebApplicationExceptionMapper.class);
		register(GenericExceptionMapper.class);	
	}
}
