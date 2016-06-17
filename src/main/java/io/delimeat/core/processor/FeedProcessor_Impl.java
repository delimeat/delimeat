package io.delimeat.core.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import io.delimeat.core.feed.FeedDao;
import io.delimeat.core.feed.FeedException;
import io.delimeat.core.feed.FeedResult;
import io.delimeat.core.feed.FeedResultRejection;
import io.delimeat.core.processor.validation.FeedResultValidator;
import io.delimeat.core.processor.validation.ValidationException;
import io.delimeat.core.processor.validation.TorrentValidator;
import io.delimeat.core.processor.writer.TorrentWriter;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowDao;
import io.delimeat.core.show.ShowException;
import io.delimeat.core.torrent.Torrent;
import io.delimeat.core.torrent.TorrentDao;
import io.delimeat.core.torrent.TorrentException;
import io.delimeat.core.torrent.TorrentNotFoundException;
import io.delimeat.common.util.DelimeatUtils;

public class FeedProcessor_Impl extends AbstractProcessor implements Processor {

  	private static final Logger LOGGER = LoggerFactory.getLogger(FeedProcessor_Impl.class);

    private ShowDao showDao;
    private List<FeedDao> feedDaos;
    private TorrentDao torrentDao;
    private List<FeedResultValidator> feedResultValidators = new ArrayList<FeedResultValidator>(); 
    private List<TorrentValidator> torrentValidators = new ArrayList<TorrentValidator>();
    private Comparator<FeedResult> resultComparator;
    private TorrentWriter torrentWriter;

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

    public TorrentWriter getTorrentWriter() {
        return torrentWriter;
    }

    public void setTorrentWriter(TorrentWriter torrentWriter) {
        this.torrentWriter = torrentWriter;
    }
    
    @Transactional
    @Override
    public void process() throws ShowException, ValidationException, FeedException {
        if (active == false) {
            try {
                active = true;
                final Show lockedShow = showDao.readAndLock(show.getShowId());
                Date now = new Date();
				lockedShow.setLastFeedCheck(now);
                // read feed results
                final List<FeedResult> readResults = fetchResults(lockedShow);
              	 LOGGER.debug("read results: " + readResults.size());
              	 LOGGER.debug(readResults.toString());
                // validate the read results
                final List<FeedResult> foundResults = validateFeedResults(readResults, lockedShow, config);
              	 LOGGER.debug("found results: " + foundResults.size());
              	 LOGGER.debug(foundResults.toString());
                // select all the valid results based on the torrent files
                final List<FeedResult> validResults = validateResultTorrents(foundResults, lockedShow, config);
              	 LOGGER.debug("validated results: " + validResults.size());
              	 LOGGER.debug(validResults.toString());
                if (active == true && validResults.isEmpty() == false) {
                    // select the best result
                    final FeedResult selectedResult = selectResult(validResults, config);
                    LOGGER.debug("selected result: " + selectedResult);

					// if something has been found and its valid output it
					if (selectedResult != null
							&& selectedResult.getTorrent() != null
							&& selectedResult.getTorrent().getInfo() != null
							&& DelimeatUtils.isNotEmpty(selectedResult
									.getTorrent().getInfo().getName())) {

						final Torrent torrent = selectedResult.getTorrent();
						LOGGER.debug("writing torrent: " + torrent);
						final String fileName = torrent.getInfo().getName()+ ".torrent";
						final byte[] bytes = torrent.getBytes();
						torrentWriter.write(fileName, bytes, config);

						updateShow(lockedShow);
					}
				}
				if(active == true){
					showDao.createOrUpdate(lockedShow);
				}
			} finally {
				alertListenersComplete();
			}
		}

	}

    public List<FeedResult> fetchResults(Show show) {
        // read feed results
        final String title = show.getTitle();
        final List<FeedResult> readResults = new ArrayList<FeedResult>();
        final Iterator<FeedDao> feedDaoIterator = feedDaos.iterator();
        LOGGER.debug("Feed Daos: " + feedDaos);
        while (active == true && feedDaoIterator.hasNext()) {
            FeedDao feedDao = feedDaoIterator.next();
            LOGGER.debug("Reading from: "+ feedDao);
            try {
                List<FeedResult> results = feedDao.read(title);
                LOGGER.debug("found results: " + results);
                readResults.addAll(results);
            } catch (FeedException ex) {
                continue;
            }
        }
        return readResults;
    }

