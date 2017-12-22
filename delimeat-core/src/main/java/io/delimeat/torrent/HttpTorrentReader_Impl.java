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

import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientBuilder;

import io.delimeat.torrent.bencode.BDictionary;
import io.delimeat.torrent.bencode.BencodeException;
import io.delimeat.torrent.entity.Torrent;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.TorrentNotFoundException;
import io.delimeat.util.jaxrs.BencodeBodyReader;

public class HttpTorrentReader_Impl implements TorrentReader {

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.delimeat.torrent.TorrentReader#read(java.net.URI)
	 */
	@Override
	public Torrent read(URI uri) throws TorrentNotFoundException, TorrentException, IOException {
		try {
			BDictionary dictionary = ClientBuilder.newClient()
					.register(BencodeBodyReader.class)
					.target(uri)
					.request()
					.accept("application/x-bittorrent")
					.header("Referer", uri.toASCIIString())
					.get(BDictionary.class);

			return TorrentUtils.parseRootDictionary(dictionary);

		} catch (NotFoundException ex) {
			throw new TorrentNotFoundException(ex.getResponse().getStatus(), ex.getMessage(), uri.toURL());
		} catch (WebApplicationException | ProcessingException | BencodeException ex) {
			throw new TorrentException(ex);
		}

	}

}
