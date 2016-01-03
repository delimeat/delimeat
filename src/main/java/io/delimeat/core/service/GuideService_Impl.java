package io.delimeat.core.service;

import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.core.guide.GuideInfo;
import io.delimeat.core.guide.GuideInfoDao;
import io.delimeat.core.guide.GuideSearchDao;
import io.delimeat.core.guide.GuideSearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuideService_Impl implements GuideService {

	private GuideInfoDao infoDao;
	private GuideSearchDao searchDao;

	public GuideInfoDao getInfoDao() {
		return infoDao;
	}

	public void setInfoDao(GuideInfoDao infoDao) {
		this.infoDao = infoDao;
	}

	public GuideSearchDao getSearchDao() {
		return searchDao;
	}

	public void setSearchDao(GuideSearchDao searchDao) {
		this.searchDao = searchDao;
	}

	@Override
	public List<GuideSearchResult> readLike(final String title) throws IOException,
			Exception {
		return searchDao.search(title);
	}

	@Override
	public GuideInfo read(final String guideId)
			throws IOException, Exception {
		return infoDao.info(guideId);
	}

	@Override
	public List<GuideEpisode> readEpisodes(final String guideId) throws IOException,
			Exception {
		List<GuideEpisode> cleanEps = new ArrayList<GuideEpisode>();
		List<GuideEpisode> eps = infoDao.episodes(guideId);
		for(GuideEpisode ep: eps){
			if(ep.getSeasonNum() !=null && ep.getSeasonNum() !=0 && ep.getAirDate() !=null){
				cleanEps.add(ep);
			}
		}
		return cleanEps;
	}


}
