package io.delimeat.core.show;

import java.util.List;

public interface ShowDao {

	/**
	 * Create a new instance of show
	 * 
	 * @param show
	 * @return showId
	 * @throws Exception
	 */
	public Show create(Show show) throws Exception;

	/**
	 * Read a show
	 * 
	 * @param showId
	 * @return Show
	 * @throws Exception
	 */
	public Show read(long showId) throws Exception;

	/**
	 * Update existing Show
	 * 
	 * @param show
	 * @throws Exception
	 */
	public Show update(Show show) throws Exception;

	/**
	 * Delete a show
	 * 
	 * @param showId
	 * @throws Exception
	 */
	public void delete(long showId) throws Exception;

	/**
	 * Read all Shows
	 * 
	 * @return List of all shows
	 * @throws Exception
	 */
	public List<Show> readAll() throws Exception;
}
