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

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.exception.FeedException;

@Service("feedServiceId")
public class FeedService_Impl implements FeedService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FeedService_Impl.class);
	
	@Autowired
	private List<FeedDao> feedDaos;
	
    public List<FeedDao> getFeedDaos() {
        return feedDaos;
    }

    public void setFeedDaos(List<FeedDao> feedDaos) {
        this.feedDaos = feedDaos;
    }
    
	@Override
	public List<FeedResult> read(String title) throws FeedException {
		List<FeedResult> readResults =  new ArrayList<>();
		for(FeedDao dao: feedDaos){
			try{
				LOGGER.debug(String.format("Reading from %s ",dao.getFeedSource()));
				List<FeedResult> results = dao.read(title);
				LOGGER.debug(String.format("read %s results from %s", results.size(), dao.getFeedSource()));
				readResults.addAll(results);
			}catch(FeedException ex){
				LOGGER.warn(String.format("Encountered an error reading from %s, continuing", dao.getFeedSource()), ex);
				//TODO add some logging of statistics of failure/success by source
			}
		}
		return readResults;
	}

}
