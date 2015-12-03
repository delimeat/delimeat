package io.delimeat.core.guide;

import java.util.ArrayList;
import java.util.List;

public class GuideSearch {

	private List<GuideSearchResult> results = new ArrayList<GuideSearchResult>();

	/**
	 * @return the results
	 */
	public List<GuideSearchResult> getResults() {
		return results;
	}

	/**
	 * @param results
	 *            the results to set
	 */
	public void setResults(List<GuideSearchResult> results) {
		this.results = results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GuideSearch [results=" + results + "]";
	}

}
