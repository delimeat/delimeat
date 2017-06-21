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

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class ProcessorService_Impl implements ProcessorService  {
		
	@Autowired
	private ApplicationContext applicationContext;
	
	@Override
	@Scheduled(fixedDelayString="${io.delimeat.processor.feed.schedule}", initialDelayString="${io.delimeat.processor.feed.schedule.initial}")
	public void processAllFeedUpdates() throws Exception {
		FeedItemReader_Impl reader = applicationContext.getBean(FeedItemReader_Impl.class);
		FeedItemProcessor_Impl processor = applicationContext.getBean(FeedItemProcessor_Impl.class);
		
		run(reader, processor);
	}
	
	@Override
	@Scheduled(fixedDelayString="${io.delimeat.processor.guide.schedule}", initialDelayString="${io.delimeat.processor.guide.schedule.initial}")
	public void processAllGuideUpdates()  throws Exception {

		GuideItemReader_Impl reader = applicationContext.getBean(GuideItemReader_Impl.class);
		GuideItemProcessor_Impl processor = applicationContext.getBean(GuideItemProcessor_Impl.class);
		
		run(reader, processor);
	}

	
	@Transactional
	public <I> void run(ItemReader<I> itemReader, ItemProcessor<I> itemProcessor) throws Exception{
		I inputItem = null;
		while((inputItem = itemReader.read()) != null){
			itemProcessor.process(inputItem);
		}
	}

}
