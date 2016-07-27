package io.delimeat.core.feed;

import javax.ws.rs.client.WebTarget;

public class BitSnoopJaxrsFeedDao_Impl extends AbstractJaxrsFeedDao {

	@Override
	public WebTarget buildTarget(WebTarget target, String encodedTitle) {
		return target.path("search/video")
				.path(encodedTitle)
				.path("c/d/1/")
				.queryParam("fmt", "rss");
	}

}
