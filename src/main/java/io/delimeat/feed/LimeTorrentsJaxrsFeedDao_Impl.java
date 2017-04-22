package io.delimeat.feed;

import java.util.List;

import javax.ws.rs.core.GenericType;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedSearch;
import io.delimeat.feed.domain.FeedSource;
import io.delimeat.feed.exception.FeedException;

public class LimeTorrentsJaxrsFeedDao_Impl extends AbstractJaxrsFeedDao{

	LimeTorrentsJaxrsFeedDao_Impl() {
		super(FeedSource.LIMETORRENTS);
	}

	@Override
	public List<FeedResult> read(String title) throws FeedException {
		return get(getTarget()
				.path(urlEncodeString(title)+"/")
				.request(getMediaType()), new GenericType<FeedSearch>(){}).getResults();
	}

}
