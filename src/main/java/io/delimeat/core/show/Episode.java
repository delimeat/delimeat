package io.delimeat.core.show;

import io.delimeat.util.jaxb.TvdbDateAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class Episode {

	private long episodeId;
	private String title;
	@XmlJavaTypeAdapter(value=TvdbDateAdapter.class)
	private Date airDate;
	private int seasonNum;
	private int episodeNum;
	private boolean doubleEp;
	private int version;
	private List<EpisodeResult> results = new ArrayList<EpisodeResult>();

	@XmlTransient
	private Show show;

	/**
	 * @return the episodeId
	 */
	public long getEpisodeId() {
		return episodeId;
	}

	/**
	 * @param episodeId
	 *            the episodeId to set
	 */
	public void setEpisodeId(long episodeId) {
		this.episodeId = episodeId;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the airDateTime
	 */
	public Date getAirDate() {
		return airDate;
	}

	/**
	 * @param airDateTime
	 *            the airDateTime to set
	 */
	public void setAirDate(Date airDate) {
		this.airDate = airDate;
	}

	/**
	 * @return the seasonNum
	 */
	public int getSeasonNum() {
		return seasonNum;
	}

	/**
	 * @param seasonNum
	 *            the seasonNum to set
	 */
	public void setSeasonNum(int seasonNum) {
		this.seasonNum = seasonNum;
	}

	/**
	 * @return the episodeNum
	 */
	public int getEpisodeNum() {
		return episodeNum;
	}

	/**
	 * @param episodeNum
	 *            the episodeNum to set
	 */
	public void setEpisodeNum(int episodeNum) {
		this.episodeNum = episodeNum;
	}

	/**
	 * @return the doubleEp
	 */
	public boolean isDoubleEp() {
		return doubleEp;
	}

	/**
	 * @param doubleEp
	 *            the doubleEp to set
	 */
	public void setDoubleEp(boolean doubleEp) {
		this.doubleEp = doubleEp;
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

	/**
	 * @return the show
	 */
	public Show getShow() {
		return show;
	}

	/**
	 * @param show
	 *            the show to set
	 */
	public void setShow(Show show) {
		this.show = show;
	}

	/**
	 * @return the results
	 */
	public List<EpisodeResult> getResults() {
		return results;
	}

	/**
	 * @param results
	 *            the results to set
	 */
	public void setResults(List<EpisodeResult> results) {
		this.results = results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Episode [episodeId=" + episodeId + ", title=" + title + ", airDate=" + airDate + ", seasonNum="
				+ seasonNum + ", episodeNum=" + episodeNum + ", doubleEp=" + doubleEp + ", showId="
				+ (show != null ? Long.toString(show.getShowId()) : null) + ", results=" + results + ", version="
				+ version + "]";
	}

}
