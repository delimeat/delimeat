package io.delimeat.core.guide;

import java.io.IOException;
import java.util.List;

public interface GuideInfoDao {

	public GuideSource getGuideSource();

	public GuideInfo info(String guideId) throws IOException, Exception;

	public List<GuideEpisode> episodes(String guideId) throws IOException, Exception;
}
