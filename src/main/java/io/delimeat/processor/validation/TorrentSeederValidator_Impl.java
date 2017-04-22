package io.delimeat.processor.validation;

import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.show.domain.Show;
import io.delimeat.torrent.TorrentService;
import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.ScrapeResult;
import io.delimeat.torrent.domain.Torrent;

public class TorrentSeederValidator_Impl implements TorrentValidator {
  	
	private static final Logger LOGGER = LoggerFactory.getLogger(TorrentSeederValidator_Impl.class);
    private static final long MINSEEDERS = 20;

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
    		result =  torrentService.scrape(new URI(tracker), infohash);
        } catch (SocketTimeoutException e) {
        	LOGGER.info("Timed out scraping tracker " + tracker, e);
        } catch (Exception e) {
        	LOGGER.info("Unnable to scrape tracker " + tracker, e);
        }
        return result;
    }

}
