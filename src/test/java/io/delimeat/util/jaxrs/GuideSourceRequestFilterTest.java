package io.delimeat.util.jaxrs;

import java.io.IOException;

import io.delimeat.rest.DelimeatRestError;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class GuideSourceRequestFilterTest {

	private GuideSourceRequestFilter filter;

	@Before
	public void setUp() throws Exception {
		filter = new GuideSourceRequestFilter();
	}

	@Test
	public void filterTest() throws IOException {
		MultivaluedMap<String, String> pathParams = new MultivaluedHashMap<String, String>();
		pathParams.add("source", "JIBERISH");

		UriInfo mockedUriInfo = Mockito.mock(UriInfo.class);
		Mockito.when(mockedUriInfo.getPathParameters()).thenReturn(pathParams);

		ContainerRequestContext mockedContext = Mockito
				.mock(ContainerRequestContext.class);
		Mockito.when(mockedContext.getUriInfo()).thenReturn(mockedUriInfo);

		filter.filter(mockedContext);

		ArgumentCaptor<Response> captor = ArgumentCaptor
				.forClass(Response.class);

		Mockito.verify(mockedContext).abortWith(captor.capture());

		final Response actualResponse = captor.getValue();
		Assert.assertEquals(404, actualResponse.getStatus());
		Assert.assertEquals(DelimeatRestError.class, actualResponse.getEntity()
				.getClass());
		Assert.assertEquals("Invalid source JIBERISH",
				((DelimeatRestError) actualResponse.getEntity()).getMessage());

	}

}
