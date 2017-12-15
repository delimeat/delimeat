package io.delimeat.feed;

import java.util.List;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;

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
import io.delimeat.util.jaxrs.AbstractJaxrsClient;

public class JaxrsFeedDataSource_Impl extends AbstractJaxrsClient implements FeedDataSource {

	private static final Logger LOGGER = LoggerFactory.getLogger(JaxrsFeedDataSource_Impl.class);

	private FeedSource feedSource;
	private FeedTargetFactory targetFactory;
			
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
	 * @return the feedTargetFactory
	 */
	public FeedTargetFactory getTargetFactory() {
		return targetFactory;
	}

	/**
	 * @param feedTargetFactory the feedTargetFactory to set
	 */
	public void setTargetFactory(FeedTargetFactory targetFactory) {
		this.targetFactory = targetFactory;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.feed.FeedDataSource#read(java.lang.String)
	 */
	@Override
	public List<FeedResult> read(String title) throws FeedTimeoutException, FeedContentTypeException,
			FeedResponseException, FeedResponseBodyException, FeedException {
		LOGGER.trace("reading for title \"{}\"", title);
		final String encodedTitle = DelimeatUtils.urlEscape(title, "UTF-8");
	     
		List<FeedResult> results = null;
        try {
           	Client client = clientFactory.build();
        	results = targetFactory.build(client, getBaseUri(), encodedTitle)
        			.request(getMediaType())
        			.get(FeedSearch.class)
        			.getResults();

        } catch (WebApplicationException | ProcessingException ex) {
        	LOGGER.error("an error occured reading", ex);
        	throw new FeedException(ex);
        }
        LOGGER.trace("returning results for \"{}\"\n{}", title, results);
        return results;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "JaxrsFeedClient_Impl [feedSource=" + feedSource + ", targetFactory=" + targetFactory + ", encoding="
				+ encoding + ", baseUri=" + baseUri + ", mediaType=" + mediaType + ", clientFactory=" + clientFactory
				+ "]";
	}
	
	

}
