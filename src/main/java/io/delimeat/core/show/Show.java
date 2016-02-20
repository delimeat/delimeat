package io.delimeat.core.show;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;
import io.delimeat.util.jaxb.AirTimeAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "showId", "title", "showType", "enabled", "airing", "airTime", "timezone", "guideSources",
		"nextEpisode", "previousEpisode", "includeSpecials", "lastGuideUpdate", "lastFeedUpdate", "minSize", "maxSize", "version" })
public class Show implements Comparable<Show> {

	private long showId;
	@XmlJavaTypeAdapter(value=AirTimeAdapter.class)
	private int airTime;
	private String timezone;
	private List<ShowGuideSource> guideSources = new ArrayList<ShowGuideSource>();
	private String title;
	private boolean airing;
	private ShowType showType;
	private Date lastGuideUpdate;
	private Date lastFeedUpdate;
	private boolean enabled;
	private Episode nextEpisode;
	private Episode previousEpisode;
	private boolean includeSpecials;
	private int minSize;
	private int maxSize;
	private int version;
	
	/**
	 * @return the showId
	 */
	public long getShowId() {
		return showId;
	}

	/**
	 * @param showId
	 *            the showId to set
	 */
	public void setShowId(long showId) {
		this.showId = showId;
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
	 * @return the airTime
	 */
	public int getAirTime() {
		return airTime;
	}

	/**
	 * @param airTime
	 *            the airTime to set
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
	 * @param timezone
	 *            the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/**
	 * @return the guideSources
	 */
	public List<ShowGuideSource> getGuideSources() {
		return guideSources;
	}

	/**
	 * @param guideSources
	 *            the guideSources to set
	 */
	public void setGuideSources(List<ShowGuideSource> guideSources) {
		this.guideSources = guideSources;
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
	 * @return the showType
	 */
	public ShowType getShowType() {
		return showType;
	}

	/**
	 * @param showType
	 *            the showType to set
	 */
	public void setShowType(ShowType showType) {
		this.showType = showType;
	}

	/**
	 * @return the lastGuideUpdate
	 */
	public Date getLastGuideUpdate() {
		return lastGuideUpdate;
	}

	/**
	 * @param lastGuideUpdate
	 *            the lastGuideUpdate to set
	 */
	public void setLastGuideUpdate(Date lastGuideUpdate) {
		this.lastGuideUpdate = lastGuideUpdate;
	}

	/**
	 * @return the lastFeedUpdate
	 */
	public Date getLastFeedUpdate() {
		return lastFeedUpdate;
	}

	/**
	 * @param lastFeedUpdate
	 *            the lastFeedUpdate to set
	 */
	public void setLastFeedUpdate(Date lastFeedUpdate) {
		this.lastFeedUpdate = lastFeedUpdate;
	}

	/**
	 * @return the airing
	 */
	public boolean isAiring() {
		return airing;
	}

	/**
	 * @param airing
	 *            the airing to set
	 */
	public void setAiring(boolean airing) {
		this.airing = airing;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the nextEpisode
	 */
	public Episode getNextEpisode() {
		return nextEpisode;
	}

	/**
	 * @param nextEpisode
	 *            the nextEpisode to set
	 */
	public void setNextEpisode(Episode nextEpisode) {
		this.nextEpisode = nextEpisode;
	}

	/**
	 * @return the previousEpisode
	 */
	public Episode getPreviousEpisode() {
		return previousEpisode;
	}

	/**
	 * @param previousEpisode
	 *            the previousEpisode to set
	 */
	public void setPreviousEpisode(Episode previousEpisode) {
		this.previousEpisode = previousEpisode;
	}

	/**
	 * @return the includeSpecials
	 */
	public boolean isIncludeSpecials() {
		return includeSpecials;
	}

	/**
	 * @param includeSpecials
	 *            the includeSpecials to set
	 */
	public void setIncludeSpecials(boolean includeSpecials) {
		this.includeSpecials = includeSpecials;
	}
 
	/**
	 * @return the minSize
	 */
	public int getMinSize() {
		return minSize;
	}

	/**
	 * @param minSize
	 *            the minSize to set
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
	 * @param maxSize
	 *            the maxSize to set
	 */
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	@Override
	public int compareTo(Show other) {
     return ComparisonChain.start()
       			.compare(this.title, other.title)
       			.result();
	}

	@Override
	public boolean equals(Object other) {
     return (other instanceof Show && showId == ((Show) other).showId);
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
              .add("guideSources", guideSources)
              .add("nextEpisode", nextEpisode)
              .add("previousEpisode", previousEpisode)
              .add("includeSpecials", includeSpecials)
              .add("lastGuideUpdate", (lastGuideUpdate != null ? lastGuideUpdate.getTime() : null))
              .add("lastFeedUpdate", (lastFeedUpdate != null ? lastFeedUpdate.getTime() : null))
              .add("minSize", minSize)
              .add("maxSize", maxSize)
              .add("version", version)
              .toString();
	}
  
  @Override 
  public int hashCode() {
    return Objects.hash(showId, version);
  }

}
