package io.delimeat.rest;

import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.core.guide.GuideException;
import io.delimeat.core.guide.GuideInfo;
import io.delimeat.core.guide.GuideNotAuthorisedException;
import io.delimeat.core.guide.GuideNotFoundException;
import io.delimeat.core.guide.GuideSearchResult;
import io.delimeat.core.service.GuideService;
import io.delimeat.util.jaxrs.ETag;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

@Path("guide")
@Produces(MediaType.APPLICATION_JSON)
public class GuideResource {

	@Inject
	private GuideService service;

	@Path("search/{title}")
	@GET
   @ETag
	public List<GuideSearchResult> get(@PathParam("title") String title, @Context Request request) throws GuideNotFoundException, GuideNotAuthorisedException, GuideException {
		List<GuideSearchResult> results = service.readLike(title);
		EntityTag etag = new EntityTag(Integer.toString(results.hashCode()));
		Response.ResponseBuilder rb = request.evaluatePreconditions(etag);
		if (rb != null) {
			throw new WebApplicationException(rb.build());
		}
		return results;
	}

	@Path("info/{id}")
	@GET
   @ETag
	public GuideInfo getInfo(@PathParam("id") String guideId, @Context Request request) throws GuideNotFoundException, GuideNotAuthorisedException, GuideException {
		GuideInfo info = service.read(guideId);
		EntityTag etag = new EntityTag(Integer.toString(info.hashCode()));
		Response.ResponseBuilder rb = request.evaluatePreconditions(etag);
		if (rb != null) {
			throw new WebApplicationException(rb.build());
		}
		return info;
	}
	
	@Path("info/{id}/episodes")
	@GET
   @ETag
	public List<GuideEpisode> getEpisodes(@PathParam("id") String guideId, @Context Request request) throws GuideNotFoundException, GuideNotAuthorisedException, GuideException {
		List<GuideEpisode> episodes = service.readEpisodes(guideId);
		EntityTag etag = new EntityTag(Integer.toString(episodes.hashCode()));
		Response.ResponseBuilder rb = request.evaluatePreconditions(etag);
		if (rb != null) {
			throw new WebApplicationException(rb.build());
		}
		return episodes;
	}
}
