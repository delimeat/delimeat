package io.delimeat.guide;

import java.util.List;

import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.guide.domain.GuideInfo;
import io.delimeat.guide.domain.GuideSearchResult;

public interface GuideService {

	public List<GuideSearchResult> readLike(final String title) throws Exception;
	
	public GuideInfo read(final String guideId) throws Exception;
	
	public List<GuideEpisode> readEpisodes(final String guideId) throws Exception;
	
}
