package io.delimeat.core.feed;

import io.delimeat.core.service.FeedProcessorService;
import io.delimeat.core.show.Show;

import java.util.List;

public class FeedProcessor_Impl implements FeedProcessor {

	private FeedProcessorService service;
	
	private Show show;
	private Exception exception;
	private FeedProcessorStatus status = FeedProcessorStatus.PENDING;
	
	private List<FeedResult> foundResults;
	private FeedResult selectedResult;

	
	public FeedProcessorService getService() {
		return service;
	}

	public void setService(FeedProcessorService service) {
		this.service = service;
	}

	@Override
	public List<FeedResult> getFoundResults(){
		return foundResults;
	}

	@Override
	public void setFoundResults(List<FeedResult> foundResults) {
		this.foundResults = foundResults;
		
	}
	
	@Override
	public FeedResult getSelectedResult(){
		return selectedResult;
	}

	@Override
	public void setSelectedResult(FeedResult selectedResult) {
		this.selectedResult = selectedResult;
	}
	
	@Override
	public Exception getException() {
		return exception;
	}
	
	@Override
	public FeedProcessorStatus getStatus() {
		return status;
	}

	@Override
	public void setShow(Show show) {
		this.show = show;
	}
	
	public Show getShow(){
		return show;
	}
	
	@Override
	public void run() {
		try{
			status = FeedProcessorStatus.STARTED;
			boolean result = service.process(this, show);
			if(result){
				status = FeedProcessorStatus.ENDED_SUCCESSFUL;
			}else{
				status = FeedProcessorStatus.ENDED_UNSUCCESSFUL;	
			}
		}catch(Exception e){
			exception = e;
			status = FeedProcessorStatus.ENDED_ERROR;
		}
	}
	
}
