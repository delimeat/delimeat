package io.delimeat.core.guide;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GuideSearchResult implements Comparable<GuideSearchResult> {

	protected String description;
	protected Date firstAired;
	protected List<GuideIdentifier> guideIds = new ArrayList<GuideIdentifier>();
	protected String title;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GuideSearchResult [title=" + title + ", firstAired=" + firstAired + ", guideIds=" + guideIds + "]";
	}

	@Override
	public int compareTo(GuideSearchResult other) {
		if (this.getTitle() == null && other.getTitle() != null) {
			return -1;
		} else if (this.getTitle() != null && other.getTitle() == null) {
			return 1;
		} else if (this.getTitle() == null && other.getTitle() == null) {
			return 0;
		} else {
			return this.getTitle().compareToIgnoreCase(other.getTitle());
		}
	}

}
