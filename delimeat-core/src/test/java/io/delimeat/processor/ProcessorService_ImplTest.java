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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;

import io.delimeat.processor.FeedItemProcessor_Impl;
import io.delimeat.processor.FeedItemReader_Impl;
import io.delimeat.processor.GuideItemProcessor_Impl;
import io.delimeat.processor.GuideItemReader_Impl;
import io.delimeat.processor.ItemProcessor;
import io.delimeat.processor.ItemReader;
import io.delimeat.show.entity.Episode;
import io.delimeat.show.entity.Show;

public class ProcessorService_ImplTest {

	private ProcessorService_Impl service;

	@BeforeEach
	public void setUp() throws Exception {
		service = new ProcessorService_Impl();
	}
	
	@Test
	public void applicationContextTest(){
		Assertions.assertNull(service.getApplicationContext());
		ApplicationContext context = Mockito.mock(ApplicationContext.class);
		service.setApplicationContext(context);
		
		Assertions.assertEquals(context, service.getApplicationContext());
	}
	
	@Test
	public void toStringTest(){
		Assertions.assertEquals("ProcessorService_Impl []", service.toString());
	}

	@Test
	public void processAllGuideUpdatesTest() throws Exception {
		Show show = new Show();
		show.setEnabled(true);
		
		GuideItemReader_Impl reader = Mockito.mock(GuideItemReader_Impl.class);
		Mockito.when(reader.readItems()).thenReturn(Arrays.asList(show));
		GuideItemProcessor_Impl processor = Mockito.mock(GuideItemProcessor_Impl.class);
		
		ApplicationContext context = Mockito.mock(ApplicationContext.class);
		Mockito.when(context.getBean(GuideItemReader_Impl.class)).thenReturn(reader);
		Mockito.when(context.getBean(GuideItemProcessor_Impl.class)).thenReturn(processor);
		service.setApplicationContext(context);
		
		service.processAllGuideUpdates();

		Mockito.verify(reader).readItems();
		Mockito.verifyNoMoreInteractions(reader);
		
		Mockito.verify(processor).process(show);
		Mockito.verifyNoMoreInteractions(processor);

		Mockito.verify(context).getBean(GuideItemReader_Impl.class);
		Mockito.verify(context).getBean(GuideItemProcessor_Impl.class);
		Mockito.verifyNoMoreInteractions(context);
		
	}
	
	@Test
	public void processAllFeedUpdatesTest() throws Exception {
		Episode episode = new Episode();
		
		FeedItemReader_Impl reader = Mockito.mock(FeedItemReader_Impl.class);
		Mockito.when(reader.readItems()).thenReturn(Arrays.asList(episode));
		FeedItemProcessor_Impl processor = Mockito.mock(FeedItemProcessor_Impl.class);
		
		ApplicationContext context = Mockito.mock(ApplicationContext.class);
		Mockito.when(context.getBean(FeedItemReader_Impl.class)).thenReturn(reader);
		Mockito.when(context.getBean(FeedItemProcessor_Impl.class)).thenReturn(processor);
		service.setApplicationContext(context);
		
		service.processAllFeedUpdates();

		Mockito.verify(reader).readItems();
		Mockito.verifyNoMoreInteractions(reader);
		
		Mockito.verify(processor).process(episode);
		Mockito.verifyNoMoreInteractions(processor);

		Mockito.verify(context).getBean(FeedItemReader_Impl.class);
		Mockito.verify(context).getBean(FeedItemProcessor_Impl.class);
		Mockito.verifyNoMoreInteractions(context);
	}
	
	@Test
	public void processorExceptionTest() throws Exception{
		@SuppressWarnings("unchecked") 
		ItemReader<Object> reader =  Mockito.mock(ItemReader.class);
		Mockito.when(reader.readItems()).thenReturn(Arrays.asList(new Object()));
		@SuppressWarnings("unchecked") 
		ItemProcessor<Object> processor = Mockito.mock(ItemProcessor.class);
		Mockito.doThrow(Exception.class).when(processor).process(Mockito.any());
		
		service.run(reader, processor);
		
		Mockito.verify(reader).readItems();
		Mockito.verify(processor).process(Mockito.any());
		Mockito.verifyNoMoreInteractions(reader,processor);
		
	}

}
