package io.delimeat.core.feed;

import io.delimeat.core.config.Config;
import io.delimeat.core.torrent.Torrent;

public interface FeedResultWriter {

	public void write(Torrent torrent, Config config) throws FeedException;
}
