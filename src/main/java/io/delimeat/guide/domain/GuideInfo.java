package io.delimeat.guide.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;


public class GuideInfo implements Comparable<GuideInfo> {

	private String description;

	private int runningTime;

	private String timezone;

	private LocalDate firstAired;

	private List<String> genres = new ArrayList<String>();

	private List<AiringDay> airDays = new ArrayList<AiringDay>();

	private boolean airing = true;

	private String title;

	private LocalTime airTime;

	private String guideId;

	private LocalDate lastUpdated;

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
	 public LocalTime getAirTime() {
		 return airTime;
	 }

	 /**
	  * @return guide id
	  */
	 public String getGuideId() {
		 return guideId;
	 }

	 /**
	  * Set Description
	  * 
	  * @param description
	  */
	 public void setDescription(String description) {
		 this.description = description;
	 }

	 /**
	  * Set the running time
	  * 
	  * @param runningTime
	  */
	 public void setRunningTime(int runningTime) {
		 this.runningTime = runningTime;
	 }

	 /**
	  * Set the timezone
	  * 
	  * @param timezone
	  */
	 public void setTimezone(String timezone) {
		 this.timezone = timezone;
	 }

	 /**
	  * Set the genres 
	  * 
	  * @param genres
	  */
	 public void setGenres(List<String> genres) {
		 this.genres = genres;
	 }

	 /**
	  * Set the airing days
	  * 
	  * @param airDays
	  */
	 public void setAirDays(List<AiringDay> airDays) {
		 this.airDays = airDays;
	 }

	 /**
	  * Set the airing status
	  * 
	  * @param airing
	  */
	 public void setAiring(boolean airing) {
		 this.airing = airing;
	 }

	 /**
	  * Set the title
	  * 
	  * @param title
	  */
	 public void setTitle(String title) {
		 this.title = title;
	 }

	 /**
	  * Set the air time
	  * 
	  * @param airTime
	  */
	 public void setAirTime(LocalTime airTime) {
		 this.airTime = airTime;
	 }

	 /**
	  * Set the guide id
	  * 
	  * @param guideId
	  */
	 public void setGuideId(String guideId) {
		 this.guideId = guideId;
	 }

	 /**
	  * @return first aired
	  */
	 public LocalDate getFirstAired() {
		 return firstAired;
	 }

	 /**
	  * Set first aired date
	  * 
	  * @param firstAired
	  */
	 public void setFirstAired(LocalDate firstAired) {
		 this.firstAired = firstAired;
	 }

	 /**
	  * @return last updated date
	  */
	 public LocalDate getLastUpdated() {
		 return lastUpdated;
	 }

	 /**
	  * Get last updated date
	  * 
	  * @param lastUpdated
	  */
	 public void setLastUpdated(LocalDate lastUpdated) {
		 this.lastUpdated = lastUpdated;
	 }

	 /* (non-Javadoc)
	  * @see java.lang.Object#equals(java.lang.Object)
	  */
	 @Override
	 public boolean equals(Object object)
	 {
		 if(object ==null){
			 return false;
		 }

		 if(this == object){
			 return true;
		 }

		 if (object instanceof GuideInfo){
			 GuideInfo other = (GuideInfo)object;
			 return ComparisonChain.start()
					 .compare(this.title, other.title, Ordering.natural().nullsFirst())
					 .compare(this.guideId, other.guideId, Ordering.natural().nullsFirst())
					 .compareFalseFirst(this.airing, other.airing)
					 .compare(this.airDays, other.airDays, Ordering.<AiringDay>natural().lexicographical().nullsFirst())
					 .compare(this.airTime,other.airTime,Ordering.natural().nullsFirst())
					 .compare(this.genres, other.genres, Ordering.<String>natural().lexicographical().nullsFirst())
					 .compare(this.runningTime,other.runningTime,Ordering.natural().nullsFirst())
					 .compare(this.timezone,other.timezone,Ordering.natural().nullsFirst())
					 .compare(this.description, other.description, Ordering.natural().nullsFirst())
					 .compare(this.firstAired, other.firstAired, Ordering.natural().nullsFirst())
					 .compare(this.lastUpdated, other.lastUpdated, Ordering.natural().nullsFirst())
					 .result() == 0 ? true : false;
		 }
		 return false;
	 }

	 /* (non-Javadoc)
	  * @see java.lang.Comparable#compareTo(java.lang.Object)
	  */
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
				 .add("firstAired",  firstAired)
				 .add("lastUpdated", lastUpdated)
				 .omitNullValues()
				 .toString();     
	 }

	 /* (non-Javadoc)
	  * @see java.lang.Object#hashCode()
	  */
	 @Override 
	 public int hashCode() {
		 return Objects.hash(title, lastUpdated);
	 }

}
