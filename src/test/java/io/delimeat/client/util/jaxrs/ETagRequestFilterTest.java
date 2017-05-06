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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.client.util.jaxrs.ETagGenerator;
import io.delimeat.client.util.jaxrs.ETagRequestFilter;

import java.lang.reflect.Method;
import java.util.Arrays;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

public class ETagRequestFilterTest {

	private ETagRequestFilter filter;
	private ContainerRequestContext requestContext;
	private ResourceInfo resourceInfo;

	@Before
	public void setUp() throws Exception {
		filter = new ETagRequestFilter();
		requestContext = Mockito.mock(ContainerRequestContext.class);
		resourceInfo = Mockito.mock(ResourceInfo.class);
	}

	@Test
	public void resourceInfoTest() {
		Assert.assertNull(filter.getResourceInfo());
		filter.setResourceInfo(resourceInfo);
		Assert.assertEquals(resourceInfo, filter.getResourceInfo());
	}

	@Test
	public void getPrimativeTypeStringTest() {
		String value = "STRING";
		Object result = filter.getPrimativeTypeValue(String.class, value);
		Assert.assertEquals(String.class, result.getClass());
		Assert.assertEquals("STRING", result.toString());
	}

	@Test
	public void getPrimativeTypeLongTest() {
		String value = "1234567890";
		Object result = filter.getPrimativeTypeValue(Long.class, value);
		Assert.assertEquals(Long.class, result.getClass());
		Assert.assertEquals("1234567890", result.toString());
	}

	@Test
	public void getPrimativeTypeIntegerTest() {
		String value = "1234567890";
		Object result = filter.getPrimativeTypeValue(Integer.class, value);
		Assert.assertEquals(Integer.class, result.getClass());
		Assert.assertEquals("1234567890", result.toString());
	}

	@Test
	public void getPrimativeTypeBooleanTest() {
		String value = "false";
		Object result = filter.getPrimativeTypeValue(Boolean.class, value);
		Assert.assertEquals(Boolean.class, result.getClass());
		Assert.assertEquals("false", result.toString());
	}

	@Test
	public void getPrimativeTypeShortTest() {
		String value = "1234";
		Object result = filter.getPrimativeTypeValue(Short.class, value);
		Assert.assertEquals(Short.class, result.getClass());
		Assert.assertEquals("1234", result.toString());
	}

	@Test
	public void getPrimativeTypeFloatTest() {
		String value = "1234.99";
		Object result = filter.getPrimativeTypeValue(Float.class, value);
		Assert.assertEquals(Float.class, result.getClass());
		Assert.assertEquals("1234.99", result.toString());
	}

	@Test
	public void getPrimativeTypeDoubleTest() {
		String value = "1234";
		Object result = filter.getPrimativeTypeValue(Double.class, value);
		Assert.assertEquals(Double.class, result.getClass());
		Assert.assertEquals("1234.0", result.toString());
	}

	@Test
	public void getPrimativeTypeObjectTest() {
		String value = "1234";
		Object result = filter.getPrimativeTypeValue(Object.class, value);
		Assert.assertNull(result);
	}

	@Test
	public void getMethodParametersNoParamsTest() throws Exception {
		Object obj = new Object() {
			@SuppressWarnings("unused")
			public void method() {
			};
		};
		
		MultivaluedMap<String, String> pathParameters = new MultivaluedHashMap<String, String>();
		MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
		Method method = obj.getClass().getMethods()[0];
		Object[] objects = filter.getMethodParameters(method, pathParameters, queryParameters);
		Assert.assertNotNull(objects);
		Assert.assertEquals(0, objects.length);
	}

	@Test(expected = RuntimeException.class)
	public void getMethodParametersNoAnnotationsTest() throws Exception {
		Object obj = new Object() {
			@SuppressWarnings("unused")
			public void method(Object parameter) {
			};
		};
		MultivaluedMap<String, String> pathParameters = new MultivaluedHashMap<String, String>();
		MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
		Method method = obj.getClass().getMethods()[0];
		Object[] objects = filter.getMethodParameters(method, pathParameters, queryParameters);
		Assert.assertNotNull(objects);
		Assert.assertEquals(0, objects.length);
	}

	@Test(expected = RuntimeException.class)
	public void getMethodParametersPathParmNoValueTest() throws Exception {
		Object obj = new Object() {
			@SuppressWarnings("unused")
			public void method(@PathParam("id") Object parameter) {
			};
		};
		MultivaluedMap<String, String> pathParameters = new MultivaluedHashMap<String, String>();
		MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
		Method method = obj.getClass().getMethods()[0];
		Object[] objects = filter.getMethodParameters(method, pathParameters, queryParameters);
		Assert.assertNotNull(objects);
		Assert.assertEquals(0, objects.length);
	}

