package io.delimeat.core.show;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import io.delimeat.core.feed.FeedSource;

@XmlAccessorType(XmlAccessType.FIELD)
public class EpisodeResult {

	private int episodeResultId;
	private FeedSource source;
	private String url;
	private String result;
	private boolean valid;
	private int version;

	@XmlTransient
	private Episode episode;

	/**
	 * @return the episodeResultId
	 */
	public int getEpisodeResultId() {
		return episodeResultId;
	}

	/**
	 * @param episodeResultId
	 *            the episodeResultId to set
	 */
	public void setEpisodeResultId(int episodeResultId) {
		this.episodeResultId = episodeResultId;
	}

	/**
	 * @return the episodeId
	 */
	public Episode getEpisode() {
		return episode;
	}

	/**
	 * @param episodeId
	 *            the episodeId to set
	 */
	public void setEpisode(Episode episode) {
		this.episode = episode;
	}

	/**
	 * @return the source
	 */
	public FeedSource getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(FeedSource source) {
		this.source = source;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @return the valid
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * @param valid
	 *            the valid to set
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EpisodeResult [episodeResultId=" + episodeResultId + ", episode="
				+ (episode != null ? episode.getEpisodeId() : null) + ", source=" + source + ", url=" + url + ", valid="
				+ valid + ", version=" + version + "]";
	}

}
