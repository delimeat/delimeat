package io.delimeat.util.jaxrs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MoxyJAXBContextProviderTest {

	@Test
	public void constructorTest() {
		Map<String,Object> properties = new HashMap<String, Object>();
		properties.put("TEST", "VALUE");
		
		MoxyJAXBContextProvider provider = new MoxyJAXBContextProvider(properties, Arrays.asList(Object.class));
		
		JAXBContext context = provider.getContext(Object.class);
		
		Assertions.assertEquals(org.eclipse.persistence.jaxb.JAXBContext.class, context.getClass());	
	}
	
}
