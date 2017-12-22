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
