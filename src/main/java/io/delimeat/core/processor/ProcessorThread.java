package io.delimeat.core.processor;

import org.slf4j.Logger;

public class ProcessorThread implements Runnable {

	private final Logger Logger;
	private final Processor processor;

	public ProcessorThread(Processor processor, Logger Logger) {
		this.processor = processor;
		this.Logger = Logger;
	}

	@Override
	public void run() {	
		try {		
			processor.process();		
		} catch (Exception ex) {
			Logger.error(String.format("ENCOUNTERED AN ERROR IN PROCESSOR %s", processor), ex);
		}
	}
}
