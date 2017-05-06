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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.delimeat.feed.domain.FeedSource;

@Component
public class TorrentProjectJaxrsFeedDao_Impl extends AbstractJaxrsFeedDao{
	
	@Autowired
	public TorrentProjectJaxrsFeedDao_Impl(@Value("${io.delimeat.feed.torrentproject.baseUri}") final URI baseUri) {
		super(FeedSource.TORRENTPROJECT, MediaType.APPLICATION_XML_TYPE, "META-INF/oxm/feed-torrentproject-oxm.xml", baseUri);
	}

	@Override
	protected WebTarget prepareRequest(String title) {
		return getTarget()
				.queryParam("s", title)
				.queryParam("out", "rss");
	}


}
