package io.delimeat.core.feed;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

import javax.transaction.Transactional;

import io.delimeat.core.config.Config;
import io.delimeat.core.feed.FeedProcessorStatus;
import io.delimeat.core.feed.validation.FeedResultValidator;
import io.delimeat.core.feed.validation.FeedValidationException;
import io.delimeat.core.feed.validation.TorrentValidator;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowDao;
import io.delimeat.core.show.ShowException;
import io.delimeat.core.show.ShowNotFoundException;
import io.delimeat.core.torrent.Torrent;
import io.delimeat.core.torrent.TorrentDao;
import io.delimeat.core.torrent.TorrentException;
import io.delimeat.util.DelimeatUtils;

public class FeedProcessor_Impl implements FeedProcessor {

    private static final Log          LOG    = LogFactory.getLog(FeedProcessor_Impl.class);

    private FeedProcessorStatus       status = FeedProcessorStatus.PENDING;
    private Exception 					  exception;

    private Show                      show;
    private Config		              config;
    private ShowDao                   showDao;
    private List<FeedDao>             feedDaos;
    private TorrentDao                torrentDao;
    private List<FeedResultValidator> feedResultValidators;
    private List<TorrentValidator>    torrentValidators;
    private Comparator<FeedResult>    resultComparator;
    private FeedResultWriter          feedResultWriter;

    public void setShow(Show show) {
        this.show = show;
    }

