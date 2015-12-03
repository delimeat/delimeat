package io.delimeat.rest;

import io.delimeat.core.config.Config;
import io.delimeat.core.config.ConfigDao;

import java.io.IOException;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("config")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConfigResource {

	@Inject
	private ConfigDao dao;

	@GET
	public Config get() throws IOException, Exception {
		return dao.read();
	}

	@PUT
	public Config update(Config config) throws IOException, Exception {
		dao.update(config);
		return config;
	}

}
