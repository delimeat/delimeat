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

import java.util.Arrays;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.delimeat.api.ConfigController;
import io.delimeat.config.ConfigService;
import io.delimeat.config.entity.Config;
import io.delimeat.config.exception.ConfigConcurrencyException;
import spark.Spark;

public class ConfigControllerTest {
	
	private static Client client;
	private static ConfigController controller;
	
	@BeforeAll
    public static void setup() throws Exception {
		Thread.sleep(1000);
		controller = new ConfigController();
		controller.init();
        
        client = ClientBuilder.newClient();
        
        Spark.after((request,response)->{
        	response.type("application/json");
        });
        
    }
	
	@BeforeEach
	public void setUp(){
		controller.setConfigService(null);
	}
    
	@AfterAll
    public static void tearDown() {
        Spark.stop();
		if(client!=null){
			client.close();
		}
    }
	
	@Test
	public void configServiceTest(){
		Assertions.assertNull(controller.getConfigService());
		ConfigService configService = Mockito.mock(ConfigService.class);
		controller.setConfigService(configService);
		Assertions.assertEquals(configService, controller.getConfigService());

	}
    
    @Test
    public void getTest() throws Exception{
		Config config = new Config();
		config.setConfigId(99L);
		config.setVersion(Long.MAX_VALUE);
		config.setPreferFiles(true);
		config.setIgnoreFolders(false);
		config.setOutputDirectory("OUT_DIR");
		config.setSearchDelay(Integer.MAX_VALUE);
		config.setSearchInterval(99);
		config.setExcludedKeywords(Arrays.asList("EXCLUDED"));
		config.setIgnoredFileTypes(Arrays.asList("IGNORED"));
		
		ConfigService configService = Mockito.mock(ConfigService.class);
		Mockito.when(configService.read()).thenReturn(config);	
		controller.setConfigService(configService);
		
    	Response response = client.target("http://localhost:4567")
    			.path("/api/config")
    			.request()
    			.accept("application/json")
    			.get();
    	
    	System.out.println(response);
    	
    	Assertions.assertEquals(200, response.getStatus());
    	Assertions.assertEquals("application/json", response.getHeaderString("Content-Type"));    	
    	Assertions.assertEquals(config, response.readEntity(Config.class));

		Mockito.verify(configService).read();
		Mockito.verifyNoMoreInteractions(configService);
    }
    
    @Test
    public void updateTest() throws Exception{
		Config config = new Config();
		config.setConfigId(99L);
		config.setVersion(Long.MAX_VALUE);
		config.setPreferFiles(true);
		config.setIgnoreFolders(false);
		config.setOutputDirectory("OUT_DIR");
		config.setSearchDelay(Integer.MAX_VALUE);
		config.setSearchInterval(99);
		config.setExcludedKeywords(Arrays.asList("EXCLUDED"));
		config.setIgnoredFileTypes(Arrays.asList("IGNORED"));
		
		ConfigService configService = Mockito.mock(ConfigService.class);
		Mockito.when(configService.update(config)).thenReturn(config);
		controller.setConfigService(configService);
		
    	Response response = client.target("http://localhost:4567")
    			.path("/api/config")
    			.request()
    			.accept("application/json")
    			.put(Entity.entity(config, "application/json"));
    	
    	Assertions.assertEquals(200, response.getStatus());
    	Assertions.assertEquals("application/json", response.getHeaderString("Content-Type"));    	
    	Assertions.assertEquals(config, response.readEntity(Config.class));
    	
		Mockito.verify(configService).update(config);
		Mockito.verifyNoMoreInteractions(configService);
    }
    
    @Test
    public void updateConcurrencyExceptionTest() throws Exception{
		Config config = new Config();
		config.setConfigId(99L);
		config.setVersion(Long.MAX_VALUE);
		config.setPreferFiles(true);
		config.setIgnoreFolders(false);
		config.setOutputDirectory("OUT_DIR");
		config.setSearchDelay(Integer.MAX_VALUE);
		config.setSearchInterval(99);
		config.setExcludedKeywords(Arrays.asList("EXCLUDED"));
		config.setIgnoredFileTypes(Arrays.asList("IGNORED"));
		
		ConfigService configService = Mockito.mock(ConfigService.class);
		Mockito.when(configService.update(config)).thenThrow(ConfigConcurrencyException.class);
		controller.setConfigService(configService);
		
    	Response response = client.target("http://localhost:4567")
    			.path("/api/config")
    			.request()
    			.accept("application/json")
    			.put(Entity.entity(config, "application/json"));
    	
    	Assertions.assertEquals(412, response.getStatus());
    	Assertions.assertEquals("application/json", response.getHeaderString("Content-Type")); 
    	Assertions.assertEquals("{\"message\":\"You are trying to update a resource that has been modified\"}",  response.readEntity(String.class));
							  	
		Mockito.verify(configService).update(config);
		Mockito.verifyNoMoreInteractions(configService);
    }

}
