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

import org.slf4j.Logger;

public class ProcessorThread extends Thread {

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
