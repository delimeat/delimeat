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
package io.delimeat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.delimeat.processor.ProcessorService;

@Component
public class ScheduledTasks {

	@Autowired
	public ProcessorService service;
	
	/**
	 * @return the service
	 */
	public ProcessorService getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(ProcessorService service) {
		this.service = service;
	}

	@Scheduled(fixedDelayString = "${io.delimeat.processor.feed.schedule}", initialDelayString = "${io.delimeat.processor.feed.initial}")
	public void processAllFeedUpdates() throws Exception {
		service.processAllFeedUpdates();
	}
	
	@Scheduled(fixedDelayString = "${io.delimeat.processor.guide.schedule}", initialDelayString = "${io.delimeat.processor.guide.initial}" )
	public void processAllGuideUpdates() throws Exception {
		service.processAllFeedUpdates();
	}
}
