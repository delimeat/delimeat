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

import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.delimeat.show.entity.Episode;
import io.delimeat.show.entity.EpisodeStatus;
import io.delimeat.show.exception.ShowConcurrencyException;
import io.delimeat.show.exception.ShowException;
import io.delimeat.show.exception.ShowNotFoundException;

@Service
public class EpisodeService_Impl implements EpisodeService {

	@Autowired
	private EpisodeDao episodeDao;

	
	/**
	 * @return the episodeDao
	 */
	public EpisodeDao getEpisodeDao() {
		return episodeDao;
	}

	/**
	 * @param episodeDao the episodeDao to set
	 */
	public void setEpisodeDao(EpisodeDao episodeDao) {
		this.episodeDao = episodeDao;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.show.EpisodeService#create(io.delimeat.show.domain.Episode)
	 */
	@Override
	public Episode create(Episode episode) throws ShowException {
		try{
			return episodeDao.create(episode);
		} catch (PersistenceException e) {
			throw new ShowException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.show.EpisodeService#read(java.lang.Long)
	 */
	@Override
	public Episode read(Long episodeId) throws ShowNotFoundException, ShowException{
		try {
			return episodeDao.read(episodeId);
		} catch (EntityNotFoundException ex) {
			throw new ShowNotFoundException(episodeId);
		} catch (PersistenceException e) {
			throw new ShowException(e);
		}
	}

	/* (non-Javadoc)
	 * @see io.delimeat.show.EpisodeService#save(io.delimeat.show.domain.Episode)
	 */
	@Override
	public Episode update(Episode episode) throws ShowConcurrencyException, ShowException {
		try{
			return episodeDao.update(episode);
		} catch (OptimisticLockException e) {
			throw new ShowConcurrencyException(e);
		} catch (PersistenceException e) {
			throw new ShowException(e);
		}
		
	}

	/* (non-Javadoc)
	 * @see io.delimeat.show.EpisodeService#delete(java.lang.Long)
	 */
	@Override
	public void delete(Long episodeId) throws ShowException {
		try{
			episodeDao.delete(episodeId);
		} catch (PersistenceException e) {
			throw new ShowException(e);
		}		
	}

	/* (non-Javadoc)
	 * @see io.delimeat.show.EpisodeService#findAllPending()
	 */
	@Override
	public List<Episode> findAllPending() throws ShowException {
		try{
			return episodeDao.findByStatus(Arrays.asList(EpisodeStatus.PENDING));
		} catch (PersistenceException e) {
			throw new ShowException(e);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EpisodeService_Impl [" + (episodeDao != null ? "episodeRepository=" + episodeDao : "")
				+ "]";
	}
	
}
