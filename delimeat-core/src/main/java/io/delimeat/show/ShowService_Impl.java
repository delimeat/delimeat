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

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.delimeat.guide.GuideService;
import io.delimeat.guide.exception.GuideException;
import io.delimeat.show.entity.Episode;
import io.delimeat.show.entity.EpisodeStatus;
import io.delimeat.show.entity.Show;
import io.delimeat.show.exception.ShowConcurrencyException;
import io.delimeat.show.exception.ShowException;
import io.delimeat.show.exception.ShowNotFoundException;
import io.delimeat.util.DelimeatUtils;

@Service
public class ShowService_Impl implements ShowService {
	
	@Autowired
	private ShowDao showDao;
	
	@Autowired
	private EpisodeService episodeService;
	
	@Autowired
	private GuideService guideService;


	/**
	 * @return the showDao
	 */
	public ShowDao getShowDao() {
		return showDao;
	}

	/**
	 * @param showDao the showDao to set
	 */
	public void setShowDao(ShowDao showDao) {
		this.showDao = showDao;
	}

	/**
	 * @return the episodeService
	 */
	public EpisodeService getEpisodeService() {
		return episodeService;
	}

	/**
	 * @param episodeService the episodeService to set
	 */
	public void setEpisodeService(EpisodeService episodeService) {
		this.episodeService = episodeService;
	}

	/**
	 * @return the guideService
	 */
	public GuideService getGuideService() {
		return guideService;
	}

	/**
	 * @param guideService the guideService to set
	 */
	public void setGuideService(GuideService guideService) {
		this.guideService = guideService;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.show.ShowService#create(io.delimeat.common.show.model.Show)
	 */
	@Override
	@Transactional
	public void create(Show show) throws ShowException {
		try{
			String cleanTitle = DelimeatUtils.cleanTitle(show.getTitle());
			show.setTitle(cleanTitle);
			
			showDao.create(show);
			final String guideId = show.getGuideId();
	
			if (guideId != null && guideId.length() > 0) {
				final List<Episode> episodes = guideService.readEpisodes(guideId).stream()
						.map(ShowUtils::fromGuideEpisode)
						.collect(Collectors.toList());
				
				Instant now = Instant.now();
				for(Episode episode: episodes){
					episode.setShow(show);
					
					Instant airDateTime = ShowUtils.determineAirTime(episode.getAirDate(), show.getAirTime(), show.getTimezone());
					if(airDateTime.isBefore(now)){
						episode.setStatus(EpisodeStatus.SKIPPED);
					}
					
					episodeService.create(episode);
				}
	
			}
		} catch (PersistenceException | GuideException e) {
			throw new ShowException(e);
		}
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.show.ShowService#read(java.lang.Long)
	 */
	@Override
	@Transactional
	public Show read(Long id)  throws ShowNotFoundException, ShowException  {
		try {
			return showDao.read(id);
		} catch(EntityNotFoundException ex) {
			throw new ShowNotFoundException(id);
		} catch (PersistenceException e) {
			throw new ShowException(e);
		}
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.show.ShowService#readAll()
	 */
	@Override
	@Transactional
	public List<Show> readAll() throws ShowException {
		try {
			return showDao.readAll();
		} catch (PersistenceException e) {
			throw new ShowException(e);
		}
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.show.ShowService#update(io.delimeat.common.show.model.Show)
	 */
	@Override
	@Transactional
	public Show update(Show show)  throws ShowConcurrencyException, ShowException {
		try{
			return showDao.update(show);
		}  catch (OptimisticLockException e) {
			throw new ShowConcurrencyException(e);
		} catch (PersistenceException e) {
			throw new ShowException(e);
		}
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.show.ShowService#delete(java.lang.Long)
	 */
	@Override
	@Transactional
	public void delete(Long id) throws ShowException {
		try{
			showDao.delete(id);
		} catch (PersistenceException e) {
			throw new ShowException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.common.show.ShowService#readAllEpisodes(java.lang.Long)
	 */
	@Override
	@Transactional
	public List<Episode> readAllEpisodes(Long id) throws ShowNotFoundException, ShowException {
		return read(id).getEpisodes();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ShowService_Impl [" + (showDao != null ? "showDao=" + showDao + ", " : "")
				+ (episodeService != null ? "episodeService=" + episodeService + ", " : "")
				+ (guideService != null ? "guideService=" + guideService : "") + "]";
	}

}
