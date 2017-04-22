package io.delimeat.feed.jaxrs;

import java.io.IOException;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

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
		Mockito.when(response.getHeaders()).thenReturn(headers);
		
		filter.filter(request, response);
		Assert.assertEquals("CONTENT", headers.getFirst("Content-Type"));
	}

}
