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
package io.delimeat.processor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.FeedService;
import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.feed.exception.FeedException;
import io.delimeat.guide.exception.GuideException;
import io.delimeat.guide.exception.GuideNotFoundException;
import io.delimeat.processor.exception.ProcessorInteruptedException;
import io.delimeat.processor.validation.FeedResultValidator;
import io.delimeat.processor.validation.TorrentValidator;
import io.delimeat.processor.validation.ValidationException;
import io.delimeat.show.EpisodeService;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.Show;
import io.delimeat.show.domain.ShowType;
import io.delimeat.torrent.TorrentService;
import io.delimeat.torrent.domain.Torrent;
import io.delimeat.torrent.exception.TorrentException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Component
@Scope("prototype")
@Getter
@Setter
@ToString
public class FeedProcessor_Impl extends AbstractProcessor<Episode> implements Processor {

  	private static final Logger LOGGER = LoggerFactory.getLogger(FeedProcessor_Impl.class);

  	@Autowired
    private EpisodeService episodeService;
  	@Autowired
    private FeedService feedService;
  	@Autowired
    private TorrentService torrentService;
  	@Autowired
    private List<FeedResultValidator> feedResultValidators = new ArrayList<FeedResultValidator>(); 
  	@Autowired
    private List<TorrentValidator> torrentValidators = new ArrayList<TorrentValidator>();
    private Comparator<FeedResult> resultComparator;

    
    public void doProcessing()  throws ValidationException, FeedException, TorrentException, ProcessorInteruptedException, GuideNotFoundException, GuideException {
        final Episode lockedEpisode = episodeService.read(processEntity.getEpisodeId());
        
		// read feed results
		final List<FeedResult> readResults = feedService.read(lockedEpisode.getShow().getTitle());
		LOGGER.debug(String.format("read %s results, %s", readResults.size(), readResults));
		// validate the read results
		final List<FeedResult> foundResults = validateFeedResults(readResults, lockedEpisode, config);
		LOGGER.debug(String.format("found %s results, %s", foundResults.size(), foundResults));
		
		// select all the valid results based on the torrent files
		final List<FeedResult> validResults = validateResultTorrents(foundResults, lockedEpisode.getShow(), config);
		LOGGER.debug(String.format("validated %s results, %s", validResults.size(), validResults));
 		
		Instant now = Instant.now();
        if (validResults.isEmpty() == false) {
            // select the best result
            final Torrent torrent = selectBestResultTorrent(validResults);
            LOGGER.debug("selected torrent: " + torrent);

         	checkThreadStatus();
    		
			torrentService.write(generateTorrentFileName(lockedEpisode), torrent, config);

			lockedEpisode.setLastFeedUpdate(now);
		}
        
        checkThreadStatus();
        
        lockedEpisode.setLastFeedCheck(now);	   
		episodeService.save(lockedEpisode);
    }

    /**
     * @param results
     * @param episode
     * @param config
     * @return
     * @throws ValidationException
     * @throws ProcessorInteruptedException
     */
    public List<FeedResult> validateFeedResults(List<FeedResult> results, Episode episode, Config config) throws ValidationException, ProcessorInteruptedException {
    	
    	for(FeedResultValidator validator: feedResultValidators){
    		
    		checkThreadStatus();
    		
    		validator.validate(results, episode, config);
    		
    	}
    	
        return results.stream()
        		.filter(p->p.getFeedResultRejections().isEmpty())
        		.collect(Collectors.toList());
    }

    /**
     * @param result
     * @return
     * @throws MalformedURLException
     * @throws URISyntaxException
     * @throws IOException
     * @throws TorrentException
     */
    public Torrent fetchTorrent(FeedResult result) throws MalformedURLException, URISyntaxException, IOException, TorrentException {
        // construct the URI for the torrent
        URL url = new URL(result.getTorrentURL());
        URI uri = new URI(url.getProtocol(), null, url.getHost(), url.getPort(), url.getPath(), null, null);

        // fetch the torrent
        return torrentService.read(uri);
    }

    /**
     * @param result
     * @param config
     * @param show
     * @return
     * @throws ValidationException
     */
    public Optional<FeedResultRejection> validateTorrent(Torrent torrent, Config config, Show show) throws ValidationException, ProcessorInteruptedException {
    	Optional<FeedResultRejection> result = Optional.empty();
    	for(TorrentValidator validator: torrentValidators){
    		
    		checkThreadStatus();
    		
    		result = validator.validate(torrent, show, config);
    		
    		if(result.isPresent()){
    			break;
    		}
    	}
    	return result;

    }

    /**
     * @param inResults
     * @param show
     * @param config
     * @return
     * @throws ValidationException
     * @throws ProcessorInteruptedException 
     */
    public List<FeedResult> validateResultTorrents(List<FeedResult> results, Show show, Config config) throws ValidationException, ProcessorInteruptedException {

        for (FeedResult result: results) {
    		
        	checkThreadStatus();
    		
            try {
            	
            	result.setTorrent(fetchTorrent(result));
            	
            } catch (MalformedURLException | URISyntaxException ex) {
                LOGGER.error("encountered an error fetching torrent " + result.getTorrentURL(), ex);
                result.getFeedResultRejections().add(FeedResultRejection.UNNABLE_TO_GET_TORRENT);
                continue;
            } catch (IOException | TorrentException ex) {
                result.getFeedResultRejections().add(FeedResultRejection.UNNABLE_TO_GET_TORRENT);
                continue;
            } 

            Optional<FeedResultRejection> validationResult = validateTorrent(result.getTorrent(), config, show);
            if(validationResult.isPresent()){
            	result.getFeedResultRejections().add(validationResult.get());
            }
            
        }
        
        // if it aint rejected yet its valid!
        return results.stream()
        		.filter(p->p.getFeedResultRejections().isEmpty())
        		.collect(Collectors.toList());
    }

    /**
     * @param results
     * @return
     */
    public Torrent selectBestResultTorrent(List<FeedResult> results) {
        return results.stream()
        		.sorted(resultComparator)
        		.findFirst()
        		.get()
        		.getTorrent();
    }
    
    public String generateTorrentFileName(Episode episode){
    	final Show show = episode.getShow();
    	if(ShowType.DAILY.equals(show.getShowType())){
    		return String.format("%s_%s_%s.torrent", show.getTitle(), DateTimeFormatter.ISO_DATE.format(episode.getAirDate()), episode.getTitle());

    	}else{
    		return String.format("%s_%sx%s_%s.torrent", show.getTitle(), episode.getSeasonNum(), episode.getEpisodeNum(), episode.getTitle());
    	}
    	
    }

}
