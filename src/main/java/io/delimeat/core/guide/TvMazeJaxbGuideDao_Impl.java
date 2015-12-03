package io.delimeat.core.guide;

import io.delimeat.util.jaxb.AbstractJaxbHelper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class TvMazeJaxbGuideDao_Impl extends AbstractJaxbHelper implements GuideSearchDao, GuideInfoDao {

	private URI baseUri;
	private Map<String, String> properties;

	@Override
	public GuideSource getGuideSource() {
		return GuideSource.TVMAZE;
	}

	@Override
	public GuideInfo info(String guideId) throws IOException, Exception {
		String urlEncodedId = URLEncoder.encode(guideId, ENCODING);

		URL url = new URL(baseUri.toString() + "/shows/" + urlEncodedId);

		InputStream input = getUrlHandler().openInput(url, getProperties());
		return unmarshal(input, GuideInfo.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GuideEpisode> episodes(String guideId) throws IOException, Exception {
		String urlEncodedId = URLEncoder.encode(guideId, ENCODING);

		URL url = new URL(baseUri.toString() + "/shows/" + urlEncodedId + "/episodes");

		InputStream input = getUrlHandler().openInput(url, getProperties());
		return (List<GuideEpisode>) unmarshal(input, GuideEpisode.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GuideSearchResult> search(String title) throws IOException, Exception {
		String urlEncodedTitle = URLEncoder.encode(title, ENCODING);

		URL url = new URL(baseUri.toString() + "/search/shows?q=" + urlEncodedTitle);

		InputStream input = getUrlHandler().openInput(url, getProperties());
		return (List<GuideSearchResult>) unmarshal(input, GuideSearchResult.class);
	}

	/**
	 * @return the baseUri
	 */
	public URI getBaseUri() {
		return baseUri;
	}

	/**
	 * @param baseUri
	 *            the baseUri to set
	 */
	public void setBaseUri(URI baseUri) {
		this.baseUri = baseUri;
	}

	/**
	 * @return the properties
	 */
	public Map<String, String> getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

}
