package io.delimeat.core.torrent;

import java.io.IOException;
import java.net.URI;

public interface TorrentDao {

	/**
	 * Read a torrent 
	 * @param uri
	 * @return
	 * @throws TorrentException
	 */
	public Torrent read(URI uri) throws IOException, TorrentNotFoundException, TorrentException;
	
	/**
	 * Perform a scrape
	 * @param url
	 * @param infoHash
	 * @return
	 * @throws UnhandledScrapeException
	 * @throws TorrentException
	 */
	public ScrapeResult scrape(URI uri, InfoHash infoHash) throws IOException, UnhandledScrapeException, TorrentException;
}