	@Test
	public void getMethodParametersPathParmValueTest() throws Exception {
		Object obj = new Object() {
			@SuppressWarnings("unused")
			public void method(@PathParam("id") String parameter) {
			};
		};
		MultivaluedMap<String, String> pathParameters = new MultivaluedHashMap<String, String>();
		pathParameters.add("id", "value");
		MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
		Method method = obj.getClass().getMethods()[0];
		Object[] objects = filter.getMethodParameters(method, pathParameters, queryParameters);
		Assert.assertNotNull(objects);
		Assert.assertEquals(1, objects.length);
		Assert.assertEquals(String.class, objects[0].getClass());
		Assert.assertEquals("value", objects[0].toString());
	}

	@Test(expected = RuntimeException.class)
	public void getMethodParametersQueryParmNoValueTest() throws Exception {
		Object obj = new Object() {
			@SuppressWarnings("unused")
			public void method(@QueryParam("id") Object parameter) {
			};
		};
		MultivaluedMap<String, String> pathParameters = new MultivaluedHashMap<String, String>();
		MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
		Method method = obj.getClass().getMethods()[0];
		Object[] objects = filter.getMethodParameters(method, pathParameters, queryParameters);
		Assert.assertNotNull(objects);
		Assert.assertEquals(0, objects.length);
	}

	@Test
	public void getMethodParametersQueryParmValueTest() throws Exception {
		Object obj = new Object() {
			@SuppressWarnings("unused")
			public void method(@QueryParam("id") String parameter) {
			};
		};
		MultivaluedMap<String, String> pathParameters = new MultivaluedHashMap<String, String>();
		MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
		queryParameters.add("id", "value");
		Method method = obj.getClass().getMethods()[0];
		Object[] objects = filter.getMethodParameters(method, pathParameters, queryParameters);
		Assert.assertNotNull(objects);
		Assert.assertEquals(1, objects.length);
		Assert.assertEquals(String.class, objects[0].getClass());
		Assert.assertEquals("value", objects[0].toString());
	}

	@Test
	public void notPostOrPutTest() throws Exception {
		Mockito.when(requestContext.getMethod()).thenReturn(HttpMethod.GET);

		filter.filter(requestContext);

		Mockito.verify(requestContext).getMethod();
		Mockito.verifyNoMoreInteractions(requestContext);

		Mockito.verifyZeroInteractions(requestContext);
	}

	@Test
	public void putMethodNoGeneratorMethodTest() throws Exception {
		Object obj = new Object() {
			@SuppressWarnings("unused")
			public void generate(@QueryParam("id") String parameter) {
			};
			@SuppressWarnings("unused")
			public void method(@QueryParam("id") String method) {
			};
		};
		Method resourceMethod = obj.getClass().getMethod("method", String.class);
		Mockito.when(resourceInfo.getResourceMethod()).thenReturn(resourceMethod);
		Mockito.doReturn(obj.getClass()).when(resourceInfo).getResourceClass();
		filter.setResourceInfo(resourceInfo);

		Mockito.when(requestContext.getMethod()).thenReturn(HttpMethod.PUT);

		filter.filter(requestContext);

		Mockito.verify(requestContext).getMethod();
		Mockito.verifyNoMoreInteractions(requestContext);

		Mockito.verify(resourceInfo).getResourceMethod();
		Mockito.verify(resourceInfo).getResourceClass();
		Mockito.verifyNoMoreInteractions(resourceInfo);
	}

	@Test
	public void putMethodGeneratorDifferentMethodTest() throws Exception {
		Object obj = new Object() {

			@ETagGenerator("other")
			public void generate(@QueryParam("id") String parameter) {
			};
			@SuppressWarnings("unused")
			public void method(@QueryParam("id") String method) {
			};
		};
		Method resourceMethod = obj.getClass().getMethod("method", String.class);
		Mockito.when(resourceInfo.getResourceMethod()).thenReturn(resourceMethod);
		Mockito.doReturn(obj.getClass()).when(resourceInfo).getResourceClass();
		filter.setResourceInfo(resourceInfo);

		Mockito.when(requestContext.getMethod()).thenReturn(HttpMethod.PUT);

		filter.filter(requestContext);

		Mockito.verify(requestContext).getMethod();
		Mockito.verifyNoMoreInteractions(requestContext);

		Mockito.verify(resourceInfo).getResourceMethod();
		Mockito.verify(resourceInfo).getResourceClass();
		Mockito.verifyNoMoreInteractions(resourceInfo);
	}

