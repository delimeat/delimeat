package io.delimeat.util.jaxrs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;
import javax.xml.bind.JAXBContext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MoxyJAXBContextProviderTest {

	private MoxyJAXBContextProvider provider;
	
	@BeforeEach
	public void setUp() {
		provider = new MoxyJAXBContextProvider();
	}
	
	@Test
	public void providerTest() {
		Assertions.assertNull(provider.getProviders());
		Providers providers = Mockito.mock(Providers.class);
		provider.setProviders(providers);
	}
	
	@Test
	public void getContextTest() {
		Providers providers = Mockito.mock(Providers.class);
		@SuppressWarnings("unchecked")
		ContextResolver<MoxyJAXBConfig> resolver = Mockito.mock(ContextResolver.class);
		Map<String,Object> properties = new HashMap<String, Object>();
		properties.put("TEST", "VALUE");
		MoxyJAXBConfig config = new MoxyJAXBConfig(properties, Arrays.asList(Object.class));
		Mockito.when(resolver.getContext(Mockito.any())).thenReturn(config);
		Mockito.when(providers.getContextResolver(MoxyJAXBConfig.class, MediaType.APPLICATION_JSON_TYPE)).thenReturn(resolver);
		provider.setProviders(providers);
		
		JAXBContext context = provider.getContext(Object.class);
		Assertions.assertEquals(org.eclipse.persistence.jaxb.JAXBContext.class, context.getClass());
	}
	
	@Test
	public void getContextExceptionTest() {
		Providers providers = Mockito.mock(Providers.class);
		@SuppressWarnings("unchecked")
		ContextResolver<MoxyJAXBConfig> resolver = Mockito.mock(ContextResolver.class);
		Map<String,Object> properties = new HashMap<String, Object>();
		properties.put("eclipselink.oxm.metadata-source", "JIBBERISH");
		MoxyJAXBConfig config = new MoxyJAXBConfig(properties, Arrays.asList(Object.class));
		Mockito.when(resolver.getContext(Mockito.any())).thenReturn(config);
		Mockito.when(providers.getContextResolver(MoxyJAXBConfig.class, MediaType.APPLICATION_JSON_TYPE)).thenReturn(resolver);
		provider.setProviders(providers);
		
		Assertions.assertNull(provider.getContext(Object.class));
	}
}
