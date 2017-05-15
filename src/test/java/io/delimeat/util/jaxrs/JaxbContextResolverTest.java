/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.delimeat.util.jaxrs;

import java.util.HashMap;
import java.util.Map;

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
     	properties.put("eclipselink.oxm.metadata-source", "JIBBERISH");
     	resolver.setProperties(properties);
     	Assert.assertNull(resolver.getContext(Class.class));
   }
}
