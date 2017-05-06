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
package io.delimeat.client.util.jaxrs;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.google.common.base.Strings;

@ETag
@Priority(Priorities.HEADER_DECORATOR)
@Provider
public class AddETagResponseFilter implements ContainerResponseFilter {

	/* (non-Javadoc)
	 * @see javax.ws.rs.container.ContainerResponseFilter#filter(javax.ws.rs.container.ContainerRequestContext, javax.ws.rs.container.ContainerResponseContext)
	 */
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		if (responseContext.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
			return;
		}

		final String etagStr = responseContext.getHeaderString(HttpHeaders.ETAG);
		final Object entity = responseContext.getEntity();
		if (entity != null && Strings.isNullOrEmpty(etagStr) == true) {
			EntityTag eTag = new EntityTag(Integer.toString(entity.hashCode()));
			responseContext.getHeaders().add(HttpHeaders.ETAG, eTag);
		}

	}
}
