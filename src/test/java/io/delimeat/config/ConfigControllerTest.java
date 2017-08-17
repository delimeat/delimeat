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

import java.util.Arrays;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.config.domain.Config;
import io.delimeat.config.exception.ConfigConcurrencyException;
import spark.Spark;

public class ConfigControllerTest {
	
	private static Client client;
	private static ConfigController controller;
	
	@BeforeClass
    public static void setup() throws Exception {
		
		controller = new ConfigController();
		controller.init();
        
        client = ClientBuilder.newClient();
        
        Spark.after((request,response)->{
        	response.type("application/json");
        });
        
    }
	
	@Before
	public void setUp(){
		controller.setConfigService(null);
	}
    
	@AfterClass
    public static void tearDown() {
        Spark.stop();
        client.close();
    }
	
	@Test
	public void configServiceTest(){
		Assert.assertNull(controller.getConfigService());
		ConfigService configService = Mockito.mock(ConfigService.class);
		controller.setConfigService(configService);
		Assert.assertEquals(configService, controller.getConfigService());

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
    	
    	Assert.assertEquals(200, response.getStatus());
    	Assert.assertEquals("application/json", response.getHeaderString("Content-Type"));    	
    	Assert.assertEquals(config, response.readEntity(Config.class));

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
    			.post(Entity.entity(config, "application/json"));
    	
    	Assert.assertEquals(200, response.getStatus());
    	Assert.assertEquals("application/json", response.getHeaderString("Content-Type"));    	
    	Assert.assertEquals(config, response.readEntity(Config.class));
    	
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
    			.post(Entity.entity(config, "application/json"));
    	
    	Assert.assertEquals(412, response.getStatus());
    	Assert.assertEquals("application/json", response.getHeaderString("Content-Type")); 
    	Assert.assertEquals("{\"message\":\"You are trying to update a resource that has been modified\"}",  response.readEntity(String.class));
							  	
		Mockito.verify(configService).update(config);
		Mockito.verifyNoMoreInteractions(configService);
    }

}
