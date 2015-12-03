package io.delimeat.util.jaxrs;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LoggingExceptionMapperTest {

	private LoggingExceptionMapper mapper;

	@Before
	public void setUp() throws Exception {
		mapper = new LoggingExceptionMapper();
	}

	@Test
	public void toResponseTest() {
		Response response = mapper.toResponse(new Exception());
		Assert.assertEquals(500, response.getStatus());
	}

}
