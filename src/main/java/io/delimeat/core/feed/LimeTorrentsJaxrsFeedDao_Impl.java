package io.delimeat.core.feed;

import javax.ws.rs.client.WebTarget;

public class LimeTorrentsJaxrsFeedDao_Impl extends AbstractJaxrsFeedDao{

	@Override
	public WebTarget buildTarget(WebTarget target, String encodedTitle) {
		return target.path(encodedTitle+"/");
	}

}
