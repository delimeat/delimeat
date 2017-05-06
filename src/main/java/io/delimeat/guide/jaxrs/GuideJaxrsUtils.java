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
package io.delimeat.guide.jaxrs;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;

import io.delimeat.guide.domain.GuideError;
import io.delimeat.guide.exception.GuideAuthorizationException;
import io.delimeat.guide.exception.GuideException;
import io.delimeat.guide.exception.GuideNotFoundException;

public class GuideJaxrsUtils {

	public static <T> T get(Builder builder, GenericType<T> returnType) throws GuideException{
		try {
			
			return builder.get(returnType);

		} catch (NotAuthorizedException ex) {
			GuideError error = ex.getResponse().readEntity(GuideError.class);
			throw new GuideAuthorizationException(error.getMessage(), ex);
		} catch (NotFoundException ex) {
			GuideError error = ex.getResponse().readEntity(GuideError.class);
			throw new GuideNotFoundException(error.getMessage(), ex);
		} catch (WebApplicationException ex) {
			throw new GuideException(ex);
		}
	}
	
	public static <T,S> T put(Builder builder, Entity<S> entity, GenericType<T> returnType) throws GuideException{
        try {
            return builder.post(entity, returnType);
          
		} catch (NotAuthorizedException ex) {
			GuideError error = ex.getResponse().readEntity(GuideError.class);
			throw new GuideAuthorizationException(error.getMessage(), ex);
		} catch (NotFoundException ex) {
			GuideError error = ex.getResponse().readEntity(GuideError.class);
			throw new GuideAuthorizationException(error.getMessage(), ex);
		} catch (WebApplicationException ex) {
			throw new GuideException(ex);
		}
	}
}
