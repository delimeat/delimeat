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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.RecoverableDataAccessException;

import io.delimeat.config.ConfigRepository;
import io.delimeat.config.ConfigService_Impl;
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
		Assertions.assertNull(service.getConfigRepository());
		ConfigRepository repository = Mockito.mock(ConfigRepository.class);
		service.setConfigRepository(repository);
		Assertions.assertEquals(repository, service.getConfigRepository());
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
		ConfigRepository repository = Mockito.mock(ConfigRepository.class);
		Mockito.when(repository.findOne(1L)).thenReturn(config);
		service.setConfigRepository(repository);

		Assertions.assertEquals(config, service.read());

		Mockito.verify(repository).findOne(1L);
		Mockito.verifyNoMoreInteractions(repository);
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
		ConfigRepository repository = Mockito.mock(ConfigRepository.class);
		Mockito.when(repository.findOne(1L)).thenReturn(null);
		Mockito.when(repository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());

		service.setConfigRepository(repository);
		service.setDefaultOutputDir("DEFAULT_OUTPUT_DIR");

		Config result = service.read();

		Assertions.assertEquals("DEFAULT_OUTPUT_DIR", result.getOutputDirectory());
		Assertions.assertEquals(60 * 60 * 1000, result.getSearchDelay());
		Assertions.assertEquals(4 * 60 * 60 * 1000, result.getSearchInterval());
		Assertions.assertTrue(result.isPreferFiles());
		Assertions.assertFalse(result.isIgnoreFolders());

		ArgumentCaptor<Config> captor = ArgumentCaptor.forClass(Config.class);
		Mockito.verify(repository).save(captor.capture());
		Assertions.assertEquals(config, captor.getValue());
		Mockito.verify(repository).findOne(1L);
		Mockito.verify(repository).save(result);
		Mockito.verifyNoMoreInteractions(repository);
	}

	@Test
	public void readEnityDataAccessExceptionTest() throws Exception {
		ConfigRepository repository = Mockito.mock(ConfigRepository.class);
		DataAccessException ex = new RecoverableDataAccessException("ERROR");
		Mockito.when(repository.findOne(1L)).thenThrow(ex);

		service.setConfigRepository(repository);
		service.setDefaultOutputDir("DEFAULT_OUTPUT_DIR");

		ConfigException exception = Assertions.assertThrows(ConfigException.class, () -> {
			service.read();
		});
		Assertions.assertEquals("org.springframework.dao.RecoverableDataAccessException: ERROR", exception.getMessage());
	}

	@Test
	public void updateTest() throws Exception {
		Config config = new Config();
		config.setIgnoreFolders(true);
		config.setPreferFiles(false);
		ConfigRepository repository = Mockito.mock(ConfigRepository.class);
		Mockito.when(repository.save(Mockito.any())).then(AdditionalAnswers.returnsFirstArg());
		service.setConfigRepository(repository);

		Config result = service.update(config);
		Assertions.assertEquals(config, result);
		Assertions.assertTrue(config.isIgnoreFolders());
		Assertions.assertTrue(config.isPreferFiles());

		ArgumentCaptor<Config> captor = ArgumentCaptor.forClass(Config.class);
		Mockito.verify(repository).save(captor.capture());
		Assertions.assertEquals(config, captor.getValue());
		Mockito.verify(repository).save(config);
		Mockito.verifyNoMoreInteractions(repository);

	}

	@Test
	public void updateDataAccessExceptionTest() throws Exception {
		Config config = new Config();
		config.setIgnoreFolders(true);
		config.setPreferFiles(false);
		ConfigRepository repository = Mockito.mock(ConfigRepository.class);
		DataAccessException ex = new RecoverableDataAccessException("ERROR");
		Mockito.when(repository.save(Mockito.any(Config.class))).thenThrow(ex);
		service.setConfigRepository(repository);

		ConfigException exception = Assertions.assertThrows(ConfigException.class, () -> {
			service.update(config);
		});
		Assertions.assertEquals("org.springframework.dao.RecoverableDataAccessException: ERROR", exception.getMessage());

	}

	@Test
	public void updateConcurrencyExceptionTest() throws Exception {
		Config config = new Config();
		config.setIgnoreFolders(true);
		config.setPreferFiles(false);
		ConfigRepository repository = Mockito.mock(ConfigRepository.class);
		DataAccessException ex = new ConcurrencyFailureException("ERROR");
		Mockito.when(repository.save(Mockito.any(Config.class))).thenThrow(ex);
		service.setConfigRepository(repository);

		ConfigConcurrencyException exception = Assertions.assertThrows(ConfigConcurrencyException.class, () -> {
			service.update(config);
		});
		Assertions.assertEquals("org.springframework.dao.ConcurrencyFailureException: ERROR", exception.getMessage());
	}

}