	@Test(expected = RuntimeException.class)
	public void putMethodGeneratorMethodNotEtagTest() throws Exception {
		Object obj = new Object() {

			@ETagGenerator({ "other", "method" })
			public void generate(@QueryParam("id") String parameter) {
			};
			@SuppressWarnings("unused")
			public void method(@QueryParam("id") String method) {
			};
		};
		Method resourceMethod = obj.getClass().getMethod("method", String.class);
		Mockito.when(resourceInfo.getResourceMethod()).thenReturn(resourceMethod);
		Mockito.doReturn(obj.getClass()).when(resourceInfo).getResourceClass();
		filter.setResourceInfo(resourceInfo);

		Mockito.when(requestContext.getMethod()).thenReturn(HttpMethod.PUT);

		filter.filter(requestContext);
	}

	@Test
	public void putMethodGeneratorMethodDifferentMatchedResourceTest()
			throws Exception {
		Object obj = new Object() {

			@ETagGenerator({ "other", "method" })
			public EntityTag generate(@QueryParam("id") String parameter) {
				return new EntityTag("value");
			};
			@SuppressWarnings("unused")
			public void method(@QueryParam("id") String method) {
			};
		};
		Method resourceMethod = obj.getClass().getMethod("method", String.class);
		Mockito.when(resourceInfo.getResourceMethod()).thenReturn(resourceMethod);
		Mockito.doReturn(obj.getClass()).when(resourceInfo).getResourceClass();
		filter.setResourceInfo(resourceInfo);

		Mockito.when(requestContext.getMethod()).thenReturn(HttpMethod.PUT);
		UriInfo uriInfo = Mockito.mock(UriInfo.class);
		Mockito.when(uriInfo.getMatchedResources()).thenReturn(Arrays.asList(new Object()));
		Mockito.when(requestContext.getUriInfo()).thenReturn(uriInfo);

		filter.filter(requestContext);

		Mockito.verify(requestContext).getMethod();
		Mockito.verify(requestContext).getUriInfo();
		Mockito.verifyNoMoreInteractions(requestContext);

		Mockito.verify(uriInfo).getMatchedResources();
		Mockito.verifyNoMoreInteractions(uriInfo);

		Mockito.verify(resourceInfo).getResourceMethod();
		Mockito.verify(resourceInfo, Mockito.times(2)).getResourceClass();
		Mockito.verifyNoMoreInteractions(resourceInfo);
	}

	@Test(expected = RuntimeException.class)
	public void putMethodGeneratorMethodExceptionTest() throws Exception {
		Object obj = new Object() {

			@ETagGenerator({ "other", "method" })
			public EntityTag generate(@QueryParam("id") String parameter) {
				throw new RuntimeException();
			};
			@SuppressWarnings("unused")
			public void method(@QueryParam("id") String method) {
			};
		};
		Method resourceMethod = obj.getClass().getMethod("method", String.class);
		Mockito.when(resourceInfo.getResourceMethod()).thenReturn(resourceMethod);
		Mockito.doReturn(obj.getClass()).when(resourceInfo).getResourceClass();
		filter.setResourceInfo(resourceInfo);

		Mockito.when(requestContext.getMethod()).thenReturn(HttpMethod.PUT);
		UriInfo uriInfo = Mockito.mock(UriInfo.class);
		Mockito.when(uriInfo.getMatchedResources()).thenReturn(Arrays.asList(obj));
		MultivaluedMap<String, String> pathParameters = new MultivaluedHashMap<String, String>();
		Mockito.when(uriInfo.getPathParameters()).thenReturn(pathParameters);
		MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
		queryParameters.add("id", "value");
		Mockito.when(uriInfo.getQueryParameters()).thenReturn(queryParameters);
		Mockito.when(requestContext.getUriInfo()).thenReturn(uriInfo);

		Request request = Mockito.mock(Request.class);
		Mockito.when(requestContext.getRequest()).thenReturn(request);

		filter.filter(requestContext);
	}

