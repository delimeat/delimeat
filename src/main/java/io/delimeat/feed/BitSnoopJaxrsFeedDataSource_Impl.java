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

import java.net.URI;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.delimeat.feed.domain.FeedSource;

@Component
public class BitSnoopJaxrsFeedDataSource_Impl extends AbstractJaxrsFeedDataSource implements FeedDataSource {

	@Value("${io.delimeat.feed.bitsnoop.baseUri}") 
	private URI baseUri;

	public BitSnoopJaxrsFeedDataSource_Impl(){
		super(FeedSource.BITSNOOP, MediaType.APPLICATION_XML_TYPE,"META-INF/oxm/feed-bitsnoop-oxm.xml");
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.feed.AbstractJaxrsFeedDataSource#getBaseUri()
	 */
	@Override
	public URI getBaseUri() {
		return baseUri;
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.feed.AbstractJaxrsFeedDataSource#setBaseUri(java.net.URI)
	 */
	@Override
	public void setBaseUri(URI baseUri) {
		this.baseUri = baseUri;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.feed.AbstractJaxrsFeedDataSource#prepareRequest(javax.ws.rs.client.WebTarget, java.lang.String)
	 */
	@Override
	protected WebTarget prepareRequest(final WebTarget target, final String title) {
		return target.path("/search/video/")
				.path(title)
				.path("/c/d/1/")
				.queryParam("fmt", "rss");
	}

}
