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
package io.delimeat.processor.filter;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.delimeat.config.entity.Config;
import io.delimeat.feed.entity.FeedResult;
import io.delimeat.show.entity.Episode;

public abstract class AbstractFeedResultFilter implements FeedResultFilter {

  	protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
  	/**
  	 * Perform filtering
  	 * 
  	 * @param results
  	 * @param episode
  	 * @param config
  	 */
  	abstract void doFilter(List<FeedResult> results, Episode episode, Config config);
  	
	/* (non-Javadoc)
	 * @see io.delimeat.processor.filter.FeedResultFilter#filter(java.util.List, io.delimeat.show.domain.Episode, io.delimeat.config.domain.Config)
	 */
	@Override
	public void filter(List<FeedResult> results, Episode episode, Config config) {
		Objects.requireNonNull(results, "Results must not be null");
		Objects.requireNonNull(episode, "Episode must not be null");
		Objects.requireNonNull(config, "Config must not be null");
		LOGGER.trace("Start filtering\n{}\n{}\n{}", results,episode,config);
		
		doFilter(results,episode,config);
		
		LOGGER.trace("End filtering {}", results);		
	}

}
