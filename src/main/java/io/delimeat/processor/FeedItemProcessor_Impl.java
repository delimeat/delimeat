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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.delimeat.config.ConfigService;
import io.delimeat.config.domain.Config;
import io.delimeat.config.exception.ConfigException;
import io.delimeat.feed.FeedService;
import io.delimeat.feed.domain.FeedResult;
import io.delimeat.processor.domain.FeedProcessUnit;
import io.delimeat.processor.domain.FeedProcessUnitRejection;
import io.delimeat.processor.filter.FeedResultFilter;
import io.delimeat.processor.validation.TorrentValidator;
import io.delimeat.show.EpisodeService;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.EpisodeStatus;
import io.delimeat.show.domain.Show;
import io.delimeat.show.domain.ShowType;
import io.delimeat.torrent.TorrentService;
import io.delimeat.torrent.domain.InfoHash;
import io.delimeat.torrent.domain.Torrent;
import io.delimeat.torrent.exception.TorrentException;
import io.delimeat.util.DelimeatUtils;

@Component
@Scope("prototype")
public class FeedItemProcessor_Impl implements ItemProcessor<Episode> {

  	private static final Logger LOGGER = LoggerFactory.getLogger(FeedItemProcessor_Impl.class);

  	@Autowired
  	private ConfigService configService;
  	@Autowired
    private EpisodeService episodeService;
  	@Autowired
    private FeedService feedService;
  	@Autowired
    private TorrentService torrentService;
  	@Autowired
    private List<TorrentValidator> torrentValidators = new ArrayList<TorrentValidator>();
  	@Autowired
  	private List<FeedResultFilter> feedResultFilters = new ArrayList<FeedResultFilter>();
  	
  	@Value("${io.delimeat.processor.feed.downloadUri}")
  	private String downloadUriTemplate;
  	  	
  	private Config config;
      	
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
	 * @see io.delimeat.processor.ItemProcessor#process(java.lang.Object)
	 */
    @Transactional(TxType.REQUIRES_NEW)
	@Override
	public void process(Episode episode) throws Exception {
    	LOGGER.debug(String.format("starting feed item processor for %s", episode.getTitle()));
		
		// read feed results
		final List<FeedResult> readResults = feedService.read(episode.getShow().getTitle());
		LOGGER.debug(String.format("read %s results, %s", readResults.size(), readResults));
				
		filterFeedResults(readResults, episode, getConfig());
		LOGGER.debug(String.format("After filtering %s results, %s", readResults.size(), readResults));

		final List<FeedProcessUnit> processUnits = readResults.stream()
				.map(p->convertFeedResult(p))
				.collect(Collectors.toList());
		
		// select all the valid results based on the torrent files
		validateResultTorrents(processUnits, episode.getShow(), getConfig());
		//todo fix to display only valid results
		LOGGER.debug(String.format("validated %s results, %s", processUnits.size(), processUnits));
 		
		final List<FeedProcessUnit> validResults = processUnits.stream()
			.filter(p->p.getRejections().size()==0)
			.collect(Collectors.toList());
		
		Instant now = Instant.now();
        if (validResults.isEmpty() == false) {
            // select the best result
            final FeedProcessUnit result = selectBestResultTorrent(validResults);
            
            final Torrent torrent = result.getTorrent();
            LOGGER.debug("selected torrent: " + torrent);
    		
			torrentService.write(generateTorrentFileName(episode), torrent, getConfig());

			episode.setLastFeedUpdate(now);
			episode.setStatus(EpisodeStatus.FOUND);
		}
        
        
        episode.setLastFeedCheck(now);	   
		episodeService.update(episode);
		
    	LOGGER.debug(String.format("ending feed item processor for %s", episode.getTitle()));	
	}

    public void filterFeedResults(List<FeedResult> results, Episode episode, Config Config){
    	for(FeedResultFilter filter: feedResultFilters){
    		filter.filter(results, episode, Config);
    	}
    }
    
