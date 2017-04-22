package io.delimeat.feed;

import java.util.List;

import javax.ws.rs.core.GenericType;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedSearch;
import io.delimeat.feed.domain.FeedSource;
import io.delimeat.feed.exception.FeedException;

public class BitSnoopJaxrsFeedDao_Impl extends AbstractJaxrsFeedDao {

	BitSnoopJaxrsFeedDao_Impl() {
		super(FeedSource.BITSNOOP);
	}

	@Override
	public List<FeedResult> read(String title) throws FeedException {
		return get(getTarget()
				.path("search/video")
				.path(urlEncodeString(title))
				.path("c/d/1/")
				.queryParam("fmt", "rss")
				.request(getMediaType()), new GenericType<FeedSearch>(){}).getResults();
	}

}
