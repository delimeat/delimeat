package io.delimeat.torrent;

import java.io.IOException;
import java.net.URI;

import io.delimeat.config.domain.Config;
import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.torrent.domain.Torrent;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.TorrentNotFoundException;
import io.delimeat.torrent.exception.UnhandledScrapeException;

public interface TorrentService {

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

	
	/**
	 * @param fileName
	 * @param torrent
	 * @param config
	 * @throws TorrentException
	 */
	public void write(String fileName, Torrent torrent, Config config) throws TorrentException;
}
