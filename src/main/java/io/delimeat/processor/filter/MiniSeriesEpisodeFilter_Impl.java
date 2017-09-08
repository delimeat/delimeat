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

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResult;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.ShowType;

@Component
@Order(5)
public class MiniSeriesEpisodeFilter_Impl extends AbstractFeedResultFilter implements FeedResultFilter {

	private static final String MINI_SERIES_REGEX = "\\d{2}(?=[Oo][Ff]\\d{2})";

	/* (non-Javadoc)
	 * @see io.delimeat.processor.filter.AbstractFeedResultFilter#doFilter(java.util.List, io.delimeat.show.domain.Episode, io.delimeat.config.domain.Config)
	 */
	@Override
	void doFilter(List<FeedResult> results, Episode episode, Config config) {
		// if its not a mini series don't bother 
		if(ShowType.MINI_SERIES.equals(episode.getShow().getShowType()) != true){
			LOGGER.trace("Not a mini series, ending");
			return;
		}
		
		final int episodeNum = episode.getEpisodeNum();

		final Pattern pattern = Pattern.compile(MINI_SERIES_REGEX);
		Matcher matcher;
		String title;
		Iterator<FeedResult> iterator = results.iterator();
		while(iterator.hasNext()){
			FeedResult result = iterator.next();
			title = result.getTitle();
			if (title != null && episodeNum > 0) {
				matcher = pattern.matcher(title);
				if (matcher.find()) {
					int resultEpisodeNum = Integer.parseInt(matcher.group());
					if (resultEpisodeNum == episodeNum) {
						continue;
					}
				}
			}
			iterator.remove();
			LOGGER.trace("Removing  {}", result);
		}
		
	}

}
