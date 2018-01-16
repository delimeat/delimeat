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
package io.delimeat.util.jaxrs;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MoxyJAXBProviderTest {

	private MoxyJAXBProvider provider;
	
	@BeforeEach
	public void setUp() {
		provider = new MoxyJAXBProvider();
	}
	
	@Test
	public void isReadableTest() {
		Assertions.assertTrue(provider.isReadable(null, null, null, MediaType.APPLICATION_JSON_TYPE));
		Assertions.assertTrue(provider.isReadable(null, null, null, MediaType.APPLICATION_XML_TYPE));
		Assertions.assertFalse(provider.isReadable(null, null, null, MediaType.TEXT_HTML_TYPE));
	}
	
	@Test
	public void getSizeTest() {
		Assertions.assertEquals(-1, provider.getSize(null, null, null, null, null));
	}
	
	@Test
	public void isWriteableTest() {
		Assertions.assertTrue(provider.isWriteable(null, null, null, MediaType.APPLICATION_JSON_TYPE));
		Assertions.assertTrue(provider.isWriteable(null, null, null, MediaType.APPLICATION_XML_TYPE));
		Assertions.assertFalse(provider.isWriteable(null, null, null, MediaType.TEXT_HTML_TYPE));
	}
}
