package io.delimeat.util;

public abstract interface AbstractDao<PK,T> {

	/**
	 * Create an entity 
	 * 
	 * @param entity
	 * @throws EntityException
	 */
	public void create(T entity) throws EntityException;
	
	/**
	 * Read an entity 
	 * 
	 * @param id
	 * @return entity
	 * @throws EntityNotFoundException
	 * @throws EntityException
	 */
	public T read(PK id) throws EntityNotFoundException, EntityException;
	
	 /**
	 * Update an entity 
	 * 
	 * @param entity
	 * @return entity
	 * @throws EntityConcurrencyException
	 * @throws EntityException
	 */
	public T update(T entity) throws EntityConcurrencyException, EntityException;
	
  	/**
	 * Delete an entity
	 * 
	 * @param id
	 * @throws EntityNotFoundException
	 * @throws EntityException
	 */
	public void delete(PK id) throws EntityNotFoundException, EntityException;
}
