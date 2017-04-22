package io.delimeat.feed.comparator;

import com.google.common.collect.ComparisonChain;

import io.delimeat.feed.domain.FeedResult;

import java.util.Comparator;

public class MaxSeederPreferFileFeedResultComparator_Impl implements Comparator<FeedResult> {

	@Override
	public int compare(FeedResult result1, FeedResult result2) {
     return ComparisonChain.start()
       			.compare(result1.getTorrent().getInfo().getFiles().size(),result2.getTorrent().getInfo().getFiles().size())
        			.compare(result2.getSeeders(),result1.getSeeders())
      			.result();
	}

}
