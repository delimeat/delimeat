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
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.delimeat.config.ConfigService;
import io.delimeat.config.entity.Config;
import io.delimeat.processor.entity.FeedProcessUnit;
import io.delimeat.processor.entity.FeedProcessUnitRejection;
import io.delimeat.processor.filter.FeedResultFilter;
import io.delimeat.processor.validation.TorrentValidator;
import io.delimeat.feed.FeedService;
import io.delimeat.feed.entity.FeedResult;
import io.delimeat.feed.entity.FeedSource;
import io.delimeat.torrent.TorrentService;
import io.delimeat.torrent.entity.InfoHash;
import io.delimeat.torrent.entity.ScrapeResult;
import io.delimeat.torrent.entity.Torrent;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.show.EpisodeService;
import io.delimeat.show.entity.Episode;
import io.delimeat.show.entity.EpisodeStatus;
import io.delimeat.show.entity.Show;
import io.delimeat.show.entity.ShowType;
import io.delimeat.util.DelimeatUtils;

public class FeedItemProcessor_Impl implements FeedItemProcessor {

  	private static final Logger LOGGER = LoggerFactory.getLogger(FeedItemProcessor_Impl.class);
  	private static final long MINSEEDERS = 20;
  			
  	private ConfigService configService;
    private EpisodeService episodeService;
    private FeedService feedService;
    private TorrentService torrentService;
    private List<TorrentValidator> torrentValidators = new ArrayList<TorrentValidator>();
  	private List<FeedResultFilter> feedResultFilters = new ArrayList<FeedResultFilter>();

      	
	/**
	 * @return the configService
	 */
	public ConfigService getConfigService() {
		return configService;
	}

