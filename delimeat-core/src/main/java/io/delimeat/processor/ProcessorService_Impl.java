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

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessorService_Impl implements ProcessorService  {
		
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorService_Impl.class);
	
	private FeedItemReader feedItemReader;
	private FeedItemProcessor feedItemProcessor;
	
	private GuideItemReader guideItemReader;
	private GuideItemProcessor guideItemProcessor;

	/**
	 * @return the feedItemReader
	 */
	public FeedItemReader getFeedItemReader() {
		return feedItemReader;
	}

	/**
	 * @param feedItemReader the feedItemReader to set
	 */
	public void setFeedItemReader(FeedItemReader feedItemReader) {
		this.feedItemReader = feedItemReader;
	}

	/**
	 * @return the feedItemProcessor
	 */
	public FeedItemProcessor getFeedItemProcessor() {
		return feedItemProcessor;
	}

	/**
	 * @param feedItemProcessor the feedItemProcessor to set
	 */
	public void setFeedItemProcessor(FeedItemProcessor feedItemProcessor) {
		this.feedItemProcessor = feedItemProcessor;
	}

	/**
	 * @return the guideItemReader
	 */
	public GuideItemReader getGuideItemReader() {
		return guideItemReader;
	}

	/**
	 * @param guideItemReader the guideItemReader to set
	 */
	public void setGuideItemReader(GuideItemReader guideItemReader) {
		this.guideItemReader = guideItemReader;
	}

	/**
	 * @return the guideItemProcessor
	 */
	public GuideItemProcessor getGuideItemProcessor() {
		return guideItemProcessor;
	}

	/**
	 * @param guideItemProcessor the guideItemProcessor to set
	 */
	public void setGuideItemProcessor(GuideItemProcessor guideItemProcessor) {
		this.guideItemProcessor = guideItemProcessor;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.processor.ProcessorService#processAllFeedUpdates()
	 */
	@Override
	//@Scheduled(fixedDelayString="${io.delimeat.processor.feed.schedule}", initialDelayString="${io.delimeat.processor.feed.initial}")
	public void processAllFeedUpdates() throws Exception {
		run(feedItemReader, feedItemProcessor);
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.processor.ProcessorService#processAllGuideUpdates()
	 */
	@Override
	//@Scheduled(fixedDelayString="${io.delimeat.processor.guide.schedule}", initialDelayString="${io.delimeat.processor.guide.initial}")
	public void processAllGuideUpdates()  throws Exception {
		run(guideItemReader, guideItemProcessor);
	}

	
	@Transactional
	public <I> void run(ItemReader<I> itemReader, ItemProcessor<I> itemProcessor) throws Exception{
		List<I> items = itemReader.readItems();
		for(I inputItem: items){
			try{
				itemProcessor.process(inputItem);
			}catch(Exception ex){
				LOGGER.error(String.format("encountered an error processing %s for item %s", itemProcessor.getClass().getName(), inputItem), ex);
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProcessorService_Impl [" + (feedItemReader != null ? "feedItemReader=" + feedItemReader + ", " : "")
				+ (feedItemProcessor != null ? "feedItemProcessor=" + feedItemProcessor + ", " : "")
				+ (guideItemReader != null ? "guideItemReader=" + guideItemReader + ", " : "")
				+ (guideItemProcessor != null ? "guideItemProcessor=" + guideItemProcessor : "") + "]";
	}

	

}
