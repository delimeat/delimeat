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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import io.delimeat.guide.domain.AiringDay;
import io.delimeat.guide.domain.GuideEpisode;
import io.delimeat.guide.domain.GuideInfo;
import io.delimeat.guide.domain.GuideSearchResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@SpringBootTest()
public class GuideControllerTest {

	private MockMvc mockMvc;
	
	@InjectMocks
    private GuideController guideController;
	
	@Mock
	private GuideService guideService;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders
                .standaloneSetup(guideController)
                .build();
	}
	
	@Test
	public void searchTest() throws Exception{
		GuideSearchResult gsr = new GuideSearchResult();
		gsr.setTitle("TITLE");
		gsr.setDescription("DESCRIPTION");
		gsr.setFirstAired(LocalDate.ofEpochDay(0));
		gsr.setGuideId("GUIDEID");
		
		Mockito.when(guideService.readLike("TITLE")).thenReturn(Arrays.asList(gsr));
		
		mockMvc.perform(
            get("/api/guide/search/TITLE")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.length()").value(1))
			.andExpect(jsonPath("$[0].title").value("TITLE"))
			.andExpect(jsonPath("$[0].description").value("DESCRIPTION"))
			//TODO once object mapper problem solved
			.andExpect(jsonPath("$[0].firstAired").exists())
			//.andExpect(jsonPath("$[0].firstAired").value(LocalDate.ofEpochDay(0).toString()))
			.andExpect(jsonPath("$[0].guideId").value("GUIDEID"));
								
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
		
		Mockito.when(guideService.read("GUIDEID")).thenReturn(info);
		
		mockMvc.perform(
            get("/api/guide/info/GUIDEID")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.title").value("TITLE"))
			.andExpect(jsonPath("$.description").value("DESCRIPTION"))
			//TODO once object mapper problem solved
			.andExpect(jsonPath("$.firstAired").exists())
			//.andExpect(jsonPath("$.firstAired").value(LocalDate.ofEpochDay(1).toString()))
			.andExpect(jsonPath("$.guideId").value("GUIDEID"))
			.andExpect(jsonPath("$.timezone").value("TIMEZONE"))
			.andExpect(jsonPath("$.runningTime").value(99))
			//TODO once object mapper problem solved
			.andExpect(jsonPath("$.lastUpdated").exists())
			//.andExpect(jsonPath("$.lastUpdated").value(LocalDate.ofEpochDay(100).toString()))
			.andExpect(jsonPath("$.genres.length()").value(1))
			.andExpect(jsonPath("$.genres[0]").value("GENRE"))
			//TODO once object mapper problem solved
			.andExpect(jsonPath("$.airTime").exists())
			//.andExpect(jsonPath("$.airTime").value(LocalTime.parse("12:45").toString()))
			.andExpect(jsonPath("$.airing").value(true))
			.andExpect(jsonPath("$.airDays.length()").value(2))
			.andExpect(jsonPath("$.airDays[0]").value("FRIDAY"))
			.andExpect(jsonPath("$.airDays[1]").value("SATURDAY"))
			;
								
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
	
		
		Mockito.when(guideService.readEpisodes("GUIDEID")).thenReturn(Arrays.asList(ep));
		
		mockMvc.perform(
            get("/api/guide/info/GUIDEID/episodes")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.length()").value(1))
			.andExpect(jsonPath("$[0].title").value("TITLE"))
			//TODO once object mapper problem solved
			.andExpect(jsonPath("$[0].airDate").exists())
			//.andExpect(jsonPath("$[0].airDate").value(LocalDate.ofEpochDay(99).toString()))
			.andExpect(jsonPath("$[0].seasonNum").value(300))
			.andExpect(jsonPath("$[0].episodeNum").value(400))
			.andExpect(jsonPath("$[0].productionNum").value(500));
								
		Mockito.verify(guideService).readEpisodes("GUIDEID");
		Mockito.verifyNoMoreInteractions(guideService);
	}
	
}
