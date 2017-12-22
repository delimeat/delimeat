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

import io.delimeat.config.entity.Config;
import io.delimeat.torrent.entity.InfoHash;
import io.delimeat.torrent.entity.ScrapeResult;
import io.delimeat.torrent.entity.Torrent;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.TorrentNotFoundException;

public interface TorrentService {

	/**
	 * Read a torrent using a URI 
	 * @param uri
	 * @return torrent file
	 * @throws IOException
	 * @throws TorrentNotFoundException
	 * @throws TorrentException
	 */
	public Torrent read(URI uri) throws IOException, TorrentNotFoundException, TorrentException;
	
	/**
	 * Read a torrent using an Info Hash
	 * @param infoHash
	 * @return torrent file
	 * @throws IOException
	 * @throws TorrentNotFoundException
	 * @throws TorrentException
	 */
	public Torrent read(InfoHash infoHash) throws IOException, TorrentNotFoundException, TorrentException; 

	/**
	 * Perform a scrape
	 * @param torrent
	 * @return scrape result
	 */
	public ScrapeResult scrape(Torrent torrent);
	
	/**
	 * @param fileName
	 * @param torrent
	 * @param config
	 * @throws TorrentException
	 */
	public void write(String fileName, Torrent torrent, Config config) throws TorrentException;
}
