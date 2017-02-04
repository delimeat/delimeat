package io.delimeat.core.service;

import java.util.Arrays;
import java.util.List;

import io.delimeat.core.show.Episode;
import io.delimeat.core.show.EpisodeDao;
import io.delimeat.core.show.EpisodeStatus;
import io.delimeat.util.EntityConcurrencyException;
import io.delimeat.util.EntityException;
import io.delimeat.util.EntityNotFoundException;
import io.delimeat.util.PaginatedResults;

public class EpisodeService_Impl implements EpisodeService {

	private EpisodeDao episodeDao;
	
	/**
	 * @return the episodeDao
	 */
	public EpisodeDao getEpisodeDao() {
		return episodeDao;
	}

	/**
	 * @param episodeDao the EpisodeDao to set
	 */
	public void setEpisodeDao(EpisodeDao episodeDao) {
		this.episodeDao = episodeDao;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.core.service.EpisodeService#create(io.delimeat.core.show.Episode)
	 */
	@Override
	public void create(Episode episode) throws EntityConcurrencyException, EntityException {
		episodeDao.create(episode);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.core.service.EpisodeService#read(java.lang.Long)
	 */
	@Override
	public Episode read(Long episodeId) throws EntityNotFoundException, EntityException {
		return episodeDao.read(episodeId);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.core.service.EpisodeService#update(io.delimeat.core.show.Episode)
	 */
	@Override
	public Episode update(Episode episode) throws EntityNotFoundException, EntityConcurrencyException, EntityException {
		return episodeDao.update(episode);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.core.service.EpisodeService#delete(java.lang.Long)
	 */
	@Override
	public void delete(Long episodeId) throws EntityNotFoundException, EntityException {
		episodeDao.delete(episodeId);

	}

	/* (non-Javadoc)
	 * @see io.delimeat.core.service.EpisodeService#readAll(int, int, boolean)
	 */
	@Override
	public PaginatedResults<Episode> readAll(int page, int pageSize, boolean includeAll) throws EntityException {
		List<EpisodeStatus> statusList;
		if(includeAll == true){
			statusList = Arrays.asList(EpisodeStatus.PENDING,EpisodeStatus.FOUND,EpisodeStatus.SKIPPED);
		}
		else{
			statusList = Arrays.asList(EpisodeStatus.PENDING);
		}
		final List<Episode> episodes = episodeDao.readAll(page, pageSize, statusList);
		final long count = episodeDao.countAll(statusList);
		return new PaginatedResults<Episode>(episodes, count);
	}

}
