package io.delimeat.util.jpa;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import io.delimeat.util.EntityConcurrencyException;
import io.delimeat.util.AbstractDao;
import io.delimeat.util.EntityException;
import io.delimeat.util.EntityNotFoundException;

public abstract class AbstractJpaDao<PK,T> implements AbstractDao<PK,T>{

	@PersistenceContext
	protected EntityManager em;
	protected final Class<T> entityClass;
	
	public AbstractJpaDao(Class<T> entityClass){
		this.entityClass = entityClass;
	}

	/**
	 * Get entity manager
	 * 
	 * @return entity manager
	 */
	public EntityManager getEm() {
		return em;
	}

	/**
	 * Set entity manager
	 * 
	 * @param em
	 */
	public void setEm(EntityManager em) {
		this.em = em;
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.util.EntityDao#create(java.lang.Object)
	 */
	@Override
	public void create(T entity) throws EntityException{
		try{
			em.persist(entity);
			
		} catch (PersistenceException ex){
			throw new EntityException(ex);
		}		
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.util.EntityDao#update(java.lang.Object)
	 */
	@Override
	public T update(T entity) throws EntityConcurrencyException, EntityException{
		try{
			return em.merge(entity);
			
		} catch (OptimisticLockException ex){
			throw new EntityConcurrencyException(ex);
		} catch (PersistenceException ex){
			throw new EntityException(ex);
		}		
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.util.EntityDao#read(java.lang.Object)
	 */
	@Override
	public T read(PK id) throws EntityNotFoundException, EntityException{
		try{
			return em.getReference(entityClass, id);
		
		} catch (javax.persistence.EntityNotFoundException ex){
			throw new EntityNotFoundException(ex);
		} catch (PersistenceException ex){
			throw new EntityException(ex);
		}
	}
		
	/* (non-Javadoc)
	 * @see io.delimeat.util.EntityDao#delete(java.lang.Object)
	 */
	@Override
	public void delete(PK id) throws EntityNotFoundException, EntityException{
		try{
			
			final T entity = read(id);
			em.remove(entity);
			
		} catch (PersistenceException ex){
			throw new EntityException(ex);
		}
	}
	
	
}
