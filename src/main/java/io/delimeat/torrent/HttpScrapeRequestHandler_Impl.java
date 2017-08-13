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

import io.delimeat.torrent.bencode.BDictionary;
import io.delimeat.torrent.bencode.BInteger;
import io.delimeat.torrent.bencode.BString;
import io.delimeat.torrent.bencode.BencodeException;
import io.delimeat.torrent.bencode.BencodeUtils;
import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.UnhandledScrapeException;
import io.delimeat.util.DelimeatUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Component
public class HttpScrapeRequestHandler_Impl implements ScrapeRequestHandler {
	
	private final BString FILES_KEY = new BString("files");
	private final BString COMPLETE_KEY = new BString("complete");
	private final BString INCOMPLETE_KEY =new BString("incomplete");
	
	private final List<String> protocols = Arrays.asList("HTTP","HTTPS");

	@Override
	public List<String> getSupportedProtocols() {
		return protocols;
	}
	
	@Override
	public ScrapeResult doScrape(URI uri, InfoHash infoHash) throws UnhandledScrapeException, TorrentException{
		
         try{
        	 final URL scrapeURL = generateScrapeURL(uri, infoHash);
        	 
 			OkHttpClient client = new OkHttpClient();
 			Request request = new Request.Builder()
 									.url(scrapeURL)
 									.build();

 			Response response;
 			try{
 			    response = client.newCall(request).execute();
 			} catch(SocketTimeoutException ex){
 	           throw new TorrentException(String.format("Timed out fetching torrent from %s", scrapeURL)); 
 	        } 
 			if (response.isSuccessful()) {
 				BDictionary dictionary = BencodeUtils.decode(response.body().byteStream());
 				return umarshalScrapeResult(dictionary,infoHash);
 			}else{
 				throw new TorrentException(String.format("Scrape returned code %s with message \"%s\" for url: %s",
						response.code(), response.message(), response.request().url()));
 			}

         } catch(BencodeException | IOException e){
        	 throw new TorrentException(e);
         }
	}
	
	public URL generateScrapeURL(URI uri, InfoHash infoHash) throws UnhandledScrapeException, TorrentException, IOException{
		
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
	
  	public ScrapeResult umarshalScrapeResult(BDictionary dictionary, InfoHash infoHash){
		long seeders = 0;
		long leechers = 0;
		if (dictionary.containsKey(FILES_KEY) && dictionary.get(FILES_KEY) instanceof BDictionary) {

			BDictionary infoHashDictionary = (BDictionary) dictionary.get(FILES_KEY);
			if (infoHashDictionary.containsKey(new BString(infoHash.getBytes())) && infoHashDictionary.get(infoHash.getBytes()) instanceof BDictionary) {

				BDictionary resultDictionary = (BDictionary) infoHashDictionary.get(infoHash.getBytes());
				if (resultDictionary.containsKey(COMPLETE_KEY) && resultDictionary.get(COMPLETE_KEY) instanceof BInteger) {
					BInteger complete = (BInteger) resultDictionary.get(COMPLETE_KEY);
					seeders = complete.longValue();
				}

				if (resultDictionary.containsKey(INCOMPLETE_KEY) && resultDictionary.get(INCOMPLETE_KEY) instanceof BInteger) {
					BInteger incomplete = (BInteger) resultDictionary.get(INCOMPLETE_KEY);
					leechers = incomplete.longValue();
				}

			}
		}
		return new ScrapeResult(seeders, leechers);   
   }


}
