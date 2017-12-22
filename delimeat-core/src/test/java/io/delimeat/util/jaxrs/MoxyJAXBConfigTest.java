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
