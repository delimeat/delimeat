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

import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import io.delimeat.config.entity.Config;
import io.delimeat.config.exception.ConfigConcurrencyException;
import io.delimeat.config.exception.ConfigException;

public class ConfigService_ImplTest {

	private ConfigService_Impl service;

	@BeforeEach
	public void setUp() throws Exception {
		service = new ConfigService_Impl();
	}

	@Test
	public void toStringTest() {
		Assertions.assertEquals("ConfigService_Impl []", service.toString());
	}

	@Test
	public void configDaoTest() {
		Assertions.assertNull(service.getConfigDao());
		ConfigDao dao = Mockito.mock(ConfigDao.class);
		service.setConfigDao(dao);
		Assertions.assertEquals(dao, service.getConfigDao());
	}

	@Test
	public void defaultOutputDirectoryTest() {
		Assertions.assertNull(service.getDefaultOutputDir());
		service.setDefaultOutputDir("DEFAULT_OUTPUT_DIR");
		Assertions.assertEquals("DEFAULT_OUTPUT_DIR", service.getDefaultOutputDir());
	}

	@Test
	public void readTest() throws Exception {
		Config config = new Config();
		ConfigDao dao = Mockito.mock(ConfigDao.class);
		Mockito.when(dao.read(1L)).thenReturn(config);
		service.setConfigDao(dao);

		Assertions.assertEquals(config, service.read());

		Mockito.verify(dao).read(1L);
		Mockito.verifyNoMoreInteractions(dao);
	}

	@Test
	public void readEnityNotFoundExceptionTest() throws Exception {
		Config config = new Config();
		config.setConfigId(1L);
		config.setOutputDirectory("DEFAULT_OUTPUT_DIR");
		config.setSearchInterval(4 * 60 * 60 * 1000);
		config.setSearchDelay(60 * 60 * 1000);
		config.setPreferFiles(true);
		config.setIgnoreFolders(false);
		
		ConfigDao dao = Mockito.mock(ConfigDao.class);
		Mockito.when(dao.read(1L)).thenThrow(EntityNotFoundException.class);
		Mockito.when(dao.update(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());
		service.setConfigDao(dao);
		service.setDefaultOutputDir("DEFAULT_OUTPUT_DIR");

		Config result = service.read();

		Assertions.assertEquals("DEFAULT_OUTPUT_DIR", result.getOutputDirectory());
		Assertions.assertEquals(60 * 60 * 1000, result.getSearchDelay());
		Assertions.assertEquals(4 * 60 * 60 * 1000, result.getSearchInterval());
		Assertions.assertTrue(result.isPreferFiles());
		Assertions.assertFalse(result.isIgnoreFolders());

		ArgumentCaptor<Config> captor = ArgumentCaptor.forClass(Config.class);
		Mockito.verify(dao).update(captor.capture());
		Assertions.assertEquals(config, captor.getValue());
		Mockito.verify(dao).read(1L);
		Mockito.verify(dao).update(result);
		Mockito.verifyNoMoreInteractions(dao);
	}

	@Test
	public void readExceptionTest() throws Exception {
		ConfigDao dao = Mockito.mock(ConfigDao.class);
		Mockito.when(dao.read(1L)).thenThrow(PersistenceException.class);
		service.setConfigDao(dao);

		service.setDefaultOutputDir("DEFAULT_OUTPUT_DIR");

		ConfigException exception = Assertions.assertThrows(ConfigException.class, () -> {
			service.read();
		});
		Assertions.assertEquals("javax.persistence.PersistenceException", exception.getMessage());
	}

	@Test
	public void updateTest() throws Exception {
		Config config = new Config();
		config.setIgnoreFolders(true);
		config.setPreferFiles(false);
		
		ConfigDao dao = Mockito.mock(ConfigDao.class);
		Mockito.when(dao.update(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());
		service.setConfigDao(dao);

		Config result = service.update(config);
		Assertions.assertEquals(config, result);
		Assertions.assertTrue(config.isIgnoreFolders());
		Assertions.assertTrue(config.isPreferFiles());

		ArgumentCaptor<Config> captor = ArgumentCaptor.forClass(Config.class);
		Mockito.verify(dao).update(captor.capture());
		Assertions.assertEquals(config, captor.getValue());
		Mockito.verifyNoMoreInteractions(dao);

	}

	@Test
	public void updateExceptionTest() throws Exception {
		Config config = new Config();
		config.setIgnoreFolders(true);
		config.setPreferFiles(false);
		
		ConfigDao dao = Mockito.mock(ConfigDao.class);
		Mockito.when(dao.update(Mockito.any())).thenThrow(PersistenceException.class);
		service.setConfigDao(dao);

		ConfigException exception = Assertions.assertThrows(ConfigException.class, () -> {
			service.update(config);
		});
		Assertions.assertEquals("javax.persistence.PersistenceException", exception.getMessage());

	}

	@Test
	public void updateConcurrencyExceptionTest() throws Exception {
		Config config = new Config();
		config.setIgnoreFolders(true);
		config.setPreferFiles(false);
		
		ConfigDao dao = Mockito.mock(ConfigDao.class);
		Mockito.when(dao.update(Mockito.any())).thenThrow(OptimisticLockException.class);
		service.setConfigDao(dao);

		ConfigConcurrencyException exception = Assertions.assertThrows(ConfigConcurrencyException.class, () -> {
			service.update(config);
		});
		Assertions.assertEquals("javax.persistence.OptimisticLockException", exception.getMessage());
	}

}
