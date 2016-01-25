package io.delimeat.core.feed;

import io.delimeat.core.config.ConfigException;
import io.delimeat.core.show.ShowException;

public interface FeedProcessor {

	public void process() throws ConfigException, FeedException, ShowException;
  	public boolean abort();
  
	public FeedProcessorStatus getStatus();
  	
}
