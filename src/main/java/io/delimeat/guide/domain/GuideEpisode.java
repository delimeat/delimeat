package io.delimeat.guide.domain;

import java.time.LocalDate;
import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import io.delimeat.show.domain.Episode;


public class GuideEpisode implements Comparable<GuideEpisode> {

	private LocalDate airDate;

	private Integer seasonNum = 0;

	private Integer episodeNum = 0;

	private Integer productionNum = 0;

	private String title;

	/**
	 * 
	 * @return Air date for the episode
	 */
	public LocalDate getAirDate() {
		return airDate;
	}

	/**
	 * @return Episode number for the episode
	 */
	public Integer getEpisodeNum() {
		return episodeNum;
	}

	/**
	 * @return Production Number for the episode
	 */
	public Integer getProductionNum() {
		return productionNum;
	}

	/**
	 * @return Season number for the episode
	 */
	public Integer getSeasonNum() {
		return seasonNum;
	}

	/**
	 * @return Title of the episode
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set air date
	 * 
	 * @param airDate
	 */
	public void setAirDate(LocalDate airDate) {
		this.airDate = airDate;
	}

	/**
	 * Set season number
	 * 
	 * @param seasonNum
	 */
	public void setSeasonNum(Integer seasonNum) {
		this.seasonNum = seasonNum;
	}

	/**
	 * Set episode number
	 * 
	 * @param episodeNum
	 */
	public void setEpisodeNum(Integer episodeNum) {
		this.episodeNum = episodeNum;
	}

	/**
	 * Set production number
	 * 
	 * @param productionNum
	 */
	public void setProductionNum(Integer productionNum) {
		this.productionNum = productionNum;
	}

	/**
	 * Set episode title
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(GuideEpisode other) {
		return ComparisonChain.start()
								.compare(this.getAirDate(), other.getAirDate(), Ordering.natural().nullsFirst())
								.compare(this.getSeasonNum(), other.getSeasonNum(), Ordering.natural().nullsFirst())
								.compare(this.getEpisodeNum(), other.getEpisodeNum(), Ordering.natural().nullsFirst())
								.result();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}

		if (this == object) {
			return true;
		}

		if (object instanceof Episode) {
			Episode otherEp = (Episode) object;
			return ComparisonChain.start()
									.compare(this.seasonNum, new Integer(otherEp.getSeasonNum()))
									.compare(this.episodeNum, new Integer(otherEp.getEpisodeNum()))
									.result() == 0 ? true : false;
		} else if (object instanceof GuideEpisode) {
			GuideEpisode otherEp = (GuideEpisode) object;
			return ComparisonChain.start()
									.compare(this.seasonNum, otherEp.getSeasonNum())
									.compare(this.episodeNum, otherEp.getEpisodeNum())
									.result() == 0 ? true : false;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
							.add("title", title)
							.add("airDate", airDate)
							.add("seasonNum", seasonNum)
							.add("episodeNum", episodeNum)
							.omitNullValues()
							.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(title, airDate, seasonNum, episodeNum);
	}

}
