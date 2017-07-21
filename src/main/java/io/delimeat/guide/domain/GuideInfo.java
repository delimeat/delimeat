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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import io.delimeat.util.jaxb.LocalDateAdapter;
import io.delimeat.util.jaxb.LocalTimeAdapter;

public class GuideInfo {

	private String description;
	private int runningTime;
	private String timezone;
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	private LocalDate firstAired;
	private List<String> genres = new ArrayList<String>();
	private List<AiringDay> airDays = new ArrayList<AiringDay>();
	private boolean airing = true;
	private String title;
	@XmlJavaTypeAdapter(LocalTimeAdapter.class)
	private LocalTime airTime;
	private String guideId;
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	private LocalDate lastUpdated;
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the runningTime
	 */
	public int getRunningTime() {
		return runningTime;
	}
	/**
	 * @param runningTime the runningTime to set
	 */
	public void setRunningTime(int runningTime) {
		this.runningTime = runningTime;
	}
	/**
	 * @return the timezone
	 */
	public String getTimezone() {
		return timezone;
	}
	/**
	 * @param timezone the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	/**
	 * @return the firstAired
	 */
	public LocalDate getFirstAired() {
		return firstAired;
	}
	/**
	 * @param firstAired the firstAired to set
	 */
	public void setFirstAired(LocalDate firstAired) {
		this.firstAired = firstAired;
	}
	/**
	 * @return the genres
	 */
	public List<String> getGenres() {
		return genres;
	}
	/**
	 * @param genres the genres to set
	 */
	public void setGenres(List<String> genres) {
		this.genres = genres;
	}
	/**
	 * @return the airDays
	 */
	public List<AiringDay> getAirDays() {
		return airDays;
	}
	/**
	 * @param airDays the airDays to set
	 */
	public void setAirDays(List<AiringDay> airDays) {
		this.airDays = airDays;
	}
	/**
	 * @return the airing
	 */
	public boolean isAiring() {
		return airing;
	}
	/**
	 * @param airing the airing to set
	 */
	public void setAiring(boolean airing) {
		this.airing = airing;
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
	/**
	 * @return the airTime
	 */
	public LocalTime getAirTime() {
		return airTime;
	}
	/**
	 * @param airTime the airTime to set
	 */
	public void setAirTime(LocalTime airTime) {
		this.airTime = airTime;
	}
	/**
	 * @return the guideId
	 */
	public String getGuideId() {
		return guideId;
	}
	/**
	 * @param guideId the guideId to set
	 */
	public void setGuideId(String guideId) {
		this.guideId = guideId;
	}
	/**
	 * @return the lastUpdated
	 */
	public LocalDate getLastUpdated() {
		return lastUpdated;
	}
	/**
	 * @param lastUpdated the lastUpdated to set
	 */
	public void setLastUpdated(LocalDate lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(airDays,airTime, airing, description, firstAired, genres, guideId, lastUpdated, runningTime, timezone, title);
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
		GuideInfo other = (GuideInfo) obj;
		if (airDays == null) {
			if (other.airDays != null)
				return false;
		} else if (!airDays.equals(other.airDays))
			return false;
		if (airTime == null) {
			if (other.airTime != null)
				return false;
		} else if (!airTime.equals(other.airTime))
			return false;
		if (airing != other.airing)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (firstAired == null) {
			if (other.firstAired != null)
				return false;
		} else if (!firstAired.equals(other.firstAired))
			return false;
		if (genres == null) {
			if (other.genres != null)
				return false;
		} else if (!genres.equals(other.genres))
			return false;
		if (guideId == null) {
			if (other.guideId != null)
				return false;
		} else if (!guideId.equals(other.guideId))
			return false;
		if (lastUpdated == null) {
			if (other.lastUpdated != null)
				return false;
		} else if (!lastUpdated.equals(other.lastUpdated))
			return false;
		if (runningTime != other.runningTime)
			return false;
		if (timezone == null) {
			if (other.timezone != null)
				return false;
		} else if (!timezone.equals(other.timezone))
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
		return "GuideInfo [" + (description != null ? "description=" + description + ", " : "") + "runningTime="
				+ runningTime + ", " + (timezone != null ? "timezone=" + timezone + ", " : "")
				+ (firstAired != null ? "firstAired=" + firstAired + ", " : "")
				+ (genres != null ? "genres=" + genres + ", " : "")
				+ (airDays != null ? "airDays=" + airDays + ", " : "") + "airing=" + airing + ", "
				+ (title != null ? "title=" + title + ", " : "") + (airTime != null ? "airTime=" + airTime + ", " : "")
				+ (guideId != null ? "guideId=" + guideId + ", " : "")
				+ (lastUpdated != null ? "lastUpdated=" + lastUpdated : "") + "]";
	}

}
