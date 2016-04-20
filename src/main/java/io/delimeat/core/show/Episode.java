package io.delimeat.core.show;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import io.delimeat.core.guide.GuideEpisode;
import io.delimeat.util.jaxb.TvdbDateAdapter;

import java.util.Date;
import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class Episode implements Comparable<Episode>{

	private long episodeId;
	private String title;
	@XmlJavaTypeAdapter(TvdbDateAdapter.class)
	private Date airDate;
	private int seasonNum;
	private int episodeNum;
	private boolean doubleEp;
	private int version;

	@XmlTransient
	private Show show;
  
   public Episode(){
     //do nothing;
   }
  
   public Episode(GuideEpisode guideEp){
     title = guideEp.getTitle();
     airDate = guideEp.getAirDate();
     seasonNum = guideEp.getSeasonNum();
     episodeNum = guideEp.getEpisodeNum();
   }

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
	public Date getAirDate() {
		return airDate;
	}

	/**
	 * @param airDate
	 *            the airDate to set
	 */
	public void setAirDate(Date airDate) {
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
        .add("airDate", (airDate != null ? airDate : null))
        .add("seasonNum", seasonNum)
        .add("episodeNum", episodeNum)
        .add("doubleEp", doubleEp)
        .add("showId", (show != null ? show.getShowId() : null))
        .add("version", version)
        .toString(); 
	}
  
 	@Override
  	public boolean equals(Object object)
  	{
      if(object ==null){
        return false;
      }
      
      if(this == object){
        return true;
      }
      
      if (object instanceof Episode)
      {
        final Episode other = (Episode)object;
        return Objects.equals(this.episodeId, other.episodeId) &&
          		Objects.equals(this.seasonNum, other.seasonNum) &&
          		Objects.equals(this.episodeNum, other.episodeNum) &&
          		Objects.equals(this.title, other.title) &&
          		Objects.equals(this.airDate, other.airDate) &&
          		Objects.equals(this.doubleEp, other.doubleEp) &&
          		Objects.equals(this.show!=null ? this.show.getShowId(): null, other.show!=null ? other.show.getShowId(): null) &&
          		Objects.equals(this.version, other.version);
      }else if (object instanceof GuideEpisode){
        final GuideEpisode other = (GuideEpisode)object;
        return Objects.equals(this.seasonNum, other.getSeasonNum()) &&
          		Objects.equals(this.episodeNum, other.getEpisodeNum());
      }
      return false;
  }


  @Override 
  public int hashCode() {
    return Objects.hash(episodeId,seasonNum,episodeNum,title,airDate,doubleEp,(show!=null ? show.getShowId(): null),version);
  }

  @Override
  public int compareTo(Episode other) {
      return ComparisonChain.start()
                .compare(this.show, other.show, Ordering.natural().nullsFirst())
                .compare(this.seasonNum, other.seasonNum)
                .compare(this.episodeNum, other.episodeNum)
                .compare(this.airDate, other.airDate, Ordering.natural().nullsFirst())
                .result();
  }

}
