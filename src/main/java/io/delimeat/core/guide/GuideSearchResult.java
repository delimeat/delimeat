package io.delimeat.core.guide;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import io.delimeat.util.jaxb.TvdbDateAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public class GuideSearchResult implements Comparable<GuideSearchResult> {

    private String description;
    @XmlJavaTypeAdapter(value=TvdbDateAdapter.class)
    private Date firstAired;
    private List<GuideIdentifier> guideIds = new ArrayList<GuideIdentifier>();
    private String title;
    private Date lastUpdated;

    /**
      * @return The description of the show
      */
    public String getDescription() {
      return description;
    }

    /**
      * @return The date the show first aired
      */
    public Date getFirstAired() {
      return firstAired;
    }

    /**
      * @return the guideIds
      */
    public List<GuideIdentifier> getGuideIds() {
      return guideIds;
    }

    /**
      * @return The title of the show
      */
    public String getTitle() {
      return title;
    }

    /**
      * @param description
      *            the description to set
      */
    public void setDescription(String description) {
      this.description = description;
    }

    /**
      * @param firstAired
      *            the firstAired to set
      */
    public void setFirstAired(Date firstAired) {
      this.firstAired = firstAired;
    }

    /**
      * @param guideIds
      *            the guideIds to set
      */
    public void setGuideIds(List<GuideIdentifier> guideIds) {
      this.guideIds = guideIds;
    }

    /**
      * @param title
      *            the title to set
      */
    public void setTitle(String title) {
      this.title = title;
    }


    public Date getLastUpdated() {
      return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
      this.lastUpdated = lastUpdated;
    }

    @Override
    public int compareTo(GuideSearchResult other) {
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
        .add("firstAired", (firstAired != null ? firstAired.getTime() : null))
        .add("guideIds", guideIds)
        .add("description", description)
        .add("lastUpdated", (lastUpdated != null ? lastUpdated.getTime() : null))
        .toString();
    }

    @Override 
    public int hashCode() {
      return Objects.hash(title, lastUpdated);
    }

}
