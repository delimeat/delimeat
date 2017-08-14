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
package io.delimeat.guide;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.guide.domain.AiringDay;
import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.guide.domain.GuideInfo;
import io.delimeat.guide.domain.GuideSearchResult;
import spark.Spark;

public class GuideControllerTest {
	
	private static Client client;
	private static GuideController controller;
    
	@BeforeClass
    public static void setup() throws Exception {

		controller = new GuideController();
		controller.init();
        
        Spark.awaitInitialization();
        
        client = ClientBuilder.newClient();
        
        Spark.after((request,response)->{
        	response.type("application/json");
        });
    }
	
	@Before
	public void setUp(){
		controller.setGuideService(null);
	}
    
	@AfterClass
    public static void tearDown() {
        Spark.stop();
        client.close();
    }
	
	@Test
	public void guideServiceTest(){
		Assert.assertNull(controller.getGuideService());
		GuideService guideService = Mockito.mock(GuideService.class);
		controller.setGuideService(guideService);
		Assert.assertEquals(guideService, controller.getGuideService());
	}
	
	@Test
	public void searchTest() throws Exception{
		GuideSearchResult gsr = new GuideSearchResult();
		gsr.setTitle("TITLE");
		gsr.setDescription("DESCRIPTION");
		gsr.setFirstAired(LocalDate.ofEpochDay(0));
		gsr.setGuideId("GUIDEID");
		
		GuideService guideService = Mockito.mock(GuideService.class);
		Mockito.when(guideService.readLike("TITLE")).thenReturn(Arrays.asList(gsr));	
		controller.setGuideService(guideService);
		
    	Response response = client.target("http://localhost:4567")
    			.path("/api/guide/search/TITLE")
    			.request()
    			.accept("application/json")
    			.get();

    	Assert.assertEquals(200, response.getStatus());
    	Assert.assertEquals("application/json", response.getHeaderString("Content-Type")); 
    	List<GuideSearchResult> results = response.readEntity(new GenericType<List<GuideSearchResult>>(){});
    	Assert.assertEquals(1, results.size());
    	Assert.assertEquals(gsr, results.get(0));
    	
		Mockito.verify(guideService).readLike("TITLE");
		Mockito.verifyNoMoreInteractions(guideService);
	}
	
	@Test
	public void infoTest() throws Exception{
		GuideInfo info = new GuideInfo();
		info.setTitle("TITLE");
		info.setTimezone("TIMEZONE");
		info.setRunningTime(99);
		info.setLastUpdated(LocalDate.ofEpochDay(100));
		info.setGuideId("GUIDEID");
		info.setGenres(Arrays.asList("GENRE"));
		info.setFirstAired(LocalDate.ofEpochDay(1));
		info.setDescription("DESCRIPTION");
		info.setAirTime(LocalTime.parse("12:45"));
		info.setAiring(true);
		info.setAirDays(Arrays.asList(AiringDay.FRIDAY,AiringDay.SATURDAY));
		
		GuideService guideService = Mockito.mock(GuideService.class);
		Mockito.when(guideService.read("GUIDEID")).thenReturn(info);
		controller.setGuideService(guideService);
		
    	Response response = client.target("http://localhost:4567")
    			.path("/api/guide/info/GUIDEID")
    			.request()
    			.accept("application/json")
    			.get();
							
    	Assert.assertEquals(200, response.getStatus());
    	Assert.assertEquals("application/json", response.getHeaderString("Content-Type")); 
    	Assert.assertEquals(info, response.readEntity(GuideInfo.class));

		Mockito.verify(guideService).read("GUIDEID");
		Mockito.verifyNoMoreInteractions(guideService);
	}
	
	@Test
	public void episodesTest() throws Exception{
		GuideEpisode ep = new GuideEpisode();
		ep.setTitle("TITLE");
		ep.setAirDate(LocalDate.ofEpochDay(99));
		ep.setSeasonNum(300);
		ep.setEpisodeNum(400);
		ep.setProductionNum(500);
	
		GuideService guideService = Mockito.mock(GuideService.class);
		Mockito.when(guideService.readEpisodes("GUIDEID")).thenReturn(Arrays.asList(ep));
		controller.setGuideService(guideService);
		
    	Response response = client.target("http://localhost:4567")
    			.path("/api/guide/info/GUIDEID/episodes")
    			.request()
    			.accept("application/json")
    			.get();

    	Assert.assertEquals(200, response.getStatus());
    	Assert.assertEquals("application/json", response.getHeaderString("Content-Type")); 
    	List<GuideEpisode> results = response.readEntity(new GenericType<List<GuideEpisode>>(){});
    	Assert.assertEquals(1, results.size());
    	Assert.assertEquals(ep, results.get(0));
								
		Mockito.verify(guideService).readEpisodes("GUIDEID");
		Mockito.verifyNoMoreInteractions(guideService);
	}
	
}
