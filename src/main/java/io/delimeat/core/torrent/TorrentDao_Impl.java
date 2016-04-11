package io.delimeat.core.torrent;

import com.google.common.io.ByteStreams;

import io.delimeat.util.DelimeatUtils;
import io.delimeat.util.UrlHandler;
import io.delimeat.util.bencode.BDictionary;
import io.delimeat.util.bencode.BInteger;
import io.delimeat.util.bencode.BList;
import io.delimeat.util.bencode.BObject;
import io.delimeat.util.bencode.BString;
import io.delimeat.util.bencode.BencodeException;
import io.delimeat.util.bencode.BencodeUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TorrentDao_Impl implements TorrentDao {

	private final static BString ANNOUNCE_KEY = new BString("announce");
	private final static BString ANNOUNCE_LIST_KEY = new BString("announce-list");
	private final static BString INFO_KEY = new BString("info");
	private final static BString FILES_KEY = new BString("files");
	private final static BString LENGTH_KEY = new BString("length");
	private final static BString NAME_KEY = new BString("name");
	private final static BString LENGHT_KEY = new BString("length");
	private final static BString PATH_KEY = new BString("path");
	
	private UrlHandler handler;
	private Map<String, ScrapeRequestHandler> scrapeRequestHandlers = new HashMap<String, ScrapeRequestHandler>();
	
	public void setUrlHandler(UrlHandler handler){
		this.handler = handler;
	}
	
	public UrlHandler getUrlHandler(){
		return handler;
	}
	
	public void setScrapeRequestHandlers(Map<String, ScrapeRequestHandler> scrapeRequestHandlers){
		this.scrapeRequestHandlers = scrapeRequestHandlers;
	}
	
	public Map<String,ScrapeRequestHandler> getScrapeReqeustHandlers(){
		return scrapeRequestHandlers;
	}
	
	@Override
	public Torrent read(URI uri) throws TorrentNotFoundException, TorrentException, IOException {

     final String protocol = uri.getScheme();
     if(!"HTTP".equalsIgnoreCase(protocol) && !"HTTPS".equalsIgnoreCase(protocol)){
       throw new TorrentException(String.format("Unsupported protocol %s expected one of HTTP or HTTPS",protocol));
     }

     final Map<String,String> headers = new HashMap<String,String>();
     headers.put("referer", uri.toASCIIString());
     final HttpURLConnection conn = (HttpURLConnection)getUrlHandler().openUrlConnection(uri.toURL(),headers);
     
     final int responseCode = conn.getResponseCode();
     if(responseCode == 404){
    	throw new TorrentNotFoundException(String.format("Unnable to retrieve torrent at url %s",uri.toURL()));
     }else if(responseCode != 200){
       throw new TorrentException(String.format("Receieved response %s  from %s",responseCode, uri.toURL()));
     }
     
     try(InputStream input = getUrlHandler().openInput(conn)){
       final byte[] bytes = ByteStreams.toByteArray(input);
       final BDictionary dictionary = BencodeUtils.decode(bytes);
       final Torrent torrent = parseRootDictionary(dictionary);
       torrent.setBytes(bytes);
       return torrent;       
     }catch(BencodeException ex){
       throw new TorrentException("Encountered an error unmarshalling torrent",ex);
     }
	}

	@Override
	public ScrapeResult scrape(URI uri, InfoHash infoHash) throws UnhandledScrapeException, TorrentException, IOException {

		final String protocol = uri.getScheme().toUpperCase();
		if(getScrapeReqeustHandlers().containsKey(protocol)){
			final ScrapeRequestHandler scraper = getScrapeReqeustHandlers().get(protocol);
			return scraper.scrape(uri, infoHash);
		}else{
			throw new UnhandledScrapeException("No scrape request handler found for protocol: " + protocol);
		}
	}
	
	public Torrent parseRootDictionary(BDictionary rootDictionary) throws BencodeException, IOException{
		final Torrent torrent = new Torrent();
		if(rootDictionary.containsKey(ANNOUNCE_KEY) && rootDictionary.get(ANNOUNCE_KEY) instanceof BString){
			BString announceValue = (BString)rootDictionary.get(ANNOUNCE_KEY);
			String tracker = announceValue.toString();
			torrent.setTracker(tracker);
		}
		if (rootDictionary.containsKey(ANNOUNCE_LIST_KEY) && rootDictionary.get(ANNOUNCE_LIST_KEY) instanceof BList) {
			BList announceListValue = (BList)rootDictionary.get(ANNOUNCE_LIST_KEY);
			List<String> announceList = parseAnnounceList(announceListValue);
			torrent.setTrackers(announceList);
		}
		if (rootDictionary.containsKey(INFO_KEY) && rootDictionary.get(INFO_KEY) instanceof BDictionary) {
			BDictionary infoValue = (BDictionary)rootDictionary.get(INFO_KEY);
			TorrentInfo info = parseInfoDictionary(infoValue);
			torrent.setInfo(info);
		}
		return torrent;
	}
	
	public List<String> parseAnnounceList(BList announceList){
		List<String> trackers = new ArrayList<String>();
		for (BObject tier : announceList) {
			if (tier instanceof BList) {
				BList tierList = (BList)tier;
				for(BObject tierVal: tierList){
					if(tierVal instanceof BString){
						BString tierTracker = (BString)tierVal;
						String tracker = tierTracker.toString();
						trackers.add(tracker);
					}
				}
			}
		}
		return trackers;
	}
	
	public TorrentInfo parseInfoDictionary(BDictionary infoDictionary ) throws BencodeException, IOException{
		final TorrentInfo info = new TorrentInfo();
		byte[] rawBytes = BencodeUtils.encode(infoDictionary);
		byte[] sha1Bytes = DelimeatUtils.getSHA1(rawBytes);
		InfoHash infoHash = new InfoHash(sha1Bytes);
		info.setInfoHash(infoHash);
		
		if (infoDictionary.containsKey(NAME_KEY) && infoDictionary.get(NAME_KEY) instanceof BString) {
			BString nameValue = (BString)infoDictionary.get(NAME_KEY);
			info.setName(nameValue.toString());
		}
		
		if (infoDictionary.containsKey(LENGTH_KEY) && infoDictionary.get(LENGTH_KEY) instanceof BInteger) {
			BInteger lenghtValue =(BInteger)infoDictionary.get(LENGTH_KEY);
			info.setLength(lenghtValue.longValue());
		}
		
		if (infoDictionary.containsKey(FILES_KEY) && infoDictionary.get(FILES_KEY) instanceof BList) {
			// get the files list
			BList filesValue = (BList)infoDictionary.get(FILES_KEY);
			// loop through the list creating a file for each value found
			for(BObject fileValue: filesValue ){
				if(fileValue instanceof BDictionary){
					TorrentFile file = parseTorrentFile((BDictionary)fileValue);
					info.getFiles().add(file);
				}
			}
		}
		
		return info;
	}
	
	public TorrentFile parseTorrentFile(BDictionary fileDictionary){
		TorrentFile file = new TorrentFile();
		if(fileDictionary.containsKey(LENGTH_KEY) && fileDictionary.get(LENGHT_KEY) instanceof BInteger){
			BInteger lengthInt = (BInteger)fileDictionary.get(LENGHT_KEY);
			long length = lengthInt.longValue();
			file.setLength(length);
		}
		if (fileDictionary.containsKey(PATH_KEY) && fileDictionary.get(PATH_KEY) instanceof BList){
			// get the list
			BList pathList = (BList)fileDictionary.get(PATH_KEY);
			// loop through the list getting the string values
			String name = "";
			boolean first = true;
			for(BObject pathValue: pathList){
				// double check if they are string values
				if(pathValue instanceof BString){
					// get the value and store it
					String pathStr = ((BString)pathValue).toString();
					if(first){
						name += pathStr;
						first = false;
					}else{
						name += System.getProperty("file.separator") + pathStr;
					}
					
				}
			}
			file.setName(name);
		}
		return file;
	}

}