    public Show getShow() {
        return show;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public ShowDao getShowDao() {
        return showDao;
    }

    public void setShowDao(ShowDao showDao) {
        this.showDao = showDao;
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

    public List<FeedResultValidator> getFeedResultValidators() {
        return feedResultValidators;
    }

    public void setFeedResultValidators(List<FeedResultValidator> feedResultValidators) {
        this.feedResultValidators = feedResultValidators;
    }

    public List<TorrentValidator> getTorrentValidators() {
        return torrentValidators;
    }

    public void setTorrentValidators(List<TorrentValidator> torrentValidators) {
        this.torrentValidators = torrentValidators;
    }

    public Comparator<FeedResult> getResultComparator() {
        return resultComparator;
    }

    public void setResultComparator(Comparator<FeedResult> resultComparator) {
        this.resultComparator = resultComparator;
    }

    public FeedResultWriter getFeedResultWriter() {
        return feedResultWriter;
    }

    public void setFeedResultWriter(FeedResultWriter feedResultWriter) {
        this.feedResultWriter = feedResultWriter;
    }

    @Override
    public boolean abort() {
        if (status == FeedProcessorStatus.STARTED) {
            status = FeedProcessorStatus.ENDED_ABORT;
            return true;
        }
        return false;

    }

    @Override
    public FeedProcessorStatus getStatus() {
        return status;
    }
    
    public void setStatus(FeedProcessorStatus status) {
        this.status = status;
    }
  
    @Override  
    public Exception getException(){
      return exception;
    }

  	 @Transactional
    @Override
    public void process() {
    	try{
        final Show lockedShow = showDao.readAndLock(show.getShowId());
        // read feed results
        final List<FeedResult> readResults = fetchResults(lockedShow);
        // validate the read results
        final List<FeedResult> foundResults = validateFeedResults(readResults, lockedShow);
        // select all the valid results based on the torrent files
        final List<FeedResult> validResults = validateResultTorrents(foundResults, lockedShow, config);

        if (status == FeedProcessorStatus.STARTED) {
            // select the best result
            final FeedResult selectedResult = selectResult(validResults, config);

            // if something has been found and its valid output it
            if (selectedResult != null && selectedResult.getTorrent() != null && selectedResult.getTorrent().getInfo() != null
                && DelimeatUtils.isEmpty(selectedResult.getTorrent().getInfo().getName())) {
                
                final Torrent torrent = selectedResult.getTorrent();
                final String fileName = torrent.getInfo().getName();
                final byte[] bytes = torrent.getBytes();
                feedResultWriter.write(fileName, bytes, config);

                updateShow(lockedShow);
              	 status = FeedProcessorStatus.ENDED_SUCCESSFUL;
              	 return;
            }
        }
        status = FeedProcessorStatus.ENDED_UNSUCCESSFUL;
      }
      catch(Exception e){
        exception = e;
        status = FeedProcessorStatus.ENDED_ERROR;
      }
    }

    public List<FeedResult> fetchResults(Show show) {
        // read feed results
        final String title = show.getTitle();
        final List<FeedResult> readResults = new ArrayList<FeedResult>();
        final Iterator<FeedDao> feedDaoIterator = feedDaos.iterator();
        while (status == FeedProcessorStatus.STARTED && feedDaoIterator.hasNext()) {
            FeedDao feedDao = feedDaoIterator.next();
            try {
                List<FeedResult> results = feedDao.read(title);
                readResults.addAll(results);
            } catch (FeedException ex) {
                continue;
            }
        }
        return readResults;
    }

    public List<FeedResult> validateFeedResults(List<FeedResult> results, Show show) throws FeedValidationException {
        final Iterator<FeedResultValidator> resultValidatorItr = feedResultValidators.iterator();

        while (status == FeedProcessorStatus.STARTED && resultValidatorItr.hasNext()) {

            FeedResultValidator validator = resultValidatorItr.next();
            validator.validate(results, show);
        }

        // keep the valid results, ignore the rest
        final List<FeedResult> foundResults = new ArrayList<FeedResult>();
        for (FeedResult result : results) {
            if (DelimeatUtils.isCollectionEmpty(result.getFeedResultRejections())) {
                foundResults.add(result);
            }
        }

        return foundResults;
    }

    public Torrent fetchTorrent(FeedResult result) throws MalformedURLException, URISyntaxException, IOException, TorrentException {
        // construct the URI for the torrent
        URL url = new URL(result.getTorrentURL());
        URI uri = new URI(url.getProtocol(), null, url.getHost(), url.getPort(), url.getPath(), null, null);

        // fetch the torrent
        return torrentDao.read(uri);
    }

    public void validateTorrent(FeedResult result, Torrent torrent, Config config, Show show) throws FeedValidationException {

        final Iterator<TorrentValidator> torrentValidatorItr = torrentValidators.iterator();

        while (status == FeedProcessorStatus.STARTED && DelimeatUtils.isCollectionEmpty(result.getFeedResultRejections())
               && torrentValidatorItr.hasNext()) {

            TorrentValidator validator = torrentValidatorItr.next();

            if (validator.validate(torrent, show, config) == false) {
                result.getFeedResultRejections().add(validator.getRejection());
            }
        }

    }

    public List<FeedResult> validateResultTorrents(List<FeedResult> inResults, Show show, Config config) throws FeedValidationException {

        final List<FeedResult> outResults = new ArrayList<FeedResult>();
        final Iterator<FeedResult> foundResultsIterator = inResults.iterator();

        while (status == FeedProcessorStatus.STARTED && foundResultsIterator.hasNext()) {
            FeedResult result = foundResultsIterator.next();

            Torrent torrent;
            try {
                torrent = fetchTorrent(result);
            } catch (MalformedURLException ex) {
                LOG.error("encountered an error fetching torrent " + result.getTorrentURL(), ex);
                result.getFeedResultRejections().add(FeedResultRejection.UNNABLE_TO_GET_TORRENT);
                continue;
            } catch (URISyntaxException ex) {
                LOG.error("encountered an error fetching torrent " + result.getTorrentURL(), ex);
                result.getFeedResultRejections().add(FeedResultRejection.UNNABLE_TO_GET_TORRENT);
                continue;
            } catch (IOException ex) {
                result.getFeedResultRejections().add(FeedResultRejection.UNNABLE_TO_GET_TORRENT);
                continue;
            } catch (TorrentException ex) {
                result.getFeedResultRejections().add(FeedResultRejection.UNNABLE_TO_GET_TORRENT);
                continue;
            }
            result.setTorrent(torrent);

            // perform torrent validations
            validateTorrent(result, torrent, config, show);

            // if it aint rejected yet its valid!
            if (DelimeatUtils.isCollectionEmpty(result.getFeedResultRejections())) {
                outResults.add(result);
            }
        }
        return outResults;
    }

    public FeedResult selectResult(List<FeedResult> results, Config config) {
        FeedResult result = null;
        if (DelimeatUtils.isCollectionEmpty(results) == false) {
            Collections.sort(results, resultComparator);
            result = results.get(0);// get the first one
        }
        return result;
    }

    public void updateShow(Show show) throws ShowNotFoundException, ShowException {
        // set the next episode
        final Episode previousEp = show.getNextEpisode();
        final Episode nextEp = showDao.readEpisodeAfter(show.getShowId(), previousEp.getAirDate());
        show.setPreviousEpisode(previousEp);
        show.setNextEpisode(nextEp);
        show.setLastFeedUpdate(new Date());
        showDao.createOrUpdate(show);
    }

}
