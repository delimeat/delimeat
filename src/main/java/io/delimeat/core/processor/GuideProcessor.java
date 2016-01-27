package io.delimeat.core.processor;

import io.delimeat.core.guide.GuideException;
import io.delimeat.core.show.ShowException;

public interface GuideProcessor {
  
	public void process() throws ShowException, GuideException;
  	public boolean abort();
  
	//public FeedProcessorStatus getStatus();
  
	public Exception getException();
}
