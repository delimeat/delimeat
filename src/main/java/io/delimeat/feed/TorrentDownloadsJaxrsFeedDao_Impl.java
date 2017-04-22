package io.delimeat.feed;

import java.util.List;

import javax.ws.rs.core.GenericType;

import io.delimeat.feed.domain.FeedResult;
import io.delimeat.feed.domain.FeedSearch;
import io.delimeat.feed.domain.FeedSource;
import io.delimeat.feed.exception.FeedException;

public class TorrentDownloadsJaxrsFeedDao_Impl extends AbstractJaxrsFeedDao {

	TorrentDownloadsJaxrsFeedDao_Impl() {
		super(FeedSource.TORRENTDOWNLOADS);
	}

	@Override
	public List<FeedResult> read(String title) throws FeedException {
		return get(getTarget()
				.queryParam("type", "search")
				.queryParam("search", urlEncodeString(title))
				.request(getMediaType()), new GenericType<FeedSearch>(){}).getResults();
	}

}
