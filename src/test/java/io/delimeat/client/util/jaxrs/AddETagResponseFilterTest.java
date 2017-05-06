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

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.client.util.jaxrs.AddETagResponseFilter;

public class AddETagResponseFilterTest {

	private AddETagResponseFilter filter;
	
	@Before
	public void setUp() throws Exception {
		filter = new AddETagResponseFilter();
	}
  
  	@Test
  	public void notSuccessfulResponsetest() throws IOException{
     	ContainerRequestContext mockedRequestContext = Mockito.mock(ContainerRequestContext.class);
     
     	ContainerResponseContext mockedResponseContext = Mockito.mock(ContainerResponseContext.class);
		Mockito.when(mockedResponseContext.getStatusInfo()).thenReturn(Status.FORBIDDEN);
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
		Mockito.when(mockedResponseContext.getHeaders()).thenReturn(headers);
        
		filter.filter(mockedRequestContext, mockedResponseContext);
     
     	Assert.assertTrue(headers.isEmpty());
     	Mockito.verify(mockedResponseContext, Mockito.times(0)).getEntity();
   }
  
  	@Test
  	public void successfulNoEntityResponsetest() throws IOException{
     	ContainerRequestContext mockedRequestContext = Mockito.mock(ContainerRequestContext.class);
     
     	ContainerResponseContext mockedResponseContext = Mockito.mock(ContainerResponseContext.class);
		Mockito.when(mockedResponseContext.getStatusInfo()).thenReturn(Status.OK);
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
		Mockito.when(mockedResponseContext.getHeaders()).thenReturn(headers);
     	Mockito.when(mockedResponseContext.getEntity()).thenReturn(null);
        
		filter.filter(mockedRequestContext, mockedResponseContext);
     
     	Assert.assertTrue(headers.isEmpty());
     	Mockito.verify(mockedResponseContext, Mockito.times(1)).getEntity();
   }
    
  	@Test
  	public void successfulEntityNoEtagResponsetest() throws IOException{
     	ContainerRequestContext mockedRequestContext = Mockito.mock(ContainerRequestContext.class);
     
     	ContainerResponseContext mockedResponseContext = Mockito.mock(ContainerResponseContext.class);
		Mockito.when(mockedResponseContext.getStatusInfo()).thenReturn(Status.OK);
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
		Mockito.when(mockedResponseContext.getHeaders()).thenReturn(headers);
     	Mockito.when(mockedResponseContext.getEntity()).thenReturn("STRING");
        
		filter.filter(mockedRequestContext, mockedResponseContext);
     
     	Assert.assertFalse(headers.isEmpty());
     	Assert.assertEquals(new EntityTag("-1838656495"),headers.getFirst(HttpHeaders.ETAG));
     	Mockito.verify(mockedResponseContext, Mockito.times(1)).getEntity();
   }
  
  	@Test
  	public void successfulEntityEtagResponsetest() throws IOException{
     	ContainerRequestContext mockedRequestContext = Mockito.mock(ContainerRequestContext.class);
     
     	ContainerResponseContext mockedResponseContext = Mockito.mock(ContainerResponseContext.class);
		Mockito.when(mockedResponseContext.getStatusInfo()).thenReturn(Status.OK);
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
     	EntityTag etag = new EntityTag("JIBERISH_ETAG");
     	headers.add(HttpHeaders.ETAG, etag);
		Mockito.when(mockedResponseContext.getHeaders()).thenReturn(headers);
     	Mockito.when(mockedResponseContext.getEntity()).thenReturn("STRING");
        
		filter.filter(mockedRequestContext, mockedResponseContext);
     
     	Assert.assertFalse(headers.isEmpty());
     	Assert.assertEquals(etag,headers.getFirst(HttpHeaders.ETAG));
     	Mockito.verify(mockedResponseContext, Mockito.times(1)).getEntity();
   }
}
