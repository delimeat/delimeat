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
import java.net.URL;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientBuilder;

import io.delimeat.torrent.bencode.BDictionary;
import io.delimeat.torrent.bencode.BInteger;
import io.delimeat.torrent.bencode.BString;
import io.delimeat.torrent.entity.InfoHash;
import io.delimeat.torrent.entity.ScrapeResult;
import io.delimeat.torrent.entity.ScrapeResult.Builder;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.TorrentNotFoundException;
import io.delimeat.util.DelimeatUtils;
import io.delimeat.util.jaxrs.BencodeBodyReader;

public class HttpScrapeRequestHandler_Impl implements ScrapeRequestHandler {

	private final BString FILES_KEY = new BString("files");
	private final BString COMPLETE_KEY = new BString("complete");
	private final BString INCOMPLETE_KEY = new BString("incomplete");

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.delimeat.torrent.ScrapeRequestHandler#scrape(java.net.URI,
	 * io.delimeat.torrent.domain.InfoHash)
	 */
	@Override
	public ScrapeResult scrape(URI uri, InfoHash infoHash) throws IOException, TorrentException {

		final URL scrapeURL = generateScrapeURL(uri, infoHash);

		try {
			BDictionary dictionary = ClientBuilder.newClient()
					.register(BencodeBodyReader.class)
					.target(scrapeURL.toURI())
					.request("text/plain")
					.get(BDictionary.class);

			return umarshalScrapeResult(dictionary, infoHash);

		} catch (NotFoundException ex) {
			throw new TorrentNotFoundException(ex.getResponse().getStatus(), ex.getMessage(), scrapeURL);
		} catch (WebApplicationException | ProcessingException | URISyntaxException ex) {
			throw new TorrentException(ex);
		}
	}

	public URL generateScrapeURL(URI uri, InfoHash infoHash) throws TorrentException, IOException {

		String path;
		if (uri.getPath().contains("announce")) {
			path = uri.getPath().replace("announce", "scrape");
		} else if (uri.getPath().contains("scrape")) {
			path = uri.getPath();
		} else {
			throw new TorrentException(String.format("Unable to scrape URI: %s", uri.toString()));
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
		Builder builder = ScrapeResult.builder();
		if (dictionary.containsKey(FILES_KEY) && dictionary.get(FILES_KEY) instanceof BDictionary) {

			BDictionary infoHashDictionary = (BDictionary) dictionary.get(FILES_KEY);
			if (infoHashDictionary.containsKey(new BString(infoHash.getBytes()))
					&& infoHashDictionary.get(infoHash.getBytes()) instanceof BDictionary) {

				BDictionary resultDictionary = (BDictionary) infoHashDictionary.get(infoHash.getBytes());
				if (resultDictionary.containsKey(COMPLETE_KEY)
						&& resultDictionary.get(COMPLETE_KEY) instanceof BInteger) {
					BInteger complete = (BInteger) resultDictionary.get(COMPLETE_KEY);
					builder.seeders(complete.longValue());
				}

				if (resultDictionary.containsKey(INCOMPLETE_KEY)
						&& resultDictionary.get(INCOMPLETE_KEY) instanceof BInteger) {
					BInteger incomplete = (BInteger) resultDictionary.get(INCOMPLETE_KEY);
					builder.leechers(incomplete.longValue());
				}

			}
		}
		return builder.build();
	}

}
