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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.ext.ContextResolver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MoxyJAXBConfigTest {

	@Test
	public void constructorTest() {
		Map<String,Object> properties = new HashMap<String, Object>();
		properties.put("TEST", "VALUE");
		MoxyJAXBConfig config = new MoxyJAXBConfig(properties, Arrays.asList(Object.class));
		
		Assertions.assertEquals(properties, config.getProperties());
		Assertions.assertEquals( Arrays.asList(Object.class), config.getClasses());
		
		ContextResolver<MoxyJAXBConfig> resolver = config.resolver();
		
		MoxyJAXBConfig resolvedConfig = resolver.getContext(Object.class);
		Assertions.assertEquals(properties, resolvedConfig.getProperties());
		Assertions.assertEquals( Arrays.asList(Object.class), resolvedConfig.getClasses());
	}
}
