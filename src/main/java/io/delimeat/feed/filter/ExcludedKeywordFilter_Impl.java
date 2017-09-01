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

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResult;
import io.delimeat.show.domain.Episode;

@Component
@Order(2)
public class ExcludedKeywordFilter_Impl implements FeedResultFilter {

	/* (non-Javadoc)
	 * @see io.delimeat.feed.filter.FeedResultFilter#filter(java.util.List, io.delimeat.show.domain.Episode, io.delimeat.config.domain.Config)
	 */
	@Override
	public void filter(List<FeedResult> results, Episode episode, Config config) {
		if (config.getExcludedKeywords() == null || config.getExcludedKeywords().isEmpty()) {
			return;
		}
		
		String regex = config.getExcludedKeywords()
				.stream()
				.collect(Collectors.joining("|", "(", ")"));
		
		final Pattern pattern = Pattern.compile(regex.toLowerCase());
		String title;
		Iterator<FeedResult> iterator = results.iterator();
		while(iterator.hasNext()){
			FeedResult result = iterator.next();
			title = Optional.ofNullable(result.getTitle())
					.map(i->i.toLowerCase())
					.orElse("");

			if (pattern.matcher(title).find()) {
				iterator.remove();
			}
		}

	}

}
