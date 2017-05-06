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
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.delimeat.config.domain.Config;
import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.torrent.domain.Torrent;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.torrent.exception.TorrentNotFoundException;
import io.delimeat.torrent.exception.UnhandledScrapeException;

@Service("torrentServiceId")
public class TorrentService_Impl implements TorrentService {

	@Autowired
	private TorrentDao dao;
	
	@Autowired
	private TorrentWriter writer;
	
	@Autowired
	private List<ScrapeRequestHandler> scrapeRequestHandlers = new ArrayList<>();

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
	
	@Override
	public Torrent read(URI uri) throws IOException, TorrentNotFoundException, TorrentException {
		return dao.read(uri);
	}

	@Override
	public ScrapeResult doScrape(URI uri, InfoHash infoHash)
			throws IOException, UnhandledScrapeException, TorrentException {
		final String protocol = uri.getScheme().toUpperCase();
		Optional<ScrapeRequestHandler> handler = scrapeRequestHandlers.stream()
																		.filter(p->p.getSupportedProtocols()
																						.contains(protocol))
																		.findAny();
		if(handler.isPresent()){
			return handler.get().doScrape(uri, infoHash);
		}else{
			throw new UnhandledScrapeException(String.format("Protocol %s not supported for scrape", protocol));
		}
	}

	@Override
	public void write(String fileName, Torrent torrent, Config config) throws TorrentException {
		writer.write(fileName, torrent.getBytes(), config);

	}


}
