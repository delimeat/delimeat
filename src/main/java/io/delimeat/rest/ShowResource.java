package io.delimeat.rest;

import io.delimeat.core.service.ShowService;
import io.delimeat.core.show.Show;
import io.delimeat.util.jaxrs.ETag;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

@Path("shows")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShowResource {

	@Inject
	private ShowService showService;

	@GET
  	@ETag
	public List<Show> getAll(@Context Request request) throws Exception {
       List<Show> shows = showService.readAll();
       EntityTag etag = new EntityTag(Integer.toString(shows.hashCode()));
       Response.ResponseBuilder rb = request.evaluatePreconditions(etag);
       if (rb != null) {
         throw new WebApplicationException(rb.build());
       }
     
       return shows;
	}

	@Path("{id}")
	@GET
  	@ETag
	public Show read(@PathParam("id") Long id, @Context Request request) throws Exception {
       Show show = showService.read(id);
       EntityTag etag = new EntityTag(Integer.toString(show.hashCode()));
       Response.ResponseBuilder rb = request.evaluatePreconditions(etag);
       if (rb != null) {
         throw new WebApplicationException(rb.build());
       }
       return show;
	}

	@Path("{id}")
	@PUT
  	@ETag
	public Show update(Show show) throws Exception {
       return showService.update(show);
	}

	@Path("{id}")
	@DELETE
	public void delete(@PathParam("id") Long id) throws Exception {
		showService.delete(id);
	}

	@POST
  	@ETag
	public Show create(Show show) throws Exception {
       return showService.create(show);
	}

}
