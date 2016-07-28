package io.delimeat.core.feed;

import javax.ws.rs.client.WebTarget;

public class ZooqleJaxrsFeedDao_Impl extends AbstractJaxrsFeedDao {

	@Override
	public WebTarget buildTarget(WebTarget target, String encodedTitle) {
		return target.path("search")
				.queryParam("q", encodedTitle + " after:60 category:TV")
				.queryParam("fmt", "rss");
		//https://zooqle.com/search?q=daily+show+after%3A30&fmt=rss
	}

}
