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

public class TorrentService_Impl implements TorrentService {

	private TorrentDao dao;
	private TorrentWriter writer;

	public TorrentDao getDao() {
		return dao;
	}

	public void setDao(TorrentDao dao) {
		this.dao = dao;
	}

	public TorrentWriter getWriter() {
		return writer;
	}

	public void setWriter(TorrentWriter writer) {
		this.writer = writer;
	}
	
	@Override
	public Torrent read(URI uri) throws IOException, TorrentNotFoundException, TorrentException {
		return dao.read(uri);
	}

	@Override
	public ScrapeResult scrape(URI uri, InfoHash infoHash)
			throws IOException, UnhandledScrapeException, TorrentException {
		return dao.scrape(uri, infoHash);
	}

	@Override
	public void write(String fileName, Torrent torrent, Config config) throws TorrentException {
		writer.write(fileName, torrent.getBytes(), config);

	}


}
