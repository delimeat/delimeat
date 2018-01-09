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

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import io.delimeat.feed.entity.FeedSource;

public class ZooqleDataSource_Impl extends AbstractJaxrsFeedDataSource {

	public ZooqleDataSource_Impl() {
		super(FeedSource.ZOOQLE, getProperties(), MediaType.APPLICATION_XML_TYPE);
	}
	
	private static Map<String,Object> getProperties(){
		Map<String,Object> properties = new HashMap<>();
		properties.put("eclipselink.media-type", "application/xml");
		properties.put("eclipselink.oxm.metadata-source", "oxm/feed-zooqle-oxm.xml");
		return properties;
	}
	
	@Override
	public Invocation buildInvocation(WebTarget target, String encodedTitle, MediaType mediaType) {
		return target.path("search")
				.queryParam("q", String.format("%s+after:60+category:TV", encodedTitle))
				.queryParam("fmt", "rss")
				.request(mediaType)
				.buildGet();
	}
	
}
