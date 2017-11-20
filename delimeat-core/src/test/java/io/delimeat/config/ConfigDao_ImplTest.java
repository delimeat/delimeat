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
package io.delimeat.config;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.delimeat.config.entity.Config;

public class ConfigDao_ImplTest {

	private ConfigDao_Impl dao;
	
	@BeforeEach
	public void setUp(){
		dao = new ConfigDao_Impl();
	}
	
	@Test
	public void readTest(){
		Config config = new Config();
		EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.getReference(Config.class, 1L)).thenReturn(config);
     	dao.setEntityManager(entityManager);
     
		Config result = dao.read(1L);
		Assertions.assertEquals(config, result);		
     	
     	Mockito.verify(entityManager).getReference(Config.class,1L);
     	Mockito.verifyNoMoreInteractions(entityManager);
	}
	
	@Test
	public void updateTest(){
		Config config = new Config();

     	EntityManager entityManager = Mockito.mock(EntityManager.class);
     	Mockito.when(entityManager.merge(config)).thenReturn(config);
     	dao.setEntityManager(entityManager);

		Config result = dao.update(config);
		Assertions.assertEquals(config, result);		
     	
		Mockito.verify(entityManager).merge(config);
		Mockito.verifyNoMoreInteractions(entityManager);
	}
}
