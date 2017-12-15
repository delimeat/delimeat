package io.delimeat.feed;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

public class TorrentProjectTargetFactory_Impl implements FeedTargetFactory {

	@Override
	public WebTarget build(Client client, URI baseUri, String title) {
		return client.target(baseUri)
				.path("rss")
				.path(title+"/");
	}

}
