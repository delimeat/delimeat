package io.delimeat.feed;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

public interface FeedTargetFactory {
	
	WebTarget build(Client client, URI baseUri, String title);

}