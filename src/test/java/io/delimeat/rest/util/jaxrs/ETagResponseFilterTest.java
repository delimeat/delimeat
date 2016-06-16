package io.delimeat.rest.util.jaxrs;

import java.io.IOException;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Request;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ETagResponseFilterTest {
  
	private ETagResponseFilter filter;
  	private ContainerRequestContext requestContext;
  	private ContainerResponseContext responseContext;
	
	@Before
	public void setUp() throws Exception {
		filter = new ETagResponseFilter();
     	requestContext = Mockito.mock(ContainerRequestContext.class);
      responseContext = Mockito.mock(ContainerResponseContext.class);
	}
  
  
  	@Test
  	public void notSuccessfulResponsetest() throws IOException{
		Mockito.when(responseContext.getStatusInfo()).thenReturn(Status.FORBIDDEN);
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
		Mockito.when(responseContext.getHeaders()).thenReturn(headers);
        
		filter.filter(requestContext, responseContext);
     
     	Assert.assertTrue(headers.isEmpty());
     
     	Mockito.verifyZeroInteractions(requestContext);
     
     	Mockito.verify(responseContext).getStatusInfo();
     	Mockito.verifyNoMoreInteractions(responseContext);
   }
  
  	@Test
  	public void successfulNoEntityResponseTest() throws IOException{
		Mockito.when(responseContext.getStatusInfo()).thenReturn(Status.OK);
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
		Mockito.when(responseContext.getHeaders()).thenReturn(headers);
     	Mockito.when(responseContext.getEntity()).thenReturn(null);

        
		filter.filter(requestContext, responseContext);
     
     	Assert.assertTrue(headers.isEmpty());
     
     	Mockito.verifyZeroInteractions(requestContext);
     
     	Mockito.verify(responseContext).getStatusInfo();
     	Mockito.verify(responseContext).getHeaderString(HttpHeaders.ETAG);
     	Mockito.verify(responseContext).getEntity();
     	Mockito.verifyNoMoreInteractions(responseContext);
   }

  
  	@Test
  	public void successfulEntityEtagResponsetest() throws IOException{
		Mockito.when(responseContext.getStatusInfo()).thenReturn(Status.OK);
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
		Mockito.when(responseContext.getHeaders()).thenReturn(headers);
     	Mockito.when(responseContext.getHeaderString(HttpHeaders.ETAG)).thenReturn("JIBERISH_ETAG");
     	Mockito.when(responseContext.getEntity()).thenReturn("ENTITY");
        
		filter.filter(requestContext, responseContext);
     
     	Assert.assertTrue(headers.isEmpty());
     
     	Mockito.verifyZeroInteractions(requestContext);
     
     	Mockito.verify(responseContext).getStatusInfo();
     	Mockito.verify(responseContext).getEntity();
     	Mockito.verify(responseContext).getHeaderString(HttpHeaders.ETAG);
     	Mockito.verifyNoMoreInteractions(responseContext);
     
   }
  
  	@Test
  	public void successfulEntityNoEtagNotGetResponsetest() throws IOException{
     	Mockito.when(requestContext.getMethod()).thenReturn(HttpMethod.POST);
     	
		Mockito.when(responseContext.getStatusInfo()).thenReturn(Status.OK);
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
		Mockito.when(responseContext.getHeaders()).thenReturn(headers);
     	Mockito.when(responseContext.getEntity()).thenReturn("ENTITY");
        
		filter.filter(requestContext, responseContext);
     
     	Assert.assertFalse(headers.isEmpty());
     
     	Assert.assertEquals(new EntityTag("2050021347"),headers.getFirst(HttpHeaders.ETAG));
     
     	Mockito.verify(requestContext).getMethod();
     	Mockito.verifyNoMoreInteractions(requestContext);
     
     	Mockito.verify(responseContext).getStatusInfo();
     	Mockito.verify(responseContext).getEntity();
     	Mockito.verify(responseContext).getHeaderString(HttpHeaders.ETAG);
     	Mockito.verify(responseContext).getHeaders();
     	Mockito.verifyNoMoreInteractions(responseContext);
   }
  
  	@Test
  	public void successfulEntityNoEtagGetNoMatchResponsetest() throws IOException{
     	Mockito.when(requestContext.getMethod()).thenReturn(HttpMethod.GET);
     	Request request = Mockito.mock(Request.class);
     	Mockito.when(request.evaluatePreconditions(new EntityTag("2050021347"))).thenReturn(null);
     	Mockito.when(requestContext.getRequest()).thenReturn(request);
     	
		Mockito.when(responseContext.getStatusInfo()).thenReturn(Status.OK);
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
		Mockito.when(responseContext.getHeaders()).thenReturn(headers);
     	Mockito.when(responseContext.getEntity()).thenReturn("ENTITY");
        
		filter.filter(requestContext, responseContext);
     
     	Assert.assertFalse(headers.isEmpty());
     
     	Assert.assertEquals(new EntityTag("2050021347"),headers.getFirst(HttpHeaders.ETAG));
     
     	Mockito.verify(requestContext).getMethod();
     	Mockito.verify(requestContext).getRequest();
     	Mockito.verifyNoMoreInteractions(requestContext);
     
     	Mockito.verify(request).evaluatePreconditions(new EntityTag("2050021347"));
     	Mockito.verifyNoMoreInteractions(request);
     
     	Mockito.verify(responseContext).getStatusInfo();
     	Mockito.verify(responseContext).getEntity();
     	Mockito.verify(responseContext).getHeaderString(HttpHeaders.ETAG);
     	Mockito.verify(responseContext).getHeaders();
     	Mockito.verifyNoMoreInteractions(responseContext);
   }
  
  	@Test(expected=WebApplicationException.class)
  	public void successfulEntityNoEtagGetMatchResponsetest() throws IOException{
     	Mockito.when(requestContext.getMethod()).thenReturn(HttpMethod.GET);
     	Request request = Mockito.mock(Request.class);
     	ResponseBuilder responseBuilder = Response.notModified();
     	Mockito.when(request.evaluatePreconditions(new EntityTag("2050021347"))).thenReturn(responseBuilder);
     	Mockito.when(requestContext.getRequest()).thenReturn(request);
     	
		Mockito.when(responseContext.getStatusInfo()).thenReturn(Status.OK);
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
		Mockito.when(responseContext.getHeaders()).thenReturn(headers);
     	Mockito.when(responseContext.getEntity()).thenReturn("ENTITY");
        
		filter.filter(requestContext, responseContext);
   }

}
