package io.delimeat.rest;

import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.core.guide.GuideInfo;
import io.delimeat.core.guide.GuideSearchResult;
import io.delimeat.core.service.GuideService;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("guide")
@Produces(MediaType.APPLICATION_JSON)
public class GuideResource {

	@Inject
	private GuideService service;

	@Path("search/{title}")
	@GET
	public List<GuideSearchResult> get(@PathParam("title") String title)
			throws IOException, Exception {
		return service.readLike(title);
	}

	@Path("info/{id}")
	@GET
	public GuideInfo getInfo(@PathParam("id") String guideId) throws IOException, Exception {
		return service.read(guideId);
	}
	
	@Path("info/{id}/episodes")
	@GET
	public List<GuideEpisode> getEpisodes(@PathParam("id") String guideId) throws IOException, Exception {
		return service.readEpisodes(guideId);
	}
}
