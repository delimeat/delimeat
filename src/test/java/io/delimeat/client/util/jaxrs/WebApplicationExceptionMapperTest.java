package io.delimeat.client.util.jaxrs;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.client.util.jaxrs.WebApplicationExceptionMapper;

public class WebApplicationExceptionMapperTest {

	private WebApplicationExceptionMapper mapper;

	@Before
	public void setUp() throws Exception {
		mapper = new WebApplicationExceptionMapper();
	}

	@Test
	public void exceptionNoReponseTest() {
		Response response = mapper.toResponse(new WebApplicationException("THIS IS A TEST"));
		Assert.assertNotNull(response);
		Assert.assertEquals(500, response.getStatus());
	}
	
	@Test
	public void exceptionReponseTest() {
		Response exceptionResponse = Response.ok().build();
		WebApplicationException exception = new WebApplicationException(exceptionResponse);
		Response response = mapper.toResponse(exception);
		Assert.assertNotNull(response);
		Assert.assertEquals(200, response.getStatus());
	}
}
