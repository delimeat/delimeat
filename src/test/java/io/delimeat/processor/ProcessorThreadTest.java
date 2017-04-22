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
