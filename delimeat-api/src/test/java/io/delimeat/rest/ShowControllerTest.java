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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.delimeat.api.ShowController;
import io.delimeat.show.EpisodeService;
import io.delimeat.show.ShowService;
import io.delimeat.show.entity.Episode;
import io.delimeat.show.entity.EpisodeStatus;
import io.delimeat.show.entity.Show;
import io.delimeat.show.entity.ShowType;
import io.delimeat.show.exception.ShowConcurrencyException;
import io.delimeat.show.exception.ShowNotFoundException;
import spark.Spark;


public class ShowControllerTest {

	private static Client client;
	private static ShowController controller;
    
	@BeforeAll
    public static void setup() throws Exception {
		Thread.sleep(1000);
		controller = new ShowController();
		controller.init();
                
        client = ClientBuilder.newClient();
        
        Spark.after((request,response)->{
        	response.type("application/json");
        });
        
    }
	
	@BeforeEach
	public void setUp(){
		controller.setEpisodeService(null);
		controller.setShowService(null);
	}
    
	@AfterAll
    public static void tearDown() {
        Spark.stop();
		if(client!=null){
			client.close();
		}
    }
	
	
	@Test
	public void showServiceTest(){
		Assertions.assertNull(controller.getShowService());

		ShowService showService = Mockito.mock(ShowService.class);
		controller.setShowService(showService);	
		
		Assertions.assertEquals(showService, controller.getShowService());
	}
	
	@Test
	public void episodeServiceTest(){
		Assertions.assertNull(controller.getEpisodeService());

		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		controller.setEpisodeService(episodeService);	
		
		Assertions.assertEquals(episodeService, controller.getEpisodeService());
	}
	
	@Test
	public void getAllTest() throws Exception{
		Show show = new Show();
		show.setAiring(true);
		show.setAirTime(LocalTime.MIDNIGHT);
		show.setEnabled(false);
		show.setGuideId("GUIDEID");
		show.setLastGuideCheck(Instant.ofEpochMilli(1));
		show.setLastGuideUpdate(Instant.EPOCH);
		show.setMaxSize(Integer.MAX_VALUE);
		show.setMinSize(Integer.MIN_VALUE);
		show.setShowId(99L);
		show.setShowType(ShowType.DAILY);
		show.setTimezone("TIMEZONE");
		show.setTitle("TITLE");
		show.setVersion(999);
		
		ShowService showService = Mockito.mock(ShowService.class);
		Mockito.when(showService.readAll()).thenReturn(Arrays.asList(show));
		controller.setShowService(showService);
		
    	Response response = client.target("http://localhost:4567")
    			.path("/api/show")
    			.request()
    			.accept("application/json")
    			.get();

    	Assertions.assertEquals(200, response.getStatus());
    	Assertions.assertEquals("application/json", response.getHeaderString("Content-Type")); 
    	List<Show> results = response.readEntity(new GenericType<List<Show>>(){});
    	Assertions.assertEquals(1, results.size());
    	Assertions.assertEquals(show, results.get(0));
								
		Mockito.verify(showService).readAll();
		Mockito.verifyNoMoreInteractions(showService);
	}
	
	@Test
	public void getTest() throws Exception{
		Show show = new Show();
		show.setAiring(true);
		show.setAirTime(LocalTime.MIDNIGHT);
		show.setEnabled(false);
		show.setGuideId("GUIDEID");
		show.setLastGuideCheck(Instant.ofEpochMilli(1));
		show.setLastGuideUpdate(Instant.EPOCH);
		show.setMaxSize(Integer.MAX_VALUE);
		show.setMinSize(Integer.MIN_VALUE);
		show.setShowId(99L);
		show.setShowType(ShowType.DAILY);
		show.setTimezone("TIMEZONE");
		show.setTitle("TITLE");
		show.setVersion(999);
			
		ShowService showService = Mockito.mock(ShowService.class);
		Mockito.when(showService.read(1L)).thenReturn(show);
		controller.setShowService(showService);
		
    	Response response = client.target("http://localhost:4567")
    			.path("/api/show/1")
    			.request()
    			.accept("application/json")
    			.get();

    	Assertions.assertEquals(200, response.getStatus());
    	Assertions.assertEquals("application/json", response.getHeaderString("Content-Type")); 
    	Assertions.assertEquals(show, response.readEntity(Show.class));
								
		Mockito.verify(showService).read(1L);
		Mockito.verifyNoMoreInteractions(showService);
	}
	
