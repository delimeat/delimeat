package io.delimeat.feed;

import java.util.List;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.exception.FeedException;

public interface FeedService {

	/**
	 * @param title
	 * @return
	 * @throws FeedException
	 */
	public List<FeedResult> read(String title) throws FeedException;
	
}
