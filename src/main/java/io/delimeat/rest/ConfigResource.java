package io.delimeat.rest;

import io.delimeat.core.config.Config;
import io.delimeat.core.config.ConfigException;
import io.delimeat.core.service.ConfigService;
import io.delimeat.rest.util.jaxrs.ETag;
import io.delimeat.rest.util.jaxrs.ETagGenerator;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;

@Path("config")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConfigResource {

	@Inject
	private ConfigService service;

	@GET
	@ETag
	public Config get() throws ConfigException {
		return service.read();
	}

	@PUT
	@ETag
	public Config update(Config config, @Context Request request) throws ConfigException {
		return service.update(config);
	}

	@ETagGenerator("update")
	public EntityTag generateConfigEtag() throws Exception {
		Config existingConfig = service.read();
		return new EntityTag(Integer.toString(existingConfig.hashCode()));
	}

}
