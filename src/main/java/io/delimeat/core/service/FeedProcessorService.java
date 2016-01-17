package io.delimeat.core.service;

import io.delimeat.core.feed.FeedProcessor;
import io.delimeat.core.show.Show;

public interface FeedProcessorService {

	public boolean process(FeedProcessor processor, Show show) throws Exception;
}
