package io.delimeat.torrent;

import io.delimeat.config.domain.Config;
import io.delimeat.torrent.exception.TorrentException;

public interface TorrentWriter {

	public void write(String fileName, byte[] bytes, Config config) throws TorrentException;
}
