package io.delimeat.feed;

import java.util.List;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedSource;
import io.delimeat.feed.exception.FeedException;

public interface FeedDao {

	public FeedSource getFeedSource();
	
	public List<FeedResult> read(String title) throws FeedException;
	
}