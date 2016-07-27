package io.delimeat.common.util.jaxrs;

import javax.xml.bind.JAXBContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class JaxbContextResolverTest {

	private JaxbContextResolver resolver;

	@Before
	public void setUp() {
		resolver = new JaxbContextResolver();
	}

  	@Test
  	public void classesTest(){
		Assert.assertNull(resolver.getClasses());
   	resolver.setClasses(Class.class);
		Assert.assertEquals(Class.class, resolver.getClasses()[0]);
   }
  
	@Test
	public void propertiesTest() {
		Assert.assertNotNull(resolver.getProperties());
     	Assert.assertEquals(0, resolver.getProperties().size());
     	Map<String, Object> properties = new HashMap<String,Object>();
     	properties.put("TEST", "VALUE");
     	resolver.setProperties(properties);
     	Assert.assertEquals(properties, resolver.getProperties());
	}
  
  	@Test
  	public void contextTest(){
     	JAXBContext context = resolver.getContext(Class.class);
		Assert.assertNotNull(context);
   }
  
  	@Test
  	public void contextInvalidTest(){
     	Map<String, Object> properties = new HashMap<String,Object>();
     	properties.put("TEST", "VALUE");
     	resolver.setProperties(properties);
     	JAXBContext context = resolver.getContext(Class.class);
		Assert.assertNull(context);
   }
}
