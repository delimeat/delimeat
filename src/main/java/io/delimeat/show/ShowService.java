package io.delimeat.show;

import java.util.List;

import io.delimeat.common.util.exception.EntityConcurrencyException;
import io.delimeat.common.util.exception.EntityException;
import io.delimeat.common.util.exception.EntityNotFoundException;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.Show;

public interface ShowService {

	/**
	 * Create a show
	 * @param show
	 * @throws GuideException
	 * @throws EntityException
	 */
	public void create(Show show) throws Exception;

	/**
	 * Read an show
	 * @param id
	 * @return show
	 * @throws EntityNotFoundException
	 * @throws EntityException
	 */
	public Show read(Long id) throws Exception;

	/**
	 * Update a show
	 * @param show
	 * @return show
	 * @throws EntityConcurrencyException
	 * @throws EntityNotFoundException
	 * @throws EntityException
	 */
	public Show update(Show show) throws Exception;

	/**
	 * Delete a show
	 * @param id
	 * @throws EntityNotFoundException
	 * @throws EntityException
	 */
	public void delete(Long id) throws Exception;

	/**
	 * Read all shows
	 * @return shows
	 * @throws EntityException
	 */
	public List<Show> readAll() throws Exception;

	/**
	 * Read all episodes for a show
	 * @param id
	 * @return episodes
	 * @throws EntityNotFoundException
	 * @throws EntityException
	 */
	public List<Episode> readAllEpisodes(Long id) throws Exception;

}
