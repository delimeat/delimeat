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
package io.delimeat.processor;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.delimeat.show.ShowService;
import io.delimeat.show.entity.Show;

public class GuideItemReader_ImplTest {

	private GuideItemReader_Impl reader;
	
	@BeforeEach
	public void setUp(){
		reader = new GuideItemReader_Impl();
	}
	
	@Test
	public void showServiceTest(){
		Assertions.assertNull(reader.getShowService());
		ShowService showService = Mockito.mock(ShowService.class);
		reader.setShowService(showService);
		Assertions.assertEquals(showService, reader.getShowService());
	}
	
	@Test
	public void toStringTest(){
		Assertions.assertEquals("GuideItemReader_Impl []", reader.toString());
	}
	
	@Test
	public void readTest() throws Exception{
		ShowService showService = Mockito.mock(ShowService.class);
		Show show1 = new Show();
		show1.setEnabled(true);
		Show show2 = new Show();
		show2.setEnabled(false);
		Mockito.when(showService.readAll()).thenReturn(Arrays.asList(show1, show2, show1));
		
		reader.setShowService(showService);
		
		List<Show> result = reader.readItems();
		Assertions.assertNotNull(result);
		Assertions.assertEquals(2, result.size());
		
		Assertions.assertEquals(show1, result.get(0));
		Assertions.assertEquals(show1, result.get(1));
	}
}
