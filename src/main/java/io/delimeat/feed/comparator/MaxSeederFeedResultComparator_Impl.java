package io.delimeat.feed.comparator;

import com.google.common.collect.ComparisonChain;

import io.delimeat.feed.domain.FeedResult;

import java.util.Comparator;

public class MaxSeederFeedResultComparator_Impl implements Comparator<FeedResult> {

	@Override
	public int compare(FeedResult result1, FeedResult result2) {
     return ComparisonChain.start()
       			.compare(result2.getSeeders(),result1.getSeeders())
      			.result();
	}

}