	@Test
	public void getConcurrencyExceptionTest() throws Exception{
	
		ShowService showService = Mockito.mock(ShowService.class);
		Mockito.when(showService.read(1L)).thenThrow(ShowConcurrencyException.class);
		controller.setShowService(showService);
		
    	Response response = client.target("http://localhost:4567")
    			.path("/api/show/1")
    			.request()
    			.accept("application/json")
    			.get();

    	Assertions.assertEquals(412, response.getStatus());
    	Assertions.assertEquals("application/json", response.getHeaderString("Content-Type")); 
    	Assertions.assertEquals("{\"message\":\"You are trying to update a resource that has been modified\"}",  response.readEntity(String.class));
								
		Mockito.verify(showService).read(1L);
		Mockito.verifyNoMoreInteractions(showService);
	}
	
	@Test
	public void getNotFoundExceptionTest() throws Exception{
	
		ShowService showService = Mockito.mock(ShowService.class);
		Mockito.when(showService.read(1L)).thenThrow(ShowNotFoundException.class);
		controller.setShowService(showService);
		
    	Response response = client.target("http://localhost:4567")
    			.path("/api/show/1")
    			.request()
    			.accept("application/json")
    			.get();

    	Assertions.assertEquals(404, response.getStatus());
    	Assertions.assertEquals("application/json", response.getHeaderString("Content-Type")); 
    	Assertions.assertEquals("{\"message\":\"Unable to find requested resource\"}",  response.readEntity(String.class));
								
		Mockito.verify(showService).read(1L);
		Mockito.verifyNoMoreInteractions(showService);
	}
	
	@Test
	public void updateTest() throws Exception{
		Show show = new Show();
		show.setAiring(true);
		show.setAirTime(LocalTime.MIDNIGHT);
		show.setEnabled(false);
		show.setGuideId("GUIDEID");
		show.setLastGuideCheck(Instant.ofEpochMilli(1));
		show.setLastGuideUpdate(Instant.EPOCH);
		show.setMaxSize(Integer.MAX_VALUE);
		show.setMinSize(Integer.MIN_VALUE);
		show.setShowId(99L);
		show.setShowType(ShowType.DAILY);
		show.setTimezone("TIMEZONE");
		show.setTitle("TITLE");
		show.setVersion(999);
				
		ShowService showService = Mockito.mock(ShowService.class);
		Mockito.when(showService.update(show)).thenReturn(show);
		controller.setShowService(showService);
		
    	Response response = client.target("http://localhost:4567")
    			.path("/api/show/1")
    			.request()
    			.accept("application/json")
    			.put(Entity.entity(show, "application/json"));

    	Assertions.assertEquals(200, response.getStatus());
    	Assertions.assertEquals("application/json", response.getHeaderString("Content-Type")); 
    	Assertions.assertEquals(show, response.readEntity(Show.class));
								
		Mockito.verify(showService).update(show);
		Mockito.verifyNoMoreInteractions(showService);
	}	
	
	@Test
	public void deleteTest() throws Exception{
		
		ShowService showService = Mockito.mock(ShowService.class);
		controller.setShowService(showService);
		
    	Response response = client.target("http://localhost:4567")
    			.path("/api/show/1")
    			.request()
    			.accept("application/json")
    			.delete();

    	Assertions.assertEquals(200, response.getStatus());
    	Assertions.assertEquals("application/json", response.getHeaderString("Content-Type")); 
    	Assertions.assertFalse(response.hasEntity());
								
		Mockito.verify(showService).delete(1L);
		Mockito.verifyNoMoreInteractions(showService);
	}
	
