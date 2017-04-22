package io.delimeat.feed;

import java.util.List;

import javax.ws.rs.core.GenericType;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedSearch;
import io.delimeat.feed.domain.FeedSource;
import io.delimeat.feed.exception.FeedException;

public class ZooqleJaxrsFeedDao_Impl extends AbstractJaxrsFeedDao {

	ZooqleJaxrsFeedDao_Impl() {
		super(FeedSource.ZOOQLE);
	}

	@Override
	public List<FeedResult> read(String title) throws FeedException {
		return get(getTarget()
				.path("search")
				.queryParam("q", urlEncodeString(title) + " after:60 category:TV")
				.queryParam("fmt", "rss")
				.request(getMediaType()), new GenericType<FeedSearch>(){}).getResults();
	}

}
