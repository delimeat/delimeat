package io.delimeat.core.show;

import java.util.List;

import io.delimeat.util.AbstractDao;
import io.delimeat.util.EntityException;

public interface EpisodeDao extends AbstractDao<Long,Episode> {

	/**
	 * Read all episodes
	 * 
	 * @param page
	 * @param pageSize
	 * @param statusList
	 * @return List of all episodes
	 * @throws EntityNotFoundException
	 * @throws EntityException
	 */
	public List<Episode> readAll(int page,int pageSize, List<EpisodeStatus> statusList) throws EntityException;
  
	
	/**
	 * Count all episodes
	 * 
	 * @param statusList
	 * @return
	 * @throws EntityException
	 */
	public long countAll(List<EpisodeStatus> statusList) throws EntityException;
}
