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
package io.delimeat.guide;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.guide.domain.GuideInfo;
import io.delimeat.guide.domain.GuideSearchResult;
import io.delimeat.guide.domain.GuideSource;
import io.delimeat.guide.exception.GuideAuthorizationException;
import io.delimeat.guide.exception.GuideException;
import io.delimeat.guide.exception.GuideNotFoundException;

public interface GuideDataSource {

	/**
	 * @return the guide data source
	 */
	public GuideSource getGuideSource();

	/**
	 * Fetch a guide info
	 * 
	 * @param guideId
	 * @return
	 * @throws GuideNotFoundException
	 * @throws GuideAuthorizationException
	 * @throws GuideException
	 */
	@Cacheable("guide-info")
	public GuideInfo info(String guideId) throws GuideNotFoundException, GuideAuthorizationException, GuideException;

	/**
	 * Fetch all episodes for a show
	 * 
	 * @param guideId
	 * @return
	 * @throws GuideNotFoundException
	 * @throws GuideAuthorizationException
	 * @throws GuideException
	 */
	@Cacheable("guide-episodes")
	public List<GuideEpisode> episodes(String guideId) throws GuideNotFoundException, GuideAuthorizationException, GuideException;

	/**
	 * Search for shows
	 * 
	 * @param title
	 * @return
	 * @throws GuideNotFoundException
	 * @throws GuideAuthorizationException
	 * @throws GuideException
	 */
	@Cacheable("guide-search")
	public List<GuideSearchResult> search(String title) throws GuideNotFoundException, GuideAuthorizationException, GuideException;

}
