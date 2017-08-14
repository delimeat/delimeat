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
package io.delimeat.feed;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedSource;
import io.delimeat.feed.exception.FeedContentTypeException;
import io.delimeat.feed.exception.FeedException;
import io.delimeat.feed.exception.FeedResponseBodyException;
import io.delimeat.feed.exception.FeedResponseException;
import io.delimeat.feed.exception.FeedTimeoutException;

public interface FeedDataSource {

	/**
	 * @return the feed source
	 */
	public FeedSource getFeedSource();
	
	/**
	 * @param title
	 * @return list of Feed Results
	 * @throws FeedTimeoutException
	 * @throws FeedContentTypeException
	 * @throws FeedResponseException
	 * @throws FeedResponseBodyException
	 * @throws FeedException
	 */
	@Cacheable("feed")
	public List<FeedResult> read(String title) throws FeedTimeoutException,FeedContentTypeException,FeedResponseException,FeedResponseBodyException, FeedException;
	
}