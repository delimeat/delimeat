package io.delimeat.rest.domain;

import java.time.Instant;
import java.util.Objects;

import com.google.common.base.MoreObjects;

import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowType;

public class ShowDTO {

	private long showId;
	private int airTime;
	private String timezone;
	private String title;
	private boolean airing;
	private ShowType showType;
	private Instant lastGuideUpdate;
	private Instant lastGuideCheck;
	private boolean enabled;
	private int minSize;
	private int maxSize;
	private int version;
	
	public ShowDTO(){};
	
	
	public ShowDTO(Show show){
		this.showId = show.getShowId();
		this.airTime = show.getAirTime();
		this.timezone = show.getTimezone();
		this.title = show.getTitle();
		this.airing = show.isAiring();
		this.showType = show.getShowType();
		this.lastGuideCheck = show.getLastGuideCheck() != null ? show.getLastGuideCheck().toInstant(): null;
		this.lastGuideUpdate = show.getLastGuideUpdate() != null ? show.getLastGuideUpdate().toInstant(): null;
		this.enabled = show.isEnabled();
		this.minSize = show.getMinSize();
		this.maxSize = show.getMinSize();
		this.version = show.getVersion();
	}

	/**
	 * @return the showId
	 */
	public long getShowId() {
		return showId;
	}


	/**
	 * @param showId the showId to set
	 */
	public void setShowId(long showId) {
		this.showId = showId;
	}


	/**
	 * @return the airTime
	 */
	public int getAirTime() {
		return airTime;
	}


	/**
	 * @param airTime the airTime to set
	 */
	public void setAirTime(int airTime) {
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


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
     	return MoreObjects.toStringHelper(this)
              .add("showId", showId)
              .add("title", title)  
              .add("showType", showType)
              .add("enabled", enabled)
              .add("airing", airing)
              .add("airTime", airTime)
              .add("timezone", timezone)
              .add("lastGuideUpdate", (lastGuideUpdate != null ? lastGuideUpdate : null))
              .add("lastGuideCheck", (lastGuideCheck != null ? lastGuideCheck : null))              
              .add("minSize", minSize)
              .add("maxSize", maxSize)
              .add("version", version)
              .toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override 
	public int hashCode() {
		return Objects.hash(showId,version);
	}
	
	
}
