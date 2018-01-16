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
package io.delimeat.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.delimeat.config.ConfigService;
import io.delimeat.config.entity.Config;
import io.delimeat.config.exception.ConfigException;

public class ConfigResourceTest {

	private ConfigResource resource;
	
	@BeforeEach
	public void setUp() {
		resource = new ConfigResource();
	}
	
	@Test
	public void readTest() throws ConfigException {
		resource.service = Mockito.mock(ConfigService.class);
		Config config = new Config();
		Mockito.when(resource.service.read()).thenReturn(config);
		
		Config result = resource.read();
		Assertions.assertEquals(config, result);
		
		Mockito.verify(resource.service).read();
		Mockito.verifyNoMoreInteractions(resource.service);
	}
	
	@Test
	public void updateTest() throws ConfigException {
		resource.service = Mockito.mock(ConfigService.class);
		Config config = new Config();
		Mockito.when(resource.service.update(config)).thenReturn(config);
		
		Config result = resource.update(config);
		Assertions.assertEquals(config, result);
		
		Mockito.verify(resource.service).update(config);
		Mockito.verifyNoMoreInteractions(resource.service);
	}
}
