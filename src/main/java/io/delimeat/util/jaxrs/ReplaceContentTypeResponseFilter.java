package io.delimeat.util.jaxrs;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;

public class ReplaceContentTypeResponseFilter implements ClientResponseFilter {

	@Override
	public void filter(ClientRequestContext request, ClientResponseContext response)
			throws IOException {
		response.getHeaders().remove("Content-Type");
		response.getHeaders().add("Content-Type", request.getHeaderString("Accept"));
	}

}
