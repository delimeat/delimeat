package io.delimeat.util.jaxrs;

import java.io.IOException;

import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CORSResponseFilterTest {

	private CORSResponseFilter filter;
	
	@Before
	public void setUp() throws Exception {
		filter = new CORSResponseFilter();
	}
	
	@Test
	public void filterTest() throws IOException{
		ContainerResponseContext mockedContext = Mockito.mock(ContainerResponseContext.class);
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<String, Object>();
		Mockito.when(mockedContext.getHeaders()).thenReturn(headers);
		
		filter.filter(null, mockedContext);
		
		Assert.assertEquals(3, headers.size());
		Assert.assertEquals("*", headers.getFirst("Access-Control-Allow-Origin"));
		Assert.assertEquals("GET, POST, DELETE, PUT", headers.getFirst("Access-Control-Allow-Methods"));
		Assert.assertEquals("X-Requested-With, Content-Type", headers.getFirst("Access-Control-Allow-Headers"));

	}

}