    public FeedProcessUnit convertFeedResult(FeedResult feedResult){

		FeedProcessUnit processUnit = new FeedProcessUnit();
		processUnit.setContentLength(feedResult.getContentLength());
		processUnit.setTitle(feedResult.getTitle());
		processUnit.setSeeders(feedResult.getSeeders());
		processUnit.setLeechers(feedResult.getLeechers());
		
		if(feedResult.getTorrentURL() != null && feedResult.getTorrentURL().length() > 0){
    		try{
    			processUnit.setDownloadUri(new URI(feedResult.getTorrentURL()));
    		}catch(URISyntaxException ex){
    			LOGGER.debug("Unable to parse uri {} for result {}", feedResult.getTorrentURL(), feedResult);
    		}
		} 
		
		if(feedResult.getInfoHashHex() != null && feedResult.getInfoHashHex().length() > 0){
			byte[] infoHashBytes = DelimeatUtils.hexToBytes(feedResult.getInfoHashHex());
			InfoHash infoHash = new InfoHash(infoHashBytes);
			processUnit.setInfoHash(infoHash);
		}else if(feedResult.getMagnetUri() != null && feedResult.getMagnetUri().length() > 0){
			InfoHash infoHash = buildInfoHashFromMagnet(feedResult.getMagnetUri());
			processUnit.setInfoHash(infoHash);
		} 
	 
		
		if(processUnit.getInfoHash() != null && processUnit.getDownloadUri() == null){
    		try{
    			URI uri = buildDownloadUri(processUnit.getInfoHash());
    			processUnit.setDownloadUri(uri);
    		}catch(URISyntaxException ex){
    			LOGGER.debug("Unable to parse uri {} for result {}", feedResult.getTorrentURL(), feedResult);
    		}
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
        
    	if(result.getDownloadUri() != null){
    		try{
	        	// construct the URI for the torrent
	        	URL url = result.getDownloadUri().toURL();
	        	URI uri = new URI(url.getProtocol(), null, url.getHost(), url.getPort(), url.getPath(), null, null);
	        	// fetch the torrent
	        	Torrent torrent = torrentService.read(uri);
	        	result.setTorrent(torrent);
	        	result.setInfoHash(torrent.getInfo().getInfoHash());
	        	return true;
            } catch (URISyntaxException | IOException | TorrentException ex) {
                LOGGER.error("encountered an error fetching torrent for " + result, ex);
            }
        }
    	result.getRejections().add(FeedProcessUnitRejection.UNNABLE_TO_GET_TORRENT);
    	return false;
        
    }

    /**
     * @param inResults
     * @param show
     * @param config
     * @return
     * @throws ValidationException
     * @throws ProcessorInteruptedException 
     */
    public void validateResultTorrents(List<FeedProcessUnit> results, Show show, Config config) {

        for (FeedProcessUnit result: results) {
    		    		
            if(fetchTorrent(result) ==  false){
            	continue;
            }

        	validateTorrent(result,show,config);    
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
        		.sorted(new Comparator<FeedProcessUnit>() {
					@Override
					public int compare(FeedProcessUnit o1, FeedProcessUnit o2) {
						return Long.compare(o2.getSeeders(), o1.getSeeders());
					}
				})
        		.findFirst()
        		.get();
    }
    
    /**
     * @param episode
     * @return
     */
    public String generateTorrentFileName(Episode episode){
    	final Show show = episode.getShow();
    	if(ShowType.DAILY.equals(show.getShowType())){
    		return String.format("%s_%s_%s.torrent", show.getTitle(), DateTimeFormatter.ISO_DATE.format(episode.getAirDate()), episode.getTitle());

    	}else{
    		return String.format("%s_%sx%s_%s.torrent", show.getTitle(), episode.getSeasonNum(), episode.getEpisodeNum(), episode.getTitle());
    	}
    	
    }
    
    public URI buildDownloadUri(InfoHash infoHash) throws URISyntaxException {
    	return new URI(String.format(downloadUriTemplate,infoHash.getHex().toUpperCase()));
    }
    
    public InfoHash buildInfoHashFromMagnet(String magnetUri){
    	Matcher m = Pattern.compile("([A-Z]|[a-z]|\\d){40}").matcher(magnetUri);
    	if(m.find() == false){
    		return null;
    	}
    	String infoHashHex = m.group();
    	byte[] infoHashBytes = DelimeatUtils.hexToBytes(infoHashHex);
    	return new InfoHash(infoHashBytes);
    }
    
  	/**
  	 * Get an instance of Config
  	 * @return config
  	 * @throws ConfigException
  	 */
  	public Config getConfig() throws ConfigException{
  		if(config == null){
  			config = configService.read();
  		}
  		return config;
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
				+ (config != null ? "config=" + config : "") + "]";
	}
  	
}
