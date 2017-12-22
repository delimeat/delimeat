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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.delimeat.config.entity.Config;
import io.delimeat.torrent.entity.InfoHash;
import io.delimeat.torrent.entity.ScrapeResult;
import io.delimeat.torrent.entity.Torrent;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.TorrentNotFoundException;
import io.delimeat.torrent.exception.UnhandledScrapeException;

public class TorrentService_Impl implements TorrentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TorrentService_Impl.class);
	private static final String MAGNET_TEMPLATE = "magnet:?xt=urn:btih:%s";

	private Map<String, TorrentReader> readers = new HashMap<>();
	
	private TorrentWriter writer;
	
	private Map<String, ScrapeRequestHandler> scrapeRequestHandlers = new HashMap<>();
	
	
	/**
	 * @return the readers
	 */
	public Map<String, TorrentReader> getReaders() {
		return readers;
	}

	/**
	 * @param readers the readers to set
	 */
	public void setReaders(Map<String, TorrentReader> readers) {
		this.readers = readers;
	}

	/**
	 * @return the writer
	 */
	public TorrentWriter getWriter() {
		return writer;
	}

	/**
	 * @param writer the writer to set
	 */
	public void setWriter(TorrentWriter writer) {
		this.writer = writer;
	}

	/**
	 * @return the scrapeRequestHandlers
	 */
	public Map<String, ScrapeRequestHandler> getScrapeRequestHandlers() {
		return scrapeRequestHandlers;
	}

	/**
	 * @param scrapeRequestHandlers the scrapeRequestHandlers to set
	 */
	public void setScrapeRequestHandlers(Map<String, ScrapeRequestHandler> scrapeRequestHandlers) {
		this.scrapeRequestHandlers = scrapeRequestHandlers;
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.torrent.TorrentService#read(java.net.URI)
	 */
	@Override
	public Torrent read(URI uri) throws IOException, TorrentNotFoundException, TorrentException {
			
		final String protocol = uri.getScheme().toUpperCase();
		TorrentReader reader = readers.get(protocol);
		if(reader == null){
			throw new TorrentException(String.format("Protocol %s not supported for read", protocol));
		}
		
		return reader.read(uri);		
	}
	

	/* (non-Javadoc)
	 * @see io.delimeat.torrent.TorrentService#read(io.delimeat.torrent.domain.InfoHash)
	 */
	@Override
	public Torrent read(InfoHash infoHash) throws IOException, TorrentNotFoundException, TorrentException {
		return read(buildMagnetUri(infoHash));
	}

	/**
	 * Perform a scrape
	 * @param uri
	 * @param infoHash
	 * @return
	 * @throws IOException
	 * @throws UnhandledScrapeException
	 * @throws TorrentException
	 */
	public ScrapeResult doScrape(URI uri, InfoHash infoHash)
			throws IOException, UnhandledScrapeException, TorrentException {
		final String protocol = uri.getScheme().toUpperCase();
		ScrapeRequestHandler handler = scrapeRequestHandlers.get(protocol);
		
		if(handler == null){
			throw new UnhandledScrapeException(String.format("Protocol %s not supported for scrape", protocol));
		}
		
		return handler.scrape(uri, infoHash);
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
        trackers = trackers.stream()
        		.filter(p->p != null)
        		.filter(p->p.isEmpty() == false)
        		.collect(Collectors.toList());
        
        if(trackers.isEmpty()){
        	try{
        		URI magnet = buildMagnetUri(infoHash);
        		trackers.add(magnet.toASCIIString());
        	}catch(TorrentException ex){
        		LOGGER.error(ex.getMessage());
        	}
        }
                        
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
	
	/**
	 * Build a magnet uri from an infohash
	 * @param infoHash
	 * @return
	 * @throws TorrentException
	 */
	public URI buildMagnetUri(InfoHash infoHash) throws TorrentException{
		String magnetUri = String.format(MAGNET_TEMPLATE,infoHash.getHex());
		try{
			return new URI(magnetUri);
		}catch(URISyntaxException ex){
			throw new TorrentException("Encountered an error creating magnet uri " + magnetUri, ex);
		}
	}

}
