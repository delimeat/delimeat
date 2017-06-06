/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.delimeat.feed.jaxrs;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

@Priority(Priorities.HEADER_DECORATOR)
public class ReplaceContentTypeResponseFilter implements ClientResponseFilter {

	/* (non-Javadoc)
	 * @see javax.ws.rs.client.ClientResponseFilter#filter(javax.ws.rs.client.ClientRequestContext, javax.ws.rs.client.ClientResponseContext)
	 */
	@Override
	public void filter(ClientRequestContext request, ClientResponseContext response) throws IOException {
		if(Response.Status.Family.SUCCESSFUL.equals(response.getStatusInfo().getFamily())){
			final String acceptString = request.getHeaderString("Accept");
			final MultivaluedMap<String, String> headers = response.getHeaders();
			if (acceptString != null && acceptString.length() > 0 && acceptString.equals(headers.getFirst("Content-Type")) == false) {
				headers.remove("Content-Type");
				headers.add("Content-Type", acceptString);
			}
		}
	}

}
