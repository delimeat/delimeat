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
package io.delimeat.http;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import io.delimeat.http.domain.HttpStatistics;

@Service
public class HttpStatisticsService_Impl implements HttpStatisticsService {

	private final Map<String,HttpStatistics> stats = new HashMap<>();
	
	/* (non-Javadoc)
	 * @see io.delimeat.feed.statistics.FeedStatisticsService#addResponse(java.net.URI, javax.ws.rs.core.Response.StatusType)
	 */
	@Override
	public void addResponse(URI uri, int statusCode) {
		HttpStatistics hostStats = stats.compute(uri.getHost(), (host,stats)->(stats==null)? new HttpStatistics(host): stats);
		hostStats.getResponseCounts().compute(statusCode, (key,value)-> (value != null) ? value + 1 : 1 );
		if(statusCode/100 == 2){
			hostStats.setLastSuccess(Instant.now());
		}else{
			hostStats.setLastFailure(Instant.now());
		}
	}
	
	public List<HttpStatistics> getStatistics(){
		return new ArrayList<>(stats.values());
	}

	/* (non-Javadoc)
	 * @see io.delimeat.http.HttpStatisticsService#addResponse(java.net.URL, int)
	 */
	@Override
	public void addResponse(URL url, int statusCode) {
		try{
			addResponse(url.toURI(),statusCode);
		}catch(URISyntaxException ex){
			//TODO add some logging of the exception?
		}
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HttpStatisticsService_Impl [" + (stats != null ? "stats=" + stats : "") + "]";
	}

}
