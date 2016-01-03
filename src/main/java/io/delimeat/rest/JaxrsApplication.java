package io.delimeat.rest;

import io.delimeat.util.jaxrs.CORSResponseFilter;

import java.util.logging.Logger;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

@ApplicationPath("api")
public class JaxrsApplication extends ResourceConfig {

	private Logger LOGGER = Logger.getLogger(this.getClass().getName());

	public JaxrsApplication(){
		register(ShowResource.class);
		register(GuideResource.class);
		register(ConfigResource.class);
		register(new LoggingFilter(LOGGER, true));
		property(ServerProperties.TRACING, "ALL");
		register(CORSResponseFilter.class);
		//TODO enable for production
		//EncodingFilter.enableFor(this, GZipEncoder.class);
	}
}
