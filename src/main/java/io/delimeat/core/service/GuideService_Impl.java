package io.delimeat.core.service;

import io.delimeat.core.guide.GuideInfo;
import io.delimeat.core.guide.GuideInfoDao;
import io.delimeat.core.guide.GuideSearchDao;
import io.delimeat.core.guide.GuideSearchResult;
import io.delimeat.core.guide.GuideSource;
import io.delimeat.core.service.exception.GuideNotFoundException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuideService_Impl implements GuideService {

	private Map<GuideSource,GuideInfoDao> infoDaos = new HashMap<GuideSource,GuideInfoDao>();
	private GuideSearchDao searchDao;

	public Map<GuideSource,GuideInfoDao> getInfoDaos() {
		return infoDaos;
	}

	public void setInfoDaos(Map<GuideSource,GuideInfoDao> infoDaos) {
		this.infoDaos = infoDaos;
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
	public GuideInfo read(GuideSource source, String guideId)
			throws GuideNotFoundException, IOException, Exception {
		if(!infoDaos.containsKey(source)){
			throw new GuideNotFoundException(source);
		}
		return infoDaos.get(source).info(guideId);
	}


}
