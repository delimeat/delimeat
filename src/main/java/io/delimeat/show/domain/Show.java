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
package io.delimeat.show.domain;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Version;

@Entity
@Table(name="SHOW")
@NamedQueries({
	@NamedQuery(name="Show.findAll", query="SELECT s FROM Show s")
})
public class Show {


  	@Id
	@Column(name="SHOW_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SHOW_SEQ")
	@TableGenerator(name="SHOW_SEQ", table="SEQUENCE", pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT", pkColumnValue="SHOW_SEQ")
	private Long showId;
	
	@Column(name="AIR_TIME", nullable=false)
	@Basic(optional=false)
	private LocalTime airTime;
	
	@Column(name="TIMEZONE", length=64, nullable=false)
	@Basic(optional=false)
	private String timezone;
	
	@Column(name="GUIDE_ID", length=255, nullable=false, unique=true)
	@Basic(optional=false)
	private String guideId;
	
	@Column(name="TITLE", length=255, nullable=false, unique=true)
	@Basic(optional=false)	
	private String title;
	

	@Column(name="AIRING", nullable=false)
	@Basic(optional=false)
	private boolean airing;
	
	@Column(name="SHOW_TYPE", nullable=false)
	@Basic(optional=false)
	@Enumerated(EnumType.STRING)
	private ShowType showType;
	
	@Column(name="LAST_GUIDE_UPDATE", nullable=true)
	@Basic(optional=true)
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "UTC")
	private Instant lastGuideUpdate;
	
	@Column(name="LAST_GUIDE_CHECK", nullable=true)
	@Basic(optional=true)
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "UTC")
	private Instant lastGuideCheck;
	
	@Column(name="ENABLED", nullable=false)
	@Basic(optional=false)	
	private boolean enabled;
	
	@Column(name="MIN_SIZE", nullable=false)
	@Basic(optional=false)
	private int minSize;
	
	@Column(name="MAX_SIZE", nullable=false)
	@Basic(optional=false)
	private int maxSize;
	
	@Version
	@Column(name="VERSION")
	private int version;

	/**
	 * @return the showId
	 */
	public Long getShowId() {
		return showId;
	}

	/**
	 * @param showId the showId to set
	 */
	public void setShowId(Long showId) {
		this.showId = showId;
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
	 * @return the showType
	 */
	public ShowType getShowType() {
		return showType;
	}

	/**
	 * @param showType the showType to set
	 */
	public void setShowType(ShowType showType) {
		this.showType = showType;
	}

	/**
	 * @return the lastGuideUpdate
	 */
	public Instant getLastGuideUpdate() {
		return lastGuideUpdate;
	}

	/**
	 * @param lastGuideUpdate the lastGuideUpdate to set
	 */
	public void setLastGuideUpdate(Instant lastGuideUpdate) {
		this.lastGuideUpdate = lastGuideUpdate;
	}

	/**
	 * @return the lastGuideCheck
	 */
	public Instant getLastGuideCheck() {
		return lastGuideCheck;
	}

	/**
	 * @param lastGuideCheck the lastGuideCheck to set
	 */
	public void setLastGuideCheck(Instant lastGuideCheck) {
		this.lastGuideCheck = lastGuideCheck;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the minSize
	 */
	public int getMinSize() {
		return minSize;
	}

	/**
	 * @param minSize the minSize to set
	 */
	public void setMinSize(int minSize) {
		this.minSize = minSize;
	}

	/**
	 * @return the maxSize
	 */
	public int getMaxSize() {
		return maxSize;
	}

	/**
	 * @param maxSize the maxSize to set
	 */
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(showId,version);
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
		Show other = (Show) obj;
		if (showId != other.showId)
			return false;
		if (version != other.version)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Show [" + (showId != null ? "showId=" + showId + ", " : "")
				+ (airTime != null ? "airTime=" + airTime + ", " : "")
				+ (timezone != null ? "timezone=" + timezone + ", " : "")
				+ (guideId != null ? "guideId=" + guideId + ", " : "") + (title != null ? "title=" + title + ", " : "")
				+ "airing=" + airing + ", " + (showType != null ? "showType=" + showType + ", " : "")
				+ (lastGuideUpdate != null ? "lastGuideUpdate=" + lastGuideUpdate + ", " : "")
				+ (lastGuideCheck != null ? "lastGuideCheck=" + lastGuideCheck + ", " : "") + "enabled=" + enabled
				+ ", minSize=" + minSize + ", maxSize=" + maxSize + ", version=" + version + "]";
	}
	
	
	
}
