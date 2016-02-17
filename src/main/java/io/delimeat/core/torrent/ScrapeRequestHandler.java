package io.delimeat.core.torrent;

import java.io.IOException;
import java.net.URI;

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
