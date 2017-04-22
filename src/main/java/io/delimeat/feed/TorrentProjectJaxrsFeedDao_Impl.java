package io.delimeat.feed;

import java.util.List;

import javax.ws.rs.core.GenericType;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedSearch;
import io.delimeat.feed.domain.FeedSource;
import io.delimeat.feed.exception.FeedException;

public class TorrentProjectJaxrsFeedDao_Impl extends AbstractJaxrsFeedDao{

	TorrentProjectJaxrsFeedDao_Impl() {
		super(FeedSource.TORRENTPROJECT);
	}

	@Override
	public List<FeedResult> read(String title) throws FeedException {
		return get(getTarget()
				.queryParam("s", urlEncodeString(title))
				.queryParam("out", "rss")
				.request(getMediaType()), new GenericType<FeedSearch>(){}).getResults();
	}


}
