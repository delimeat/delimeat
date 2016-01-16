package io.delimeat.core.feed;

import java.util.Comparator;

public class MaxSeederFeedResultComparator_Impl implements Comparator<FeedResult> {

	@Override
	public int compare(FeedResult result1, FeedResult result2) {
		return Long.compare(result2.getSeeders(), result1.getSeeders());
	}

}
