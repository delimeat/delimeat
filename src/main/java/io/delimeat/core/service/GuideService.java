package io.delimeat.core.service;

import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.core.guide.GuideInfo;
import io.delimeat.core.guide.GuideSearchResult;

import java.io.IOException;
import java.util.List;

public interface GuideService {

	public List<GuideSearchResult> readLike(final String title) throws IOException, Exception;
	
	public GuideInfo read(final String guideId) throws IOException, Exception;
	
	public List<GuideEpisode> readEpisodes(final String guideId) throws IOException, Exception;
	
}
