package io.delimeat.core.processor;

import io.delimeat.core.config.Config;
import io.delimeat.core.show.Show;

public interface ProcessorFactory {

	public Processor build(Show show, Config config);

}