	@Test
	public void createTest() throws Exception{
		Show show = new Show();
		show.setAiring(true);
		show.setAirTime(LocalTime.MIDNIGHT);
		show.setEnabled(false);
		show.setGuideId("GUIDEID");
		show.setLastGuideCheck(Instant.ofEpochMilli(1));
		show.setLastGuideUpdate(Instant.EPOCH);
		show.setMaxSize(Integer.MAX_VALUE);
		show.setMinSize(Integer.MIN_VALUE);
		show.setShowId(99L);
		show.setShowType(ShowType.DAILY);
		show.setTimezone("TIMEZONE");
		show.setTitle("TITLE");
		show.setVersion(999);
		
		ShowService showService = Mockito.mock(ShowService.class);
		Mockito.when(showService.update(show)).thenReturn(show);
		controller.setShowService(showService);
		
    	Response response = client.target("http://localhost:4567")
    			.path("/api/show")
    			.request()
    			.accept("application/json")
    			.post(Entity.entity(show, "application/json"));

    	Assertions.assertEquals(200, response.getStatus());
    	Assertions.assertEquals("application/json", response.getHeaderString("Content-Type")); 
    	Assertions.assertEquals(show, response.readEntity(Show.class));
								
		Mockito.verify(showService).create(Mockito.any());
		Mockito.verifyNoMoreInteractions(showService);
	}
	
	@Test
	public void getAllEpisodesTest() throws Exception{
		Show show = new Show();
		show.setAiring(true);
		show.setAirTime(LocalTime.MIDNIGHT);
		show.setEnabled(false);
		show.setGuideId("GUIDEID");
		show.setLastGuideCheck(Instant.ofEpochMilli(1));
		show.setLastGuideUpdate(Instant.EPOCH);
		show.setMaxSize(Integer.MAX_VALUE);
		show.setMinSize(Integer.MIN_VALUE);
		show.setShowId(99L);
		show.setShowType(ShowType.DAILY);
		show.setTimezone("TIMEZONE");
		show.setTitle("TITLE");
		show.setVersion(999);
		
		Episode episode = new Episode();
		episode.setAirDate(LocalDate.ofEpochDay(100));
		episode.setDoubleEp(true);
		episode.setEpisodeId(Long.MAX_VALUE);
		episode.setEpisodeNum(99);
		episode.setLastFeedCheck(Instant.ofEpochSecond(100000));
		episode.setLastFeedUpdate(Instant.ofEpochSecond(10000));
		episode.setSeasonNum(50);
		episode.setStatus(EpisodeStatus.SKIPPED);
		episode.setTitle("TITLE");
		episode.setVersion(1);
		
		ShowService showService = Mockito.mock(ShowService.class);
		Mockito.when(showService.read(1L)).thenReturn(show);
		controller.setShowService(showService);
		
		EpisodeService episodeService = Mockito.mock(EpisodeService.class);
		Mockito.when(episodeService.findByShow(99L)).thenReturn(Arrays.asList(episode));
		controller.setEpisodeService(episodeService);
		
    	Response response = client.target("http://localhost:4567")
    			.path("/api/show/99/episode")
    			.request()
    			.accept("application/json")
    			.get();

    	Assertions.assertEquals(200, response.getStatus());
    	Assertions.assertEquals("application/json", response.getHeaderString("Content-Type")); 
    	List<Episode> results = response.readEntity(new GenericType<List<Episode>>(){});
    	Assertions.assertEquals(1, results.size());
    	Assertions.assertEquals(episode, results.get(0));
		
		Mockito.verify(episodeService).findByShow(99L);
		Mockito.verifyNoMoreInteractions(episodeService);
	}

}
