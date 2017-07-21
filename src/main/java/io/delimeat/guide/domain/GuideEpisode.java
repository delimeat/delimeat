/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.delimeat.guide.domain;

import java.time.LocalDate;
import java.util.Objects;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import io.delimeat.util.jaxb.LocalDateAdapter;

public class GuideEpisode {

	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	private LocalDate airDate;
	private Integer seasonNum = 0;
	private Integer episodeNum = 0;
	private Integer productionNum = 0;
	private String title;
	/**
	 * @return the airDate
	 */
	public LocalDate getAirDate() {
		return airDate;
	}
	/**
	 * @param airDate the airDate to set
	 */
	public void setAirDate(LocalDate airDate) {
		this.airDate = airDate;
	}
	/**
	 * @return the seasonNum
	 */
	public Integer getSeasonNum() {
		return seasonNum;
	}
	/**
	 * @param seasonNum the seasonNum to set
	 */
	public void setSeasonNum(Integer seasonNum) {
		this.seasonNum = seasonNum;
	}
	/**
	 * @return the episodeNum
	 */
	public Integer getEpisodeNum() {
		return episodeNum;
	}
	/**
	 * @param episodeNum the episodeNum to set
	 */
	public void setEpisodeNum(Integer episodeNum) {
		this.episodeNum = episodeNum;
	}
	/**
	 * @return the productionNum
	 */
	public Integer getProductionNum() {
		return productionNum;
	}
	/**
	 * @param productionNum the productionNum to set
	 */
	public void setProductionNum(Integer productionNum) {
		this.productionNum = productionNum;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(airDate,episodeNum, productionNum, seasonNum, title);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GuideEpisode other = (GuideEpisode) obj;
		if (airDate == null) {
			if (other.airDate != null)
				return false;
		} else if (!airDate.equals(other.airDate))
			return false;
		if (episodeNum == null) {
			if (other.episodeNum != null)
				return false;
		} else if (!episodeNum.equals(other.episodeNum))
			return false;
		if (productionNum == null) {
			if (other.productionNum != null)
				return false;
		} else if (!productionNum.equals(other.productionNum))
			return false;
		if (seasonNum == null) {
			if (other.seasonNum != null)
				return false;
		} else if (!seasonNum.equals(other.seasonNum))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GuideEpisode [" + (airDate != null ? "airDate=" + airDate + ", " : "")
				+ (seasonNum != null ? "seasonNum=" + seasonNum + ", " : "")
				+ (episodeNum != null ? "episodeNum=" + episodeNum + ", " : "")
				+ (productionNum != null ? "productionNum=" + productionNum + ", " : "")
				+ (title != null ? "title=" + title : "") + "]";
	}

}
