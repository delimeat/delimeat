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

import io.delimeat.torrent.bencode.BDictionary;
import io.delimeat.torrent.bencode.BInteger;
import io.delimeat.torrent.bencode.BString;
import io.delimeat.torrent.bencode.BencodeException;
import io.delimeat.torrent.bencode.BencodeUtils;
import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.UnhandledScrapeException;
import io.delimeat.util.UrlHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HttpScrapeRequestHandler_Impl implements ScrapeRequestHandler {
	
	private final static BString FILES_KEY = new BString("files");
	private final static BString COMPLETE_KEY = new BString("complete");
	private final static BString INCOMPLETE_KEY =new BString("incomplete");
	
	private final List<String> protocols = Arrays.asList("HTTP","HTTPS");
	
	@Autowired
	private UrlHandler handler;
	
	public void setUrlHandler(UrlHandler handler){
		this.handler = handler;
	}
	
	public UrlHandler getUrlHandler(){
		return handler;
	}
	
	@Override
	public List<String> getSupportedProtocols() {
		return protocols;
	}
	
	@Override
	public ScrapeResult doScrape(URI uri, InfoHash infoHash) throws UnhandledScrapeException, TorrentException{
		try{
         final String protocol = uri.getScheme();
         if (!"HTTP".equalsIgnoreCase(protocol) && !"HTTPS".equalsIgnoreCase(protocol)) {
           throw new TorrentException(String.format("Unsupported protocol %s expected one of HTTP or HTTPS",protocol));
         }

         final URL scrapeURL = generateScrapeURL(uri, infoHash);
         HttpURLConnection conn = (HttpURLConnection) getUrlHandler().openUrlConnection(scrapeURL);
         final int responseCode = conn.getResponseCode();
         if (responseCode != 200) {
           throw new TorrentException(String.format("Receieved response %s from %s", responseCode,scrapeURL));
         }

         try(InputStream input = getUrlHandler().openInput(conn)){

           final BDictionary dictionary = BencodeUtils.decode(input);
           return umarshalDictionary(dictionary, infoHash);

         } catch (BencodeException ex) {
           throw new TorrentException("Encountered an error unmarshalling torrent", ex);
         }
        
      }catch(IOException e){
        	throw new TorrentException(e);
      }

	}
  
  	public ScrapeResult umarshalDictionary(BDictionary dictionary, InfoHash infoHash){
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
		final String infoHashQuery = "info_hash=" + URLEncoder.encode(infoHashString, "ISO-8859-1");

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


}
