package io.delimeat.core.service;

import io.delimeat.core.guide.GuideInfo;
import io.delimeat.core.guide.GuideSearchResult;
import io.delimeat.core.guide.GuideSource;
import io.delimeat.core.service.exception.GuideNotFoundException;

import java.io.IOException;
import java.util.List;

public interface GuideService {

	public List<GuideSearchResult> readLike(String id) throws IOException, Exception;
	
	public GuideInfo read(GuideSource source, String guideId) throws GuideNotFoundException, IOException, Exception;
	
}
