package io.delimeat.core.feed;

import io.delimeat.core.feed.FeedProcessor;

public class FeedProcessorThread_Impl implements FeedProcessorThread {

  	 private FeedProcessor processor;
    private Exception exception;
    
  	 @Override
    public FeedProcessor getFeedProcessor() {
        return processor;
    }
  
    @Override
    public void setFeedProcessor(FeedProcessor processor) {
       this.processor = processor;    
    }
  
    @Override  
    public Exception getException(){
      return exception;
    }

    @Override
    public void run() {
		try{
			processor.process();
		}catch(Exception e){
			exception = e;
		}
        
    }


}
