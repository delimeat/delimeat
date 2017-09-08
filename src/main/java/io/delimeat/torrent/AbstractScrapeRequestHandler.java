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
import java.util.List;

import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.UnhandledScrapeException;

public abstract class AbstractScrapeRequestHandler implements ScrapeRequestHandler {

	private final List<String> protocols;
	
	public AbstractScrapeRequestHandler(List<String> protocols){
		this.protocols = protocols;
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.torrent.ScrapeRequestHandler#getSupportedProtocols()
	 */
	@Override
	public List<String> getSupportedProtocols() {
		return protocols;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.torrent.ScrapeRequestHandler#scrape(java.net.URI, io.delimeat.torrent.domain.InfoHash)
	 */
	@Override
	public ScrapeResult scrape(URI uri, InfoHash infoHash)
			throws IOException, UnhandledScrapeException, TorrentException {
		
		if(protocols.contains(uri.getScheme().toUpperCase()) == false ){
			throw new TorrentException(String.format("Unsupported protocol %s", uri.getScheme()));
		}
		
		ScrapeResult result = doScrape(uri, infoHash);
		
		return result;
	}
	
	abstract ScrapeResult doScrape(URI uri, InfoHash infoHash) throws IOException, UnhandledScrapeException, TorrentException;

}
