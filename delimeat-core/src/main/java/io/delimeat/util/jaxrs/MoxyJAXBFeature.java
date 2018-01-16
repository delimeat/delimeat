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

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoxyJAXBFeature implements Feature {

	private static final Logger LOGGER = LoggerFactory.getLogger(MoxyJAXBFeature.class);

	private final Map<String, Object> properties;
	private final List<Class<?>> classes;
	
	public MoxyJAXBFeature(Map<String, Object> properties, List<Class<?>> classes) {
		this.properties = properties;
		this.classes = classes;
	}
	
	@Override
	public boolean configure(final FeatureContext context) {
		LOGGER.trace("Configuring");
		context.register(MoxyJAXBContextProvider.class);
		context.register(MoxyJAXBProvider.class);
		context.register(new MoxyJAXBConfig(properties,classes).resolver());
		LOGGER.trace("Configured");
		return true;
	}

}