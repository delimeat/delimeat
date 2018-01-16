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

import javax.ws.rs.core.FeatureContext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MoxyJAXBFeatureTest {

	@Test
	public void constructorTest() {
		Map<String,Object> properties = new HashMap<String, Object>();
		properties.put("TEST", "VALUE");
		MoxyJAXBFeature feature = new MoxyJAXBFeature(properties, Arrays.asList(Object.class));
		
		FeatureContext context = Mockito.mock(FeatureContext.class);
		Assertions.assertEquals(true, feature.configure(context));
		
		
		Mockito.verify(context).register(MoxyJAXBContextProvider.class);
		Mockito.verify(context).register(MoxyJAXBProvider.class);
	}
}
