/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
	public ScrapeResult doScrape(URI uri, InfoHash infoHash) throws IOException, UnhandledScrapeException, TorrentException;

	
	/**
	 * @param fileName
	 * @param torrent
	 * @param config
	 * @throws TorrentException
	 */
	public void write(String fileName, Torrent torrent, Config config) throws TorrentException;
}
