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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.delimeat.torrent.bencode.BDictionary;
import io.delimeat.torrent.bencode.BInteger;
import io.delimeat.torrent.bencode.BList;
import io.delimeat.torrent.bencode.BObject;
import io.delimeat.torrent.bencode.BString;
import io.delimeat.torrent.bencode.BencodeException;
import io.delimeat.torrent.bencode.BencodeUtils;
import io.delimeat.torrent.entity.InfoHash;
import io.delimeat.torrent.entity.Torrent;
import io.delimeat.torrent.entity.TorrentFile;
import io.delimeat.torrent.entity.TorrentInfo;
import io.delimeat.util.DelimeatUtils;

public class TorrentUtils {
	
	private final static BString ANNOUNCE_KEY = new BString("announce");
	private final static BString ANNOUNCE_LIST_KEY = new BString("announce-list");
	private final static BString INFO_KEY = new BString("info");
	private final static BString FILES_KEY = new BString("files");
	private final static BString LENGTH_KEY = new BString("length");
	private final static BString NAME_KEY = new BString("name");
	private final static BString LENGHT_KEY = new BString("length");
	private final static BString PATH_KEY = new BString("path");
	
    public static InfoHash infoHashFromMagnet(URI magnetUri){
    	Matcher m = Pattern.compile("([A-Z]|[a-z]|\\d){40}").matcher(magnetUri.toASCIIString());
    	if(m.find() == false){
    		return null;
    	}
    	String infoHashStr = m.group();
    	byte[] infoHashBytes = DelimeatUtils.hexToBytes(infoHashStr);
    	return new InfoHash(infoHashBytes);
    }
    
    public static Torrent parseRootDictionary(byte[] bytes) throws BencodeException, IOException{
    	BDictionary rootDictionary = BencodeUtils.decode(bytes);
		final Torrent torrent = new Torrent();
		torrent.setBytes(bytes);
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

	public static List<String> parseAnnounceList(BList announceList){
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

	public static TorrentInfo parseInfoDictionary(BDictionary infoDictionary ) throws BencodeException, IOException{
		final TorrentInfo info = new TorrentInfo();
		byte[] rawBytes = BencodeUtils.encode(infoDictionary);
		byte[] sha1Bytes = DelimeatUtils.hashBytes(rawBytes, "SHA-1");

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

	public static TorrentFile parseTorrentFile(BDictionary fileDictionary){
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
