package io.delimeat.core.service;

import javax.transaction.Transactional;

import io.delimeat.core.feed.FeedProcessor;
import io.delimeat.core.show.Show;

public interface FeedProcessorService {

   @Transactional
	public boolean process(FeedProcessor processor, Show show) throws Exception;
}
