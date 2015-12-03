package io.delimeat.core.guide;

import java.util.ArrayList;
import java.util.List;

public class GuideInfo implements Comparable<GuideInfo> {

	protected String description;
	protected int runningTime;
	protected String network;
	protected List<String> genres = new ArrayList<String>();
	protected List<AiringDay> airDays = new ArrayList<AiringDay>();
	protected AiringStatus airStatus = AiringStatus.UNKNOWN;
	protected String title;
	protected int airTime;
	protected List<GuideIdentifier> guideIds = new ArrayList<GuideIdentifier>();

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
	 * @return the network
	 */
	public String getNetwork() {
		return network;
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
	 * @return the airStatus
	 */
	public AiringStatus getAirStatus() {
		return airStatus;
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

	public List<GuideIdentifier> getGuideIds() {
		return guideIds;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setRunningTime(int runningTime) {
		this.runningTime = runningTime;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public void setGenres(List<String> genres) {
		this.genres = genres;
	}

	public void setAirDays(List<AiringDay> airDays) {
		this.airDays = airDays;
	}

	public void setAirStatus(AiringStatus airStatus) {
		this.airStatus = airStatus;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setAirTime(int airTime) {
		this.airTime = airTime;
	}

	public void setGuideIds(List<GuideIdentifier> guideIds) {
		this.guideIds = guideIds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GuideInfo [title=" + title + ", guideIds=" + guideIds + ", airStatus=" + airStatus + ", airDays="
				+ airDays + ", airTime=" + airTime + ", genres=" + genres + ", runningTime=" + runningTime
				+ ", network=" + network + ", description=" + description + "]";
	}

	@Override
	public int compareTo(GuideInfo other) {
		if (this.getTitle() == null && other.getTitle() == null) {
			return 0;
		}
		if (this.getTitle() != null && other.getTitle() == null) {
			return 1;
		} else if (this.getTitle() == null && other.getTitle() != null) {
			return -1;
		} else {
			return this.getTitle().compareToIgnoreCase(other.getTitle());
		}
	}

}
