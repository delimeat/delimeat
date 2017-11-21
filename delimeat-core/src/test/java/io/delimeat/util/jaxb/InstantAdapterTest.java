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
package io.delimeat.util.jaxb;

import java.time.Instant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.delimeat.util.jaxb.InstantAdapter;

public class InstantAdapterTest {
	
	private InstantAdapter adapter;

	@BeforeEach
	public void setUp() {
		adapter = new InstantAdapter();
	}

	@Test
	public void marshalTest() throws Exception {
		Assertions.assertEquals("2017-04-05T04:00:00Z" ,adapter.marshal(Instant.parse("2017-04-05T04:00:00Z")));
	}
	@Test
	public void marshalNullTest() throws Exception {
		Assertions.assertNull(adapter.marshal(null));
	}

	@Test
	public void setValidDateTest() throws Exception {
		Assertions.assertEquals(Instant.ofEpochMilli(1), adapter.unmarshal("1970-01-01T00:00:00.001Z"));
	}

	@Test
	public void unmarshalNullTest() throws Exception {
		Assertions.assertNull(adapter.unmarshal(null));
	}

	@Test
	public void unmarshalEmptyStringTest() throws Exception {
		Assertions.assertNull(adapter.unmarshal(""));
	}
}
