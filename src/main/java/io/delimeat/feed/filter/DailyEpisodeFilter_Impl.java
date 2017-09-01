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
package io.delimeat.feed.filter;

import java.time.DateTimeException;
import java.time.LocalDate;
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
@Order(4)
public class DailyEpisodeFilter_Impl implements FeedResultFilter {

	private static final String YEAR_REGEX = "\\d{4}(?=[\\s\\.]\\d{2}[\\s\\.]\\d{2})";
	private static final String MONTH_REGEX = "(?<=\\d{4}[\\s\\.])\\d{2}(?=[\\s\\.]\\d{2})";
	private static final String DAY_REGEX = "(?<=\\d{4}[\\s\\.]\\d{2}[\\s\\.])\\d{2}";
	
	/* (non-Javadoc)
	 * @see io.delimeat.feed.filter.FeedResultFilter#filter(java.util.List, io.delimeat.show.domain.Episode, io.delimeat.config.domain.Config)
	 */
	@Override
	public void filter(List<FeedResult> results, Episode episode, Config config){
		// if its not a daily show don't bother 
		if(ShowType.DAILY.equals(episode.getShow().getShowType()) == false){
			return;
		}
		
		final LocalDate airDate = episode.getAirDate();
		
		final Pattern yearSelectPattern = Pattern.compile(YEAR_REGEX);
		final Pattern monthSelectPattern = Pattern.compile(MONTH_REGEX);
		final Pattern daySelectPattern = Pattern.compile(DAY_REGEX);
		Matcher yearMatcher;
		Matcher monthMatcher;
		Matcher dayMatcher;
		String title;
		Iterator<FeedResult> iterator = results.iterator();
		while(iterator.hasNext()){
			FeedResult result = iterator.next();
			title = result.getTitle();
			if (title != null && airDate != null) {
				yearMatcher = yearSelectPattern.matcher(title);
				monthMatcher = monthSelectPattern.matcher(title);
				dayMatcher = daySelectPattern.matcher(title);
				if (yearMatcher.find() && monthMatcher.find() && dayMatcher.find()) {
					int year = Integer.valueOf(yearMatcher.group());
					int month = Integer.valueOf(monthMatcher.group());
					int day = Integer.valueOf(dayMatcher.group());
					try {
						LocalDate resultAirDate = LocalDate.of(year, month, day);
						if (airDate.equals(resultAirDate)) {
							continue;
						}
					} catch (DateTimeException ex) {
						// do nothing
					}
				}
			}
			iterator.remove();
		}

	}

}
