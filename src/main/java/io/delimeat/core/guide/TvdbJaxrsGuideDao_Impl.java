package io.delimeat.core.guide;

import io.delimeat.util.jaxrs.AbstractJaxrsClientHelper;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.xml.bind.JAXBException;

public class TvdbJaxrsGuideDao_Impl extends AbstractJaxrsClientHelper implements GuideInfoDao, GuideSearchDao {

	private String apiKey;
	private TvdbToken token;
	private int validPeriodInMs = 0;

	@Override
	public GuideSource getGuideSource() {
		return GuideSource.TVDB;
	}

	public TvdbToken login(String apiKey) throws Exception {
		TvdbApiKey key = new TvdbApiKey();
		key.setValue(apiKey);
		Entity<TvdbApiKey> entity = Entity.entity(key, getMediaType());
		return getTarget().path("login").request(getMediaType()).post(entity, TvdbToken.class);

	}

	public TvdbToken refreshToken(TvdbToken token) throws Exception {
		return getTarget().path("refresh_token").request(getMediaType())
				.header("Authorization", "Bearer " + token.getValue()).get(TvdbToken.class);

	}

	@Override
	public List<GuideSearchResult> search(String id) throws IOException, Exception {
		return getTarget().path("search").path("series").queryParam("name", URLEncoder.encode(id, ENCODING))
				.request(getMediaType()).header("Authorization", "Bearer " + getToken().getValue())
				.get(GuideSearch.class).getResults();
	}

	@Override
	public GuideInfo info(String guideId) throws IOException, Exception {
		return getTarget().path("series").path(URLEncoder.encode(guideId, ENCODING)).request(getMediaType())
				.header("Authorization", "Bearer " + getToken().getValue()).get(GuideInfo.class);
	}

	@Override
	public List<GuideEpisode> episodes(String guideId) throws IOException, Exception {
		List<GuideEpisode> episodes = new ArrayList<GuideEpisode>();
		Integer page = 1;
		do {
			TvdbEpisodes result = episodes(guideId, page);
			episodes.addAll(result.getEpisodes());
			page = result.getNext();
		} while (page != null && page > 0);

		return episodes;
	}

	public TvdbEpisodes episodes(String guideId, int page) throws IOException, Exception {
		return getTarget().path("series").path(URLEncoder.encode(guideId, ENCODING)).path("episodes")
				.queryParam("page", page).request(getMediaType())
				.header("Authorization", "Bearer " + getToken().getValue()).get(TvdbEpisodes.class);
	}

	/**
	 * @return the apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * @param apiKey
	 *            the apiKey to set
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * @return the token
	 * @throws IOException
	 * @throws JAXBException
	 */
	public TvdbToken getToken() throws JAXBException, IOException, Exception {
		long now = System.currentTimeMillis();
		if (token == null || now >= token.getTime() + validPeriodInMs) { 
			token = login(apiKey);
		} else if (now >= token.getTime() + validPeriodInMs - 10 * 60 * 1000) {
			token = refreshToken(token);
		}
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(TvdbToken token) {
		this.token = token;
	}

	/**
	 * @return the validPeriodInMs
	 */
	public int getValidPeriodInMs() {
		return validPeriodInMs;
	}

	/**
	 * @param validPeriodInMs
	 *            the validPeriodInMs to set
	 */
	public void setValidPeriodInMs(int validPeriodInMs) {
		this.validPeriodInMs = validPeriodInMs;
	}
}
