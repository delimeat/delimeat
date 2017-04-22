package io.delimeat.processor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.delimeat.common.util.exception.EntityException;
import io.delimeat.config.ConfigService;
import io.delimeat.config.domain.Config;
import io.delimeat.show.EpisodeService;
import io.delimeat.show.ShowService;
import io.delimeat.show.ShowUtils;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.Show;

public class ProcessorService_Impl implements ProcessorService,
		ProcessorListener {

  	private static final Logger LOGGER = LoggerFactory.getLogger(FeedProcessor_Impl.class);
	
	private GuideProcessorFactory guideProcessorFactory;
	private FeedProcessorFactory feedProcessorFactory;
	private ShowService showService;
	private EpisodeService episodeService;
	private ConfigService configService;
	private final List<Processor> processors = new ArrayList<Processor>();
	private Executor executor;

	public void setGuideProcessorFactory(GuideProcessorFactory guideProcessorFactory) {
		this.guideProcessorFactory = guideProcessorFactory;
	}

	public GuideProcessorFactory getGuideProcessorFactory() {
		return guideProcessorFactory;
	}

	public void setFeedProcessorFactory(FeedProcessorFactory feedProcessorFactory) {
		this.feedProcessorFactory = feedProcessorFactory;
	}

	public FeedProcessorFactory getFeedProcessorFactory() {
		return feedProcessorFactory;
	}


	public ShowService getShowService() {
		return showService;
	}

	public void setShowService(ShowService showService) {
		this.showService = showService;
	}

	public EpisodeService getEpisodeService() {
		return episodeService;
	}

	public void setEpisodeService(EpisodeService episodeService) {
		this.episodeService = episodeService;
	}

	public ConfigService getConfigService() {
		return configService;
	}

	public void setConfigService(ConfigService configService) {
		this.configService = configService;
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
	public void processAllFeedUpdates() throws EntityException {

		final Instant now = Instant.now();
		final List<Episode> episodes = episodeService.findAllPending();		
		final Config config = configService.read();		
		final long searchInterval = config.getSearchInterval();
		final long searchDelay = config.getSearchDelay();
		final Instant searchWindow = now.minusMillis(searchInterval);
		for (Episode episode : episodes) {
			if(episode.getShow().isEnabled() == false){
				continue;
			}
			
			final Instant lastFeedCheck = Optional.ofNullable(episode.getLastFeedCheck())
												.orElse(Instant.EPOCH);
			
			if(lastFeedCheck.isAfter(searchWindow) == true){
				continue;
			}
			
			Instant delayedAirDateTime = ShowUtils.determineAirTime(episode.getAirDate(), episode.getShow().getAirTime(), episode.getShow().getTimezone())
														.plusMillis(searchDelay);

           	if (delayedAirDateTime.isBefore(now) == true) {
				Processor processor = feedProcessorFactory.build(episode,config);
				processor.addListener(this);
				processors.add(processor);
				executor.execute(new ProcessorThread(processor,LOGGER));
			}
		}
	}

	@Override
	public void processAllGuideUpdates()  throws Exception {
		
		final List<Show> shows = showService.readAll();
		final Config config = configService.read();
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
