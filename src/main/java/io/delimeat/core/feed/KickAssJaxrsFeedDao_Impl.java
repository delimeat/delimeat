package io.delimeat.core.feed;

import io.delimeat.util.jaxrs.AbstractJaxrsClientHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.ws.rs.WebApplicationException;

public class KickAssJaxrsFeedDao_Impl extends AbstractJaxrsClientHelper implements FeedDao {

	@Override
	public FeedSource getFeedSource() {
		return FeedSource.KAT;
	}
	
	@Override
	public List<FeedResult> read(String title) throws FeedException {
        String encodedTitle;
        try {
            encodedTitle = URLEncoder.encode(title, ENCODING);
        } catch (UnsupportedEncodingException ex) {
        	encodedTitle = title;
        }
        try {
            return getTarget()
            		.queryParam("q", encodedTitle + " category:tv")
              		.request(getMediaType())
              		.get(FeedSearch.class)
              		.getResults();
          
        } catch (WebApplicationException ex) {
            throw new FeedException(ex);
        }
	}
}
