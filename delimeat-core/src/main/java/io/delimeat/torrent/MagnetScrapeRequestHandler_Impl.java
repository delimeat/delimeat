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

import io.delimeat.torrent.entity.InfoHash;
import io.delimeat.torrent.entity.ScrapeResult;
import io.delimeat.torrent.exception.TorrentException;

public class MagnetScrapeRequestHandler_Impl extends HttpScrapeRequestHandler_Impl implements ScrapeRequestHandler {

	private URI defaultTracker;

	/**
	 * @return the defaultTracker
	 */
	public URI getDefaultTracker() {
		return defaultTracker;
	}

	/**
	 * @param defaultTracker the defaultTracker to set
	 */
	public void setDefaultTracker(URI defaultTracker) {
		this.defaultTracker = defaultTracker;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.torrent.ScrapeRequestHandler#scrape(java.net.URI, io.delimeat.torrent.domain.InfoHash)
	 */
	@Override
	public ScrapeResult scrape(URI uri, InfoHash infoHash) throws IOException, TorrentException {		
		return super.scrape(defaultTracker, infoHash);
	}

}
