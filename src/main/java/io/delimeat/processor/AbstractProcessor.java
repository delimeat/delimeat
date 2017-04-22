package io.delimeat.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import io.delimeat.config.domain.Config;
import io.delimeat.processor.exception.ProcessorInteruptedException;

public abstract class AbstractProcessor<T> implements Processor {

	protected T processEntity;
    protected boolean active = false;
    protected final List<ProcessorListener> listeners = new ArrayList<ProcessorListener>();
    protected Config config;

    @Override
    public void abort() {
      active = false;
    }

    public void setActive(boolean active) {
      this.active = active;
    }

    public boolean isActive() {
      return active;
    }

    @Override
    public void addListener(ProcessorListener listener) {
      listeners.add(listener);
    }

    @Override
    public void removeListener(ProcessorListener listener) {
      listeners.remove(listener);
    }

    public List<ProcessorListener> getListeners(){
      return listeners;
    }
  
    public void setProcessEntity(T processEntity){
    	this.processEntity = processEntity;
    }
    
    public T getProcessEntity(){
    	return processEntity;
    }
    
    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
    
    public void checkThreadStatus() throws ProcessorInteruptedException{
    	if(Thread.currentThread().isInterrupted()){
    		throw new ProcessorInteruptedException();
    	}	
    }
    
    public abstract void doProcessing() throws Exception;
    
    @Transactional(TxType.REQUIRES_NEW)
    @Override
    public void process() throws Exception {
    	Objects.requireNonNull(config);
        if (active == false) {
            try {
                active = true;
                // this does all the work
                doProcessing();
				
			} finally {
		        active = false;
		        listeners.stream().forEach(s->s.alertComplete(this));
			}
		}
	}
}
