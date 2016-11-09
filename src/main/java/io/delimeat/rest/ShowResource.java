package io.delimeat.rest;

import io.delimeat.core.service.ShowService;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;
import io.delimeat.rest.util.jaxrs.ETag;
import io.delimeat.rest.util.jaxrs.ETagGenerator;

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
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;

@Path("shows")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShowResource {

	@Inject
	private ShowService showService;

	public void setShowService(ShowService showService) {
		this.showService = showService;
	}

	public ShowService getShowService() {
		return showService;
	}

	@GET
	@ETag
	public List<Show> getAll() throws Exception {
		return showService.readAll();
	}

	@Path("{id}")
	@GET
	@ETag
	public Show read(@PathParam("id") Long id) throws Exception {
		return showService.read(id);
	}

	@Path("{id}")
	@PUT
	@ETag
	public Show update(@PathParam("id") Long id, Show show) throws Exception {
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

	@Path("{id}/episodes")
	@GET
	@ETag
	public List<Episode> getAllEpisodes(@PathParam("id") Long id) throws Exception {
		return showService.readAllEpisodes(id);
	}

	@ETagGenerator("update")
	public EntityTag generateEtagShow(@PathParam("id") Long id) throws Exception {
		Show show = showService.read(id);
		return new EntityTag(Integer.toString(show.hashCode()));
	}

}
