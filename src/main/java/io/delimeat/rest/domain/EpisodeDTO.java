package io.delimeat.rest.domain;

import java.time.Instant;
import java.util.Objects;

import com.google.common.base.MoreObjects;

import io.delimeat.core.show.Episode;
import io.delimeat.core.show.EpisodeStatus;
import io.delimeat.core.show.ShowUtils;

public class EpisodeDTO {
	
	private long episodeId;
	private String title;
	private Instant airDateTime;
	private int seasonNum;
	private int episodeNum;
	private boolean doubleEp;
	private EpisodeStatus status;
	private int version;
	private ShowDTO show;
	
	public EpisodeDTO(){}
	
	/**
	 * Constructor for dto from episode
	 * 
	 * @param episode
	 */
	public EpisodeDTO(Episode episode){
		Objects.requireNonNull(episode.getShow());
		
		this.episodeId = episode.getEpisodeId();
		this.title = episode.getTitle();
		this.airDateTime = ShowUtils.determineAirTime(episode.getAirDate(),episode.getShow());
		this.seasonNum = episode.getSeasonNum();
		this.episodeNum = episode.getEpisodeNum();
		this.doubleEp = episode.isDoubleEp();
		this.status = episode.getStatus();
		this.version = episode.getVersion();
		
		if(episode.getShow() != null){
			this.show = new ShowDTO(episode.getShow());
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
	public ShowDTO getShow() {
		return show;
	}
	/**
	 * @param show the show to set
	 */
	public void setShow(ShowDTO show) {
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
				.add("airDateTime", (airDateTime != null ? airDateTime : null))
				.add("seasonNum", seasonNum)
				.add("episodeNum", episodeNum)
				.add("doubleEp", doubleEp)
				.add("status", status)
				.add("show", (show != null ? show : null))
				.add("version", version)
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
