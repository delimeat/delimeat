package io.delimeat.core.service;

import java.util.List;
import java.util.stream.Collectors;

import io.delimeat.core.guide.GuideDao;
import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.core.guide.GuideException;
import io.delimeat.core.guide.GuideInfo;
import io.delimeat.core.guide.GuideNotAuthorisedException;
import io.delimeat.core.guide.GuideNotFoundException;
import io.delimeat.core.guide.GuideSearchResult;

public class GuideService_Impl implements GuideService {

	private GuideDao guideDao;

	public GuideDao getGuideDao() {
		return guideDao;
	}

	public void setGuideDao(GuideDao infoDao) {
		this.guideDao = infoDao;
	}

	@Override
	public List<GuideSearchResult> readLike(final String title) throws GuideNotFoundException, GuideNotAuthorisedException, GuideException {
		return guideDao.search(title);
	}

	@Override
	public GuideInfo read(final String guideId) throws GuideNotFoundException, GuideNotAuthorisedException, GuideException {
		return guideDao.info(guideId);
	}

	@Override
	public List<GuideEpisode> readEpisodes(final String guideId)
			throws GuideNotFoundException, GuideNotAuthorisedException,
			GuideException {
		return guideDao.episodes(guideId).stream()
				.filter(p -> (p.getSeasonNum() != null && p.getSeasonNum() != 0 && p.getAirDate() != null))
				.sorted()
				.collect(Collectors.toList());
	}


}
