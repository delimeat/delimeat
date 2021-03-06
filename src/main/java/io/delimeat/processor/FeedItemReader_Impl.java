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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import io.delimeat.config.ConfigService;
import io.delimeat.config.domain.Config;
import io.delimeat.show.EpisodeService;
import io.delimeat.show.ShowUtils;
import io.delimeat.show.domain.Episode;

@Component
@Scope("prototype")
public class FeedItemReader_Impl implements ItemReader<Episode> {

	@Autowired
	private EpisodeService episodeService;
	
	@Autowired
	private ConfigService configService;
	
	private List<Episode> episodes = null;
	
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

	/* (non-Javadoc)
	 * @see io.delimeat.processor.ItemReader#read()
	 */
	@Override
	public Episode read() throws Exception {			
		if(episodes == null){
			episodes = getEpisodes();		
		}
		
		try{
			return episodes.remove(0);
		}catch(IndexOutOfBoundsException ex){
			return null;
		}
	}
	
	private List<Episode> getEpisodes() throws Exception{
		final Instant now = Instant.now();
		final Config config = configService.read();		
		final long searchInterval = config.getSearchInterval();
		final long searchDelay = config.getSearchDelay();
		final Instant searchWindow = now.minusMillis(searchInterval);
		
		return episodeService.findAllPending()
										.stream()
										.filter(ep->ep.getShow().isEnabled())
										.filter(ep->Optional.ofNullable(ep.getLastFeedCheck())
												.orElse(Instant.EPOCH).isAfter(searchWindow) == false)
										.filter(ep->ShowUtils.determineAirTime(ep.getAirDate(), ep.getShow().getAirTime(), ep.getShow().getTimezone())
														.plusMillis(searchDelay).isBefore(now))
										.collect(Collectors.toList());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FeedItemReader_Impl [" + (episodeService != null ? "episodeService=" + episodeService + ", " : "")
				+ (configService != null ? "configService=" + configService + ", " : "")
				+ (episodes != null ? "episodes=" + episodes : "") + "]";
	}

}
