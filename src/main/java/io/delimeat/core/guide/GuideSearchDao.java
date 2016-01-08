package io.delimeat.core.guide;

import java.util.List;

public interface GuideSearchDao {

	public GuideSource getGuideSource();

	public List<GuideSearchResult> search(String title) throws GuideNotFoundException, GuideNotAuthorisedException, GuideException;

}
