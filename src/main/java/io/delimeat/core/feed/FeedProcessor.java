package io.delimeat.core.feed;

import io.delimeat.core.feed.validation.FeedValidationException;
import io.delimeat.core.show.ShowException;

public interface FeedProcessor {

	public void process() throws ShowException, FeedValidationException,FeedException;
  	public boolean abort();
  
	public FeedProcessorStatus getStatus();
  
	public Exception getException();
  	
}
