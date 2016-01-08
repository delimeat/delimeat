package io.delimeat.core.service;

import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.core.guide.GuideException;
import io.delimeat.core.guide.GuideInfo;
import io.delimeat.core.guide.GuideNotAuthorisedException;
import io.delimeat.core.guide.GuideNotFoundException;
import io.delimeat.core.guide.GuideSearchResult;

import java.util.List;

public interface GuideService {

	public List<GuideSearchResult> readLike(final String title) throws GuideNotFoundException, GuideNotAuthorisedException, GuideException;
	
	public GuideInfo read(final String guideId) throws GuideNotFoundException, GuideNotAuthorisedException, GuideException;
	
	public List<GuideEpisode> readEpisodes(final String guideId) throws GuideNotFoundException, GuideNotAuthorisedException, GuideException;
	
}
