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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import io.delimeat.config.domain.Config;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@SpringBootTest()
public class ConfigControllerTest {

	private MockMvc mockMvc;
	
	@InjectMocks
    private ConfigController configController;
	
	@Mock
	private ConfigService configService;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders
                .standaloneSetup(configController)
                .build();
	}
	
	@Test
	public void readTest() throws Exception{
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
		
		Mockito.when(configService.read()).thenReturn(config);
		
		mockMvc.perform(
            get("/api/config")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.configId").doesNotExist())
			.andExpect(jsonPath("$.version").value(Long.MAX_VALUE))
			.andExpect(jsonPath("$.preferFiles").value(true))
			.andExpect(jsonPath("$.ignoreFolders").value(false))
			.andExpect(jsonPath("$.outputDirectory").value("OUT_DIR"))
			.andExpect(jsonPath("$.searchDelay").value(Integer.MAX_VALUE))
			.andExpect(jsonPath("$.searchInterval").value(99))
			.andExpect(jsonPath("$.excludedKeywords.length()").value(1))
			.andExpect(jsonPath("$.excludedKeywords[0]").value("EXCLUDED"))
			.andExpect(jsonPath("$.ignoredFileTypes.length()").value(1))
			.andExpect(jsonPath("$.ignoredFileTypes[0]").value("IGNORED"));
								
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
		
		Mockito.when(configService.update(Mockito.any())).thenReturn(config);
		
		mockMvc.perform(put("/api/config")
                .content(json(config))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
    			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
    			.andExpect(jsonPath("$.configId").doesNotExist())
    			.andExpect(jsonPath("$.version").value(Long.MAX_VALUE))
    			.andExpect(jsonPath("$.preferFiles").value(true))
    			.andExpect(jsonPath("$.ignoreFolders").value(false))
    			.andExpect(jsonPath("$.outputDirectory").value("OUT_DIR"))
    			.andExpect(jsonPath("$.searchDelay").value(Integer.MAX_VALUE))
    			.andExpect(jsonPath("$.searchInterval").value(99))
    			.andExpect(jsonPath("$.excludedKeywords.length()").value(1))
    			.andExpect(jsonPath("$.excludedKeywords[0]").value("EXCLUDED"))
    			.andExpect(jsonPath("$.ignoredFileTypes.length()").value(1))
    			.andExpect(jsonPath("$.ignoredFileTypes[0]").value("IGNORED"));
								
		Mockito.verify(configService).update(Mockito.any());
		Mockito.verifyNoMoreInteractions(configService);
	}
	
	private String json(Object object) throws Exception{
		return new ObjectMapper()
				.registerModule(new JavaTimeModule())
				.writeValueAsString(object);
	}

}
