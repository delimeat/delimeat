package io.delimeat.core.guide;

import java.util.List;

public interface GuideInfoDao {

	public GuideSource getGuideSource();

	public GuideInfo info(String guideId) throws GuideNotFoundException, GuideNotAuthorisedException, GuideException;

	public List<GuideEpisode> episodes(String guideId) throws GuideNotFoundException, GuideNotAuthorisedException, GuideException;
}
