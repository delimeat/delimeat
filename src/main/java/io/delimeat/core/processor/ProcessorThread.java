package io.delimeat.core.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ProcessorThread implements Runnable {

  private static final Log LOG = LogFactory.getLog(ProcessorThread.class);

  private final Processor processor;
  
  public ProcessorThread(Processor processor){
    this.processor = processor;
  }
  @Override
  public void run() {
    try{
    	processor.process();
    }catch(Exception ex){
      LOG.error("ENCOUNTERED AN ERROR IN PROCESSOR THREAD", ex);
    }

  }
}
