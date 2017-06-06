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
package io.delimeat.http.domain;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.MoreObjects;

public class HttpStatistics {

	private final String host;
	private final Map<Integer, Integer> responseCounts;
	
	private Instant lastSuccess;
	private Instant lastFailure;
	
	public HttpStatistics(String host){
		this.host = host;
		responseCounts = new HashMap<>();
	}

	/**
	 * @return the feedSource
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return the responseCounts
	 */
	public Map<Integer, Integer> getResponseCounts() {
		return responseCounts;
	}
	
	/**
	 * @return the lastSuccess
	 */
	public Instant getLastSuccess() {
		return lastSuccess;
	}

	/**
	 * @param lastSuccess the lastSuccess to set
	 */
	public void setLastSuccess(Instant lastSuccess) {
		this.lastSuccess = lastSuccess;
	}

	/**
	 * @return the lastFailure
	 */
	public Instant getLastFailure() {
		return lastFailure;
	}

	/**
	 * @param lastFailure the lastFailure to set
	 */
	public void setLastFailure(Instant lastFailure) {
		this.lastFailure = lastFailure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
							.add("host", host)
							.add("responseCounts", responseCounts)
							.add("lastSuccess", lastSuccess)
							.add("lastFailure", lastFailure)
							.omitNullValues()
							.toString();
	}

}
