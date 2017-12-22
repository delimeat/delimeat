package io.delimeat.feed;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
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

public class JaxrsFeedDataSource_Impl implements FeedDataSource {

	private static final Logger LOGGER = LoggerFactory.getLogger(JaxrsFeedDataSource_Impl.class);
	private static final String ENCODING = "UTF-8";

  	private URI baseUri;
	private FeedSource feedSource;
	private FeedTargetFactory targetFactory;
	private Map<String, Object> moxyProperties = new HashMap<>();
	private MediaType mediaType;
	
	private Client client = null;
	
	public Client getClient() {
		if(client == null) {
			client = ClientBuilder.newClient();
			client.register(new MoxyJAXBFeature(moxyProperties, Arrays.asList(FeedResult.class,FeedSearch.class)));
		}
		return client;
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
	 * @param feedSource the feedSource to set
	 */
	public void setFeedSource(FeedSource feedSource) {
		this.feedSource = feedSource;
	}

	/**
	 * @return the targetFactory
	 */
	public FeedTargetFactory getTargetFactory() {
		return targetFactory;
	}

	/**
	 * @param targetFactory the targetFactory to set
	 */
	public void setTargetFactory(FeedTargetFactory targetFactory) {
		this.targetFactory = targetFactory;
	}

	/**
	 * @return the moxyProperties
	 */
	public Map<String, Object> getMoxyProperties() {
		return moxyProperties;
	}

	/**
	 * @param moxyProperties the moxyProperties to set
	 */
	public void setMoxyProperties(Map<String, Object> moxyProperties) {
		this.moxyProperties = moxyProperties;
	}

	/**
	 * @return the mediaType
	 */
	public MediaType getMediaType() {
		return mediaType;
	}

	/**
	 * @param mediaType the mediaType to set
	 */
	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.feed.FeedDataSource#read(java.lang.String)
	 */
	@Override
	public List<FeedResult> read(String title) throws FeedTimeoutException, FeedContentTypeException,
			FeedResponseException, FeedResponseBodyException, FeedException {
		LOGGER.trace("reading for title \"{}\"", title);
		final String encodedTitle = DelimeatUtils.urlEscape(title, ENCODING);
	     
		List<FeedResult> results = null;
        try {
        	results = targetFactory.build(getClient(), getBaseUri(), encodedTitle)
        			.request(mediaType)
        			.get(FeedSearch.class)
        			.getResults();

        } catch (WebApplicationException | ProcessingException ex) {
        	LOGGER.error("an error occured reading", ex);
        	throw new FeedException(ex);
        }
        
        results.forEach(p->p.setSource(feedSource));
        LOGGER.trace("returning results for \"{}\"\n{}", title, results);
        return results;
	}

}
