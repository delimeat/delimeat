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
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.delimeat.api.ApplicationController;
import io.delimeat.api.util.SparkController;
import spark.Spark;

public class ApplicationControllerTest {

	private static Client client;
	private static ApplicationController controller;
	
	@BeforeAll
    public static void setup() throws Exception {
		Thread.sleep(1000);
		controller = new ApplicationController();
		controller.setPort(4567);
		controller.init();
        
        client = ClientBuilder.newClient();
    }
    
	@AfterAll
    public static void tearDown() {
        Spark.stop();
		if(client!=null){
			client.close();
		}
    }
	
	@Test
	public void portTest(){
		Assertions.assertEquals(4567,Spark.port());
		Assertions.assertEquals(4567, controller.getPort());
		controller.setPort(9090);
		Assertions.assertEquals(9090, controller.getPort());		
	}
	
	@Test
	public void controllersTest(){
		SparkController newController = Mockito.mock(SparkController.class);
		Assertions.assertEquals(0, controller.getControllers().size());
		controller.setControllers(Arrays.asList(newController));
		Assertions.assertEquals(1, controller.getControllers().size());
		Assertions.assertEquals(newController, controller.getControllers().get(0));

	}
	
	@Test
	public void notFoundTest(){
    	Response response = client.target("http://localhost:4567")
    			.path("/notfound")
    			.request()
    			.accept("application/json")
    			.get();
    	
    	Assertions.assertEquals(404, response.getStatus());
    	Assertions.assertEquals("application/json;charset=utf-8", response.getHeaderString("Content-Type")); 
    	//Assertions.assertEquals("gzip", response.getHeaderString("Content-Encoding"));
    	Assertions.assertEquals("{\"message\":\"resource not found\"}",  response.readEntity(String.class));
	}
	
	@Test
	public void internalServerErrorTest(){
		Spark.get("internalservererror",(spark.Request request, spark.Response response) -> {
			throw new Exception();
		});
		
    	Response response = client.target("http://localhost:4567")
    			.path("internalservererror")
    			.request()
    			.accept("application/json")
    			.get();
    	
    	Assertions.assertEquals(500, response.getStatus());
    	Assertions.assertEquals("application/json;charset=utf-8", response.getHeaderString("Content-Type")); 
    	//Assertions.assertEquals("gzip", response.getHeaderString("Content-Encoding"));
    	Assertions.assertEquals("{\"message\":\"internal server error\"}",  response.readEntity(String.class));
	}
	
	@Test
	public void numberFormatErrorTest(){
		Spark.get("numberformaterror",(spark.Request request, spark.Response response) -> {
			throw new NumberFormatException();
		});
		
    	Response response = client.target("http://localhost:4567")
    			.path("numberformaterror")
    			.request()
    			.accept("application/json")
    			.get();
    	
    	Assertions.assertEquals(400, response.getStatus());
    	Assertions.assertEquals("application/json;charset=utf-8", response.getHeaderString("Content-Type")); 
    	//Assertions.assertEquals("gzip", response.getHeaderString("Content-Encoding"));
    	Assertions.assertEquals("{\"message\":\"invalid request format\"}",  response.readEntity(String.class));
	}
	
	@Test
	public void apiContentTypeTest(){
		Spark.get("/api/contenttype",(spark.Request request, spark.Response response) -> {
			return "{\"message\":\"this is json\"}";
		});
		
    	Response response = client.target("http://localhost:4567")
    			.path("/api/contenttype")
    			.request()
    			.accept("application/json")
    			.get();
    	
    	Assertions.assertEquals(200, response.getStatus());
    	Assertions.assertEquals("application/json;charset=utf-8", response.getHeaderString("Content-Type")); 
    	//Assertions.assertEquals("gzip", response.getHeaderString("Content-Encoding"));
    	//Assertions.assertEquals("26", response.getHeaderString("Content-Length"));
    	Assertions.assertEquals("{\"message\":\"this is json\"}",  response.readEntity(String.class));
	}
	
	@Test
	public void apiContentLengthNullTest(){
		Spark.get("/api/contentlength",(spark.Request request, spark.Response response) -> {
			return "[]";
		});
		
    	Response response = client.target("http://localhost:4567")
    			.path("/api/contentlength")
    			.request()
    			.accept("application/json")
    			.get();
    	
    	Assertions.assertEquals(200, response.getStatus());
    	Assertions.assertEquals("application/json;charset=utf-8", response.getHeaderString("Content-Type")); 
    	//Assertions.assertEquals("gzip", response.getHeaderString("Content-Encoding"));
    	//Assertions.assertEquals("2", response.getHeaderString("Content-Length"));
    	Assertions.assertEquals("[]",  response.readEntity(String.class));
	}
}
