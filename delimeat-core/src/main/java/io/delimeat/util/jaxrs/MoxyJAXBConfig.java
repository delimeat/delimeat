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
