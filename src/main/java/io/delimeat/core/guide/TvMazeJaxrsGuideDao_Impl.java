package io.delimeat.core.guide;

import io.delimeat.util.jaxrs.AbstractJaxrsClientHelper;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.ws.rs.core.GenericType;

public class TvMazeJaxrsGuideDao_Impl extends AbstractJaxrsClientHelper implements GuideInfoDao, GuideSearchDao {

	@Override
	public GuideSource getGuideSource() {
		return GuideSource.TVMAZE;
	}

	@Override
	public List<GuideSearchResult> search(String id) throws IOException, Exception {
		return getTarget()
				.path("search")
				.path("shows")
				.queryParam("q", URLEncoder.encode(id, ENCODING))
				.request(getMediaType()).get(new GenericType<List<GuideSearchResult>>() {});
	}

	@Override
	public GuideInfo info(String guideId) throws IOException, Exception {
		return getTarget()
				.path("shows")
				.path(URLEncoder.encode(guideId, ENCODING))
				.request(getMediaType())
				.get(GuideInfo.class);
	}

	@Override
	public List<GuideEpisode> episodes(String guideId) throws IOException, Exception {
		return getTarget()
				.path("shows")
				.path(URLEncoder.encode(guideId, ENCODING))
				.path("episodes")
				.request(getMediaType())
				.get(new GenericType<List<GuideEpisode>>() {});
	}

}
