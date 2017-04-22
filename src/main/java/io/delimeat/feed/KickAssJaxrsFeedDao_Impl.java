package io.delimeat.feed;

import java.util.List;

import javax.ws.rs.core.GenericType;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedSearch;
import io.delimeat.feed.domain.FeedSource;
import io.delimeat.feed.exception.FeedException;

public class KickAssJaxrsFeedDao_Impl extends AbstractJaxrsFeedDao{

	KickAssJaxrsFeedDao_Impl() {
		super(FeedSource.KAT);
	}

	@Override
	public List<FeedResult> read(String title) throws FeedException {
		return get(getTarget()
				.queryParam("q", urlEncodeString(title) + " category:tv")
				.request(getMediaType()), new GenericType<FeedSearch>(){}).getResults();
	}

}
