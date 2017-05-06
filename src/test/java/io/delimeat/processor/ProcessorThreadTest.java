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

import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import io.delimeat.processor.Processor;
import io.delimeat.processor.ProcessorThread;

public class ProcessorThreadTest {
  	
	@Test
	public void successfulTest() throws Exception {
		Processor processor = Mockito.mock(Processor.class);
		Logger log = Mockito.mock(Logger.class);
     
     	ProcessorThread thread = new ProcessorThread(processor,log);
     	thread.run();
     
     	Mockito.verify(processor,Mockito.times(1)).process();
     	Mockito.verify(log, Mockito.times(0)).error(Mockito.anyString(),Mockito.any(Exception.class));
	}
  
	@Test
	public void exceptionTest() throws Exception {
		Processor processor = Mockito.mock(Processor.class);
     	Mockito.doThrow(Exception.class).when(processor).process();
     	Logger log = Mockito.mock(Logger.class);
     
     	ProcessorThread thread = new ProcessorThread(processor,log);
     	thread.run();
     
     	Mockito.verify(processor,Mockito.times(1)).process();
     	Mockito.verify(log, Mockito.times(1)).error(Mockito.anyString(),Mockito.any(Exception.class));
	}
}
