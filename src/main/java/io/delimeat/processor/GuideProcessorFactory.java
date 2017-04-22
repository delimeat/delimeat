package io.delimeat.processor;

import io.delimeat.config.domain.Config;
import io.delimeat.show.domain.Show;

public interface GuideProcessorFactory {

	/**
	 * Create a guide processor instance
	 * 
	 * @param show
	 * @param config
	 * @return processor
	 */
	public Processor build(Show show, Config config);

}