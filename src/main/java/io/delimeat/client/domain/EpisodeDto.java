package io.delimeat.client.domain;

import java.time.Instant;
import java.util.Objects;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.google.common.base.MoreObjects;

import io.delimeat.show.ShowUtils;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.EpisodeStatus;

@XmlRootElement
public class EpisodeDto {
	
	private long episodeId;
	private String title;
	@XmlJavaTypeAdapter(value=io.delimeat.util.jaxb.InstantAdapter.class)
	private Instant airDateTime;
	private int seasonNum;
	private int episodeNum;
	private boolean doubleEp;
	private EpisodeStatus status;
	@XmlJavaTypeAdapter(value=io.delimeat.util.jaxb.InstantAdapter.class)
	private Instant lastFeedCheck;
	@XmlJavaTypeAdapter(value=io.delimeat.util.jaxb.InstantAdapter.class)
	private Instant lastFeedUpdate;
	private int version;
	private ShowDto show;
	
	/**
	 * Default constructor
	 */
	public EpisodeDto(){}
	
	/**
	 * Constructor for dto from episode
	 * 
	 * @param episode
	 */
	public EpisodeDto(Episode episode){
		Objects.requireNonNull(episode.getShow());
		
		this.episodeId = episode.getEpisodeId();
		this.title = episode.getTitle();
		this.airDateTime = ShowUtils.determineAirTime(episode.getAirDate(),episode.getShow().getAirTime(),episode.getShow().getTimezone());
		this.seasonNum = episode.getSeasonNum();
		this.episodeNum = episode.getEpisodeNum();
		this.doubleEp = episode.isDoubleEp();
		this.status = episode.getStatus();
		this.lastFeedCheck =  episode.getLastFeedCheck();
		this.lastFeedUpdate = episode.getLastFeedUpdate();
		this.version = episode.getVersion();
		
		if(episode.getShow() != null){
			this.show = new ShowDto(episode.getShow());
		}
	}
	/**
	 * @return the episodeId
	 */
	public long getEpisodeId() {
		return episodeId;
	}
	/**
	 * @param episodeId the episodeId to set
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
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the airDateTime
	 */
	public Instant getAirDateTime() {
		return airDateTime;
	}
	/**
	 * @param airDateTime the airDateTime to set
	 */
	public void setAirDateTime(Instant airDateTime) {
		this.airDateTime = airDateTime;
	}
	/**
	 * @return the seasonNum
	 */
	public int getSeasonNum() {
		return seasonNum;
	}
	/**
	 * @param seasonNum the seasonNum to set
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
	 * @param episodeNum the episodeNum to set
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
	 * @param doubleEp the doubleEp to set
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
	 * @param status the status to set
	 */
	public void setStatus(EpisodeStatus status) {
		this.status = status;
	}

	
	/**
	 * @return the last feed check
	 */
	public Instant getLastFeedCheck() {
		return lastFeedCheck;
	}

	/**
	 * @param lastFeedCheck to set
	 */
	public void setLastFeedCheck(Instant lastFeedCheck) {
		this.lastFeedCheck = lastFeedCheck;
	}

	/**
	 * @return the last feed update
	 */
	public Instant getLastFeedUpdate() {
		return lastFeedUpdate;
	}

	/**
	 * @param lastFeedUpdate to set
	 */
	public void setLastFeedUpdate(Instant lastFeedUpdate) {
		this.lastFeedUpdate = lastFeedUpdate;
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
	/**
	 * @return the show
	 */
	public ShowDto getShow() {
		return show;
	}
	/**
	 * @param show the show to set
	 */
	public void setShow(ShowDto show) {
		this.show = show;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("episodeId", episodeId)
				.add("title", title)  
				.add("airDateTime", airDateTime)
				.add("seasonNum", seasonNum)
				.add("episodeNum", episodeNum)
				.add("doubleEp", doubleEp)
				.add("status", status)
				.add("lastFeedCheck", lastFeedCheck)
				.add("lastFeedUpdate", lastFeedUpdate)
				.add("show", show)
				.add("version", version)
				.omitNullValues()
				.toString();
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override 
	public int hashCode() {
		return Objects.hash(episodeId,version);
	}
	
}
