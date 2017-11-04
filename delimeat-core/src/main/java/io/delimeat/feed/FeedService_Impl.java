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
package io.delimeat.feed;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.delimeat.feed.entity.FeedResult;
import io.delimeat.feed.entity.FeedSource;
import io.delimeat.feed.exception.FeedException;

@Service
public class FeedService_Impl implements FeedService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FeedService_Impl.class);
	
	@Autowired
	private List<FeedDataSource> feedDataSources;
    
	/**
	 * @return the feedDataSources
	 */
	public List<FeedDataSource> getFeedDataSources() {
		return feedDataSources;
	}

	/**
	 * @param feedDataSources the feedDataSources to set
	 */
	public void setFeedDataSources(List<FeedDataSource> feedDataSources) {
		this.feedDataSources = feedDataSources;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.processor.feed.FeedService#read(java.lang.String)
	 */
	@Override
	public List<FeedResult> read(String title) throws FeedException {
		List<FeedResult> readResults =  new ArrayList<>();
		for(FeedDataSource ds: feedDataSources){
			FeedSource source = ds.getFeedSource();
			try{
				LOGGER.debug(String.format("Reading from %s ",source));
				List<FeedResult> results = ds.read(title);
				LOGGER.debug(String.format("read %s results from %s", results.size(), source));
				readResults.addAll(results);
			}catch(FeedException ex){
				LOGGER.warn(String.format("Encountered an error reading from %s, continuing",source));
				//TODO add some logging of statistics of failure/success by source
			}
		}
		return readResults;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FeedService_Impl [" + (feedDataSources != null ? "feedDataSources=" + feedDataSources : "") + "]";
	}
	
	
	

}
