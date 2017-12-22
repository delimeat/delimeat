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
import java.net.URISyntaxException;

import io.delimeat.torrent.entity.InfoHash;
import io.delimeat.torrent.entity.Torrent;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.TorrentNotFoundException;

public class MagnetTorrentReader_Impl extends HttpTorrentReader_Impl implements TorrentReader {
	
	private String defaultUri;
	
	/**
	 * @return the defaultUri
	 */
	public String getDownloadUriTemplate() {
		return defaultUri;
	}

	/**
	 * @param defaultUri the defaultUri to set
	 */
	public void setDownloadUriTemplate(String defaultUri) {
		this.defaultUri = defaultUri;
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.torrent.TorrentReader#read(java.net.URI)
	 */
	@Override
	public Torrent read(URI uri) throws IOException, TorrentNotFoundException, TorrentException {
		
		InfoHash infoHash = TorrentUtils.infoHashFromMagnet(uri);
		String downloadUri = String.format(defaultUri, infoHash.getHex().toUpperCase());
		try{
			return super.read(new URI(downloadUri));
		}catch(URISyntaxException ex){
			throw new TorrentException("Encountered an error creating uri for " + infoHash.getHex(), ex);
		}
	}
}
