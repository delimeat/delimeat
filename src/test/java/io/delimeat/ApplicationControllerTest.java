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
package io.delimeat;

import java.util.Arrays;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.util.spark.SparkController;
import spark.Spark;

public class ApplicationControllerTest {

	private static Client client;
	private static ApplicationController controller;
	
	@BeforeClass
    public static void setup() throws Exception {
		Thread.sleep(1000);
		controller = new ApplicationController();
		controller.setPort(4567);
		controller.init();
        
        client = ClientBuilder.newClient();
    }
    
	@AfterClass
    public static void tearDown() {
        Spark.stop();
		if(client!=null){
			client.close();
		}
    }
	
	@Test
	public void portTest(){
		Assert.assertEquals(4567,Spark.port());
		Assert.assertEquals(4567, controller.getPort());
		controller.setPort(9090);
		Assert.assertEquals(9090, controller.getPort());		
	}
	
	@Test
	public void controllersTest(){
		SparkController newController = Mockito.mock(SparkController.class);
		Assert.assertEquals(0, controller.getControllers().size());
		controller.setControllers(Arrays.asList(newController));
		Assert.assertEquals(1, controller.getControllers().size());
		Assert.assertEquals(newController, controller.getControllers().get(0));

	}
	
	@Test
	public void notFoundTest(){
    	Response response = client.target("http://localhost:4567")
    			.path("/notfound")
    			.request()
    			.accept("application/json")
    			.get();
    	
    	Assert.assertEquals(404, response.getStatus());
    	Assert.assertEquals("application/json", response.getHeaderString("Content-Type")); 
    	Assert.assertEquals("gzip", response.getHeaderString("Content-Encoding"));
    	Assert.assertEquals("{\"message\":\"resource not found\"}",  response.readEntity(String.class));
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
    	
    	Assert.assertEquals(500, response.getStatus());
    	Assert.assertEquals("application/json", response.getHeaderString("Content-Type")); 
    	Assert.assertEquals("gzip", response.getHeaderString("Content-Encoding"));
    	Assert.assertEquals("{\"message\":\"internal server error\"}",  response.readEntity(String.class));
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
    	
    	Assert.assertEquals(400, response.getStatus());
    	Assert.assertEquals("application/json", response.getHeaderString("Content-Type")); 
    	Assert.assertEquals("gzip", response.getHeaderString("Content-Encoding"));
    	Assert.assertEquals("{\"message\":\"invalid request format\"}",  response.readEntity(String.class));
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
    	
    	Assert.assertEquals(200, response.getStatus());
    	Assert.assertEquals("application/json", response.getHeaderString("Content-Type")); 
    	Assert.assertEquals("gzip", response.getHeaderString("Content-Encoding"));
    	Assert.assertEquals("{\"message\":\"this is json\"}",  response.readEntity(String.class));
	}
}
