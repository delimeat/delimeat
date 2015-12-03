package io.delimeat.core.service;

import io.delimeat.core.guide.GuideInfo;
import io.delimeat.core.guide.GuideSearchResult;

import java.io.IOException;
import java.util.List;

public interface GuideService {

	public List<GuideSearchResult> readLike(String id) throws IOException, Exception;

	public GuideInfo read(String guideId) throws IOException, Exception;
}
