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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ext.ContextResolver;

public class MoxyJAXBConfig {

	private final Map<String, Object> properties = new HashMap<>();
	private final List<Class<?>> classes = new ArrayList<>();
	
	MoxyJAXBConfig(Map<String, Object> properties, List<Class<?>> classes) {
		this.classes.addAll(classes);
		this.properties.putAll(properties);
	}
	
	private MoxyJAXBConfig(final MoxyJAXBConfig that) {
		this(that.getProperties(), that.getClasses());
	}
	
	/**
	 * @return the properties
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}

	/**
	 * @return the classes
	 */
	public List<Class<?>> getClasses() {
		return classes;
	}

	public ContextResolver<MoxyJAXBConfig> resolver() {
        return new ContextResolver<MoxyJAXBConfig>() {

            private final MoxyJAXBConfig config = new MoxyJAXBConfig(MoxyJAXBConfig.this);

            @Override
            public MoxyJAXBConfig getContext(final Class<?> type) {
                return config;
            }
        };
    }
}
