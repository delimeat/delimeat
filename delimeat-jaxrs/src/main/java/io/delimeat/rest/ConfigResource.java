package io.delimeat.rest;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import io.delimeat.config.ConfigService;
import io.delimeat.config.entity.Config;
import io.delimeat.config.exception.ConfigConcurrencyException;
import io.delimeat.config.exception.ConfigException;

@Path("/config")
@Produces("application/json")
@Consumes("application/json")
public class ConfigResource {

	@Inject ConfigService service;

	@GET
	public Config read() throws ConfigException {
		return service.read();
	}
	
	@PUT
	public Config update(Config config) throws ConfigConcurrencyException, ConfigException {
		return service.update(config);
	}
}
