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
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import io.delimeat.torrent.bencode.BencodeException;
import io.delimeat.torrent.domain.Torrent;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.TorrentNotFoundException;
import io.delimeat.torrent.exception.TorrentResponseBodyException;
import io.delimeat.torrent.exception.TorrentResponseException;
import io.delimeat.torrent.exception.TorrentTimeoutException;
import io.delimeat.util.DelimeatUtils;
import okhttp3.Request;
import okhttp3.Response;

@Component
public class HttpTorrentReader_Impl implements TorrentReader {

	private final List<String> protocols = Arrays.asList("HTTP", "HTTPS");

	/* (non-Javadoc)
	 * @see io.delimeat.torrent.TorrentReader#getSupportedProtocols()
	 */
	@Override
	public List<String> getSupportedProtocols() {
		return protocols;
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.torrent.TorrentReader#read(java.net.URI)
	 */
	@Override
	public Torrent read(URI uri) throws TorrentNotFoundException,TorrentResponseException, TorrentResponseBodyException, TorrentException, IOException {
		if(protocols.contains(uri.getScheme().toUpperCase()) == false ){
			throw new TorrentException(String.format("Unsupported protocol %s", uri.getScheme()));
		}
		
		try {
			URL url = uri.toURL();
			Request request = new Request.Builder()
										.addHeader("Referer", uri.toASCIIString())
										.addHeader("Accept", "application/x-bittorrent")
										.url(uri.toURL())
										.build();

			Response response;
			try{
				response = DelimeatUtils.httpClient().newCall(request).execute();
			}catch(SocketTimeoutException ex){
				throw new TorrentTimeoutException(uri.toURL());
			}
			
			if (response.isSuccessful() == false) {
				switch (response.code()) {
				case 404:
					throw new TorrentNotFoundException(response.code(), response.message(), url);
				default:
					throw new TorrentResponseException(response.code(), response.message(), url);
				}
			}
			
			final byte[] responseBytes = response.body().bytes();
			response.body().close();
			
			try{
				return TorrentUtils.parseRootDictionary(responseBytes);
			}catch(BencodeException ex){
				throw new TorrentResponseBodyException(url, new String(responseBytes), ex);
			}

		}catch (IOException e) {
			throw new TorrentException(e);
		}

	}

	

}
