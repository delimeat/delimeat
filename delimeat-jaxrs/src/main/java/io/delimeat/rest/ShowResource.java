package io.delimeat.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import io.delimeat.show.ShowService;
import io.delimeat.show.entity.Episode;
import io.delimeat.show.entity.Show;
import io.delimeat.show.exception.ShowException;
import io.delimeat.show.exception.ShowNotFoundException;

@Path("/show")
@Produces("application/json")
@Consumes("application/json")
public class ShowResource {

	@Inject
	private ShowService showService;

	public ShowService getShowService() {
		return showService;
	}

	public void setShowService(ShowService showService) {
		this.showService = showService;
	}
	
	@GET
	public List<Show> readAll() throws ShowException{
		return showService.readAll();
	}
	
	@POST
	public Show createShow(Show show) throws ShowException {
		showService.create(show);
		return show;
	}
	
	@GET
	@Path("/{id}")
	public Show read(@QueryParam("id") Long id) throws ShowNotFoundException, ShowException {
		return showService.read(id);
	}
	
	@PUT
	@Path("/{id}")
	public Show update(@QueryParam("id") Long id, Show show) throws ShowNotFoundException, ShowException {
		return showService.update(show);
	}
	
	@DELETE
	@Path("/{id}")
	public void delete(@QueryParam("id") Long id) throws ShowException {
		showService.delete(id);
	}
	
	@GET
	@Path("/{id}/episode")
	public List<Episode> episodes(@QueryParam("id") Long id) throws ShowNotFoundException, ShowException{
		return showService.read(id).getEpisodes();
	}	
}
