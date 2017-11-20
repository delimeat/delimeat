/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.delimeat.util;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class AbstractJpaDao<I,T> {

	private final Class<T> entityClass;
	
	@PersistenceContext
	private EntityManager entityManager;

	public AbstractJpaDao(Class<T> entityClass){
		this.entityClass = entityClass;
	}
	
	/**
	 * @return the entityManager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * @param entityManager the entityManager to set
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	/**
	 * @param entity the entity to create
	 */
	public void create(T entity){
		entityManager.persist(entity);
	}
	
	/**
	 * @param id entity id to read
	 * @return the entity
	 */
	public T read(I id){
		return entityManager.getReference(entityClass, id);
	}
	
	/**
	 * @param entity the entity to update
	 * @return the updated entity
	 */
	public T update(T entity){
		return entityManager.merge(entity);
	}
	
	/**
	 * @param id the entity id to delete
	 */
	public void delete(I id){
		entityManager.remove(read(id));
	}
	
}
