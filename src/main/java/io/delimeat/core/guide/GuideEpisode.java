package io.delimeat.core.guide;

import java.util.Date;
import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import io.delimeat.core.show.Episode;

public class GuideEpisode implements Comparable<GuideEpisode> {

    private Date airDate = null;
    private Integer seasonNum = 0;
    private Integer episodeNum = 0;
    private Integer productionNum = 0;
    private String title;

    /**
      * 
      * @return Air date for the episode
      */
    public Date getAirDate() {
      return airDate;
    }

    /**
      * @return Episode number for the episode
      */
    public Integer getEpisodeNum() {
      return episodeNum;
    }

    /**
      * @return Production Number for the episode
      */
    public Integer getProductionNum() {
      return productionNum;
    }

    /**
      * @return Season number for the episode
      */
    public Integer getSeasonNum() {
      return seasonNum;
    }

    /**
      * @return Title of the episode
      */
    public String getTitle() {
      return title;
    }

    public void setAirDate(Date airDate) {
      this.airDate = airDate;
    }

    public void setSeasonNum(Integer seasonNum) {
      this.seasonNum = seasonNum;
    }

    public void setEpisodeNum(Integer episodeNum) {
      this.episodeNum = episodeNum;
    }

    public void setProductionNum(Integer productionNum) {
      this.productionNum = productionNum;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    @Override
    public int compareTo(GuideEpisode other) {
		return ComparisonChain.start()
        			  .compare(this.getAirDate(),other.getAirDate(), Ordering.natural().nullsFirst())
                 .compare(this.getSeasonNum(), other.getSeasonNum(), Ordering.natural().nullsFirst())
                 .compare(this.getEpisodeNum(), other.getEpisodeNum(), Ordering.natural().nullsFirst())
                 .result();     
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
        Episode otherEp = (Episode)object;
        return ComparisonChain.start()
                 .compare(this.seasonNum, new Integer(otherEp.getSeasonNum()))
                 .compare(this.episodeNum, new Integer(otherEp.getEpisodeNum()))
                 .result() == 0 ? true : false;
      }else if (object instanceof GuideEpisode){
        GuideEpisode otherEp = (GuideEpisode)object;
        return ComparisonChain.start()
                 .compare(this.seasonNum, otherEp.getSeasonNum())
                 .compare(this.episodeNum, otherEp.getEpisodeNum())
                 .result() == 0 ? true : false;
      }
      return false;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this)
        .add("title", title)
        .add("airDate", (airDate != null ? airDate : null))  
        .add("seasonNum", seasonNum)
        .add("episodeNum", episodeNum)
        .toString();
    }

    @Override 
    public int hashCode() {
      return Objects.hash(title, airDate, seasonNum, episodeNum);
    }

}
