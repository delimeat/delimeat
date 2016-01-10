package io.delimeat.core.show;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
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
			
			TypedQuery<Show> query = em.createQuery("SELECT e FROM Show e", Show.class);
			return query.getResultList();
		
		} catch (PersistenceException ex){
			throw new ShowException(ex);
		}
	}

}
