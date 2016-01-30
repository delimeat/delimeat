package io.delimeat.core.processor;

import org.apache.commons.logging.Log;

public class ProcessorThread implements Runnable {

	private final Log log;
	private final Processor processor;

	public ProcessorThread(Processor processor, Log log) {
		this.processor = processor;
		this.log = log;
	}

	@Override
	public void run() {	
		try {		
			processor.process();		
		} catch (Exception ex) {
			log.error("ENCOUNTERED AN ERROR IN PROCESSOR " + processor, ex);
		}
	}
}
