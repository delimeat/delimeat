package io.delimeat.rest;

import io.delimeat.core.config.Config;
import io.delimeat.core.config.ConfigException;
import io.delimeat.core.service.ConfigService;
import io.delimeat.util.DelimeatUtils;

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
    public Response get(@Context Request request) throws ConfigException {
      Config config = service.read();
      EntityTag etag = createConfigEtag(config);
      Response.ResponseBuilder rb = request.evaluatePreconditions(etag);
      if (rb != null) {
        throw new WebApplicationException(rb.build());
      }

      return Response.ok(config).tag(etag).build();
    }

    @PUT
    public Response update(Config config) throws ConfigException {
      Config updatedConfig = service.update(config);
      EntityTag etag = createConfigEtag(updatedConfig);
      return Response.ok(updatedConfig).tag(etag).build();
    }

    public static EntityTag createConfigEtag(Config config) {
      byte[] sha1 = DelimeatUtils.getSHA1(config.toString().getBytes());
      String hex = DelimeatUtils.toHex(sha1);
      return new EntityTag(hex);
    }

}
