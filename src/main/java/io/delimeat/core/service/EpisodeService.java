package io.delimeat.core.service;

import io.delimeat.core.show.Episode;
import io.delimeat.util.EntityConcurrencyException;
import io.delimeat.util.EntityException;
import io.delimeat.util.EntityNotFoundException;
import io.delimeat.util.PaginatedResults;

public interface EpisodeService {

	/**
	 * Create an episode
	 * 
	 * @param episode
	 * @return episode
	 * @throws EntityConcurrencyException
	 * @throws EntityException
	 */
	public void create(Episode episode) throws EntityConcurrencyException, EntityException;

	/**
	 * Read episode
	 * 
	 * @param episodeId
	 * @param date
	 * @return episode
	 * @throws EntityNotFoundException
	 * @throws EntityException
	 */
	public Episode read(Long episodeId) throws EntityNotFoundException, EntityException;

	/**
	 * Update an episode
	 * 
	 * @param episodeId
	 * @throws EntityNotFoundException
	 * @throws EntityConcurrencyException
	 * @throws EntityException
	 */
	public Episode update(Episode episode) throws EntityNotFoundException, EntityConcurrencyException, EntityException;

	/**
	 * Delete an episode
	 * 
	 * @param episodeId
	 * @throws EntityNotFoundException
	 * @throws EntityException
	 */
	public void delete(Long episodeId)  throws EntityNotFoundException, EntityException;

	/**
	 * Read all episodes
	 * 
	 * @param page
	 * @param pageSize
	 * @param includeAll
	 * @throws EntityException
	 */
	public PaginatedResults<Episode> readAll(int page, int pageSize, boolean includeAll)  throws EntityException;

}
