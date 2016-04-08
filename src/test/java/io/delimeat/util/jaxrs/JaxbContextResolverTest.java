package io.delimeat.util.jaxrs;

import static org.mockito.Mockito.mock;

import javax.xml.bind.JAXBContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class JaxbContextResolverTest {

	private JaxbContextResolver resolver;

	@Before
	public void setUp() {
		resolver = new JaxbContextResolver();
	}

	@Test
	public void jaxbContextTest() {
		JAXBContext mockedContext = mock(JAXBContext.class);
		resolver.setContext(mockedContext);
     	List<Class<?>> classes = Arrays.<Class<?>>asList(Class.class);
		resolver.setClasses(classes);
		Assert.assertEquals(mockedContext, resolver.getContext(Class.class));
	}
}
