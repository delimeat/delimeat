package io.delimeat.rest;

import io.delimeat.core.service.ShowService;
import io.delimeat.core.show.Show;
import io.delimeat.util.DelimeatUtils;

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
import javax.ws.rs.core.GenericEntity;
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
	public Response getAll(@Context Request request) throws Exception {
       List<Show> shows = showService.readAll();
       EntityTag etag = createShowsEtag(shows);
       Response.ResponseBuilder rb = request.evaluatePreconditions(etag);
       if (rb != null) {
         throw new WebApplicationException(rb.build());
       }
     
       return Response.ok(new GenericEntity<List<Show>>(shows){}).tag(etag).build();
	}

	@Path("{id}")
	@GET
	public Response read(@PathParam("id") Long id, @Context Request request) throws Exception {
       Show show = showService.read(id);
       EntityTag etag = createShowEtag(show);
       Response.ResponseBuilder rb = request.evaluatePreconditions(etag);
       if (rb != null) {
         throw new WebApplicationException(rb.build());
       }
       return Response.ok(show).tag(etag).build();
	}

	@Path("{id}")
	@PUT
	public Response update(Show show) throws Exception {
       Show updatedShow = showService.update(show);
       EntityTag etag = createShowEtag(updatedShow);
       return Response.ok(updatedShow).tag(etag).build();
	}

	@Path("{id}")
	@DELETE
	public void delete(@PathParam("id") Long id) throws Exception {
		showService.delete(id);
	}

	@POST
	public Response create(Show show) throws Exception {
       Show createdShow = showService.create(show);
       EntityTag etag = createShowEtag(createdShow);
       return Response.ok(createdShow).tag(etag).build();
	}
  
  public static EntityTag createShowEtag(Show show) {
    	StringBuilder builder = new StringBuilder();
    	builder.append(show.getVersion());
    	builder.append(";");
    	builder.append(show.getShowId());
		byte[] sha1 = DelimeatUtils.getSHA1(builder.toString().getBytes());
    	String hex = DelimeatUtils.toHex(sha1);
    	return new EntityTag(hex);
  }
  
  public static EntityTag createShowsEtag(List<Show> shows) {
    	StringBuilder builder = new StringBuilder();
    	for(Show show: shows){
        builder.append(show.getVersion());
        builder.append(";");
        builder.append(show.getShowId());
      }
		byte[] sha1 = DelimeatUtils.getSHA1(builder.toString().getBytes());
    	String hex = DelimeatUtils.toHex(sha1);
    	return new EntityTag(hex);
  }

}
