package io.delimeat.core.show;

import java.util.List;

public interface ShowDao {

	/**
	 * Read a show
	 * 
	 * @param showId
	 * @return Show
	 * @throws Exception
	 */
	public Show read(long showId) throws ShowNotFoundException, ShowConcurrencyException, ShowException;

	/**
	 * Create or Update existing Show
	 * 
	 * @param show
	 * @throws Exception
	 */
	public Show createOrUpdate(Show show) throws ShowConcurrencyException, ShowException;

	/**
	 * Delete a show
	 * 
	 * @param showId
	 * @throws Exception
	 */
	public void delete(long showId) throws ShowNotFoundException, ShowException;

	/**
	 * Read all Shows
	 * 
	 * @return List of all shows
	 * @throws Exception
	 */
	public List<Show> readAll() throws ShowException;
}
