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
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.delimeat.config.entity.Config;
import io.delimeat.feed.entity.FeedResult;
import io.delimeat.show.entity.Episode;

@Component
@Order(2)
public class ExcludedKeywordFilter_Impl extends AbstractFeedResultFilter implements FeedResultFilter {


	/* (non-Javadoc)
	 * @see io.delimeat.processor.filter.AbstractFeedResultFilter#doFilter(java.util.List, io.delimeat.show.domain.Episode, io.delimeat.config.domain.Config)
	 */
	@Override
	void doFilter(List<FeedResult> results, Episode episode, Config config) {
		List<String> keywords = config.getExcludedKeywords();
		if (keywords == null || keywords.isEmpty()) {
			LOGGER.trace("No excluded keywords, ending");
			return;
		}
		
		String regex = "(";
		for(int i = 0; i < keywords.size(); i++){
			if(i>0){
				regex += "|";
			}
			regex += keywords.get(i);
		}
		regex += ")";
		regex = regex.toLowerCase();

		LOGGER.trace("Using regex {}",regex);
		
		final Pattern pattern = Pattern.compile(regex);
		String title;
		Iterator<FeedResult> iterator = results.iterator();
		while(iterator.hasNext()){
			FeedResult result = iterator.next();
			title = Optional.ofNullable(result.getTitle())
					.map(i->i.toLowerCase())
					.orElse("");

			if (pattern.matcher(title).find()) {
				iterator.remove();
				LOGGER.trace("Removing  {}", result);
			}
		}
		
	}

}
