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
package io.delimeat.rest.util;

import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GenericExceptionMapperTest {

	private GenericExceptionMapper mapper;

	@BeforeEach
	public void setUp() {
		mapper = new GenericExceptionMapper();
	}

	@Test
	public void exceptionTest() {
		Response response = mapper.toResponse(new Exception("THIS IS A TEST"));
		Assertions.assertNotNull(response);
		Assertions.assertEquals(500, response.getStatus());
		Assertions.assertEquals(JaxrsError.class, response.getEntity().getClass());
		JaxrsError error = (JaxrsError) response.getEntity();
		Assertions.assertEquals(500, error.getStatus());
		Assertions.assertEquals("THIS IS A TEST", error.getMessage());
	}
}
