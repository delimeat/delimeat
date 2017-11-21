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
package io.delimeat.guide.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.delimeat.guide.util.TvdbAiringStatusAdapter;

public class TvdbAiringStatusAdapterTest {

	private TvdbAiringStatusAdapter adapter;

	@BeforeEach
	public void setUp() {
		adapter = new TvdbAiringStatusAdapter();
	}

	@Test
	public void marshalTest() throws Exception {
		Assertions.assertNull(adapter.marshal(true));
	}

	@Test
	public void airingUnmarshalTest() throws Exception {
		Assertions.assertTrue(adapter.unmarshal("Continuing"));
	}

	@Test
	public void endedUnmarshalTest() throws Exception {
		Assertions.assertFalse(adapter.unmarshal("Ended"));
	}

	@Test
	public void unknownUnmarshalTest() throws Exception {
		Assertions.assertFalse(adapter.unmarshal("GIBERISH"));
	}

	@Test
	public void unmarshalNullTest() throws Exception {
		Assertions.assertFalse(adapter.unmarshal(null));
	}

	@Test
	public void unmarshalEmptyStringTest() throws Exception {
		Assertions.assertFalse(adapter.unmarshal(""));

	}
}
