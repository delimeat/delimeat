package io.delimeat.util.jaxrs.client;

import io.delimeat.util.DelimeatUtils;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.MultivaluedMap;

@Priority(Priorities.HEADER_DECORATOR)
public class ReplaceContentTypeResponseFilter implements ClientResponseFilter {

	@Override
	public void filter(ClientRequestContext request, ClientResponseContext response)
			throws IOException {
		final String acceptString = request.getHeaderString("Accept");
		final MultivaluedMap<String, String> headers = response.getHeaders();
		if(DelimeatUtils.isNotEmpty(acceptString) &&  headers.getFirst("Content-Type") != acceptString){
			headers.remove("Content-Type");
			headers.add("Content-Type", acceptString);
		}
	}

}
