package io.delimeat.core.processor;

import java.util.ArrayList;
import java.util.List;

import io.delimeat.core.config.Config;
import io.delimeat.core.processor.ProcessorListener;
import io.delimeat.core.show.Show;

public abstract class AbstractProcessor implements Processor {

    protected boolean active = false;
    protected final List<ProcessorListener> listeners = new ArrayList<ProcessorListener>();
    protected Show show;
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

    public void setShow(Show show) {
      this.show = show;
    }

    public Show getShow() {
      return show;
    }
  
    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
  
    public void alertListenersComplete(){
        active = false;
        final List<ProcessorListener> listenersCopy = new ArrayList<ProcessorListener>(listeners);
        for(ProcessorListener listener: listenersCopy){
          listener.alertComplete(this);
        }
    }
}
