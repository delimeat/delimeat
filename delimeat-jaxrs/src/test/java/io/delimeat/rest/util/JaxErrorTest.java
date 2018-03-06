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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.delimeat.rest.util.JaxrsError.JaxErrorBuilder;

public class JaxErrorTest {

	@Test
	public void statusTest() {
		JaxrsError error = new JaxrsError();
		Assertions.assertEquals(0, error.getStatus());
		error.setStatus(500);
		Assertions.assertEquals(500, error.getStatus());
	}

	@Test
	public void messageTest() {
		JaxrsError error = new JaxrsError();
		Assertions.assertNull(error.getMessage());
		error.setMessage("MESSAGE");
		Assertions.assertEquals("MESSAGE", error.getMessage());
	}

	@Test
	public void toStringTest() {
		Assertions.assertEquals("JaxrsError [status=0]", new JaxrsError().toString());
	}

	@Test
	public void builderTest() {
		JaxrsError error = JaxErrorBuilder.start().setStatus(500).setMessage("MESSAGE").build();
		Assertions.assertEquals(500, error.getStatus());
		Assertions.assertEquals("MESSAGE", error.getMessage());
	}

}
