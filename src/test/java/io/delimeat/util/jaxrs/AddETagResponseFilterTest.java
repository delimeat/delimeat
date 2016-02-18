package io.delimeat.util.jaxrs;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
  	public void successfulEntityResponsetest() throws IOException{
     	ContainerRequestContext mockedRequestContext = Mockito.mock(ContainerRequestContext.class);
     
     	ContainerResponseContext mockedResponseContext = Mockito.mock(ContainerResponseContext.class);
		Mockito.when(mockedResponseContext.getStatusInfo()).thenReturn(Status.OK);
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
		Mockito.when(mockedResponseContext.getHeaders()).thenReturn(headers);
     	Mockito.when(mockedResponseContext.getEntity()).thenReturn("STRING");
        
		filter.filter(mockedRequestContext, mockedResponseContext);
     
     	Assert.assertFalse(headers.isEmpty());
     	Assert.assertEquals("\"da5aba16204ebd88821d2bbf3642268c4231fb1f\"",headers.getFirst("ETag"));
     	Mockito.verify(mockedResponseContext, Mockito.times(1)).getEntity();
     
   }
}
