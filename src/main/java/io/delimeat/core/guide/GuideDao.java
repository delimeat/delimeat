package io.delimeat.core.guide;

import java.util.List;

public interface GuideDao {

	public GuideSource getGuideSource();

	public GuideInfo info(String guideId) throws GuideNotFoundException, GuideNotAuthorisedException, GuideException;

	public List<GuideEpisode> episodes(String guideId) throws GuideNotFoundException, GuideNotAuthorisedException, GuideException;
	
	public List<GuideSearchResult> search(String title) throws GuideNotFoundException, GuideNotAuthorisedException, GuideException;

}
