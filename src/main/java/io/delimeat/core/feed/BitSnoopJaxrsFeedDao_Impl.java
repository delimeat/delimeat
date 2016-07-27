package io.delimeat.core.feed;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;

import io.delimeat.common.util.jaxrs.client.AbstractJaxrsClientHelper;

public class BitSnoopJaxrsFeedDao_Impl extends AbstractJaxrsClientHelper
		implements FeedDao {

	@Override
	public FeedSource getFeedSource() {
		return FeedSource.BITSNOOP;
	}

	@Override
	public List<FeedResult> read(String title) throws FeedException {
        final String encodedTitle;
        try {
            encodedTitle = URLEncoder.encode(title, getEncoding());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
      
        try {
            return getTarget().path("search/video")
            					.path(encodedTitle)
            					.path("c/d/1/")
            					.queryParam("fmt", "rss")
              					.request(getMediaType())
              					.get(FeedSearch.class)
              					.getResults();

        } catch (WebApplicationException | ProcessingException ex) {
            throw new FeedException(ex);
        }
	}

}
