package io.delimeat.core.show;

import java.util.List;

public interface ShowDao {

	/**
	 * Read a show
	 * 
	 * @param showId
	 * @return Show
	 * @throws ShowNotFoundException
	 * @throws ShowException
	 */
	public Show read(long showId) throws ShowNotFoundException, ShowException;

	/**
	 * Create or Update existing Show
	 * 
	 * @param show
	 * @throws ShowConcurrencyException
	 * @throws ShowException
	 */
	public Show createOrUpdate(Show show) throws ShowConcurrencyException, ShowException;

	/**
	 * Delete a show
	 * 
	 * @param showId
	 * @throws ShowNotFoundException
	 * @throws ShowException
	 */
	public void delete(long showId) throws ShowNotFoundException, ShowException;

	/**
	 * Read all Shows
	 * 
	 * @return List of all shows
	 * @throws ShowException
	 */
	public List<Show> readAll() throws ShowException;
  
	/**
	 * read and lock a show
	 * 
	 * @param showId
	 * @return Show
	 * @throws ShowConcurrencyException
	 * @throws ShowException
	 */
	public Show readAndLock(long showId) throws ShowNotFoundException, ShowConcurrencyException, ShowException;

	/**
	 * Read all episodes
	 * 
	 * @param showId
	 * @return List of all shows
	 * @throws ShowNotFoundException
	 * @throws ShowException
	 */
	public List<Episode> readAllEpisodes(long showId) throws ShowNotFoundException, ShowException;
 
	/**
	 * Read next episode
	 * 
	 * @param showId
	 * @param date
	 * @return episode
	 * @throws ShowNotFoundException
	 * @throws ShowException
	 */
  public Episode readNextEpisode(Episode episode) throws ShowNotFoundException, ShowException;
  
  	/**
	 * Read episode 
	 * 
	 * @param episodeId
	 * @param date
	 * @return episode
	 * @throws ShowNotFoundException
	 * @throws ShowException
	 */
	public Episode readEpisode(long episodeId) throws ShowNotFoundException, ShowException;
  
  	/**
	 * Create or Update episode 
	 * 
	 * @param episode
	 * @return episode
	 * @throws ShowConcurrencyException
	 * @throws ShowException
	 */
	 public Episode createOrUpdateEpisode(Episode episode) throws ShowConcurrencyException, ShowException;

  	/**
	 * Create or Update episodes 
	 * 
	 * @param episodes
	 * @return episodes
	 * @throws ShowConcurrencyException
	 * @throws ShowException
	 */
	 public List<Episode> createOrUpdateEpisodes(List<Episode> episodes) throws ShowConcurrencyException, ShowException;
  
  	/**
	 * Delete an episode
	 * 
	 * @param episodeId
	 * @throws ShowNotFoundException
	 * @throws ShowException
	 */
	 public void deleteEpisode(long episodeId) throws ShowNotFoundException, ShowException;
  
  
	
}
