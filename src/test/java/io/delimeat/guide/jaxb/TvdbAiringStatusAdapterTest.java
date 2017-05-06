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
package io.delimeat.guide.jaxb;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.guide.jaxb.TvdbAiringStatusAdapter;

public class TvdbAiringStatusAdapterTest {

	private TvdbAiringStatusAdapter adapter;

	@Before
	public void setUp() {
		adapter = new TvdbAiringStatusAdapter();
	}

	@Test
	public void marshalTest() throws Exception {
		Assert.assertNull(adapter.marshal(true));
	}

	@Test
	public void airingUnmarshalTest() throws Exception {
		Assert.assertTrue(adapter.unmarshal("Continuing"));
	}

	@Test
	public void endedUnmarshalTest() throws Exception {
		Assert.assertFalse(adapter.unmarshal("Ended"));
	}

	@Test
	public void unknownUnmarshalTest() throws Exception {
		Assert.assertFalse(adapter.unmarshal("GIBERISH"));
	}

	@Test
	public void unmarshalNullTest() throws Exception {
		Assert.assertFalse(adapter.unmarshal(null));
	}

	@Test
	public void unmarshalEmptyStringTest() throws Exception {
		Assert.assertFalse(adapter.unmarshal(""));

	}
}
