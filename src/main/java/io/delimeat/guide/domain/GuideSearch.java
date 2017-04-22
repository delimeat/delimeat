package io.delimeat.guide.domain;

import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
		return MoreObjects.toStringHelper(this)
							.add("results", results)
							.omitNullValues()
							.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hash(results);
	}
}
