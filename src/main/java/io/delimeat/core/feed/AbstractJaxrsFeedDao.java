package io.delimeat.core.feed;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.WebTarget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.delimeat.util.jaxrs.client.AbstractJaxrsClientHelper;

public abstract class AbstractJaxrsFeedDao extends AbstractJaxrsClientHelper implements
		FeedDao {

	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

	private FeedSource feedSource;
	
	public void setFeedSource(FeedSource feedSource){
		this.feedSource = feedSource;
	}
	
	@Override
	public FeedSource getFeedSource() {
		return feedSource;
	}

	public abstract WebTarget buildTarget(final WebTarget target, final String encodedTitle);
	
	@Override
	public List<FeedResult> read(String title) throws FeedException {
	       final String encodedTitle;
	        try {
	            encodedTitle = URLEncoder.encode(title, getEncoding());
	        } catch (UnsupportedEncodingException ex) {
	            throw new RuntimeException(ex);
	        }
	      
	        try {
	        	WebTarget target = buildTarget(getTarget(), encodedTitle);
	        	log.info(String.format("Reading from %s", target.getUri()));
	        	
	            return target.request(getMediaType())
	            		.get(FeedSearch.class)
	              		.getResults();

	        } catch (WebApplicationException | ProcessingException ex) {
	            throw new FeedException(ex);
	        }
	}

}
