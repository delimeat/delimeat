package io.delimeat.feed;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;

import io.delimeat.feed.domain.FeedSource;
import io.delimeat.feed.exception.FeedException;
import io.delimeat.util.jaxrs.client.AbstractJaxrsClientHelper;

public abstract class AbstractJaxrsFeedDao extends AbstractJaxrsClientHelper implements
		FeedDao {

	protected final FeedSource feedSource;
	
	AbstractJaxrsFeedDao(FeedSource feedSource){
		this.feedSource = feedSource;
	}
	
	@Override
	public FeedSource getFeedSource() {
		return feedSource;
	}
	
	public <T> T get(Builder builder, GenericType<T> returnType) throws FeedException{
		try {
			
			return builder.get(returnType);

        } catch (WebApplicationException | ProcessingException ex) {
            throw new FeedException(ex);
        }
		
	}


}
