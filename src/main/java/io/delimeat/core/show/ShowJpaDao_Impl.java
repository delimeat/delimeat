package io.delimeat.core.show;

import io.delimeat.core.show.Episode;
import io.delimeat.core.show.ShowException;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

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
	public Show read(long showId) throws ShowNotFoundException, ShowException {
		try{
			
			return em.getReference(Show.class, showId);
		
		} catch (EntityNotFoundException ex){
			throw new ShowNotFoundException(ex);
		} catch (PersistenceException ex){
			throw new ShowException(ex);
		}
	}

	@Override
	public Show createOrUpdate(Show show) throws ShowConcurrencyException, ShowException {
		try{
			
			return em.merge(show);
			
		} catch (EntityNotFoundException ex){
			throw new ShowNotFoundException(ex);
		} catch (OptimisticLockException ex){
			throw new ShowConcurrencyException(ex);
		} catch (PersistenceException ex){
			throw new ShowException(ex);
		}

	}

	@Override
	public void delete(long showId) throws ShowNotFoundException, ShowException {
		try{
			
			em.remove(read(showId));
			
		} catch (EntityNotFoundException ex){
			throw new ShowNotFoundException(ex);
		} catch (PersistenceException ex){
			throw new ShowException(ex);
		}
	}

	@Override
	public List<Show> readAll() throws ShowException {
		try{
        
         return em.createNamedQuery("Show.getAll",Show.class).getResultList();
		
		} catch (PersistenceException ex){
			throw new ShowException(ex);
		}
	}
  
	@Override
	public Show readAndLock(long showId) throws ShowNotFoundException, ShowException {
		try{
         Show show = em.getReference(Show.class, showId);
         em.lock(show, LockModeType.PESSIMISTIC_WRITE);
			return show;
		
		} catch (EntityNotFoundException ex){
			throw new ShowNotFoundException(ex);
		} catch (PersistenceException ex){
			throw new ShowException(ex);
		}
	}

	@Override
	public List<Episode> readAllEpisodes(long showId)
			throws ShowException {
		try{
	         return em.createNamedQuery("Show.getAllEpisodes",Episode.class).setParameter("showId", showId).getResultList();
			
			} catch (PersistenceException ex){
				throw new ShowException(ex);
			}
	}

    @Override
    public Episode readEpisodeAfter(long showId, Date date) throws ShowException {
		try{
	         return em.createNamedQuery("Show.getEpisodeAfter",Episode.class)
              			.setParameter("showId", showId)
              			.setParameter("airDate",date)
              			.setMaxResults(1)
              			.getSingleResult();
			
			} catch(NoResultException ex){  
				return null; 
    		} catch (PersistenceException ex){
				throw new ShowException(ex);
			}
    }

}
