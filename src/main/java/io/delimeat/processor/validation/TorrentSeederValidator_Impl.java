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
package io.delimeat.processor.validation;

import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.show.domain.Show;
import io.delimeat.torrent.TorrentService;
import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.torrent.domain.Torrent;

@Component
@Order(5)
public class TorrentSeederValidator_Impl implements TorrentValidator {
  	
	private static final Logger LOGGER = LoggerFactory.getLogger(TorrentSeederValidator_Impl.class);
    private static final long MINSEEDERS = 20;

    @Autowired
    private TorrentService torrentService;


    public TorrentService getTorrentService() {
		return torrentService;
	}

	public void setTorrentService(TorrentService torrentService) {
		this.torrentService = torrentService;
	}

	@Override
    public Optional<FeedResultRejection> validate(Torrent torrent, Show show, Config config) throws ValidationException {

        final InfoHash infoHash = torrent.getInfo().getInfoHash();
        
        List<String> trackers = Optional.ofNullable(torrent.getTrackers())
        									.orElse(Collections.emptyList());
        
        Optional.ofNullable(torrent.getTracker())
        			.ifPresent(tracker->trackers.add(tracker));

        
        ScrapeResult scrape = null;
        for(String tracker: trackers){
        	scrape = scrape(tracker, infoHash);
        	if(scrape != null){
        		break;
        	}
        }
     
        if(scrape == null || scrape.getSeeders() < MINSEEDERS){
        	return Optional.of(FeedResultRejection.INSUFFICENT_SEEDERS);
        }
        
        return Optional.empty();
    }

    public ScrapeResult scrape(String tracker, InfoHash infohash) {
        ScrapeResult result = null;
    	try {
    		result =  torrentService.doScrape(new URI(tracker), infohash);
        } catch (SocketTimeoutException e) {
        	LOGGER.info("Timed out scraping tracker " + tracker, e);
        } catch (Exception e) {
        	LOGGER.info("Unnable to scrape tracker " + tracker, e);
        }
        return result;
    }

}
