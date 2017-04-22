package io.delimeat.processor;

public interface ProcessorService {

	public void processAllFeedUpdates()  throws Exception;

	public void processAllGuideUpdates() throws Exception;
}
