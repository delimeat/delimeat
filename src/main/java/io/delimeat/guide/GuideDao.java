package io.delimeat.guide;

import java.util.List;

import io.delimeat.common.util.exception.EntityAuthorizationException;
import io.delimeat.common.util.exception.EntityException;
import io.delimeat.common.util.exception.EntityNotFoundException;
import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.guide.domain.GuideInfo;
import io.delimeat.guide.domain.GuideSearchResult;
import io.delimeat.guide.domain.GuideSource;

public interface GuideDao {

	public GuideSource getGuideSource();

	public GuideInfo info(String guideId) throws EntityNotFoundException, EntityAuthorizationException, EntityException;

	public List<GuideEpisode> episodes(String guideId) throws EntityNotFoundException, EntityAuthorizationException, EntityException;
	
	public List<GuideSearchResult> search(String title) throws EntityNotFoundException, EntityAuthorizationException, EntityException;

}
