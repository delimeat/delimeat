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

import io.delimeat.guide.jaxb.TvdbTimezoneAdapter;

public class TvdbTimezoneAdapterTest {

	private TvdbTimezoneAdapter adapter;
	
	@Before
	public void setUp() throws Exception {
		adapter = new TvdbTimezoneAdapter();
	}

	@Test
	public void marshalTest() throws Exception {
		Assert.assertNull(adapter.marshal(null));
	}

	@Test
	public void testExistsUnmarshal() throws Exception {
		Assert.assertEquals("Etc/GMT+12", adapter.unmarshal("TVNZ"));
	}
	
	@Test
	public void testNotExistsUnmarshal() throws Exception {
		Assert.assertEquals("America/Los_Angeles", adapter.unmarshal("RANDOM"));
	}
	
	@Test
	public void testNullUnmarshal() throws Exception {
		Assert.assertNull(adapter.unmarshal(null));
	}
	
	@Test
	public void testEmptyUnmarshal() throws Exception {
		Assert.assertNull(adapter.unmarshal(""));
	}

}
