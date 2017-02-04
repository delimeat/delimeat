package io.delimeat.core.show;

import java.util.List;

import javax.persistence.PersistenceException;

import io.delimeat.util.EntityException;
import io.delimeat.util.jpa.AbstractJpaDao;

public class EpisodeJpaDao_Impl extends AbstractJpaDao<Long, Episode> implements EpisodeDao {

	public EpisodeJpaDao_Impl() {
		super(Episode.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.delimeat.core.show.EpisodeDao#readAll(int, int, boolean)
	 */
	@Override
	public List<Episode> readAll(int page, int pageSize, List<EpisodeStatus> statusList) throws EntityException {
		try {
			return em.createNamedQuery("Episode.getAll", entityClass)
					.setParameter("statusList", statusList)
					.setMaxResults(pageSize)
					.setFirstResult((page-1) * pageSize)
					.getResultList();

		} catch (PersistenceException ex) {
			throw new EntityException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.delimeat.core.show.EpisodeDao#countAll(boolean)
	 */
	@Override
	public long countAll(List<EpisodeStatus> statusList) throws EntityException {
		try {
			return em.createNamedQuery("Episode.getAll.count", Long.class)
					.setParameter("statusList", statusList)
					.getSingleResult();

		} catch (PersistenceException ex) {
			throw new EntityException(ex);
		}
	}

}
