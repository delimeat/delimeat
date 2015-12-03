package io.delimeat.core.show;

import io.delimeat.core.guide.GuideSource;

public class ShowGuideSourcePK {

	private long showId;
	private GuideSource guideSource;

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
	 * @return the guideSource
	 */
	public GuideSource getGuideSource() {
		return guideSource;
	}

	/**
	 * @param guideSource
	 *            the guideSource to set
	 */
	public void setGuideSource(GuideSource guideSource) {
		this.guideSource = guideSource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ShowGuideSourcePK [showId=" + showId + ", guideSource=" + guideSource + "]";
	}

}
