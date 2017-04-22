package io.delimeat.util.jaxrs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.util.jaxrs.CustomMOXyJsonProvider;

public class CustomMOXyJsonProviderTest {

	private CustomMOXyJsonProvider provider;
	
	@Before
	public void setUp() throws Exception {
		provider = new CustomMOXyJsonProvider();
	}
	
	@Test
	public void getJAXBContextTest() throws JAXBException{
		Set<Class<?>> domainClasses = new HashSet<Class<?>>();
		domainClasses.addAll(Arrays.asList(ArrayList.class,Set.class,Set.class, Object.class));
		
		provider.getJAXBContext(domainClasses,null, MediaType.APPLICATION_JSON_TYPE, null);
		Assert.assertEquals(1, domainClasses.size());
		Assert.assertEquals(domainClasses.iterator().next(), Object.class);
	}

}
