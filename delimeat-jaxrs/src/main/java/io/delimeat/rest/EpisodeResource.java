package io.delimeat.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import io.delimeat.show.EpisodeService;
import io.delimeat.show.entity.Episode;
import io.delimeat.show.exception.ShowException;
import io.delimeat.show.exception.ShowNotFoundException;

@Path("/episode")
@Produces("application/json")
@Consumes("application/json")
public class EpisodeResource {

	@Inject EpisodeService service;

	@GET
	public List<Episode> read() throws ShowException {
		return service.findAllPending();
	}
	
	@PUT
	@Path("/{id}")
	public Episode update(@PathParam("id") Long episodeId, Episode episode) throws ShowNotFoundException, ShowException {
		return service.update(episode);
	}
	
}
