package io.delimeat.core.processor;

public interface Processor {

	public boolean process() throws Exception;

	public void abort();

	public void addListener(ProcessorListener listener);

	public void removeListener(ProcessorListener listener);
}