	@Test
	public void putMethodGeneratorMethodUnmatchedEtagTest() throws Exception {
		Object obj = new Object() {

			@ETagGenerator({ "other", "method" })
			public EntityTag generate(@QueryParam("id") String parameter) {
				return new EntityTag("value");
			};
			@SuppressWarnings("unused")
			public void method(@QueryParam("id") String method) {
			};
		};
		Method resourceMethod = obj.getClass().getMethod("method", String.class);
		Mockito.when(resourceInfo.getResourceMethod()).thenReturn(resourceMethod);
		Mockito.doReturn(obj.getClass()).when(resourceInfo).getResourceClass();
		filter.setResourceInfo(resourceInfo);

		Mockito.when(requestContext.getMethod()).thenReturn(HttpMethod.PUT);
		UriInfo uriInfo = Mockito.mock(UriInfo.class);
		Mockito.when(uriInfo.getMatchedResources()).thenReturn(Arrays.asList(obj));
		MultivaluedMap<String, String> pathParameters = new MultivaluedHashMap<String, String>();
		Mockito.when(uriInfo.getPathParameters()).thenReturn(pathParameters);
		MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
		queryParameters.add("id", "value");
		Mockito.when(uriInfo.getQueryParameters()).thenReturn(queryParameters);
		Mockito.when(requestContext.getUriInfo()).thenReturn(uriInfo);

		Request request = Mockito.mock(Request.class);
		Mockito.when(request.evaluatePreconditions(Mockito.any(EntityTag.class))).thenReturn(null);
		Mockito.when(requestContext.getRequest()).thenReturn(request);

		filter.filter(requestContext);

		Mockito.verify(requestContext).getMethod();
		Mockito.verify(requestContext).getUriInfo();
		Mockito.verify(requestContext).getRequest();
		Mockito.verifyNoMoreInteractions(requestContext);

		Mockito.verify(uriInfo).getMatchedResources();
		Mockito.verify(uriInfo).getPathParameters();
		Mockito.verify(uriInfo).getQueryParameters();
		Mockito.verifyNoMoreInteractions(uriInfo);

		Mockito.verify(request).evaluatePreconditions(
				Mockito.any(EntityTag.class));
		Mockito.verifyNoMoreInteractions(request);

		Mockito.verify(resourceInfo).getResourceMethod();
		Mockito.verify(resourceInfo, Mockito.times(2)).getResourceClass();
		Mockito.verifyNoMoreInteractions(resourceInfo);
	}

	@Test
	public void putMethodGeneratorMatchedEtagTest() throws Exception {
		Object obj = new Object() {

			@ETagGenerator({ "other", "method" })
			public EntityTag generate(@QueryParam("id") String parameter) {
				return new EntityTag("value");
			};
			@SuppressWarnings("unused")
			public void method(@QueryParam("id") String method) {
			};
		};
		Method resourceMethod = obj.getClass().getMethod("method", String.class);
		Mockito.when(resourceInfo.getResourceMethod()).thenReturn(resourceMethod);
		Mockito.doReturn(obj.getClass()).when(resourceInfo).getResourceClass();
		filter.setResourceInfo(resourceInfo);

		Mockito.when(requestContext.getMethod()).thenReturn(HttpMethod.PUT);
		UriInfo uriInfo = Mockito.mock(UriInfo.class);
		Mockito.when(uriInfo.getMatchedResources()).thenReturn(Arrays.asList(obj));
		MultivaluedMap<String, String> pathParameters = new MultivaluedHashMap<String, String>();
		Mockito.when(uriInfo.getPathParameters()).thenReturn(pathParameters);
		MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>();
		queryParameters.add("id", "value");
		Mockito.when(uriInfo.getQueryParameters()).thenReturn(queryParameters);
		Mockito.when(requestContext.getUriInfo()).thenReturn(uriInfo);

		Request request = Mockito.mock(Request.class);
		ResponseBuilder responseBuilder = Response.notModified();
		Mockito.when(request.evaluatePreconditions(Mockito.any(EntityTag.class))).thenReturn(responseBuilder);
		Mockito.when(requestContext.getRequest()).thenReturn(request);

		filter.filter(requestContext);

		Mockito.verify(requestContext).getMethod();
		Mockito.verify(requestContext).getUriInfo();
		Mockito.verify(requestContext).getRequest();
		Mockito.verify(requestContext).abortWith(Mockito.any(Response.class));
		Mockito.verifyNoMoreInteractions(requestContext);

		Mockito.verify(uriInfo).getMatchedResources();
		Mockito.verify(uriInfo).getPathParameters();
		Mockito.verify(uriInfo).getQueryParameters();
		Mockito.verifyNoMoreInteractions(uriInfo);

		Mockito.verify(request).evaluatePreconditions(Mockito.any(EntityTag.class));
		Mockito.verifyNoMoreInteractions(request);

		Mockito.verify(resourceInfo).getResourceMethod();
		Mockito.verify(resourceInfo, Mockito.times(2)).getResourceClass();
		Mockito.verifyNoMoreInteractions(resourceInfo);
	}

}
