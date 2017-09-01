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

import io.delimeat.config.domain.Config;
import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.exception.FeedException;
import io.delimeat.show.domain.Episode;

public interface FeedService {

	/**
	 * @param title
	 * @return list of Feed Results
	 * @throws FeedException
	 */
	public List<FeedResult> read(String title) throws FeedException;
	
	/**
	 * @param episode
	 * @param config
	 * @return
	 * @throws FeedException
	 */
	public List<FeedResult> read(Episode episode, Config config) throws FeedException;
	
}
