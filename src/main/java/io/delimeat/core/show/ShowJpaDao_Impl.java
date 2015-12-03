package io.delimeat.core.show;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

public class ShowJpaDao_Impl implements ShowDao {

	@PersistenceContext
	protected EntityManager em;

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public Show create(Show show) throws Exception {
		em.persist(show);
		if (show.getGuideSources() != null && show.getGuideSources().size() > 0) {
			for (ShowGuideSource guideSource : show.getGuideSources()) {
				guideSource.getId().setShowId(show.getShowId());
				guideSource.setShow(show);
				em.persist(guideSource);
			}
		}
		if (show.getNextEpisode() != null) {
			show.getNextEpisode().setShow(show);
			em.persist(show.getNextEpisode());
		}
		if (show.getPreviousEpisode() != null) {
			show.getPreviousEpisode().setShow(show);
			em.persist(show.getPreviousEpisode());
		}
		return show;
	}

	@Override
	public Show read(long showId) throws PersistenceException {
		return em.getReference(Show.class, showId);
	}

	@Override
	public Show update(Show show) throws Exception {
		if (show.getGuideSources() != null && show.getGuideSources().size() > 0) {
			for (ShowGuideSource guideSource : show.getGuideSources()) {
				guideSource.getId().setShowId(show.getShowId());
				guideSource.setShow(show);
				em.persist(guideSource);
			}
		}
		if (show.getNextEpisode() != null) {
			show.getNextEpisode().setShow(show);

		}
		if (show.getPreviousEpisode() != null) {
			show.getPreviousEpisode().setShow(show);
		}
		return em.merge(show);

	}

	@Override
	public void delete(long showId) throws Exception {
		em.remove(read(showId));
	}

	@Override
	public List<Show> readAll() throws Exception {
		TypedQuery<Show> query = em.createQuery("SELECT e FROM Show e", Show.class);
		return query.getResultList();
	}

}
