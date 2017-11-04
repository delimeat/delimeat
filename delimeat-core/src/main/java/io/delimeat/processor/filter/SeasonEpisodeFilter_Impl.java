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

import io.delimeat.config.entity.Config;
import io.delimeat.feed.entity.FeedResult;
import io.delimeat.show.entity.Episode;
import io.delimeat.show.entity.ShowType;

@Component
@Order(3)
public class SeasonEpisodeFilter_Impl extends AbstractFeedResultFilter implements FeedResultFilter {

	private static final String SEASON_REGEX = "(?<=[Ss]?)\\d{1,2}(?=[xXeE]\\d{1,2})";
	private static final String EPISODE_REGEX = "(?<=[Ss]?\\d{1,2}[eExX])\\d{1,2}";

	/* (non-Javadoc)
	 * @see io.delimeat.processor.filter.AbstractFeedResultFilter#doFilter(java.util.List, io.delimeat.show.domain.Episode, io.delimeat.config.domain.Config)
	 */
	@Override
	void doFilter(List<FeedResult> results, Episode episode, Config config) {
		// if its not a season episode do nothing
		if(ShowType.SEASON.equals(episode.getShow().getShowType()) != true){
			LOGGER.trace("Not a season show, ending");
			return;
		}
		
		
		final int seasonNum = episode.getSeasonNum();
		final int episodeNum = episode.getEpisodeNum();

		final Pattern seasonSelectPattern = Pattern.compile(SEASON_REGEX);
		final Pattern episodeSelectPattern = Pattern.compile(EPISODE_REGEX);
		Matcher seasonMatcher;
		Matcher episodeMatcher;
		String title;
		Iterator<FeedResult> iterator = results.iterator();
		while(iterator.hasNext()){
			FeedResult result = iterator.next();
			title = result.getTitle();
			if (title != null && seasonNum > 0 && episodeNum > 0) {
				seasonMatcher = seasonSelectPattern.matcher(title);
				episodeMatcher = episodeSelectPattern.matcher(title);

				if (seasonMatcher.find() && episodeMatcher.find()) {
					int resultSeasonNum = Integer.parseInt(seasonMatcher.group().trim());
					int resultEpisodeNum = Integer.parseInt(episodeMatcher.group().trim());
					if (resultSeasonNum == seasonNum && resultEpisodeNum == episodeNum) {
						continue;
					}
				}
			}
			
			iterator.remove();
			LOGGER.trace("Removing  {}", result);
		}
	}

}
