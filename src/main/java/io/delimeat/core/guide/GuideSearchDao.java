package io.delimeat.core.guide;

import java.io.IOException;
import java.util.List;

public interface GuideSearchDao {

	public GuideSource getGuideSource();

	public List<GuideSearchResult> search(String id) throws IOException, Exception;

}