	/**
	 * @param configService the configService to set
	 */
	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}

	/**
	 * @return the episodeService
	 */
	public EpisodeService getEpisodeService() {
		return episodeService;
	}

	/**
	 * @param episodeService the episodeService to set
	 */
	public void setEpisodeService(EpisodeService episodeService) {
		this.episodeService = episodeService;
	}

	/**
	 * @return the feedService
	 */
	public FeedService getFeedService() {
		return feedService;
	}

	/**
	 * @param feedService the feedService to set
	 */
	public void setFeedService(FeedService feedService) {
		this.feedService = feedService;
	}

	/**
	 * @return the torrentService
	 */
	public TorrentService getTorrentService() {
		return torrentService;
	}

	/**
	 * @param torrentService the torrentService to set
	 */
	public void setTorrentService(TorrentService torrentService) {
		this.torrentService = torrentService;
	}

	/**
	 * @return the torrentValidators
	 */
	public List<TorrentValidator> getTorrentValidators() {
		return torrentValidators;
	}

	/**
	 * @param torrentValidators the torrentValidators to set
	 */
	public void setTorrentValidators(List<TorrentValidator> torrentValidators) {
		this.torrentValidators = torrentValidators;
	}

	/**
	 * @return the feedResultFilters
	 */
	public List<FeedResultFilter> getFeedResultFilters() {
		return feedResultFilters;
	}

	/**
	 * @param feedResultFilters the feedResultFilters to set
	 */
	public void setFeedResultFilters(List<FeedResultFilter> feedResultFilters) {
		this.feedResultFilters = feedResultFilters;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.ItemProcessor#process(java.lang.Object)
	 */
    /* (non-Javadoc)
	 * @see io.delimeat.processor.FeedItemProcessor#process(io.delimeat.show.entity.Episode)
	 */
    @Transactional(TxType.REQUIRES_NEW)
	@Override
	public void process(Episode episode) throws Exception {
    	LOGGER.debug("starting feed item processor for {} ", episode);
		
    	final Config config = configService.read();
    	
		// read feed results
		final List<FeedResult> readResults = feedService.read(episode.getShow().getTitle());
		LOGGER.debug(String.format("read %s results, %s", readResults.size(), readResults));
				
		feedResultFilters.forEach(p->p.filter(readResults, episode, config));
		LOGGER.debug(String.format("After filtering %s results, %s", readResults.size(), readResults));

		final List<FeedProcessUnit> processUnits = readResults.stream()
				.map(p->convertFeedResult(p))
				.collect(Collectors.toList());
		
		// fetch all the torrents
		processUnits.forEach(p->fetchTorrent(p));
		
		// validate each torrent 
		processUnits.stream()
			.filter(p->p.getTorrent() != null)
			.forEach(p->validateTorrent(p,episode.getShow(), config));
		
		// scrape each torrent
		processUnits.stream()
			.filter(p->p.getTorrent() != null)
			.filter(p->p.getScrape() == null)
			.forEach(this::scrapeTorrent);
		
		// count the valid results
		long validResultsCnt = processUnits.stream()
				.filter(p->p.getRejections().isEmpty())
				.count();
		
		LOGGER.debug("validated {} results, {}", validResultsCnt, processUnits);
		
		Instant now = Instant.now();
        // select the best result
		final FeedProcessUnit result = selectBestResultTorrent(processUnits);
        if (result != null) {
            LOGGER.debug("selected best result: " + result);

            final Torrent torrent = result.getTorrent();
            LOGGER.debug("selected torrent: " + torrent);
    		
			torrentService.write(generateTorrentFileName(result.getSource(), episode), torrent, config);

			episode.setLastFeedUpdate(now);
			episode.setStatus(EpisodeStatus.FOUND);
		}
        
        
        episode.setLastFeedCheck(now);	   
		episodeService.update(episode);
		
    	LOGGER.debug("ending feed item processor for {}", episode);	
	}
    
    public FeedProcessUnit convertFeedResult(FeedResult feedResult){

		FeedProcessUnit processUnit = new FeedProcessUnit();
		processUnit.setSource(feedResult.getSource());
		processUnit.setTitle(feedResult.getTitle());
		if(feedResult.getSeeders() != 0 || feedResult.getLeechers() != 0){
			ScrapeResult result = ScrapeResult.builder()
					.seeders(feedResult.getSeeders())
					.leechers(feedResult.getLeechers())
					.build();
			
			processUnit.setScrape(result);
		}
		
		if(feedResult.getTorrentURL() != null){
    		try{
    			processUnit.setDownloadUri(new URI(feedResult.getTorrentURL()));
    		}catch(URISyntaxException ex){
    			LOGGER.debug("Unable to parse uri {} for result {}", feedResult.getTorrentURL(), feedResult);
    		}
		} 
		
		if(feedResult.getInfoHashHex() != null){
			byte[] infoHashBytes = DelimeatUtils.hexToBytes(feedResult.getInfoHashHex());
			InfoHash infoHash = new InfoHash(infoHashBytes);
			processUnit.setInfoHash(infoHash);
		}
		return processUnit;
    }

    /**
     * @param result
     * @return
     * @throws MalformedURLException
     * @throws URISyntaxException
     * @throws IOException
     * @throws TorrentException
     */
    public boolean fetchTorrent(FeedProcessUnit result) {
       Torrent torrent = null;
    	if(result.getDownloadUri() != null){
    		try{
	        	// fetch the torrent
	        	torrent = torrentService.read(result.getDownloadUri());
            } catch (IOException | TorrentException ex) {
                LOGGER.error("encountered an error fetching torrent for " + result, ex);
            }
        }else if(result.getInfoHash() != null){
    		try{
	        	// fetch the torrent
	        	torrent = torrentService.read(result.getInfoHash());
            } catch (IOException | TorrentException ex) {
                LOGGER.error("encountered an error fetching torrent for " + result, ex);
            }
        }
    	
    	if(torrent != null){
        	result.setTorrent(torrent);
        	result.setInfoHash(torrent.getInfo().getInfoHash());
        	return true;
    	}else{
    		result.getRejections().add(FeedProcessUnitRejection.UNNABLE_TO_GET_TORRENT);
    		return false;
    	}
        
    }
    
    public void validateTorrent(FeedProcessUnit result, Show show, Config config) {
    	for(TorrentValidator validator: torrentValidators){
    		if(validator.validate(result.getTorrent(), show, config) == false){
    			result.getRejections().add(validator.getRejection());
    			break;
    		}
    	}
    }

    /**
     * @param results
     * @return
     */
    public FeedProcessUnit selectBestResultTorrent(List<FeedProcessUnit> results) {
        return results.stream()
        		.filter(p->p.getRejections().isEmpty())
        		.sorted(new Comparator<FeedProcessUnit>() {
					@Override
					public int compare(FeedProcessUnit o1, FeedProcessUnit o2) {
						return Long.compare(o2.getScrape().getSeeders(), o1.getScrape().getSeeders());
					}
				})
        		.findFirst()
        		.orElse(null);
    }
    
    /**
     * @param episode
     * @return
     */
    public String generateTorrentFileName(FeedSource source, Episode episode){
    	final Show show = episode.getShow();
    	StringBuilder sb = new StringBuilder();
    	sb.append(source.toString())
    		.append("_")
    		.append(show.getTitle());
    	if(ShowType.DAILY.equals(show.getShowType())){
    		sb.append("_").append( DateTimeFormatter.ISO_DATE.format(episode.getAirDate()));

    	}else{
    		sb.append("_").append(String.format("%02dx%02d", episode.getSeasonNum(), episode.getEpisodeNum()));
    	}
    	
    	if(episode.getTitle() != null && episode.getTitle().isEmpty() == false){
    		sb.append("_").append(episode.getTitle());
    	}
    	
    	sb.append(".torrent");
    	return sb.toString();
    	
    }
  	
  	public void scrapeTorrent(FeedProcessUnit processUnit){
		processUnit.setScrape(torrentService.scrape(processUnit.getTorrent()));
		
		if(processUnit.getScrape() == null || processUnit.getScrape().getSeeders() < MINSEEDERS){
			processUnit.getRejections().add(FeedProcessUnitRejection.INSUFFICENT_SEEDERS);
			LOGGER.debug("Rejected {} for {}",processUnit.getTitle(), FeedProcessUnitRejection.INSUFFICENT_SEEDERS);
		}
  	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FeedItemProcessor_Impl [" + (configService != null ? "configService=" + configService + ", " : "")
				+ (episodeService != null ? "episodeService=" + episodeService + ", " : "")
				+ (feedService != null ? "feedService=" + feedService + ", " : "")
				+ (torrentService != null ? "torrentService=" + torrentService + ", " : "")
				+ (torrentValidators != null ? "torrentValidators=" + torrentValidators + ", " : "")
				+ "]";
	}
  	
}
