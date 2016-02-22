package io.delimeat.core.guide;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import io.delimeat.util.jaxb.AirTimeAdapter;
import io.delimeat.util.jaxb.TvdbDateAdapter;

@XmlRootElement
public class GuideInfo implements Comparable<GuideInfo> {

    private String description;
    private int runningTime;
    private String timezone;
    @XmlJavaTypeAdapter(value=TvdbDateAdapter.class)
    private Date firstAired;
    private List<String> genres = new ArrayList<String>();
    private List<AiringDay> airDays = new ArrayList<AiringDay>();
    private boolean airing = true;
    private String title;
    @XmlJavaTypeAdapter(value=AirTimeAdapter.class)
    private int airTime;
    private String guideId;
    private Date lastUpdated;

    /**
      * @return the description
      */
    public String getDescription() {
      return description;
    }

    /**
      * @return the episodeLength
      */
    public int getRunningTime() {
      return runningTime;
    }

    /**
      * @return the timezone
      */
    public String getTimezone() {
      return timezone;
    }

    /**
      * @return the genres
      */
    public List<String> getGenres() {
      return genres;
    }

    /**
      * @return the airDay
      */
    public List<AiringDay> getAirDays() {
      return airDays;
    }

    /**
      * @return the airing
      */
    public boolean isAiring() {
      return airing;
    }

    /**
      * @return the title
      */
    public String getTitle() {
      return title;
    }

    /**
      * @return the airTime
      */
    public int getAirTime() {
      return airTime;
    }

    public String getGuideId() {
      return guideId;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public void setRunningTime(int runningTime) {
      this.runningTime = runningTime;
    }

    public void setTimezone(String timezone) {
      this.timezone = timezone;
    }

    public void setGenres(List<String> genres) {
      this.genres = genres;
    }

    public void setAirDays(List<AiringDay> airDays) {
      this.airDays = airDays;
    }

    public void setAiring(boolean airing) {
      this.airing = airing;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public void setAirTime(int airTime) {
      this.airTime = airTime;
    }

    public void setGuideId(String guideId) {
      this.guideId = guideId;
    }

    public Date getFirstAired() {
      return firstAired;
    }

    public void setFirstAired(Date firstAired) {
      this.firstAired = firstAired;
    }

    public Date getLastUpdated() {
      return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
      this.lastUpdated = lastUpdated;
    }


    @Override
    public int compareTo(GuideInfo other) {
      return ComparisonChain.start()
        .compare(this.title, other.title, Ordering.natural().nullsFirst())
        .result();
    }
    /*
      * (non-Javadoc)
      * 
      * @see java.lang.Object#toString()
      */
    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this)
        .add("title", title)
        .add("guideId", guideId)  
        .add("airing", airing)
        .add("airDays", airDays)
        .add("airTime", airTime)
        .add("genres", genres)
        .add("runningTime", runningTime)
        .add("timezone", timezone)
        .add("description", description)
        .add("lastUpdated", (lastUpdated != null ? lastUpdated.getTime() : null))
        .toString();     
    }

    @Override 
    public int hashCode() {
      return Objects.hash(title, lastUpdated);
    }

}
