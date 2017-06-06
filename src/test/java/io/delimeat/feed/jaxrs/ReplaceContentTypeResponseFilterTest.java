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

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.feed.jaxrs.ReplaceContentTypeResponseFilter;

public class ReplaceContentTypeResponseFilterTest {

	private ReplaceContentTypeResponseFilter filter; 
	private MultivaluedMap<String, String> headers;
	
	@Before
	public void setUp() throws Exception {
		filter = new ReplaceContentTypeResponseFilter();
		headers = new MultivaluedHashMap<String,String>();
	}
	
	@Test
	public void filterNullTest() throws IOException{
		ClientRequestContext request = Mockito.mock(ClientRequestContext.class);
		Mockito.when(request.getHeaderString("Accept")).thenReturn(null);
		
		headers.add("Content-Type", "CONTENT");
		ClientResponseContext response = Mockito.mock(ClientResponseContext.class);
		Mockito.when(response.getStatusInfo()).thenReturn(Response.Status.OK);
		Mockito.when(response.getHeaders()).thenReturn(headers);
		
		filter.filter(request, response);
		Assert.assertEquals("CONTENT", headers.getFirst("Content-Type"));		
	}
	
	@Test
	public void filterEmptyTest() throws IOException{
		ClientRequestContext request = Mockito.mock(ClientRequestContext.class);
		Mockito.when(request.getHeaderString("Accept")).thenReturn("");
		
		headers.add("Content-Type", "CONTENT");
		ClientResponseContext response = Mockito.mock(ClientResponseContext.class);
		Mockito.when(response.getStatusInfo()).thenReturn(Response.Status.OK);
		Mockito.when(response.getHeaders()).thenReturn(headers);
		
		filter.filter(request, response);
		Assert.assertEquals("CONTENT", headers.getFirst("Content-Type"));
	}

	@Test
	public void filterNotMatchTest() throws IOException{
		ClientRequestContext request = Mockito.mock(ClientRequestContext.class);
		Mockito.when(request.getHeaderString("Accept")).thenReturn("CONTENT");
		
		headers.add("Content-Type", "DIFFERENT");
		ClientResponseContext response = Mockito.mock(ClientResponseContext.class);
		Mockito.when(response.getStatusInfo()).thenReturn(Response.Status.OK);
		Mockito.when(response.getHeaders()).thenReturn(headers);
		
		filter.filter(request, response);
		Assert.assertEquals("CONTENT", headers.getFirst("Content-Type"));
	}
	
	@Test
	public void filterMatchTest() throws IOException{
		ClientRequestContext request = Mockito.mock(ClientRequestContext.class);
		Mockito.when(request.getHeaderString("Accept")).thenReturn("CONTENT");
		
		headers.add("Content-Type", "CONTENT");
		ClientResponseContext response = Mockito.mock(ClientResponseContext.class);
		Mockito.when(response.getStatusInfo()).thenReturn(Response.Status.OK);
		Mockito.when(response.getHeaders()).thenReturn(headers);
		
		filter.filter(request, response);
		Assert.assertEquals("CONTENT", headers.getFirst("Content-Type"));
	}
	
	@Test
	public void filterNotSuccessfulTest() throws IOException{
		ClientRequestContext request = Mockito.mock(ClientRequestContext.class);
		Mockito.when(request.getHeaderString("Accept")).thenReturn("CONTENT");
		
		headers.add("Content-Type", "DIFFERENT");
		ClientResponseContext response = Mockito.mock(ClientResponseContext.class);
		Mockito.when(response.getStatusInfo()).thenReturn(Response.Status.BAD_GATEWAY);
		Mockito.when(response.getHeaders()).thenReturn(headers);
		
		filter.filter(request, response);
		Assert.assertEquals("DIFFERENT", headers.getFirst("Content-Type"));
	}

}
