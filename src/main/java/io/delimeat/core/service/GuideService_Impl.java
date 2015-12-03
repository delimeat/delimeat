package io.delimeat.core.service;

import io.delimeat.core.guide.GuideInfo;
import io.delimeat.core.guide.GuideInfoDao;
import io.delimeat.core.guide.GuideSearchDao;
import io.delimeat.core.guide.GuideSearchResult;

import java.io.IOException;
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
	public List<GuideSearchResult> readLike(String id) throws IOException,
			Exception {
		return searchDao.search(id);
	}

	@Override
	public GuideInfo read(String guideId) throws IOException, Exception {
		return infoDao.info(guideId);
	}

}
