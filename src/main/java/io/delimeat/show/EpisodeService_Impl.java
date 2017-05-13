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

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.EpisodeStatus;
import io.delimeat.show.domain.Show;

@Service
public class EpisodeService_Impl implements EpisodeService {

	@Autowired
	private EpisodeRepository episodeRepository;
	
	/**
	 * @return the episodeRepository
	 */
	public EpisodeRepository getEpisodeRepository() {
		return episodeRepository;
	}

	/**
	 * @param episodeRepository the episodeRepository to set
	 */
	public void setEpisodeRepository(EpisodeRepository episodeRepository) {
		this.episodeRepository = episodeRepository;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.show.EpisodeService#read(java.lang.Long)
	 */
	@Override
	public Episode read(Long episodeId) {
		return episodeRepository.findOne(episodeId);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.show.EpisodeService#save(io.delimeat.show.domain.Episode)
	 */
	@Override
	public Episode save(Episode episode) {
		return episodeRepository.save(episode);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.show.EpisodeService#delete(java.lang.Long)
	 */
	@Override
	public void delete(Long episodeId) {
		episodeRepository.delete(episodeId);	
	}

	/* (non-Javadoc)
	 * @see io.delimeat.show.EpisodeService#findAllPending()
	 */
	@Override
	public List<Episode> findAllPending() {
		return episodeRepository.findByStatusIn(Arrays.asList(EpisodeStatus.PENDING));
	}

	@Override
	public List<Episode> findByShow(Show show) {
		return episodeRepository.findByShow(show);
	}



}
