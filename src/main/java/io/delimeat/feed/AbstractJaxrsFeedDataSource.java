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
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.logging.LoggingFeature;
import org.springframework.beans.factory.annotation.Autowired;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedSearch;
import io.delimeat.feed.domain.FeedSource;
import io.delimeat.feed.exception.FeedException;
import io.delimeat.feed.jaxrs.ReplaceContentTypeResponseFilter;
import io.delimeat.http.jaxrs.HttpStatisticsResponseFilter;
import io.delimeat.util.DelimeatUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractJaxrsFeedDataSource implements FeedDataSource {
	
	private final MediaType mediaType;
	private final FeedSource feedSource;
	private final List<Object> resources;
	private final List<Class<?>> resourceClasses;
	
	@Autowired
	private HttpStatisticsResponseFilter statsFilter;
	
	private Client client;

	public AbstractJaxrsFeedDataSource(FeedSource feedSource, MediaType mediaType, List<Object> resources, List<Class<?>> resourceClasses){
		this.feedSource = feedSource;
		this.mediaType = mediaType;
		this.resources = resources;
		this.resourceClasses = resourceClasses;
	}
	
	private Client getClient(){
		if(client == null){
			client = ClientBuilder.newClient();
			resources.forEach(resource->client.register(resource));
			resourceClasses.forEach(resource->client.register(resource));
			if(statsFilter != null){
				client.register(statsFilter);
			}
			client.register(ReplaceContentTypeResponseFilter.class);
			client.register(new LoggingFeature(Logger.getLogger(this.getClass().getName()), java.util.logging.Level.SEVERE, LoggingFeature.Verbosity.PAYLOAD_ANY, LoggingFeature.DEFAULT_MAX_ENTITY_SIZE));
		}
		return client;
	}
	
	/**
	 * @return the baseUri
	 */
	public abstract URI getBaseUri();
	
	/**
	 * @param baseUri the baseUri to set
	 */
	public abstract void setBaseUri(URI baseUri);
	
	/**
	 * Prepare a jax-rs request
	 * @param target
	 * @param title
	 * @return target
	 */
	protected abstract WebTarget prepareRequest(final WebTarget target, final String title);

	/* (non-Javadoc)
	 * @see io.delimeat.feed.FeedDao#read(java.lang.String)
	 */
	@Override
	public List<FeedResult> read(String title) throws FeedException {
		
		try {

			return prepareRequest(getClient().target(getBaseUri()), DelimeatUtils.urlEscape(title))
					.request(mediaType)
					.get(new GenericType<FeedSearch>(){})
					.getResults();

        } catch ( WebApplicationException | ProcessingException ex) {
            throw new FeedException(ex);
        }
	}

}
