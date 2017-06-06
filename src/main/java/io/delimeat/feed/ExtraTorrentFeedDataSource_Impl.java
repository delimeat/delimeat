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

import io.delimeat.feed.domain.FeedSource;
import lombok.Getter;
import lombok.Setter;

//@Component
@Getter
@Setter
public class ExtraTorrentFeedDataSource_Impl extends AbstractMoxyFeedDataSource implements FeedDataSource{

	@Value("${io.delimeat.feed.extratorrent.baseUri}") 
	private URI baseUri;
	
	public ExtraTorrentFeedDataSource_Impl() {
		super(FeedSource.EXTRATORRENT,MediaType.APPLICATION_XML_TYPE,"META-INF/oxm/feed-extratorrent-oxm.xml");
	}

	/* (non-Javadoc)
	 * @see io.delimeat.feed.AbstractJaxrsFeedDataSource#prepareRequest(javax.ws.rs.client.WebTarget, java.lang.String)
	 */
	@Override
	protected WebTarget prepareRequest(final WebTarget target, final String title) {
		return target.path("rss.xml")
				.queryParam("type", "search")
				.queryParam("search", title);
	}
}
