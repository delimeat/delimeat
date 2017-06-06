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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedSearch;
import io.delimeat.feed.domain.FeedSource;
import io.delimeat.util.jaxrs.MoxyContextResolver;

public abstract class AbstractMoxyFeedDataSource extends AbstractJaxrsFeedDataSource {	

	public AbstractMoxyFeedDataSource(FeedSource feedSource, MediaType mediaType, String metadata_source){
		super(feedSource, mediaType, Arrays.asList(getResolver(metadata_source, mediaType.toString())), Collections.emptyList());
	}
	
	private static MoxyContextResolver getResolver(String metadata_source, String mediaType){
		MoxyContextResolver resolver = new MoxyContextResolver();
		resolver.setClasses(FeedSearch.class,FeedResult.class);
		Map<String,Object> properties = new HashMap<String,Object>();
		properties.put("eclipselink.oxm.metadata-source", metadata_source);
		properties.put("eclipselink.media-type", mediaType);
		properties.put("eclipselink.json.include-root", false);
		resolver.setProperties(properties);	
		return resolver;
	}
}
