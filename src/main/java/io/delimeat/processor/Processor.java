package io.delimeat.processor;

public interface Processor {

	public void process() throws Exception;

	public void abort();

	public void addListener(ProcessorListener listener);

	public void removeListener(ProcessorListener listener);
}
