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
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import io.delimeat.show.entity.Episode;
import io.delimeat.show.entity.EpisodeStatus;
import io.delimeat.show.exception.ShowConcurrencyException;
import io.delimeat.show.exception.ShowException;
import io.delimeat.show.exception.ShowNotFoundException;

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
	 * @see io.delimeat.show.EpisodeService#create(io.delimeat.show.domain.Episode)
	 */
	@Override
	public Episode create(Episode episode) throws ShowException {
		try{
			return episodeRepository.save(episode);
		} catch (DataAccessException e) {
			throw new ShowException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.show.EpisodeService#read(java.lang.Long)
	 */
	@Override
	public Episode read(Long episodeId) throws ShowNotFoundException, ShowException{
		try{
			Optional<Episode> optional = Optional.ofNullable(episodeRepository.findOne(episodeId));
			if(optional.isPresent() == false){
				throw new ShowNotFoundException(episodeId);
			}
			return optional.get();
		} catch (DataAccessException e) {
			throw new ShowException(e);
		}
	}

	/* (non-Javadoc)
	 * @see io.delimeat.show.EpisodeService#save(io.delimeat.show.domain.Episode)
	 */
	@Override
	public Episode update(Episode episode) throws ShowConcurrencyException, ShowException {
		try{
			return episodeRepository.save(episode);
		} catch (ConcurrencyFailureException e) {
			throw new ShowConcurrencyException(e);
		} catch (DataAccessException e) {
			throw new ShowException(e);
		}
		
	}

	/* (non-Javadoc)
	 * @see io.delimeat.show.EpisodeService#delete(java.lang.Long)
	 */
	@Override
	public void delete(Long episodeId) throws ShowException {
		try{
			episodeRepository.delete(episodeId);
		} catch (DataAccessException e) {
			throw new ShowException(e);
		}		
	}

	/* (non-Javadoc)
	 * @see io.delimeat.show.EpisodeService#findAllPending()
	 */
	@Override
	public List<Episode> findAllPending() throws ShowException {
		try{
			return episodeRepository.findByStatusIn(Arrays.asList(EpisodeStatus.PENDING));
		} catch (DataAccessException e) {
			throw new ShowException(e);
		}
	}

	/* (non-Javadoc)
	 * @see io.delimeat.show.EpisodeService#findByShow(java.lang.Long)
	 */
	@Override
	public List<Episode> findByShow(Long showId) throws ShowException {
		try{
			return episodeRepository.findEpisodeByShowShowId(showId);
		} catch (DataAccessException e) {
			throw new ShowException(e);
		}
	}
	

	@Override
	public void deleteByShow(Long showId) throws ShowException {
		try{
			episodeRepository.deleteByShowShowId(showId);
		} catch (DataAccessException e) {
			throw new ShowException(e);
		}
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EpisodeService_Impl [" + (episodeRepository != null ? "episodeRepository=" + episodeRepository : "")
				+ "]";
	}
	
}
