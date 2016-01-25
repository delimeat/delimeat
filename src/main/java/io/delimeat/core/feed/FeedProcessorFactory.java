package io.delimeat.core.feed;

import io.delimeat.core.config.Config;
import io.delimeat.core.show.Show;

public interface FeedProcessorFactory {
  
	public FeedProcessor build(Show show, Config config);
}
