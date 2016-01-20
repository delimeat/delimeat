package io.delimeat.core.show;

import java.util.Date;
import java.util.List;

public interface ShowDao {

	/**
	 * Read a show
	 * 
	 * @param showId
	 * @return Show
	 * @throws Exception
	 */
	public Show read(long showId) throws ShowNotFoundException, ShowException;

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
  
	/**
	 * read and lock a show
	 * 
	 * @param showId
	 * @return Show
	 * @throws Exception
	 */
	public Show readAndLock(long showId) throws ShowNotFoundException, ShowConcurrencyException, ShowException;

	/**
	 * Read all episodes
	 * 
	 * @param showId
	 * @return List of all shows
	 * @throws Exception
	 */
	public List<Episode> readAllEpisodes(long showId) throws ShowException;
 
	/**
	 * Read episode after date
	 * 
	 * @param showId
	 * @param date
	 * @return episode
	 * @throws Exception
	 */
	public Episode readEpisodeAfter(long showId, Date date) throws ShowException;
  
  	/**
	 * Read episode 
	 * 
	 * @param episodeId
	 * @param date
	 * @return episode
	 * @throws Exception
	 */
	public Episode readEpisode(long episodeId) throws ShowNotFoundException, ShowConcurrencyException, ShowException;
  
  	/**
	 * Create episode 
	 * 
	 * @param episodeId
	 * @param date
	 * @return episode
	 * @throws Exception
	 */
	 public Episode createOrUpdateEpisode(Episode episode) throws ShowConcurrencyException, ShowException;

  	/**
	 * Delete an episode
	 * 
	 * @param episodeId
	 * @throws Exception
	 */
	 public void deleteEpisode(long episodeId) throws ShowNotFoundException, ShowException;
  
  
	
}
