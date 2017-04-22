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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;


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
	private long showId;
	
	@Column(name="AIR_TIME", nullable=false)
	@Basic(optional=false)
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
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
	@JsonIgnore
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
	public LocalTime getAirTime() {
		return airTime;
	}

	/**
	 * @param airTime
	 *            the airTime to set
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
	 * @param timezone
	 *            the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/**
	 * @return the guideSources
	 */
	public String getGuideId() {
		return guideId;
	}

	/**
	 * @param guideSources
	 *            the guideSources to set
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
	public Instant getLastGuideUpdate() {
		return lastGuideUpdate;
	}

	/**
	 * @param lastGuideUpdate
	 *            the lastGuideUpdate to set
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
	 * @param lastGuideCheck
	 *            the lastGuideCheck to set
	 */
	public void setLastGuideCheck(Instant lastGuideCheck) {
		this.lastGuideCheck = lastGuideCheck;
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
              .add("guideId", guideId)
              .add("lastGuideUpdate", lastGuideUpdate)
              .add("lastGuideCheck", lastGuideCheck)
              .add("minSize", minSize)
              .add("maxSize", maxSize)
              .add("version", version)
              .omitNullValues()
              .toString();
	}

	@Override
	public boolean equals(Object obj) {
     if(obj == null)
       	return false;
     
     if(this == obj)
       	return true;
     
     if(obj instanceof Show){
       	final Show other = (Show)obj;
       	return Objects.equals(this.showId, other.showId) &&
           		 Objects.equals(this.version, other.version);	
     }
     return false;
	}
  
    @Override 
    public int hashCode() {
      return Objects.hash(showId,version);
    }

}
