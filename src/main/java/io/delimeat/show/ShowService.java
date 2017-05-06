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

import io.delimeat.guide.exception.GuideConcurrencyException;
import io.delimeat.guide.exception.GuideException;
import io.delimeat.guide.exception.GuideNotFoundException;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.Show;

public interface ShowService {

	/**
	 * Create a show
	 * @param show
	 * @throws GuideException
	 * @throws GuideException
	 */
	public void create(Show show) throws Exception;

	/**
	 * Read an show
	 * @param id
	 * @return show
	 * @throws GuideNotFoundException
	 * @throws GuideException
	 */
	public Show read(Long id) throws Exception;

	/**
	 * Update a show
	 * @param show
	 * @return show
	 * @throws GuideConcurrencyException
	 * @throws GuideNotFoundException
	 * @throws GuideException
	 */
	public Show update(Show show) throws Exception;

	/**
	 * Delete a show
	 * @param id
	 * @throws GuideNotFoundException
	 * @throws GuideException
	 */
	public void delete(Long id) throws Exception;

	/**
	 * Read all shows
	 * @return shows
	 * @throws GuideException
	 */
	public List<Show> readAll() throws Exception;

	/**
	 * Read all episodes for a show
	 * @param id
	 * @return episodes
	 * @throws GuideNotFoundException
	 * @throws GuideException
	 */
	public List<Episode> readAllEpisodes(Long id) throws Exception;

}
