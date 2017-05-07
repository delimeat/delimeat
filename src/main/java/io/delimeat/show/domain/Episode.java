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
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.util.jaxb.LocalDateAdapter;


@Entity
@Table(name="EPISODE",uniqueConstraints={@UniqueConstraint(columnNames={"SEASON_NUM","EPISODE_NUM","SHOW_ID"})})
@NamedQueries({
	@NamedQuery(name="Episode.findAllByStatus", query="SELECT e FROM Episode e WHERE e.status IN :statusList ORDER BY e.airDate"),
	@NamedQuery(name="Episode.findAllByStatus.count", query="SELECT count(e) FROM Episode e WHERE e.status IN :statusList"),
	@NamedQuery(name="Episode.findAllByShow", query="SELECT e FROM Episode e WHERE e.show = :show"),
	@NamedQuery(name="Episode.findAllPending", query="SELECT e FROM Episode e WHERE e.status = 'PENDING' ORDER BY e.airDate")
})
@XmlAccessorType(XmlAccessType.FIELD)
public class Episode implements Comparable<Episode> {


	@Id
	@Column(name="EPISODE_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="EP_SEQ")
	@TableGenerator(name="EP_SEQ", table="SEQUENCE", pkColumnName="SEQ_NAME", valueColumnName="SEQ_COUNT", pkColumnValue="EP_SEQ")
	private long episodeId;
	
	@Column(name="TITLE", nullable=true)
	@Basic(optional=true)
	private String title;
	
	@Column(name="AIR_DATE", nullable=false)
	@Basic(optional=false)
	//@Temporal(TemporalType.DATE)
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	private LocalDate airDate;
	
	@Column(name="SEASON_NUM", nullable=false)
	@Basic(optional=false)
	private int seasonNum;
	
	@Column(name="EPISODE_NUM", nullable=false)
	@Basic(optional=false)
	private int episodeNum;
	
	@Column(name="DOUBLE_EP", nullable=false)
	@Basic(optional=false)
	private boolean doubleEp;
	
	@Column(name="STATUS", nullable=false)
	@Basic(optional=false)
	@Enumerated(EnumType.STRING)
	private EpisodeStatus status = EpisodeStatus.PENDING;
	
	@Column(name="LAST_FEED_UPDATE", nullable=true)
	@Basic(optional=true)
	private Instant lastFeedUpdate;
	
	@Column(name="LAST_FEED_CHECK", nullable=true)
	@Basic(optional=true)
	private Instant lastFeedCheck;
	
	@Version
	@Column(name="VERSION")
	@JsonIgnore
	private int version;
	
	@ManyToOne(targetEntity=Show.class, optional=false, fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="SHOW_ID", referencedColumnName="SHOW_ID", nullable=false)
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
	 * @return the airDate
	 */
	public LocalDate getAirDate() {
		return airDate;
	}

	/**
	 * @param airDate
	 *            the airDate to set
	 */
	public void setAirDate(LocalDate airDate) {
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
	 * @return the status
	 */
	public EpisodeStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(EpisodeStatus status) {
		this.status = status;
	}

	/**
	 * @return the lastFeedUpdate
	 */
	public Instant getLastFeedUpdate() {
		return lastFeedUpdate;
	}

	/**
	 * @param lastFeedUpdate
	 *            the lastFeedUpdate to set
	 */
	public void setLastFeedUpdate(Instant lastFeedUpdate) {
		this.lastFeedUpdate = lastFeedUpdate;
	}

	/**
	 * @return the lastFeedCheck
	 */
	public Instant getLastFeedCheck() {
		return lastFeedCheck;
	}

	/**
	 * @param lastFeedCheck
	 *            the lastFeedCheck to set
	 */
	public void setLastFeedCheck(Instant lastFeedCheck) {
		this.lastFeedCheck = lastFeedCheck;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
					.add("episodeId", episodeId)
					.add("title", title)
					.add("airDate", airDate)
					.add("seasonNum", seasonNum)
					.add("episodeNum", episodeNum)
					.add("doubleEp", doubleEp)
					.add("show", show)
					.add("lastFeedCheck", lastFeedCheck)
					.add("lastFeedUpdate", lastFeedUpdate)
					.add("status", status)
					.add("version", version)
					.omitNullValues()
					.toString();
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}

		if (this == object) {
			return true;
		}

		if (object instanceof Episode) {
			final Episode other = (Episode) object;
			return Objects.equals(this.episodeId, other.episodeId) 
					&& Objects.equals(this.version, other.version);
		} else if (object instanceof GuideEpisode) {
			final GuideEpisode other = (GuideEpisode) object;
			return Objects.equals(this.seasonNum, other.getSeasonNum())
					&& Objects.equals(this.episodeNum, other.getEpisodeNum());
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(episodeId, version);
	}

	@Override
	public int compareTo(Episode other) {
		return ComparisonChain.start()
				.compare(this.getAirDate(), other.getAirDate(), Ordering.natural().nullsFirst())
				.compare(this.getSeasonNum(), other.getSeasonNum(), Ordering.natural().nullsFirst())
				.compare(this.getEpisodeNum(), other.getEpisodeNum(), Ordering.natural().nullsFirst())
				.result(); 
	}

}
