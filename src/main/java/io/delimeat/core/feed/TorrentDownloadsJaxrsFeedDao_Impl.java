package io.delimeat.core.feed;

import javax.ws.rs.client.WebTarget;

public class TorrentDownloadsJaxrsFeedDao_Impl extends AbstractJaxrsFeedDao {

	@Override
	public WebTarget buildTarget(WebTarget target, String encodedTitle) {
		return target.queryParam("type", "search")
					.queryParam("search", encodedTitle);
	}

}
