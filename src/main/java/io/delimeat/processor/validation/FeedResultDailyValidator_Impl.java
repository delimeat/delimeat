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
package io.delimeat.processor.validation;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.ShowType;

@Component
@Order(4)
public class FeedResultDailyValidator_Impl extends AbstractFeedResultValidator implements FeedResultValidator {

	private static final String YEAR_REGEX = "\\d{4}(?=[\\s\\.]\\d{2}[\\s\\.]\\d{2})";
	private static final String MONTH_REGEX = "(?<=\\d{4}[\\s\\.])\\d{2}(?=[\\s\\.]\\d{2})";
	private static final String DAY_REGEX = "(?<=\\d{4}[\\s\\.]\\d{2}[\\s\\.])\\d{2}";

	public FeedResultDailyValidator_Impl(){
		super(FeedResultRejection.INVALID_DAILY_RESULT);
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.server.processor.validation.FeedResultValidator#validate(java.util.List, io.delimeat.common.show.model.Episode, io.delimeat.common.config.model.Config)
	 */
	@Override
	public void validate(List<FeedResult> results, Episode episode, Config config) throws ValidationException {
		if(ShowType.DAILY.equals(episode.getShow().getShowType()) == true){
			final LocalDate airDate = episode.getAirDate();
	
			final Pattern yearSelectPattern = Pattern.compile(YEAR_REGEX);
			final Pattern monthSelectPattern = Pattern.compile(MONTH_REGEX);
			final Pattern daySelectPattern = Pattern.compile(DAY_REGEX);
			Matcher yearMatcher;
			Matcher monthMatcher;
			Matcher dayMatcher;
			String title;
			for (FeedResult result : results) {
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
				addRejection(result);
			}
		}

	}

}
