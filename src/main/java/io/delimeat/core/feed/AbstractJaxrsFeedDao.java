package io.delimeat.core.feed;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.WebTarget;

import io.delimeat.common.util.jaxrs.client.AbstractJaxrsClientHelper;

public abstract class AbstractJaxrsFeedDao extends AbstractJaxrsClientHelper implements
		FeedDao {

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
	            return buildTarget(getTarget(), encodedTitle)
	              					.request(getMediaType())
	              					.get(FeedSearch.class)
	              					.getResults();

	        } catch (WebApplicationException | ProcessingException ex) {
	            throw new FeedException(ex);
	        }
	}

}
