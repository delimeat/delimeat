package io.delimeat.core.feed;

public interface FeedProcessorThread extends Runnable {
  
	@Override
	public void run();
  
   public void setFeedProcessor(FeedProcessor processor);
   public FeedProcessor getFeedProcessor();
  
   public Exception getException();
}