    public List<FeedResult> validateFeedResults(List<FeedResult> results, Show show, Config config) throws ValidationException {
        final Iterator<FeedResultValidator> resultValidatorItr = feedResultValidators.iterator();

        while (active == true && resultValidatorItr.hasNext()) {

            FeedResultValidator validator = resultValidatorItr.next();
            validator.validate(results, show, config);
        }

        // keep the valid results, ignore the rest
        final List<FeedResult> foundResults = new ArrayList<FeedResult>();
        for (FeedResult result : results) {
            if (DelimeatUtils.isEmpty(result.getFeedResultRejections())) {
                foundResults.add(result);
            }else{
              LOGGER.debug("rejected result validation: " + result);
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

    public void validateTorrent(FeedResult result, Torrent torrent, Config config, Show show) throws ValidationException {

        final Iterator<TorrentValidator> torrentValidatorItr = torrentValidators.iterator();

        while (active == true && DelimeatUtils.isEmpty(result.getFeedResultRejections()) && torrentValidatorItr.hasNext()) {

            TorrentValidator validator = torrentValidatorItr.next();

            if (validator.validate(torrent, show, config) == false) {
                result.getFeedResultRejections().add(validator.getRejection());
            }
        }

    }

    public List<FeedResult> validateResultTorrents(List<FeedResult> inResults, Show show, Config config) throws ValidationException {

        final List<FeedResult> outResults = new ArrayList<FeedResult>();
        final Iterator<FeedResult> foundResultsIterator = inResults.iterator();

        while (active == true && foundResultsIterator.hasNext()) {
            FeedResult result = foundResultsIterator.next();

            Torrent torrent;
            try {
                torrent = fetchTorrent(result);
            } catch (MalformedURLException ex) {
                LOGGER.error("encountered an error fetching torrent " + result.getTorrentURL(), ex);
                result.getFeedResultRejections().add(FeedResultRejection.UNNABLE_TO_GET_TORRENT);
                continue;
            } catch (URISyntaxException ex) {
                LOGGER.error("encountered an error fetching torrent " + result.getTorrentURL(), ex);
                result.getFeedResultRejections().add(FeedResultRejection.UNNABLE_TO_GET_TORRENT);
                continue;
            } catch (IOException ex) {
                result.getFeedResultRejections().add(FeedResultRejection.UNNABLE_TO_GET_TORRENT);
                continue;
            } catch (TorrentNotFoundException ex) {
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
            if (DelimeatUtils.isEmpty(result.getFeedResultRejections())) {
                outResults.add(result);
            }
          	else{
            	LOGGER.debug("rejected result torrent: " + result);
            }
        }
        return outResults;
    }

    public FeedResult selectResult(List<FeedResult> results, Config config) {
        FeedResult result = null;
        if (DelimeatUtils.isNotEmpty(results) == true) {
            Collections.sort(results, resultComparator);
            result = results.get(0);// get the first one
        }
        return result;
    }

    public void updateShow(Show show) throws ShowException {
        // set the next episode
        final Episode previousEp = show.getNextEpisode();
        Episode nextEp = showDao.readNextEpisode(previousEp);
        show.setPreviousEpisode(previousEp);
        show.setNextEpisode(nextEp);
        show.setLastFeedUpdate(new Date());
        show.setLastFeedCheck(null);
      
    }

	@Override
	public String toString() {
		return "FeedProcessor_Impl [show=" + show + ", config=" + config
				+ ", active=" + active + ", listeners=" + listeners 
				+ ", showDao=" + showDao + ", feedDaos="
				+ feedDaos + ", torrentDao=" + torrentDao
				+ ", feedResultValidators=" + feedResultValidators
				+ ", torrentValidators=" + torrentValidators
				+ ", resultComparator=" + resultComparator + ", torrentWriter="
				+ torrentWriter + "]";
	}

}
