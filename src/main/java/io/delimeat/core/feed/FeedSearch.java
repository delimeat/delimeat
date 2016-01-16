package io.delimeat.core.feed;

import java.util.ArrayList;
import java.util.List;

public class FeedSearch {

	private List<FeedResult> results = new ArrayList<FeedResult>();

	public List<FeedResult> getResults() {
		return results;
	}

	public void setResults(List<FeedResult> results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return "FeedSearch [results=" + results + "]";
	}

}
