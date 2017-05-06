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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.logging.LoggingFeature;

import com.google.common.net.UrlEscapers;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedSearch;
import io.delimeat.feed.domain.FeedSource;
import io.delimeat.feed.exception.FeedException;
import io.delimeat.util.jaxrs.JaxbContextResolver;

public abstract class AbstractJaxrsFeedDao implements FeedDao {

	private final FeedSource feedSource;
	private final MediaType mediaType;
	private final Client client;
	private final URI baseUri;
	
	AbstractJaxrsFeedDao(FeedSource feedSource, MediaType mediaType, String metadata_source, URI baseUri){
		this.feedSource = feedSource;
		this.mediaType =  mediaType;
		this.baseUri = baseUri;
		client = ClientBuilder.newClient();
		JaxbContextResolver resolver = new JaxbContextResolver();
		resolver.setClasses(FeedSearch.class,FeedResult.class);
		Map<String,Object> properties = new HashMap<String,Object>();
		properties.put("eclipselink.oxm.metadata-source", metadata_source);
		properties.put("eclipselink.media-type", mediaType.toString());
		properties.put("eclipselink.json.include-root", false);
		resolver.setProperties(properties);
		client.register(resolver);
		client.register(new LoggingFeature(Logger.getLogger(LoggingFeature.class.getName()), java.util.logging.Level.SEVERE, LoggingFeature.Verbosity.PAYLOAD_ANY, LoggingFeature.DEFAULT_MAX_ENTITY_SIZE));
	}
	
	/**
	 * @return the baseUri
	 */
	protected URI getBaseUri() {
		return baseUri;
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.feed.FeedDao#getFeedSource()
	 */
	@Override
	public FeedSource getFeedSource() {
		return feedSource;
	}
	
	protected WebTarget getTarget(){
		return client.target(baseUri);
	}
	
	protected abstract WebTarget prepareRequest(String title);

	/* (non-Javadoc)
	 * @see io.delimeat.feed.FeedDao#read(java.lang.String)
	 */
	@Override
	public List<FeedResult> read(String title) throws FeedException {
		try {
			
			return prepareRequest(UrlEscapers.urlPathSegmentEscaper().escape(title))
					.request(mediaType)
					.get(new GenericType<FeedSearch>(){})
					.getResults();

        } catch (WebApplicationException | ProcessingException ex) {
            throw new FeedException(ex);
        }
	}
	/*
	protected static <T> T get(Builder builder, GenericType<T> returnType) throws FeedException{
		try {
			
			return builder.get(returnType);

        } catch (WebApplicationException | ProcessingException ex) {
            throw new FeedException(ex);
        }
		
	}*/

}
