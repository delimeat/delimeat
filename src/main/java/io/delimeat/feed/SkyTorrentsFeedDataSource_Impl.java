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

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.delimeat.feed.domain.FeedSource;

@Component
public class SkyTorrentsFeedDataSource_Impl extends AbstractJaxbFeedDataSource {

	@Value("${io.delimeat.feed.skytorrents.baseUri}") 
	private String baseUri;
	
	public SkyTorrentsFeedDataSource_Impl(){
		super(FeedSource.SKYTORRENTS, "application/xml","oxm/feed-skytorrents-oxm.xml", "text/xml");
	}
	
	/**
	 * @return the baseUri
	 */
	public String getBaseUri() {
		return baseUri;
	}

	/**
	 * @param baseUri the baseUri to set
	 */
	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.feed.AbstractJaxbFeedDataSource#generateUrl(java.lang.String)
	 */
	@Override
	public URL generateUrl(String title) throws MalformedURLException {
		return new URL(String.format("%s/rss/all/ad/1/%s", baseUri, title));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SkyTorrentsFeedDataSource_Impl [" + (baseUri != null ? "baseUri=" + baseUri + ", " : "")
				+ (getFeedSource() != null ? "getFeedSource()=" + getFeedSource() + ", " : "")
				+ (getProperties() != null ? "getProperties()=" + getProperties() + ", " : "")
				+ (getHeaders() != null ? "getHeaders()=" + getHeaders() : "") + "]";
	}

}
