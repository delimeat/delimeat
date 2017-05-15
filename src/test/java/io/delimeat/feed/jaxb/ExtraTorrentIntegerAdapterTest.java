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
package io.delimeat.feed.jaxb;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExtraTorrentIntegerAdapterTest {

	private ExtraTorrentIntegerAdapter adapter;
	
	@Before
	public void setUp(){
		adapter = new ExtraTorrentIntegerAdapter();
	}
	
	@Test
	public void marshallNullTest() throws Exception{
		Assert.assertNull(adapter.marshal(Long.MIN_VALUE));
	}
	
	@Test
	public void unmarshallNullTest() throws Exception{
		Assert.assertNull(adapter.unmarshal(null));
	}
	
	@Test
	public void unmarshallEmptyStringTest() throws Exception{
		Assert.assertNull(adapter.unmarshal(""));
	}
	
	@Test
	public void unmarshallStringTest() throws Exception{
		Assert.assertEquals(0, adapter.unmarshal("STRING").longValue());
	}
	
	@Test
	public void unmarshallNumberStringTest() throws Exception{
		Assert.assertEquals(123456, adapter.unmarshal("123456").longValue());
	}
}
