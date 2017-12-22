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
