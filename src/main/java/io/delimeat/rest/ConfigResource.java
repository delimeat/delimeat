package io.delimeat.rest;

import io.delimeat.core.config.Config;
import io.delimeat.core.config.ConfigException;
import io.delimeat.core.service.ConfigService;
import io.delimeat.util.jaxrs.ETag;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

@Path("config")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConfigResource {

    @Inject
    private ConfigService service;

    @GET
    @ETag
    public Config get(@Context Request request) throws ConfigException {
      Config config = service.read();
      EntityTag etag = new EntityTag(Integer.toString(config.hashCode()));
      Response.ResponseBuilder rb = request.evaluatePreconditions(etag);
      if (rb != null) {
        throw new WebApplicationException(rb.build());
      }
      return config;
    }

    @PUT
    @ETag
    public Config update(Config config, @Context Request request) throws ConfigException {
      Config oldConfig = service.read();
      EntityTag etag = new EntityTag(Integer.toString(oldConfig.hashCode()));
      System.out.println(etag);
      Response.ResponseBuilder rb = request.evaluatePreconditions(etag);
      if (rb != null) {
        throw new WebApplicationException(rb.build());
      }
      return service.update(config);
    }

}
