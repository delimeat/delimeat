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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import io.delimeat.torrent.bencode.BDictionary;
import io.delimeat.torrent.bencode.BInteger;
import io.delimeat.torrent.bencode.BString;
import io.delimeat.torrent.bencode.BencodeException;
import io.delimeat.torrent.bencode.BencodeUtils;
import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.TorrentNotFoundException;
import io.delimeat.torrent.exception.TorrentResponseBodyException;
import io.delimeat.torrent.exception.TorrentResponseException;
import io.delimeat.torrent.exception.TorrentTimeoutException;
import io.delimeat.torrent.exception.UnhandledScrapeException;
import io.delimeat.util.DelimeatUtils;
import okhttp3.Request;
import okhttp3.Response;

@Component
public class HttpScrapeRequestHandler_Impl implements ScrapeRequestHandler {

	private final BString FILES_KEY = new BString("files");
	private final BString COMPLETE_KEY = new BString("complete");
	private final BString INCOMPLETE_KEY = new BString("incomplete");

	private final List<String> protocols = Arrays.asList("HTTP", "HTTPS");

	@Override
	public List<String> getSupportedProtocols() {
		return protocols;
	}

	@Override
	public ScrapeResult doScrape(URI uri, InfoHash infoHash) throws UnhandledScrapeException, TorrentTimeoutException,TorrentResponseException, TorrentResponseBodyException, TorrentException {

		try {
			final URL scrapeURL = generateScrapeURL(uri, infoHash);

			Request request = new Request.Builder().url(scrapeURL).addHeader("Accept", "text/plain").build();

			Response response;
			try {
				response = DelimeatUtils.httpClient().newCall(request).execute();
			} catch (SocketTimeoutException ex) {
				throw new TorrentTimeoutException(scrapeURL);
			}
			
			if (response.isSuccessful() == false) {
				switch (response.code()) {
				case 404:
					throw new TorrentNotFoundException(response.code(), response.message(), scrapeURL);
				default:
					throw new TorrentResponseException(response.code(), response.message(), scrapeURL);
				}
			}
			
			BDictionary dictionary; 
			byte[] responseBytes = response.body().bytes();
			response.body().close();
			try{
				 dictionary = BencodeUtils.decode(new ByteArrayInputStream(responseBytes));
			}catch(BencodeException ex){
				throw new TorrentResponseBodyException(scrapeURL, new String(responseBytes), ex);
			}
			return umarshalScrapeResult(dictionary, infoHash);

		} catch (IOException e) {
			throw new TorrentException(e);
		}
	}

	public URL generateScrapeURL(URI uri, InfoHash infoHash)
			throws UnhandledScrapeException, IOException {

		String path;
		if (uri.getPath().contains("announce")) {
			path = uri.getPath().replace("announce", "scrape");
		} else if (uri.getPath().contains("scrape")) {
			path = uri.getPath();
		} else {
			throw new UnhandledScrapeException(String.format("Unable to scrape URI: %s", uri.toString()));
		}

		final String infoHashString = new String(infoHash.getBytes(), "ISO-8859-1");
		final String infoHashQuery = "info_hash=" + DelimeatUtils.urlEscape(infoHashString, "ISO-8859-1");

		final String query;
		if (uri.getRawQuery() == null) {
			query = infoHashQuery;
		} else if (uri.getRawQuery().contains(infoHashQuery)) {
			query = uri.getRawQuery();
		} else {
			query = uri.getRawQuery() + "&" + infoHashQuery;
		}

		return new URL(uri.getScheme(), uri.getHost(), uri.getPort(), path + "?" + query);

	}

	public ScrapeResult umarshalScrapeResult(BDictionary dictionary, InfoHash infoHash) {
		long seeders = 0;
		long leechers = 0;
		if (dictionary.containsKey(FILES_KEY) && dictionary.get(FILES_KEY) instanceof BDictionary) {

			BDictionary infoHashDictionary = (BDictionary) dictionary.get(FILES_KEY);
			if (infoHashDictionary.containsKey(new BString(infoHash.getBytes()))
					&& infoHashDictionary.get(infoHash.getBytes()) instanceof BDictionary) {

				BDictionary resultDictionary = (BDictionary) infoHashDictionary.get(infoHash.getBytes());
				if (resultDictionary.containsKey(COMPLETE_KEY)
						&& resultDictionary.get(COMPLETE_KEY) instanceof BInteger) {
					BInteger complete = (BInteger) resultDictionary.get(COMPLETE_KEY);
					seeders = complete.longValue();
				}

				if (resultDictionary.containsKey(INCOMPLETE_KEY)
						&& resultDictionary.get(INCOMPLETE_KEY) instanceof BInteger) {
					BInteger incomplete = (BInteger) resultDictionary.get(INCOMPLETE_KEY);
					leechers = incomplete.longValue();
				}

			}
		}
		return new ScrapeResult(seeders, leechers);
	}

}
