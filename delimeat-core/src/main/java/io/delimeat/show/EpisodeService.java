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
package io.delimeat.show;

import java.util.List;

import io.delimeat.show.entity.Episode;
import io.delimeat.show.exception.ShowConcurrencyException;
import io.delimeat.show.exception.ShowException;
import io.delimeat.show.exception.ShowNotFoundException;

public interface EpisodeService {

	/**
	 * Create an episode
	 * @param episode
	 * @return
	 * @throws ShowException
	 */
	public Episode create(Episode episode) throws ShowException;
	
	/**
	 * Read an episode
	 * @param episodeId
	 * @return
	 * @throws ShowNotFoundException
	 * @throws ShowException
	 */
	public Episode read(Long episodeId) throws ShowNotFoundException, ShowException;

	/**
	 * Update an episode
	 * @param episode
	 * @return
	 * @throws ShowConcurrencyException
	 * @throws ShowException
	 */
	public Episode update(Episode episode)  throws ShowConcurrencyException, ShowException;

	/**
	 * Delete an episode
	 * @param episodeId
	 * @throws ShowException
	 */
	public void delete(Long episodeId) throws ShowException;
	
	/**
	 * Find all pending episodes
	 * @return
	 * @throws ShowException
	 */
	public List<Episode> findAllPending() throws ShowException;

}
