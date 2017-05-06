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

import io.delimeat.guide.jaxb.TvMazeStringAdapter;

public class TvMazeStringAdapterTest {

	private TvMazeStringAdapter adapter;

	@Before
	public void setUp() {
		adapter = new TvMazeStringAdapter();
	}

	@Test
	public void marshalTest() throws Exception {
		Assert.assertNull(adapter.marshal(null));
	}

	@Test
	public void unmarshalNullTest() throws Exception {
		Assert.assertNull(adapter.unmarshal(null));
	}

	@Test
	public void unmarshalEmptyStringTest() throws Exception {
		Assert.assertNull(adapter.unmarshal(""));
	}

	@Test
	public void unmarshalValueTest() throws Exception {
		Assert.assertEquals("TEXT", adapter.unmarshal("<html>TEXT</html>"));
	}
}
