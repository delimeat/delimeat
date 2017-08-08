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
import java.util.Objects;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import io.delimeat.util.jaxb.InstantAdapter;

public class HttpStatistics {

	private String host;

	private final Map<Integer, Integer> responseCounts = new HashMap<>();
	
	@XmlJavaTypeAdapter(InstantAdapter.class)
	private Instant lastSuccess;
	@XmlJavaTypeAdapter(InstantAdapter.class)
	private Instant lastFailure;
		
	public HttpStatistics(){	
	}
	/**
	 * @param host
	 */
	public HttpStatistics(String host) {
		super();
		this.host = host;
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
	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}
	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}
	/**
	 * @return the responseCounts
	 */
	public Map<Integer, Integer> getResponseCounts() {
		return responseCounts;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(host,lastFailure,lastSuccess);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HttpStatistics other = (HttpStatistics) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (lastFailure == null) {
			if (other.lastFailure != null)
				return false;
		} else if (!lastFailure.equals(other.lastFailure))
			return false;
		if (lastSuccess == null) {
			if (other.lastSuccess != null)
				return false;
		} else if (!lastSuccess.equals(other.lastSuccess))
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HttpStatistics [" + (host != null ? "host=" + host + ", " : "")
				+ (responseCounts != null ? "responseCounts=" + responseCounts + ", " : "")
				+ (lastSuccess != null ? "lastSuccess=" + lastSuccess + ", " : "")
				+ (lastFailure != null ? "lastFailure=" + lastFailure : "") + "]";
	}
	
	
}
