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

import org.springframework.data.repository.Repository;

import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.EpisodeStatus;
import io.delimeat.show.domain.Show;

public interface EpisodeRepository extends Repository<Episode, Long> {

	public Episode findOne(Long id);
	
	public Episode save(Episode episode);
	
	public void delete(Long id);
	
	public List<Episode> findByStatusIn(List<EpisodeStatus> statuses);
	
	public List<Episode> findByShow(Show show);
}
