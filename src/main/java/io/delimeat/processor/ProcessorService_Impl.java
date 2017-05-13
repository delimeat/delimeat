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

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import io.delimeat.config.ConfigService;
import io.delimeat.config.domain.Config;
import io.delimeat.guide.exception.GuideException;
import io.delimeat.show.EpisodeService;
import io.delimeat.show.ShowService;
import io.delimeat.show.ShowUtils;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.Show;

@Service
public class ProcessorService_Impl implements ProcessorService,
		ProcessorListener {

  	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorService_Impl.class);
	
	private final List<Processor> processors = new ArrayList<>();

  	@Autowired
	private GuideProcessorFactory guideProcessorFactory;
  	@Autowired
	private FeedProcessorFactory feedProcessorFactory;
	@Autowired
	private ShowService showService;
	@Autowired
	private EpisodeService episodeService;
	@Autowired
	private ConfigService configService;
	@Autowired
	@Qualifier("processorExecutorId")
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
	@Scheduled(fixedDelayString="${io.delimeat.processor.feed.schedule}", initialDelayString="${io.delimeat.processor.feed.schedule.initial}")
	public void processAllFeedUpdates() throws GuideException {

		final Instant now = Instant.now();
		final List<Episode> episodes = episodeService.findAllPending()
										.stream()
										.filter(ep->ep.getShow().isEnabled())
										.collect(Collectors.toList());
		
		// stop is there is nothing to process
		if(episodes.isEmpty()){
			return;
		}
		
		final Config config = configService.read();		
		final long searchInterval = config.getSearchInterval();
		final long searchDelay = config.getSearchDelay();
		final Instant searchWindow = now.minusMillis(searchInterval);
		for (Episode episode : episodes) {
			
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
	@Scheduled(fixedDelayString="${io.delimeat.processor.guide.schedule}", initialDelayString="${io.delimeat.processor.guide.schedule.initial}")
	public void processAllGuideUpdates()  throws Exception {
		
		final List<Show> shows = showService.readAll()
									.stream()
									.filter(show->show.isEnabled())
									.collect(Collectors.toList());
		
		// stop if there is nothing to process
		if(shows.isEmpty()){
			return;
		}
		
		final Config config = configService.read();
		
		for (Show show : shows) {	
			
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
