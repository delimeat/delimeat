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
