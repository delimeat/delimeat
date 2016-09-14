package io.delimeat.rest;

import io.delimeat.core.config.Config;
import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.core.guide.GuideInfo;
import io.delimeat.core.guide.GuideSearchResult;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;
import io.delimeat.rest.util.jaxrs.ETagRequestFilter;
import io.delimeat.rest.util.jaxrs.ETagResponseFilter;
import io.delimeat.rest.util.jaxrs.GenericExceptionMapper;
import io.delimeat.rest.util.jaxrs.GuideExceptionMapper;
import io.delimeat.rest.util.jaxrs.JaxrsError;
import io.delimeat.rest.util.jaxrs.ShowExceptionMapper;
import io.delimeat.rest.util.jaxrs.WebApplicationExceptionMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.ApplicationPath;
import javax.xml.bind.Marshaller;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("api")
public class JaxrsApplication extends ResourceConfig {

	private static final Logger LOGGER = Logger.getLogger(LoggingFeature.class.getName());

	public JaxrsApplication(){
		register(ShowResource.class);
		register(GuideResource.class);
		register(ConfigResource.class);
		register(ETagResponseFilter.class); 
     	register(ETagRequestFilter.class);     
		register(ShowExceptionMapper.class);
		register(GuideExceptionMapper.class);
		register(WebApplicationExceptionMapper.class);
		register(GenericExceptionMapper.class);
     
        register(new LoggingFeature(LOGGER, java.util.logging.Level.SEVERE, LoggingFeature.Verbosity.PAYLOAD_ANY, Integer.MAX_VALUE));

		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, Arrays.asList("META-INF/oxm/config-oxm.xml","META-INF/oxm/guide-oxm.xml","META-INF/oxm/show-oxm.xml"));
     	properties.put(JAXBContextProperties.MEDIA_TYPE,"application/json");
     	properties.put(MarshallerProperties.JSON_INCLUDE_ROOT,false);
     	properties.put(Marshaller.JAXB_FORMATTED_OUTPUT, true);
     
     	register(new MoxyXmlFeature(properties, null, false, new Class[] {JaxrsError.class,  Config.class, Show.class, Episode.class, GuideEpisode.class, GuideSearchResult.class, GuideInfo.class}));
	}

}