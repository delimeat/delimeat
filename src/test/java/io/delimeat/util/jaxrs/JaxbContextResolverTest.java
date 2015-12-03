package io.delimeat.util.jaxrs;

import static org.mockito.Mockito.mock;

import javax.xml.bind.JAXBContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
		resolver.getClasses().add(Class.class);
		Assert.assertEquals(mockedContext, resolver.getContext(Class.class));
	}
}
