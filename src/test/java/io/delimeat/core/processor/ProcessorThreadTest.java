package io.delimeat.core.processor;

import org.apache.commons.logging.Log;
import org.junit.Test;
import org.mockito.Mockito;

public class ProcessorThreadTest {
  	
	@Test
	public void successfulTest() throws Exception {
		Processor processor = Mockito.mock(Processor.class);
		Log log = Mockito.mock(Log.class);
     
     	ProcessorThread thread = new ProcessorThread(processor,log);
     	thread.run();
     
     	Mockito.verify(processor,Mockito.times(1)).process();
     	Mockito.verify(log, Mockito.times(0)).error(Mockito.anyString(),Mockito.any(Exception.class));
	}
  
	@Test
	public void exceptionTest() throws Exception {
		Processor processor = Mockito.mock(Processor.class);
     	Mockito.doThrow(Exception.class).when(processor).process();
     	Log log = Mockito.mock(Log.class);
     
     	ProcessorThread thread = new ProcessorThread(processor,log);
     	thread.run();
     
     	Mockito.verify(processor,Mockito.times(1)).process();
     	Mockito.verify(log, Mockito.times(1)).error(Mockito.anyString(),Mockito.any(Exception.class));
	}
}
