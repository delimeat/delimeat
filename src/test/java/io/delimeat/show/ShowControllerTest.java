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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.delimeat.WebConfig;
import io.delimeat.show.domain.Episode;
import io.delimeat.show.domain.EpisodeStatus;
import io.delimeat.show.domain.Show;
import io.delimeat.show.domain.ShowType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=WebConfig.class)
@SpringBootTest()
public class ShowControllerTest {

	private MockMvc mockMvc;
	
	@InjectMocks
    private ShowController showController;
	
	@Mock
	private ShowService showService;
	
	@Mock
	private EpisodeService episodeService;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders
                .standaloneSetup(showController)
                .build();
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
		show.setShowId(99);
		show.setShowType(ShowType.DAILY);
		show.setTimezone("TIMEZONE");
		show.setTitle("TITLE");
		show.setVersion(999);
		
		Mockito.when(showService.readAll()).thenReturn(Arrays.asList(show));
		
		mockMvc.perform(
            get("/api/show")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.length()").value(1))
			.andExpect(jsonPath("$[0].airing").value(true))
			//TODO once object mapper problem solved
			.andExpect(jsonPath("$[0].airTime").exists())
			//.andExpect(jsonPath("$[0].airTime").value(LocalTime.MIDNIGHT.toString()))
			.andExpect(jsonPath("$[0].enabled").value(false))
			.andExpect(jsonPath("$[0].guideId").value("GUIDEID"))
			//TODO once object mapper problem solved
			.andExpect(jsonPath("$[0].lastGuideCheck").exists())
			//.andExpect(jsonPath("$[0].lastGuideCheck").value(Instant.ofEpochMilli(1).toString()))
			//TODO once object mapper problem solved
			.andExpect(jsonPath("$[0].lastGuideUpdate").exists())
			//.andExpect(jsonPath("$[0].lastGuideUpdate").value(Instant.EPOCH.toString()))
			.andExpect(jsonPath("$[0].maxSize").value(Integer.MAX_VALUE))
			.andExpect(jsonPath("$[0].minSize").value(Integer.MIN_VALUE))
			.andExpect(jsonPath("$[0].showId").value(99))
			.andExpect(jsonPath("$[0].showType").value("DAILY"))
			.andExpect(jsonPath("$[0].timezone").value("TIMEZONE"))
			.andExpect(jsonPath("$[0].title").value("TITLE"))
			.andExpect(jsonPath("$[0].version").doesNotExist());
								
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
		show.setShowId(99);
		show.setShowType(ShowType.DAILY);
		show.setTimezone("TIMEZONE");
		show.setTitle("TITLE");
		show.setVersion(999);
		
		Mockito.when(showService.read(1L)).thenReturn(show);
		
		mockMvc.perform(
            get("/api/show/1")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.airing").value(true))
			//TODO once object mapper problem solved
			.andExpect(jsonPath("$.airTime").exists())
			//.andExpect(jsonPath("$[0].airTime").value(LocalTime.MIDNIGHT.toString()))
			.andExpect(jsonPath("$.enabled").value(false))
			.andExpect(jsonPath("$.guideId").value("GUIDEID"))
			//TODO once object mapper problem solved
			.andExpect(jsonPath("$.lastGuideCheck").exists())
			//.andExpect(jsonPath("$[0].lastGuideCheck").value(Instant.ofEpochMilli(1).toString()))
			//TODO once object mapper problem solved
			.andExpect(jsonPath("$.lastGuideUpdate").exists())
			//.andExpect(jsonPath("$[0].lastGuideUpdate").value(Instant.EPOCH.toString()))
			.andExpect(jsonPath("$.maxSize").value(Integer.MAX_VALUE))
			.andExpect(jsonPath("$.minSize").value(Integer.MIN_VALUE))
			.andExpect(jsonPath("$.showId").value(99))
			.andExpect(jsonPath("$.showType").value("DAILY"))
			.andExpect(jsonPath("$.timezone").value("TIMEZONE"))
			.andExpect(jsonPath("$.title").value("TITLE"))
			.andExpect(jsonPath("$.version").doesNotExist());
								
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
		show.setShowId(99);
		show.setShowType(ShowType.DAILY);
		show.setTimezone("TIMEZONE");
		show.setTitle("TITLE");
		show.setVersion(999);
		
		Mockito.when(showService.update(Mockito.any())).thenReturn(show);
		
		mockMvc.perform(
            put("/api/show/1")
            		.content(json(show))
            		.accept(MediaType.APPLICATION_JSON)
            		.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.airing").value(true))
			//TODO once object mapper problem solved
			.andExpect(jsonPath("$.airTime").exists())
			//.andExpect(jsonPath("$[0].airTime").value(LocalTime.MIDNIGHT.toString()))
			.andExpect(jsonPath("$.enabled").value(false))
			.andExpect(jsonPath("$.guideId").value("GUIDEID"))
			//TODO once object mapper problem solved
			.andExpect(jsonPath("$.lastGuideCheck").exists())
			//.andExpect(jsonPath("$[0].lastGuideCheck").value(Instant.ofEpochMilli(1).toString()))
			//TODO once object mapper problem solved
			.andExpect(jsonPath("$.lastGuideUpdate").exists())
			//.andExpect(jsonPath("$[0].lastGuideUpdate").value(Instant.EPOCH.toString()))
			.andExpect(jsonPath("$.maxSize").value(Integer.MAX_VALUE))
			.andExpect(jsonPath("$.minSize").value(Integer.MIN_VALUE))
			.andExpect(jsonPath("$.showId").value(99))
			.andExpect(jsonPath("$.showType").value("DAILY"))
			.andExpect(jsonPath("$.timezone").value("TIMEZONE"))
			.andExpect(jsonPath("$.title").value("TITLE"))
			.andExpect(jsonPath("$.version").doesNotExist());
								
		Mockito.verify(showService).update(Mockito.any());
		Mockito.verifyNoMoreInteractions(showService);
	}	
	
	@Test
	public void deleteTest() throws Exception{
		
		mockMvc.perform(
            delete("/api/show/1")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk());
								
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
		show.setShowId(99);
		show.setShowType(ShowType.DAILY);
		show.setTimezone("TIMEZONE");
		show.setTitle("TITLE");
		show.setVersion(999);
		
		mockMvc.perform(
            post("/api/show")
            		.content(json(show))
            		.accept(MediaType.APPLICATION_JSON)
            		.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.airing").value(true))
			//TODO once object mapper problem solved
			.andExpect(jsonPath("$.airTime").exists())
			//.andExpect(jsonPath("$[0].airTime").value(LocalTime.MIDNIGHT.toString()))
			.andExpect(jsonPath("$.enabled").value(false))
			.andExpect(jsonPath("$.guideId").value("GUIDEID"))
			//TODO once object mapper problem solved
			.andExpect(jsonPath("$.lastGuideCheck").exists())
			//.andExpect(jsonPath("$[0].lastGuideCheck").value(Instant.ofEpochMilli(1).toString()))
			//TODO once object mapper problem solved
			.andExpect(jsonPath("$.lastGuideUpdate").exists())
			//.andExpect(jsonPath("$[0].lastGuideUpdate").value(Instant.EPOCH.toString()))
			.andExpect(jsonPath("$.maxSize").value(Integer.MAX_VALUE))
			.andExpect(jsonPath("$.minSize").value(Integer.MIN_VALUE))
			.andExpect(jsonPath("$.showId").value(99))
			.andExpect(jsonPath("$.showType").value("DAILY"))
			.andExpect(jsonPath("$.timezone").value("TIMEZONE"))
			.andExpect(jsonPath("$.title").value("TITLE"))
			.andExpect(jsonPath("$.version").doesNotExist());
								
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
		show.setShowId(99);
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
		episode.setShow(show);
		episode.setStatus(EpisodeStatus.SKIPPED);
		episode.setTitle("TITLE");
		episode.setVersion(1);
		
		Mockito.when(episodeService.findByShow(Mockito.any())).thenReturn(Arrays.asList(episode));
		
		mockMvc.perform(
            get("/api/show/1/episodes")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.length()").value(1))
			//TODO once object mapper problem solved
			.andExpect(jsonPath("$[0].airDate").exists())
			//.andExpect(jsonPath("$[0].airDate").value(LocalDate.ofEpochDay(100)))
			.andExpect(jsonPath("$[0].doubleEp").value(true))
			.andExpect(jsonPath("$[0].episodeId").value(Long.MAX_VALUE))
			.andExpect(jsonPath("$[0].episodeNum").value(99))
			//TODO once object mapper problem solved
			.andExpect(jsonPath("$[0].lastFeedCheck").exists())
			//.andExpect(jsonPath("$[0].lastFeedCheck").value(Instant.ofEpochSecond(100000)))
			//TODO once object mapper problem solved
			.andExpect(jsonPath("$[0].lastFeedUpdate").exists())
			//.andExpect(jsonPath("$[0].lastFeedUpdate").value(Instant.ofEpochSecond(10000)))
			.andExpect(jsonPath("$[0].seasonNum").value(50))
			.andExpect(jsonPath("$[0].status").value("SKIPPED"))
			.andExpect(jsonPath("$[0].title").value("TITLE"))
			.andExpect(jsonPath("$[0].version").doesNotExist())
			.andExpect(jsonPath("$[0].show").doesNotExist());
								
		Mockito.verify(episodeService).findByShow(Mockito.any());
		Mockito.verifyNoMoreInteractions(episodeService);
	}
	
	private String json(Object object) throws Exception{
		return new ObjectMapper()
				.registerModule(new JavaTimeModule())
				.writeValueAsString(object);
	}

}
