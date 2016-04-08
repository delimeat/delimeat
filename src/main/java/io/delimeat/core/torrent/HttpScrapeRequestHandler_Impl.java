package io.delimeat.core.torrent;

import io.delimeat.util.UrlHandler;
import io.delimeat.util.bencode.BDictionary;
import io.delimeat.util.bencode.BInteger;
import io.delimeat.util.bencode.BString;
import io.delimeat.util.bencode.BencodeException;
import io.delimeat.util.bencode.BencodeUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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
	public ScrapeResult scrape(URI uri, InfoHash infoHash) throws UnhandledScrapeException, TorrentException{
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
		if(uri.getPath().contains("announce")){
			path = uri.getPath().replace("announce", "scrape");
		}else if(uri.getPath().contains("scrape")){
			path = uri.getPath();
		}else {
			throw new UnhandledScrapeException(String.format("Unable to scrape URI: %s",uri.toString()));
		}
                     
     	final String infoHashString = new String(infoHash.getBytes(),"ISO-8859-1");
     	final String infoHashQuery = "info_hash=" + URLEncoder.encode(infoHashString, "ISO-8859-1");

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
     	url = new URL(uri.getScheme(), uri.getHost(), uri.getPort(), path+"?"+query);

		return url;
	}

}
