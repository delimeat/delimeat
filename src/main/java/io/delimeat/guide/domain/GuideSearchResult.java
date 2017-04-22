package io.delimeat.guide.domain;

import java.time.LocalDate;
import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

public class GuideSearchResult implements Comparable<GuideSearchResult> {

	private String description;

	private LocalDate firstAired;

	private String guideId;

	private String title;

	/**
	 * @return The description of the show
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return The date the show first aired
	 */
	public LocalDate getFirstAired() {
		return firstAired;
	}

	/**
	 * @return the guideId
	 */
	public String getGuideId() {
		return guideId;
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
	public void setFirstAired(LocalDate firstAired) {
		this.firstAired = firstAired;
	}

	/**
	 * @param guideIds
	 *            the guideIds to set
	 */
	public void setGuideId(String guideId) {
		this.guideId = guideId;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}

		if (this == object) {
			return true;
		}

		if (object instanceof GuideSearchResult) {
			GuideSearchResult other = (GuideSearchResult) object;
			return ComparisonChain.start()
									.compare(this.title, other.title, Ordering.natural().nullsFirst())
									.compare(this.firstAired, other.firstAired, Ordering.natural().nullsFirst())
									.compare(this.guideId, other.guideId, Ordering.natural().nullsFirst())
									.compare(this.description, other.description, Ordering.natural().nullsFirst())
									.result() == 0 ? true : false;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
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
							.add("firstAired", firstAired)
							.add("guideId", guideId)
							.add("description", description)
							.omitNullValues()
							.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(title, firstAired);
	}

}
