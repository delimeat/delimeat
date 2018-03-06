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
package io.delimeat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.delimeat.processor.ProcessorService;

public class ScheduledTasksTest {
	
	private ScheduledTasks tasks;
	
	@BeforeEach
	public void setUp() {
		tasks = new ScheduledTasks();
	}
	
	@Test
	public void serviceTest() {
		Assertions.assertNull(tasks.getService());
		ProcessorService service = Mockito.mock(ProcessorService.class);
		tasks.setService(service);
		
		Assertions.assertEquals(service, tasks.getService());
	}
	
	@Test
	public void processAllFeedUpdatesTest() throws Exception {
		ProcessorService service = Mockito.mock(ProcessorService.class);
		tasks.setService(service);
		
		tasks.processAllFeedUpdates();
		
		Mockito.verify(service).processAllFeedUpdates();
	}
	
	@Test
	public void processAllGuideUpdatesTest() throws Exception {
		ProcessorService service = Mockito.mock(ProcessorService.class);
		tasks.setService(service);
		
		tasks.processAllGuideUpdates();
		
		Mockito.verify(service).processAllGuideUpdates();
	}
	
}
