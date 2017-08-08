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
package io.delimeat.http;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.http.domain.HttpStatistics;
import spark.Spark;

public class HttpStatisticsControllerTest {

	private static Client client;
	private static HttpStatisticsController controller;
	
	@BeforeClass
    public static void setup() throws Exception {
		
		controller = new HttpStatisticsController();
		controller.init();
        
        client = ClientBuilder.newClient();
        
        Spark.after((request,response)->{
        	response.type("application/json");
        });
    }
    
	@AfterClass
    public static void tearDown() {
        Spark.stop();
        client.close();
    }
	
    @Test
    public void getTest() throws Exception{
    	HttpStatistics stats = new HttpStatistics("http://test.com");
    	stats.setLastSuccess(Instant.EPOCH);
    	stats.setLastFailure(Instant.ofEpochMilli(1));
		
		HttpStatisticsService statsService = Mockito.mock(HttpStatisticsService.class);
		Mockito.when(statsService.getStatistics()).thenReturn(Arrays.asList(stats));	
		controller.setHttpStatsService(statsService);
		
    	Response response = client.target("http://localhost:4567")
    			.path("/api/stats")
    			.request()
    			.accept("application/json")
    			.get();
    	
    	System.out.println(response);
    	
    	Assert.assertEquals(200, response.getStatus());
    	Assert.assertEquals("application/json", response.getHeaderString("Content-Type"));    	
    	List<HttpStatistics> results = response.readEntity(new GenericType<List<HttpStatistics>>(){});
    	Assert.assertEquals(1, results.size());
    	Assert.assertEquals(stats, results.get(0));

		Mockito.verify(statsService).getStatistics();
		Mockito.verifyNoMoreInteractions(statsService);
    }
}
