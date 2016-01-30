package io.delimeat.core.service;

import io.delimeat.core.config.ConfigException;
import io.delimeat.core.show.ShowException;

public interface ProcessorService {

	public void processAllFeedUpdates() throws ConfigException, ShowException;

	public void processAllGuideUpdates() throws ConfigException, ShowException;
}
