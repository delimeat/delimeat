package io.delimeat.guide;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.guide.domain.GuideInfo;
import io.delimeat.guide.domain.GuideSearchResult;

@RestController
@RequestMapping(path = "/api")
public class GuideController {
	
	@Autowired
	private GuideService service;

	@RequestMapping(value = "/guide/search/{title}", method = RequestMethod.GET, produces = "application/json")
	public List<GuideSearchResult> get(@PathVariable(value="title", required=true) String title) throws Exception {
		return service.readLike(title);
	}

	@RequestMapping(value = "/guide/info/{id}", method = RequestMethod.GET, produces = "application/json")
	public GuideInfo getInfo(@PathVariable(value="id", required=true) String guideId) throws Exception {
		return service.read(guideId);
	}

	@RequestMapping(value = "/guide/info/{id}/episodes", method = RequestMethod.GET, produces = "application/json")
	public List<GuideEpisode> getEpisodes(@PathVariable(value="id", required=true) String guideId) throws Exception{
		return service.readEpisodes(guideId);
	}

}
