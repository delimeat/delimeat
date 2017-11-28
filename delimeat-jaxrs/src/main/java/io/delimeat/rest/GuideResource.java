package io.delimeat.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import io.delimeat.guide.GuideService;
import io.delimeat.guide.entity.GuideEpisode;
import io.delimeat.guide.entity.GuideInfo;
import io.delimeat.guide.entity.GuideSearchResult;
import io.delimeat.guide.exception.GuideAuthorizationException;
import io.delimeat.guide.exception.GuideException;
import io.delimeat.guide.exception.GuideNotFoundException;

@Path("/guide")
@Produces("application/json")
@Consumes("application/json")
public class GuideResource {

	@Inject
	private GuideService guideService;

	public GuideService getGuideService() {
		return guideService;
	}

	public void setGuideService(GuideService guideService) {
		this.guideService = guideService;
	}
	
	@GET
	@Path("/search/{title}")
	public List<GuideSearchResult> search(@QueryParam("title") String title) throws GuideNotFoundException, GuideAuthorizationException, GuideException {
		return guideService.readLike(title);
	}
	
	@GET
	@Path("/info/{id}")
	public GuideInfo info(@QueryParam("id") String id) throws GuideNotFoundException, GuideAuthorizationException, GuideException {
		return guideService.read(id);
	}
	
	@GET
	@Path("/info/{id}/episode")
	public List<GuideEpisode> episodes(@QueryParam("id") String id) throws GuideNotFoundException, GuideAuthorizationException, GuideException{
		return guideService.readEpisodes(id);
	}
	
}
