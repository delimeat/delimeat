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

import org.springframework.cache.annotation.Cacheable;

import io.delimeat.torrent.domain.Torrent;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.TorrentNotFoundException;

public interface TorrentReader {

	/**
	 * Get list of supported protocols
	 * 
	 * @return list of supported protocols
	 */
	public List<String> getSupportedProtocols();
	
	/**
	 * Read a torrent
	 * @param uri
	 * @return
	 * @throws IOException
	 * @throws TorrentNotFoundException
	 * @throws TorrentException
	 */
	@Cacheable(value="torrent", key="uri")
	public Torrent read(URI uri) throws IOException, TorrentNotFoundException, TorrentException;

}
