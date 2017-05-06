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
@Order(5)
public class FeedResultMiniSeriesValidator_Impl extends AbstractFeedResultValidator implements FeedResultValidator {

	private static final String MINI_SERIES_REGEX = "\\d{2}(?=[Oo][Ff]\\d{2})";
	
	public FeedResultMiniSeriesValidator_Impl() {
		super(FeedResultRejection.INVALID_MINI_SERIES_RESULT);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.server.processor.validation.FeedResultValidator#validate(java.util.List, io.delimeat.common.show.model.Episode, io.delimeat.common.config.model.Config)
	 */
	@Override
	public void validate(List<FeedResult> results, Episode episode, Config config) throws ValidationException {
		if(ShowType.MINI_SERIES.equals(episode.getShow().getShowType()) == true){
			final int episodeNum = episode.getEpisodeNum();
	
			final Pattern pattern = Pattern.compile(MINI_SERIES_REGEX);
			Matcher matcher;
			String title;
			for (FeedResult result : results) {
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
				addRejection(result);
			}
		}

	}

}
