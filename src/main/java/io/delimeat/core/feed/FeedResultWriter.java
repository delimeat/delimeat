package io.delimeat.core.feed;

import io.delimeat.core.config.Config;

public interface FeedResultWriter {

	public void write(String fileName, byte[] bytes, Config config) throws FeedException;
}
