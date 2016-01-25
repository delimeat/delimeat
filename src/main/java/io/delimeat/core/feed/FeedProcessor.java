package io.delimeat.core.feed;

public interface FeedProcessor {

	public void process();
  	public boolean abort();
  
	public FeedProcessorStatus getStatus();
  
   public Exception getException();
  	
}
