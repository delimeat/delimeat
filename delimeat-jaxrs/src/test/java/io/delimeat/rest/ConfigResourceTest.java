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
	}
	
	@Test
	public void updateTest() throws ConfigException {
		resource.service = Mockito.mock(ConfigService.class);
		Config config = new Config();
		Mockito.when(resource.service.update(config)).thenReturn(config);
		
		Config result = resource.update(config);
		Assertions.assertEquals(config, result);
		
		Mockito.verify(resource.service).update(config);
	}
}
