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

import io.delimeat.show.entity.Episode;
import io.delimeat.show.entity.Show;

public class ProcessorService_ImplTest {

	private ProcessorService_Impl service;

	@BeforeEach
	public void setUp() throws Exception {
		service = new ProcessorService_Impl();
	}
	
	@Test
	public void toStringTest(){
		Assertions.assertEquals("ProcessorService_Impl []", service.toString());
	}
	
	@Test
	public void feedItemReaderTest() {
		Assertions.assertNull(service.getFeedItemReader());
		FeedItemReader itemReader = Mockito.mock(FeedItemReader.class);
		service.setFeedItemReader(itemReader);
		Assertions.assertEquals(itemReader, service.getFeedItemReader());
	}
	
	@Test
	public void feedItemProcessorTest() {
		Assertions.assertNull(service.getFeedItemProcessor());
		FeedItemProcessor itemProcessor = Mockito.mock(FeedItemProcessor.class);
		service.setFeedItemProcessor(itemProcessor);
		Assertions.assertEquals(itemProcessor, service.getFeedItemProcessor());
	}
	
	@Test
	public void guideItemReaderTest() {
		Assertions.assertNull(service.getGuideItemReader());
		GuideItemReader itemReader = Mockito.mock(GuideItemReader.class);
		service.setGuideItemReader(itemReader);
		Assertions.assertEquals(itemReader, service.getGuideItemReader());
	}
	
	@Test
	public void guideItemProcessorTest() {
		Assertions.assertNull(service.getGuideItemProcessor());
		GuideItemProcessor itemProcessor = Mockito.mock(GuideItemProcessor.class);
		service.setGuideItemProcessor(itemProcessor);
		Assertions.assertEquals(itemProcessor, service.getGuideItemProcessor());
	}

	@Test
	public void processAllGuideUpdatesTest() throws Exception {
		Show show = new Show();
		show.setEnabled(true);
		
		GuideItemReader reader = Mockito.mock(GuideItemReader.class);
		Mockito.when(reader.readItems()).thenReturn(Arrays.asList(show));
		service.setGuideItemReader(reader);
		GuideItemProcessor processor = Mockito.mock(GuideItemProcessor.class);
		service.setGuideItemProcessor(processor);
		
		service.processAllGuideUpdates();

		Mockito.verify(reader).readItems();
		Mockito.verifyNoMoreInteractions(reader);
		
		Mockito.verify(processor).process(show);
		Mockito.verifyNoMoreInteractions(processor);
	}
	
	@Test
	public void processAllFeedUpdatesTest() throws Exception {
		Episode episode = new Episode();
		
		FeedItemReader reader = Mockito.mock(FeedItemReader.class);
		Mockito.when(reader.readItems()).thenReturn(Arrays.asList(episode));
		service.setFeedItemReader(reader);
		FeedItemProcessor processor = Mockito.mock(FeedItemProcessor.class);
		service.setFeedItemProcessor(processor);
		
		service.processAllFeedUpdates();

		Mockito.verify(reader).readItems();
		Mockito.verifyNoMoreInteractions(reader);
		
		Mockito.verify(processor).process(episode);
		Mockito.verifyNoMoreInteractions(processor);

	}
	
	@Test
	public void processorExceptionTest() throws Exception{
		FeedItemReader reader =  Mockito.mock(FeedItemReader.class);
		Mockito.when(reader.readItems()).thenReturn(Arrays.asList(new Episode()));
		FeedItemProcessor processor = Mockito.mock(FeedItemProcessor.class);
		Mockito.doThrow(Exception.class).when(processor).process(Mockito.any());
		
		service.run(reader, processor);
		
		Mockito.verify(reader).readItems();
		Mockito.verify(processor).process(Mockito.any());
		Mockito.verifyNoMoreInteractions(reader,processor);
		
	}

}
