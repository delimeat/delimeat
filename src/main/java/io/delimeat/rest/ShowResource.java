package io.delimeat.rest;

import io.delimeat.core.service.ShowService;
import io.delimeat.core.show.Show;

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
import javax.ws.rs.core.MediaType;

@Path("shows")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShowResource {

	@Inject
	private ShowService showService;

	@GET
	public List<Show> getAll() throws Exception {
		return showService.readAll();
	}

	@Path("{id}")
	@GET
	public Show read(@PathParam("id") Long id) throws Exception {
		return showService.read(id);
	}

	@Path("{id}")
	@PUT
	public Show update(Show show) throws Exception {
		return showService.update(show);
	}

	@Path("{id}")
	@DELETE
	public void delete(@PathParam("id") Long id) throws Exception {
		showService.delete(id);
	}

	@POST
	public Show create(Show show) throws Exception {
		return showService.create(show);
	}

}
