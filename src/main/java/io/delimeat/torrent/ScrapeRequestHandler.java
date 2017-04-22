package io.delimeat.torrent;

import java.io.IOException;
import java.net.URI;

import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.UnhandledScrapeException;

public interface ScrapeRequestHandler {

	/**
	 * Perform a scrape
	 * @param uri
	 * @param infoHash
	 * @return
	 * @throws UnhandledScrapeException
	 * @throws TorrentException
	 */
	public ScrapeResult scrape(URI uri, InfoHash infoHash) throws IOException, UnhandledScrapeException, TorrentException;
}
