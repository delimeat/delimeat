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
package io.delimeat.show;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.EpisodeStatus;
import spark.Spark;

public class EpisodeControllerTest {

	private static Client client;
	private static EpisodeController controller;
    
	@BeforeClass
    public static void setup() throws Exception {

		controller = new EpisodeController();
		controller.init();
        
        Spark.awaitInitialization();
        
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
	public void getAllTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.ofEpochDay(2));
		episode.setDoubleEp(false);
		episode.setEpisodeId(Long.MAX_VALUE);
		episode.setLastFeedCheck(Instant.ofEpochMilli(10000));
		episode.setLastFeedUpdate(Instant.ofEpochMilli(10000));
		episode.setSeasonNum(99);
		episode.setEpisodeNum(100);
		episode.setStatus(EpisodeStatus.FOUND);
		episode.setTitle("TITLE");
		episode.setVersion(99);
		
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		Mockito.when(episodeService.findAllPending()).thenReturn(Arrays.asList(episode));
		controller.setEpisodeService(episodeService);
		
    	Response response = client.target("http://localhost:4567")
    			.path("/api/episode")
    			.request()
    			.accept("application/json")
    			.get();

    	Assert.assertEquals(200, response.getStatus());
    	Assert.assertEquals("application/json", response.getHeaderString("Content-Type")); 
    	List<Episode> results = response.readEntity(new GenericType<List<Episode>>(){});
    	Assert.assertEquals(1, results.size());
    	Assert.assertEquals(episode, results.get(0));
								
		Mockito.verify(episodeService).findAllPending();
		Mockito.verifyNoMoreInteractions(episodeService);
	}
	
	@Test
	public void updateTest() throws Exception{
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.ofEpochDay(2));
		episode.setDoubleEp(false);
		episode.setEpisodeId(Long.MAX_VALUE);
		episode.setLastFeedCheck(Instant.ofEpochMilli(10000));
		episode.setLastFeedUpdate(Instant.ofEpochMilli(10000));
		episode.setSeasonNum(99);
		episode.setEpisodeNum(100);
		episode.setStatus(EpisodeStatus.FOUND);
		episode.setTitle("TITLE");
		episode.setVersion(99);
				
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		Mockito.when(episodeService.update(episode)).thenReturn(episode);
		controller.setEpisodeService(episodeService);
		
    	Response response = client.target("http://localhost:4567")
    			.path("/api/episode/1")
    			.request()
    			.accept("application/json")
    			.put(Entity.entity(episode, "application/json"));

    	Assert.assertEquals(200, response.getStatus());
    	Assert.assertEquals("application/json", response.getHeaderString("Content-Type")); 
    	Assert.assertEquals(episode, response.readEntity(Episode.class));
								
		Mockito.verify(episodeService).update(episode);
		Mockito.verifyNoMoreInteractions(episodeService);
	}
}
