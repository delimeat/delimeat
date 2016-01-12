package io.delimeat.core.torrent;

import io.delimeat.util.UrlHandler;
import io.delimeat.util.bencode.BDictionary;
import io.delimeat.util.bencode.BInteger;
import io.delimeat.util.bencode.BObject;
import io.delimeat.util.bencode.BString;
import io.delimeat.util.bencode.BencodeException;
import io.delimeat.util.bencode.BencodeUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Set;

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
	public ScrapeResult scrape(URI uri, byte[] infoHash) throws UnhandledScrapeException, TorrentException, IOException{
		try{
			URL scrapeURL = generateScrapeURL(uri, infoHash);
			BDictionary dictionary = BencodeUtils.decode(getUrlHandler().openInput(scrapeURL));
			long seeders = 0;
			long leechers = 0;
			if(dictionary.containsKey(FILES_KEY) && dictionary.get(FILES_KEY) instanceof BDictionary){
				BDictionary infoHashDictionary = (BDictionary)dictionary.get(FILES_KEY);
				if(infoHashDictionary.containsKey(new BString(infoHash)) && infoHashDictionary.get(infoHash) instanceof BDictionary){
					BDictionary resultDictionary = (BDictionary)infoHashDictionary.get(infoHash);
					Set<BString> keys = resultDictionary.keySet();
					for (BString key : keys) {
						BObject value = resultDictionary.get(key);
						// set seeder value
						if (key.equals(COMPLETE_KEY)
								&& value instanceof BInteger) {
							seeders = ((BInteger) value).getValue();
						}// set leechers value
						else if (key.equals(INCOMPLETE_KEY)
								&& value instanceof BInteger) {
							leechers = ((BInteger) value).getValue();
						}
					}
				}
			}
			return new ScrapeResult(seeders, leechers);
		}catch(URISyntaxException e){
			throw new TorrentException(e);
		}catch(BencodeException e){
			throw new TorrentException(e);
		}
	}
	
	public URL generateScrapeURL(URI uri, byte[] infoHash) throws UnhandledScrapeException, URISyntaxException, UnsupportedEncodingException, MalformedURLException{
		String path;
		if(uri.getPath().contains("announce")){
			path = uri.getPath().replace("announce", "scrape");
		}else if(uri.getPath().contains("scrape")){
			path = uri.getPath();
		}else {
			throw new UnhandledScrapeException("Unable to scrape URI: " + uri.toString());
		}
		String infoHashS = new String(infoHash, "ISO-8859-1");
		String infoHashStr = "info_hash="+URLEncoder.encode(infoHashS, "ISO-8859-1");
		String query;
		if(uri.getQuery()==null){
			query = infoHashStr;
		}
		else if(uri.getQuery().contains(infoHashStr)){
			query = uri.getQuery();
		}
		else{
			query = uri.getQuery() + "&" + infoHashStr;
		}
		return new URL(uri.getScheme(), uri.getHost(), uri.getPort(), path+"?"+query);
	}

}
