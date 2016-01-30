package io.delimeat.core.processor.writer;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedException;

public interface TorrentWriter {

	public void write(String fileName, byte[] bytes, Config config) throws FeedException;
}
