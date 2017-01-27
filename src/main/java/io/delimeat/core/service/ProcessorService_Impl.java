package io.delimeat.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.delimeat.core.config.Config;
import io.delimeat.core.config.ConfigDao;
import io.delimeat.core.config.ConfigException;
import io.delimeat.core.processor.FeedProcessor_Impl;
import io.delimeat.core.processor.Processor;
import io.delimeat.core.processor.ProcessorFactory;
import io.delimeat.core.processor.ProcessorListener;
import io.delimeat.core.processor.ProcessorThread;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowDao;
import io.delimeat.core.show.ShowException;

public class ProcessorService_Impl implements ProcessorService,
		ProcessorListener {

  	private static final Logger LOGGER = LoggerFactory.getLogger(FeedProcessor_Impl.class);
	
	private ProcessorFactory guideProcessorFactory;
	private ProcessorFactory feedProcessorFactory;
	private ShowDao showDao;
	private ConfigDao configDao;
	private final List<Processor> processors = new ArrayList<Processor>();
	private Executor executor;

	public void setGuideProcessorFactory(ProcessorFactory guideProcessorFactory) {
		this.guideProcessorFactory = guideProcessorFactory;
	}

	public ProcessorFactory getGuideProcessorFactory() {
		return guideProcessorFactory;
	}

	public void setFeedProcessorFactory(ProcessorFactory feedProcessorFactory) {
		this.feedProcessorFactory = feedProcessorFactory;
	}

	public ProcessorFactory getFeedProcessorFactory() {
		return feedProcessorFactory;
	}

	public ShowDao getShowDao() {
		return showDao;
	}

	public void setShowDao(ShowDao showDao) {
		this.showDao = showDao;
	}

	public ConfigDao getConfigDao() {
		return configDao;
	}

	public void setConfigDao(ConfigDao configDao) {
		this.configDao = configDao;
	}

	public Executor getExecutor() {
		return executor;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}
  
  	public List<Processor> getProcessors(){
   	return processors;
   }

	@Override
	public void processAllFeedUpdates() throws ConfigException, ShowException {

		final Date now = new Date();
		final List<Show> shows = showDao.readAll();
		if(shows == null || shows.isEmpty()){
			return;
		}
		
		final Config config = configDao.read();		
		final long searchInterval = config.getSearchInterval();
		final long searchDelay = config.getSearchDelay();
		final Date searchWindow = new Date(now.getTime() - searchInterval);
		for (Show show : shows) {
			if(show.isEnabled() == false){
				continue;
			}
			
			if(show.getNextEpisode() == null){
				continue;
			}
			
			final Date lastFeedCheck;
			if(show.getLastFeedCheck() != null){
				lastFeedCheck = show.getLastFeedCheck();
			}else{
				lastFeedCheck = new Date(0);
			}
			
			if(lastFeedCheck.after(searchWindow) == true){
				continue;
			}
			
			Episode nextEp = show.getNextEpisode();
            // horrible timezone hack
            String timezone = show.getTimezone();
            TimeZone showTz = TimeZone.getTimeZone(timezone);
           	int showTzOffset = showTz.getRawOffset();
           	TimeZone localTz = TimeZone.getDefault();
           	int localTzOffset = localTz.getRawOffset();        	
           	Date nextEpAirDateTime = new Date(nextEp.getAirDate().getTime() + show.getAirTime() - showTzOffset + localTzOffset + searchDelay);
           	if (nextEpAirDateTime.before(now) == true) {
				Processor processor = feedProcessorFactory.build(show,config);
				processor.addListener(this);
				processors.add(processor);
				executor.execute(new ProcessorThread(processor,LOGGER));
			}
		}
	}

	@Override
	public void processAllGuideUpdates() throws ConfigException, ShowException {
		
		final List<Show> shows = showDao.readAll();
		if(shows == null || shows.isEmpty() ){
			return;
		}
		
		final Config config = configDao.read();
		for (Show show : shows) {	
		
			if(show.isEnabled() == false){
				continue;
			}
			
			Processor processor = guideProcessorFactory.build(show,config);
			processor.addListener(this);
			processors.add(processor);
           	executor.execute(new ProcessorThread(processor,LOGGER));
		}

	}

	@Override
	public void alertComplete(Processor processor) {
		processors.remove(processor);
		processor.removeListener(this);
	}

}
