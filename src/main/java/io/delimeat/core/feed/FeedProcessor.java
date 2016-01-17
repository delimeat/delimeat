package io.delimeat.core.feed;

import io.delimeat.core.show.Show;

import java.util.List;

public interface FeedProcessor extends Runnable {

	@Override
	public void run();
	
	public void setShow(Show show);
	
	public void setFoundResults(List<FeedResult> foundResults);
	public List<FeedResult> getFoundResults();
	
	public void setSelectedResult(FeedResult selectedResult);
	public FeedResult getSelectedResult();
			
	public Exception getException();
	public FeedProcessorStatus getStatus();

}
