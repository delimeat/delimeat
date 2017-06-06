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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import io.delimeat.config.domain.Config;
import io.delimeat.processor.exception.ProcessorInteruptedException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractProcessor<T> implements Processor {

	protected T processEntity;
    protected boolean active = false;
    protected final List<ProcessorListener> listeners = new ArrayList<ProcessorListener>();
    protected Config config;

    @Override
    public void abort() {
      active = false;
    }

    @Override
    public void addListener(ProcessorListener listener) {
      listeners.add(listener);
    }

    @Override
    public void removeListener(ProcessorListener listener) {
      listeners.remove(listener);
    }
    
    public void checkThreadStatus() throws ProcessorInteruptedException{
    	if(Thread.currentThread().isInterrupted()){
    		throw new ProcessorInteruptedException(this);
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
