package io.delimeat.core.feed;

import java.util.List;


public interface FeedDao {

	public FeedSource getFeedSource();
	
	public List<FeedResult> read(String showTitle) throws Exception;
	
}
