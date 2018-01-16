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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.delimeat.feed.entity.FeedResult;
import io.delimeat.feed.entity.FeedSearch;
import io.delimeat.feed.entity.FeedSource;
import io.delimeat.feed.exception.FeedContentTypeException;
import io.delimeat.feed.exception.FeedException;
import io.delimeat.feed.exception.FeedResponseBodyException;
import io.delimeat.feed.exception.FeedResponseException;
import io.delimeat.feed.exception.FeedTimeoutException;
import io.delimeat.util.DelimeatUtils;
import io.delimeat.util.jaxrs.MoxyJAXBFeature;

public abstract class AbstractJaxrsFeedDataSource implements FeedDataSource {

	private static final String ENCODING = "UTF-8";
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	private final FeedSource feedSource;
	private final Map<String, Object> moxyProperties;
	private final MediaType mediaType;
	
  	private URI baseUri;
  	
  	public AbstractJaxrsFeedDataSource(FeedSource feedSource, Map<String, Object> moxyProperties, MediaType mediaType) {
  		this.feedSource = feedSource;
  		this.moxyProperties = moxyProperties;
  		this.mediaType = mediaType;
  	}

	/**
	 * @return the baseUri
	 */
	public URI getBaseUri() {
		return baseUri;
	}

	/**
	 * @param baseUri the baseUri to set
	 */
	public void setBaseUri(URI baseUri) {
		this.baseUri = baseUri;
	}

	/**
	 * @return the feedSource
	 */
	public FeedSource getFeedSource() {
		return feedSource;
	}

	/**
	 * @return the moxyProperties
	 */
	public Map<String, Object> getMoxyProperties() {
		return moxyProperties;
	}

	/**
	 * @return the mediaType
	 */
	public MediaType getMediaType() {
		return mediaType;
	}

	abstract Invocation buildInvocation(WebTarget target, String encodedTitle, MediaType mediaType);
	
	/* (non-Javadoc)
	 * @see io.delimeat.feed.FeedDataSource#read(java.lang.String)
	 */
	@Override
	public List<FeedResult> read(String title) throws FeedTimeoutException, FeedContentTypeException,
			FeedResponseException, FeedResponseBodyException, FeedException {
		LOGGER.trace("reading for title \"{}\"", title);
		String encodedTitle = DelimeatUtils.urlEscape(title, ENCODING);
	     
		List<FeedResult> results = null;
        try {
        	WebTarget target = ClientBuilder.newBuilder()
								.register(new MoxyJAXBFeature(moxyProperties, Arrays.asList(FeedResult.class, FeedSearch.class)))
								.build()
								.target(baseUri);
        	
        	results = buildInvocation(target, encodedTitle, mediaType)
        			.invoke(FeedSearch.class)
        			.getResults();

        	//TODO better exception handling
        } catch (WebApplicationException | ProcessingException ex) {
        	LOGGER.error("an error occured reading", ex);
        	throw new FeedException(ex);
        }
        
        results.forEach(p->p.setSource(feedSource));
        LOGGER.trace("returning results for \"{}\"\n{}", title, results);
        return results;
	}

}
