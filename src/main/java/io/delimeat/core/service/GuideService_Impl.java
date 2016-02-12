package io.delimeat.core.service;

import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.core.guide.GuideException;
import io.delimeat.core.guide.GuideInfo;
import io.delimeat.core.guide.GuideDao;
import io.delimeat.core.guide.GuideNotAuthorisedException;
import io.delimeat.core.guide.GuideNotFoundException;
import io.delimeat.core.guide.GuideSearchResult;

import java.util.Iterator;
import java.util.List;

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
		List<GuideEpisode> eps = guideDao.episodes(guideId);
		Iterator<GuideEpisode> it = eps.iterator();
		while (it.hasNext()) {
			GuideEpisode ep = it.next();
			if (ep.getSeasonNum() == null || ep.getSeasonNum() == 0
					|| ep.getAirDate() == null) {
				it.remove();
			}
		}
		return eps;
	}


}
