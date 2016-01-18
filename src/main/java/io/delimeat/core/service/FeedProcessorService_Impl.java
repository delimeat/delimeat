package io.delimeat.core.service;

import io.delimeat.core.config.Config;
import io.delimeat.core.config.ConfigException;
import io.delimeat.core.feed.FeedDao;
import io.delimeat.core.feed.FeedException;
import io.delimeat.core.feed.FeedProcessor;
import io.delimeat.core.feed.FeedProcessorStatus;
import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.feed.FeedResultWriter;
import io.delimeat.core.feed.validation.FeedResultValidator;
import io.delimeat.core.feed.validation.FeedValidationException;
import io.delimeat.core.feed.validation.TorrentValidator;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowException;
import io.delimeat.core.torrent.Torrent;
import io.delimeat.core.torrent.TorrentDao;
import io.delimeat.core.torrent.TorrentException;
import io.delimeat.util.DelimeatUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FeedProcessorService_Impl implements FeedProcessorService {

	private static final Log LOG = LogFactory.getLog(FeedProcessorService_Impl.class);
	
	private ConfigService configService;
	private ShowService showService;
	private GuideService guideService;
	private List<FeedDao> feedDaos;
	private TorrentDao torrentDao;
	private List<FeedResultValidator> dailyFeedResultValidators;
	private List<FeedResultValidator> seasonFeedResultValidators;
	private List<TorrentValidator> torrentValidators;
	private TorrentValidator folderTorrentValidator;
	private Comparator<FeedResult> preferFilesComparator;
	private Comparator<FeedResult> maxSeedersComparator;
	private FeedResultWriter feedResultWriter;

	public ConfigService getConfigService() {
		return configService;
	}

	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}

	public ShowService getShowService() {
		return showService;
	}

	public void setShowService(ShowService showService) {
		this.showService = showService;
	}

	public GuideService getGuideService() {
		return guideService;
	}

	public void setGuideService(GuideService guideService) {
		this.guideService = guideService;
	}

	public List<FeedDao> getFeedDaos() {
		return feedDaos;
	}

	public void setFeedDaos(List<FeedDao> feedDaos) {
		this.feedDaos = feedDaos;
	}

	public TorrentDao getTorrentDao() {
		return torrentDao;
	}

	public void setTorrentDao(TorrentDao torrentDao) {
		this.torrentDao = torrentDao;
	}

	public List<FeedResultValidator> getDailyFeedResultValidators() {
		return dailyFeedResultValidators;
	}

	public void setDailyFeedResultValidators(
			List<FeedResultValidator> dailyFeedResultValidators) {
		this.dailyFeedResultValidators = dailyFeedResultValidators;
	}

	public List<FeedResultValidator> getSeasonFeedResultValidators() {
		return seasonFeedResultValidators;
	}

	public void setSeasonFeedResultValidators(
			List<FeedResultValidator> seasonFeedResultValidators) {
		this.seasonFeedResultValidators = seasonFeedResultValidators;
	}

	public List<TorrentValidator> getTorrentValidators() {
		return torrentValidators;
	}

	public void setTorrentValidators(List<TorrentValidator> torrentValidators) {
		this.torrentValidators = torrentValidators;
	}

	public TorrentValidator getFolderTorrentValidator() {
		return folderTorrentValidator;
	}

	public void setFolderTorrentValidator(TorrentValidator folderTorrentValidator) {
		this.folderTorrentValidator = folderTorrentValidator;
	}

	public Comparator<FeedResult> getPreferFilesComparator() {
		return preferFilesComparator;
	}

	public void setPreferFilesComparator(
			Comparator<FeedResult> preferFilesComparator) {
		this.preferFilesComparator = preferFilesComparator;
	}

	public Comparator<FeedResult> getMaxSeedersComparator() {
		return maxSeedersComparator;
	}

	public void setMaxSeedersComparator(Comparator<FeedResult> maxSeedersComparator) {
		this.maxSeedersComparator = maxSeedersComparator;
	}

	public FeedResultWriter getFeedResultWriter() {
		return feedResultWriter;
	}

	public void setFeedResultWriter(FeedResultWriter feedResultWriter) {
		this.feedResultWriter = feedResultWriter;
	}

	//TODO add test case
	@Override
	public boolean process(FeedProcessor processor, Show show) throws ConfigException, FeedException, ShowException {
		//TODO make transactional & lock the show
      // read feed results
		final List<FeedResult> readResults = fetchResults(processor, show);
		// validate the read results
		final List<FeedResult> foundResults = validateFeedResults(processor, show, readResults);
		// get the config
		final Config config = configService.read();
		// select all the valid results based on the torrent files
		final List<FeedResult> validResults = validateResultTorrents(processor, foundResults, config, show);
		
		if(processor.getStatus() == FeedProcessorStatus.STARTED){
         // select the best result
			final FeedResult selectedResult =  selectResult(validResults,config);
			
			// if something has been found and its valid output it
			if(selectedResult != null && selectedResult.getTorrent() != null 
					&& selectedResult.getTorrent().getInfo() != null
					&& DelimeatUtils.isEmpty(selectedResult.getTorrent().getInfo().getName()) )
			{
               final Torrent torrent = selectedResult.getTorrent();
               final String fileName = torrent.getInfo().getName();
               feedResultWriter.write(fileName, torrent.getBytes(), config);

               // try setting the next episode
               //TODO maybe need to include functionality for double eps
               show.setPreviousEpisode(show.getNextEpisode());
               show.setNextEpisode(null);
               show.setLastFeedUpdate(new Date());
               showService.update(show); //maybe add in catch for concurrent updates???

               processor.setFoundResults(foundResults);
               processor.setSelectedResult(selectedResult);
               return true;
         }
		}
		return false;

	}
	
	public List<FeedResult> fetchResults(FeedProcessor processor, Show show){
		// read feed results
		final String title = show.getTitle();
		final List<FeedResult> readResults = new ArrayList<FeedResult>();
		final Iterator<FeedDao> feedDaoIterator = feedDaos.iterator();
		while(processor.getStatus() == FeedProcessorStatus.STARTED 
				&& feedDaoIterator.hasNext()){
			FeedDao feedDao = feedDaoIterator.next();
			try{
				List<FeedResult> results = feedDao.read(title);
				readResults.addAll(results);
			}catch(FeedException ex){
				continue;
			}
		}
		return readResults;
	}
	
	public List<FeedResult> validateFeedResults(FeedProcessor processor, Show show, List<FeedResult> results) throws FeedValidationException{
		final Iterator<FeedResultValidator> resultValidatorIterator;
		switch(show.getShowType()){
			case DAILY:
				resultValidatorIterator = dailyFeedResultValidators.iterator();
			break;
			default:
				resultValidatorIterator = seasonFeedResultValidators.iterator();	
		}
		while(processor.getStatus() == FeedProcessorStatus.STARTED 
				&& resultValidatorIterator.hasNext()){
			FeedResultValidator validator = resultValidatorIterator.next();
			validator.validate(results, show);
		}
		
		// keep the valid results, ignore the rest
		final List<FeedResult> foundResults = new ArrayList<FeedResult>();
		for(FeedResult result: results){
			if( DelimeatUtils.isCollectionEmpty(result.getFeedResultRejections()) ){
				foundResults.add(result);
			}
		}
		return foundResults;
	}
	
	public Torrent fetchTorrent(FeedResult result) throws MalformedURLException,URISyntaxException,IOException,TorrentException{
		// construct the URI for the torrent
		URL url = new URL(result.getTorrentURL());
		URI uri = new URI(url.getProtocol(),null, url.getHost(),url.getPort(), url.getPath(),null,null);
		
		// fetch the torrent
		return torrentDao.read(uri);
	}
	
	public void validateTorrent(FeedProcessor processor, FeedResult result, Torrent torrent, Config config, Show show) throws FeedValidationException{
		// perform torrent validations
		final List<TorrentValidator> validators = new ArrayList<TorrentValidator>();
		validators.addAll(torrentValidators);
		if(config.isIgnoreFolders()){
			validators.add(folderTorrentValidator);
		}
		final Iterator<TorrentValidator> torrentValidatorIterator = validators.iterator();
		while( processor.getStatus() == FeedProcessorStatus.STARTED 
				&& DelimeatUtils.isCollectionEmpty(result.getFeedResultRejections())
				&& torrentValidatorIterator.hasNext() ){
			TorrentValidator validator = torrentValidatorIterator.next();
			if(validator.validate(torrent, show, config) == false){
				result.getFeedResultRejections().add(validator.getRejection());
			}
		}
	}
	
	public FeedResult selectResult(List<FeedResult> results, Config config){
		FeedResult result = null;
		if( DelimeatUtils.isCollectionEmpty(results) == false){
			final Comparator<FeedResult> comparator;
			if(config.isPreferFiles()){
				comparator = preferFilesComparator;
			}else{
				comparator = maxSeedersComparator;
			}
			Collections.sort(results, comparator);
			result =  results.get(0);// get the first one
		}
		return result;
	}
  
  public List<FeedResult> validateResultTorrents(FeedProcessor processor, List<FeedResult> inResults, Config config, Show show) throws FeedValidationException{
    	final List<FeedResult> outResults = new ArrayList<FeedResult>();
		final Iterator<FeedResult> foundResultsIterator = inResults.iterator();
		while(processor.getStatus() == FeedProcessorStatus.STARTED 
				&& foundResultsIterator.hasNext()){
			FeedResult result = foundResultsIterator.next();
			
			Torrent torrent;
			try{			
				torrent = fetchTorrent(result);
			}catch(MalformedURLException ex){
				LOG.error("encountered an error fetching torrent " + result.getTorrentURL(), ex);
				result.getFeedResultRejections().add(FeedResultRejection.UNNABLE_TO_GET_TORRENT);	
				continue;
			}catch(URISyntaxException ex){
				LOG.error("encountered an error fetching torrent " + result.getTorrentURL(), ex);
				result.getFeedResultRejections().add(FeedResultRejection.UNNABLE_TO_GET_TORRENT);
				continue;
			}catch(IOException ex){
				result.getFeedResultRejections().add(FeedResultRejection.UNNABLE_TO_GET_TORRENT);
				continue;					
			}catch(TorrentException ex){
				result.getFeedResultRejections().add(FeedResultRejection.UNNABLE_TO_GET_TORRENT);
				continue;					
			}
			result.setTorrent(torrent);
			
			// perform torrent validations
			validateTorrent(processor,result,torrent,config,show);
			
			// if it aint rejected yet its valid!
			if( DelimeatUtils.isCollectionEmpty(result.getFeedResultRejections()) ){
				outResults.add(result);
			}
		}
    return outResults;
  }
}
