package io.delimeat.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.delimeat.core.config.Config;
import io.delimeat.core.config.ConfigDao;
import io.delimeat.core.config.ConfigException;
import io.delimeat.core.processor.Processor;
import io.delimeat.core.processor.ProcessorFactory;
import io.delimeat.core.processor.ProcessorListener;
import io.delimeat.core.processor.ProcessorThread;
import io.delimeat.core.show.Episode;
import io.delimeat.core.show.Show;
import io.delimeat.core.show.ShowDao;
import io.delimeat.core.show.ShowException;

public class ProcessorService_Impl implements ProcessorService, ProcessorListener {

  private ProcessorFactory guideProcessorFactory;
  private ProcessorFactory feedProcessorFactory;
  private ShowDao showDao;
  private ConfigDao configDao;
  private final List<Processor> processors = new ArrayList<Processor>();
  
  public void setGuideProcessorFactory(ProcessorFactory guideProcessorFactory){
    this.guideProcessorFactory = guideProcessorFactory;
  }

  public ProcessorFactory getGuideProcessorFactory(){
    return guideProcessorFactory;
  }

  public void setFeedProcessorFactory(ProcessorFactory feedProcessorFactory){
    this.feedProcessorFactory = feedProcessorFactory;
  }

  public ProcessorFactory getFeedProcessorFactory(){
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
  
  @Override
  public void processAllFeedUpdates() throws ConfigException, ShowException {
    final Config config = configDao.read();
    final List<Show> shows = showDao.readAll();
    final int searchInterval = config.getSearchInterval();
    final Date now = new Date();
    for(Show show: shows){
      if(show.getNextEpisode() != null ){
        Episode nextEp = show.getNextEpisode();
        TimeZone showTimeZone = TimeZone.getTimeZone(show.getTimezone());
        Long nextEpAirDateTime = nextEp.getAirDate().getTime() + showTimeZone.getRawOffset() + show.getAirTime();
        Date nextEpActualAirDate = new Date(nextEpAirDateTime);
        if(nextEpActualAirDate.before(now) == true){
          Processor processor = feedProcessorFactory.build(show, config); 
          processor.addListener(this);
          processors.add(processor);
			 ProcessorThread thread = new ProcessorThread(processor);
        	 // put it into the thread pool  
        }
      }
    }
  }

  @Override
  public void processAllGuideUpdates() {
    // TODO Auto-generated method stub

  }

  @Override
  public void alertComplete(Processor processor) {
     processors.remove(processor);
  }
  
  
}
