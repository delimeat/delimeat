package io.delimeat.processor;

import io.delimeat.config.domain.Config;
import io.delimeat.show.domain.Episode;

public interface FeedProcessorFactory {

	/**
	 * Create a feed processor instance
	 * 
	 * @param episode
	 * @param config
	 * @return processor
	 */
	public Processor build(Episode episode, Config config);

}