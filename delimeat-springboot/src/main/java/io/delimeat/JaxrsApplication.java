package io.delimeat;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import io.delimeat.rest.ConfigResource;
import io.delimeat.rest.EpisodeResource;
import io.delimeat.rest.GuideResource;
import io.delimeat.rest.ShowResource;

@Component
@ApplicationPath("/api")
public class JaxrsApplication extends ResourceConfig {

	public JaxrsApplication() {
		register(ConfigResource.class);
		register(EpisodeResource.class);
		register(GuideResource.class);
		register(ShowResource.class);
	}
}
