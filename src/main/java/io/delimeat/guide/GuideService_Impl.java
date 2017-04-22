package io.delimeat.guide;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.guide.domain.GuideInfo;
import io.delimeat.guide.domain.GuideSearchResult;

@Service("guideServiceId")
public class GuideService_Impl implements GuideService {

	@Autowired
	private GuideDao guideDao;

	public GuideDao getGuideDao() {
		return guideDao;
	}

	public void setGuideDao(GuideDao infoDao) {
		this.guideDao = infoDao;
	}

	@Override
	public List<GuideSearchResult> readLike(final String title) throws Exception {
		return guideDao.search(title);
	}

	@Override
	public GuideInfo read(final String guideId) throws Exception {
		return guideDao.info(guideId);
	}

	@Override
	public List<GuideEpisode> readEpisodes(final String guideId) throws Exception {
		return guideDao.episodes(guideId).stream()
				.filter(p -> (p.getSeasonNum() != null && p.getSeasonNum() != 0 && p.getAirDate() != null))
				.sorted()
				.collect(Collectors.toList());
	}


}
