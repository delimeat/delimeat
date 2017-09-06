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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.delimeat.config.domain.Config;
import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.torrent.domain.Torrent;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.TorrentNotFoundException;
import io.delimeat.torrent.exception.UnhandledScrapeException;
import io.delimeat.util.DelimeatUtils;

@Service
public class TorrentService_Impl implements TorrentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TorrentService_Impl.class);

	@Autowired
	private TorrentDao dao;
	
	@Autowired
	private TorrentWriter writer;
	
	@Autowired
	private List<ScrapeRequestHandler> scrapeRequestHandlers = new ArrayList<>();
	
  	@Value("${io.delimeat.torrent.downloadUriTemplate}")
  	private String downloadUriTemplate;

	public TorrentDao getDao() {
		return dao;
	}

	public void setDao(TorrentDao dao) {
		this.dao = dao;
	}

	public TorrentWriter getWriter() {
		return writer;
	}

	public void setWriter(TorrentWriter writer) {
		this.writer = writer;
	}
	
	public List<ScrapeRequestHandler> getScrapeRequestHandlers(){
		return scrapeRequestHandlers;
	}
	
	public void setScrapeRequestHandlers(List<ScrapeRequestHandler> scrapeRequestHandlers){
		this.scrapeRequestHandlers = scrapeRequestHandlers;
	}
	
	/**
	 * @return the downloadUriTemplate
	 */
	public String getDownloadUriTemplate() {
		return downloadUriTemplate;
	}

	/**
	 * @param downloadUriTemplate the downloadUriTemplate to set
	 */
	public void setDownloadUriTemplate(String downloadUriTemplate) {
		this.downloadUriTemplate = downloadUriTemplate;
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.torrent.TorrentService#read(java.net.URI)
	 */
	@Override
	public Torrent read(URI uri) throws IOException, TorrentNotFoundException, TorrentException {
		switch(uri.getScheme().toUpperCase()){
		case "MAGNET":
			return read(infoHashFromMagnet(uri));
		case "HTTP":
		case "HTTPS":
			return dao.read(uri);
		default:
			throw new TorrentException("Unsupported scheme for uri " + uri.toASCIIString());
		}
		
	}
	

	/* (non-Javadoc)
	 * @see io.delimeat.torrent.TorrentService#read(io.delimeat.torrent.domain.InfoHash)
	 */
	@Override
	public Torrent read(InfoHash infoHash) throws IOException, TorrentNotFoundException, TorrentException {
		String downloadUri = String.format(downloadUriTemplate,infoHash.getHex().toUpperCase());
		try{
			return read(new URI(downloadUri));
		}catch(URISyntaxException ex){
			throw new TorrentException("Encountered an error creating download uri for " + infoHash.getHex(), ex);
		}
	}

	/* (non-Javadoc)
	 * @see io.delimeat.torrent.TorrentService#doScrape(java.net.URI, io.delimeat.torrent.domain.InfoHash)
	 */
	@Override
	public ScrapeResult doScrape(URI uri, InfoHash infoHash)
			throws IOException, UnhandledScrapeException, TorrentException {
		final String protocol = uri.getScheme().toUpperCase();
		ScrapeRequestHandler handler = scrapeRequestHandlers.stream()
				.filter(p->p.getSupportedProtocols().contains(protocol))
				.findAny()
				.orElse(null);
		
		if(handler != null){
			return handler.doScrape(uri, infoHash);
		}else{
			throw new UnhandledScrapeException(String.format("Protocol %s not supported for scrape", protocol));
		}
	}

	/* (non-Javadoc)
	 * @see io.delimeat.torrent.TorrentService#write(java.lang.String, io.delimeat.torrent.domain.Torrent, io.delimeat.config.domain.Config)
	 */
	@Override
	public void write(String fileName, Torrent torrent, Config config) throws TorrentException {
		writer.write(fileName, torrent.getBytes(), config);

	}

	/* (non-Javadoc)
	 * @see io.delimeat.torrent.TorrentService#scrape(io.delimeat.torrent.domain.Torrent)
	 */
	@Override
	public ScrapeResult scrape(Torrent torrent) {

        final InfoHash infoHash = torrent.getInfo().getInfoHash();
        
        List<String> trackers = new ArrayList<>();
        trackers.addAll(torrent.getTrackers());
        trackers.add(torrent.getTracker());
                
        ScrapeResult scrape = null;
        Iterator<String> iterator = trackers.iterator();
        while(scrape == null && iterator.hasNext()){
        	String tracker = iterator.next();
        	try {
				scrape = doScrape(new URI(tracker), infoHash);
			} catch (IOException | TorrentException | URISyntaxException e) {
				LOGGER.info("Unnable to scrape tracker " + tracker, e);	
			}
        }
        return scrape;
	}
	
    public InfoHash infoHashFromMagnet(URI magnetUri){
    	Matcher m = Pattern.compile("([A-Z]|[a-z]|\\d){40}").matcher(magnetUri.toASCIIString());
    	if(m.find() == false){
    		return null;
    	}
    	String infoHashStr = m.group();
    	byte[] infoHashBytes = DelimeatUtils.hexToBytes(infoHashStr);
    	return new InfoHash(infoHashBytes);
    }

}
