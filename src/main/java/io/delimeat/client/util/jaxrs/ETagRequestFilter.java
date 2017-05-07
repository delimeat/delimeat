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

import com.google.common.collect.Iterables;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Priority;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.PathParam;
import javax.ws.rs.Priorities;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

@ETag
@Priority(Priorities.HEADER_DECORATOR)
@Provider
public class ETagRequestFilter implements ContainerRequestFilter {

	private static final String UNEXPECTED_RETURN_TYPE = "Expected return type of %s on method %s but found %s ";
	private static final String EXPECTED_PARAM_MISSING = "Expected %s with value %s for parameter # %s on method %s for class %s";
	private static final String UNEXPECTED_GENERATOR_PARAM = "Expected QueryParam/PathParam annotation for parameter # %s on method %s for class %s";

	@Context
	private ResourceInfo resourceInfo;

	/**
	 * Get resource info
	 * 
	 * @param resourceInfo
	 */
	public void setResourceInfo(ResourceInfo resourceInfo) {
		this.resourceInfo = resourceInfo;
	}

	/**
	 * @return resource info
	 */
	public ResourceInfo getResourceInfo() {
		return resourceInfo;
	}

	/* (non-Javadoc)
	 * @see javax.ws.rs.container.ContainerRequestFilter#filter(javax.ws.rs.container.ContainerRequestContext)
	 */
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		final String requestMethod = requestContext.getMethod();
		if (HttpMethod.PUT.equals(requestMethod) == false && HttpMethod.POST.equals(requestMethod) == false) {
			return;
		}

		final String resourceMethodName = resourceInfo.getResourceMethod().getName();
		Method generatorMethod = null;
		for (Method method : resourceInfo.getResourceClass().getMethods()) {
			final ETagGenerator etagGenAnnotation = method.getAnnotation(ETagGenerator.class);
			if (etagGenAnnotation == null) {
				continue;
			}

			if (Arrays.asList(etagGenAnnotation.value()).contains(resourceMethodName)) {
				generatorMethod = method;
				break;
			}
		}

		// if there is no generator method then do nothing
		if (generatorMethod == null) {
			return;
		}

		if (generatorMethod.getReturnType() != EntityTag.class) {
			throw new RuntimeException(String.format(UNEXPECTED_RETURN_TYPE, EntityTag.class.toString(),
					generatorMethod, generatorMethod.getReturnType()));
		}

		final UriInfo uriInfo = requestContext.getUriInfo();
		for (final Object matchedResource : uriInfo.getMatchedResources()) {
			if (resourceInfo.getResourceClass().equals(matchedResource.getClass()) == false) {
				continue;
			}

			final Object[] parameters = getMethodParameters(generatorMethod, uriInfo.getPathParameters(),
					uriInfo.getQueryParameters());

			EntityTag etag = null;
			try {
				etag = (EntityTag) generatorMethod.invoke(matchedResource, parameters);
			} catch (ReflectiveOperationException ex) {
				throw new RuntimeException(ex);
			}

			Response.ResponseBuilder rb = requestContext.getRequest().evaluatePreconditions(etag);
			if (rb != null) {
				requestContext.abortWith(rb.build());
			}
		}
	}

	/**
	 * Get parameters for a method
	 * 
	 * @param method
	 * @param pathParameters
	 * @param queryParameters
	 * @return
	 */
	public Object[] getMethodParameters(Method method, MultivaluedMap<String, String> pathParameters,
			MultivaluedMap<String, String> queryParameters) {
		List<Object> values = new ArrayList<Object>();

		int paramsCnt = method.getParameterTypes().length;
		for (int idx = 0; idx < paramsCnt; idx++) {
			final Class<?> parameterType = method.getParameterTypes()[idx];
			for (Annotation annotation : method.getParameterAnnotations()[idx]) {

				if (annotation.annotationType().equals(PathParam.class)) {
					final PathParam pathParam = PathParam.class.cast(annotation);
					final String paramId = pathParam.value();
					if (pathParameters.containsKey(paramId)) {
						String paramValue = pathParameters.getFirst(paramId);
						values.add(getPrimativeTypeValue(parameterType, paramValue));
						break;
					} else {
						throw new RuntimeException(String.format(EXPECTED_PARAM_MISSING, PathParam.class.getName(), paramId, idx, method, method.getDeclaringClass()));
					}

				} else if (annotation.annotationType().equals(QueryParam.class)) {
					final QueryParam queryParam = QueryParam.class.cast(annotation);
					final String paramId = queryParam.value();
					if (queryParameters.containsKey(paramId)) {
						String paramValue = queryParameters.getFirst(paramId);
						values.add(getPrimativeTypeValue(parameterType, paramValue));
						break;
					} else {
						throw new RuntimeException(String.format(EXPECTED_PARAM_MISSING, PathParam.class.getName(), paramId, idx, method, method.getDeclaringClass()));
					}
				}
			}
			
			if (values.size() < idx + 1) {
				// If it gets this far then there is no path/query param for the parameter so abort
				throw new RuntimeException(String.format(UNEXPECTED_GENERATOR_PARAM, idx, method, method.getDeclaringClass()));
			}
		}
		
		return Iterables.toArray(values, Object.class);
	}

	/**
	 * Get the primitive type object of a value
	 * 
	 * @param type
	 * @param value
	 * @return
	 */
	public Object getPrimativeTypeValue(Class<?> type, String value) {
		Object obj = null;
		if (type == String.class) {
			obj = value;
		} else if (type == Long.class) {
			obj = Long.parseLong(value);
		} else if (type == Integer.class) {
			obj = Integer.parseInt(value);
		} else if (type == Boolean.class) {
			obj = Boolean.parseBoolean(value);
		} else if (type == Short.class) {
			obj = Short.parseShort(value);
		} else if (type == Float.class) {
			obj = Float.parseFloat(value);
		} else if (type == Double.class) {
			obj = Double.parseDouble(value);
		}
		return obj;
	}

}