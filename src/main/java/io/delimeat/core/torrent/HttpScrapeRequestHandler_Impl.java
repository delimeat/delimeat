package io.delimeat.core.torrent;

import io.delimeat.util.UrlHandler;
import io.delimeat.util.bencode.BDictionary;
import io.delimeat.util.bencode.BInteger;
import io.delimeat.util.bencode.BString;
import io.delimeat.util.bencode.BencodeException;
import io.delimeat.util.bencode.BencodeUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

public class HttpScrapeRequestHandler_Impl implements ScrapeRequestHandler {
	
	private final static BString FILES_KEY = new BString("files");
	private final static BString COMPLETE_KEY = new BString("complete");
	private final static BString INCOMPLETE_KEY =new BString("incomplete");

	private UrlHandler handler;
	
	public void setUrlHandler(UrlHandler handler){
		this.handler = handler;
	}
	
	public UrlHandler getUrlHandler(){
		return handler;
	}
	
	@Override
	public ScrapeResult scrape(URI uri, InfoHash infoHash) throws UnhandledScrapeException, TorrentException, IOException{

		final String protocol = uri.getScheme();
		if (!"HTTP".equalsIgnoreCase(protocol)
				&& !"HTTPS".equalsIgnoreCase(protocol)) {
			throw new TorrentException("Unsupported protocol " + protocol
					+ " expected one of HTTP or HTTPS");
		}
		final URL scrapeURL = generateScrapeURL(uri, infoHash);
		HttpURLConnection conn = null;
		InputStream input = null;
		final BDictionary dictionary;
		try{
			conn = (HttpURLConnection) getUrlHandler()
					.openUrlConnection(scrapeURL);
			final int responseCode = conn.getResponseCode();
			if (responseCode != 200) {
				throw new TorrentException("Receieved response " + responseCode
						+ " from " + scrapeURL);
			}
			input = getUrlHandler().openInput(conn);
	
			
			try {
				dictionary = BencodeUtils.decode(input);
			} catch (BencodeException ex) {
				throw new TorrentException(
						"Encountered an error unmarshalling torrent", ex);
			}
		}finally{
			
			if(input != null)
				input.close();
			
			if(conn != null)
				conn.disconnect();
		}
		
		long seeders = 0;
		long leechers = 0;
		if (dictionary.containsKey(FILES_KEY)
				&& dictionary.get(FILES_KEY) instanceof BDictionary) {

			BDictionary infoHashDictionary = (BDictionary) dictionary
					.get(FILES_KEY);
			if (infoHashDictionary
					.containsKey(new BString(infoHash.getBytes()))
					&& infoHashDictionary.get(infoHash.getBytes()) instanceof BDictionary) {

				BDictionary resultDictionary = (BDictionary) infoHashDictionary
						.get(infoHash.getBytes());
				if (resultDictionary.containsKey(COMPLETE_KEY)
						&& resultDictionary.get(COMPLETE_KEY) instanceof BInteger) {
					BInteger complete = (BInteger) resultDictionary
							.get(COMPLETE_KEY);
					seeders = complete.longValue();
				}

				if (resultDictionary.containsKey(INCOMPLETE_KEY)
						&& resultDictionary.get(INCOMPLETE_KEY) instanceof BInteger) {
					BInteger incomplete = (BInteger) resultDictionary
							.get(INCOMPLETE_KEY);
					leechers = incomplete.longValue();
				}

			}
		}
		return new ScrapeResult(seeders, leechers);

	}
	
	public URL generateScrapeURL(URI uri, InfoHash infoHash) throws UnhandledScrapeException, TorrentException{
		
      String path;
		if(uri.getPath().contains("announce")){
			path = uri.getPath().replace("announce", "scrape");
		}else if(uri.getPath().contains("scrape")){
			path = uri.getPath();
		}else {
			throw new UnhandledScrapeException("Unable to scrape URI: " + uri.toString());
		}
     
		final String infoHashString = new String(infoHash.getBytes());
		final String infoHashQuery;
      try{
        infoHashQuery = "info_hash=" + URLEncoder.encode(infoHashString, "ISO-8859-1");
      } catch (UnsupportedEncodingException ex) {
      	throw new RuntimeException(ex);
    	}
     
		final String query;
		if(uri.getRawQuery()==null){
			query = infoHashQuery;
		}
		else if(uri.getRawQuery().contains(infoHashQuery)){
			query = uri.getRawQuery();
		}
		else{
			query = uri.getRawQuery() + "&" + infoHashQuery;
		}
     
      final URL url;
     	try{
      	url = new URL(uri.getScheme(), uri.getHost(), uri.getPort(), path+"?"+query);
      } catch (MalformedURLException ex) {
      	throw new TorrentException(ex);
    	}
		return url;
	}

}
