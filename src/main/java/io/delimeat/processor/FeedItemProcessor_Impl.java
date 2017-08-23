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
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.processor.domain.FeedProcessUnit;
import io.delimeat.processor.validation.FeedResultValidator;
import io.delimeat.processor.validation.TorrentValidator;
import io.delimeat.processor.validation.ValidationException;
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
    private List<FeedResultValidator> feedResultValidators = new ArrayList<FeedResultValidator>(); 
  	@Autowired
    private List<TorrentValidator> torrentValidators = new ArrayList<TorrentValidator>();
  	
  	@Value("${io.delimeat.processor.feed.magnetUri}")
  	private String magnetUriTemplate;
  	
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
	 * @return the feedResultValidators
	 */
	public List<FeedResultValidator> getFeedResultValidators() {
		return feedResultValidators;
	}

	/**
	 * @param feedResultValidators the feedResultValidators to set
	 */
	public void setFeedResultValidators(List<FeedResultValidator> feedResultValidators) {
		this.feedResultValidators = feedResultValidators;
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
	 * @return the magnetUriTemplate
	 */
	public String getMagnetUriTemplate() {
		return magnetUriTemplate;
	}

	/**
	 * @param magnetUriTemplate the magnetUriTemplate to set
	 */
	public void setMagnetUriTemplate(String magnetUriTemplate) {
		this.magnetUriTemplate = magnetUriTemplate;
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
		
		// validate the read results
		final List<FeedResult> foundResults = validateFeedResults(readResults, episode, getConfig());
		LOGGER.debug(String.format("found %s results, %s", foundResults.size(), foundResults));
		
		// select all the valid results based on the torrent files
		final List<FeedResult> validResults = validateResultTorrents(foundResults, episode.getShow(), getConfig());
		LOGGER.debug(String.format("validated %s results, %s", validResults.size(), validResults));
 		
		Instant now = Instant.now();
        if (validResults.isEmpty() == false) {
            // select the best result
            final Torrent torrent = selectBestResultTorrent(validResults);
            LOGGER.debug("selected torrent: " + torrent);
    		
			torrentService.write(generateTorrentFileName(episode), torrent, getConfig());

			episode.setLastFeedUpdate(now);
			episode.setStatus(EpisodeStatus.FOUND);
		}
        
        
        episode.setLastFeedCheck(now);	   
		episodeService.update(episode);
		
    	LOGGER.debug(String.format("ending feed item processor for %s", episode.getTitle()));	
	}

    public List<FeedProcessUnit> convertFeedResults(List<FeedResult> feedResults){
    	List<FeedProcessUnit> processUnits = new ArrayList<FeedProcessUnit>();
    	for(FeedResult feedResult: feedResults){
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
    		
    		if(feedResult.getMagnetUri() != null && feedResult.getMagnetUri().length() > 0){
	    		try{
	    			processUnit.setMagnetUri(new URI(feedResult.getMagnetUri()));
	    		}catch(URISyntaxException ex){
	    			LOGGER.debug("Unable to parse uri {} for result {}", feedResult.getMagnetUri(), feedResult);
	    		}
    		}  
    		
    		if(feedResult.getInfoHashHex() != null && feedResult.getInfoHashHex().length() > 0){
    			byte[] infoHashBytes = DelimeatUtils.hexToBytes(feedResult.getInfoHashHex());
    			InfoHash infoHash = new InfoHash(infoHashBytes);
    			processUnit.setInfoHash(infoHash);
    		}
    		
    		if (processUnit.getMagnetUri() != null && processUnit.getInfoHash() == null){
    			InfoHash infoHash = buildInfoHashFromMagnet(processUnit.getMagnetUri());
    			processUnit.setInfoHash(infoHash);
     		} else if(processUnit.getInfoHash() != null && processUnit.getMagnetUri() == null){
     			URI uri = buildMagnetUri(processUnit.getInfoHash());
     			processUnit.setMagnetUri(uri);
    		} 
    		
    		if(processUnit.getInfoHash() != null && processUnit.getDownloadUri() == null){
    			URI uri = buildDownloadUri(processUnit.getInfoHash());
    			processUnit.setDownloadUri(uri);
    		}
    		
    		processUnits.add(processUnit);
    		
    	}
    	
    	return processUnits;
    }
    /**
     * @param results
     * @param episode
     * @param config
     * @return
     * @throws ValidationException
     * @throws ProcessorInteruptedException
     */
    public List<FeedResult> validateFeedResults(List<FeedResult> results, Episode episode, Config config) throws ValidationException {
    	
    	for(FeedResultValidator validator: feedResultValidators){
    		    		
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
    public Optional<FeedResultRejection> validateTorrent(Torrent torrent, Config config, Show show) throws ValidationException {
    	Optional<FeedResultRejection> result = Optional.empty();
    	for(TorrentValidator validator: torrentValidators){
    		    		
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
    public List<FeedResult> validateResultTorrents(List<FeedResult> results, Show show, Config config) throws ValidationException {

        for (FeedResult result: results) {
    		    		
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
        		.sorted(new Comparator<FeedResult>() {
					@Override
					public int compare(FeedResult o1, FeedResult o2) {
						return Long.compare(o2.getSeeders(), o1.getSeeders());
					}
				})
        		.findFirst()
        		.get()
        		.getTorrent();
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
    
    public URI buildMagnetUri(InfoHash infoHash){
		URI uri = null;
    	String magnetUri = String.format(magnetUriTemplate, infoHash.getHex());
		try{
			uri = new URI(magnetUri);
		}catch(URISyntaxException ex){
			LOGGER.debug("Unable to parse magnet uri {} for infoHash {}", magnetUri, infoHash.getHex());
		}
		return uri;
    }
    
    public URI buildDownloadUri(InfoHash infoHash){
		URI uri = null;
    	String downloadUri = String.format(downloadUriTemplate,infoHash.getHex().toUpperCase());
		try{
			uri = new URI(downloadUri);
		}catch(URISyntaxException ex){
			LOGGER.debug("Unable to parse download uri {} for infoHash {}", downloadUri, infoHash.getHex());
		}
		return uri;
    }
    
    public InfoHash buildInfoHashFromMagnet(URI magnetUri){
    	Matcher m = Pattern.compile("([A-Z]|[a-z]|\\d){40}").matcher(magnetUri.toASCIIString());
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
				+ (feedResultValidators != null ? "feedResultValidators=" + feedResultValidators + ", " : "")
				+ (torrentValidators != null ? "torrentValidators=" + torrentValidators + ", " : "")
				+ (config != null ? "config=" + config : "") + "]";
	}
  	
}
