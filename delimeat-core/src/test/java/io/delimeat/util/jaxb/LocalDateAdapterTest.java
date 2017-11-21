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

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.delimeat.util.jaxb.LocalDateAdapter;

public class LocalDateAdapterTest {

	private LocalDateAdapter adapter;

	@BeforeEach
	public void setUp() {
		adapter = new LocalDateAdapter();
	}

	@Test
	public void marshalTest() throws Exception {
		Assertions.assertEquals("2016-01-03" ,adapter.marshal(LocalDate.parse("2016-01-03")));
	}
	@Test
	public void marshalNullTest() throws Exception {
		Assertions.assertNull(adapter.marshal(null));
	}

	@Test
	public void setValidDateTest() throws Exception {
		Assertions.assertEquals(LocalDate.parse("2012-02-04"), adapter.unmarshal("2012-02-04"));
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
