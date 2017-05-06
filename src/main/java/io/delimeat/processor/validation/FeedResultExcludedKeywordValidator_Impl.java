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
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedResultRejection;
import io.delimeat.show.domain.Episode;

@Component
@Order(2)
public class FeedResultExcludedKeywordValidator_Impl extends AbstractFeedResultValidator implements FeedResultValidator {

	public FeedResultExcludedKeywordValidator_Impl(){
		super(FeedResultRejection.EXCLUDED_KEYWORD);
	}
	/* (non-Javadoc)
	 * @see io.delimeat.server.processor.validation.FeedResultValidator#validate(java.util.List, io.delimeat.common.show.model.Episode, io.delimeat.common.config.model.Config)
	 */
	@Override
	public void validate(List<FeedResult> results, Episode episode, Config config) throws ValidationException {
		if (config.getExcludedKeywords() == null || config.getExcludedKeywords().isEmpty()) {
			return;
		}

		String regex = config.getExcludedKeywords()
								.stream()
								.collect(Collectors.joining("|", "(", ")"));

		final Pattern pattern = Pattern.compile(regex.toLowerCase());
		String title;
		for (final FeedResult result : results) {
			title = Optional.ofNullable(result.getTitle())
								.map(i->i.toLowerCase())
								.orElse("");
			
			if (pattern.matcher(title).find()) {
				addRejection(result);
			}
		}

	}
}
