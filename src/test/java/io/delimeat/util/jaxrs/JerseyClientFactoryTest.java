package io.delimeat.util.jaxrs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.ext.ContextResolver;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.spi.ConnectorProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class JerseyClientFactoryTest {

	private JerseyClientFactory factory;

	@Before
	public void setUp() throws Exception {
		factory = new JerseyClientFactory();
	}

	@Test
	public void connectorProviderTest() {
		Assert.assertNull(factory.getConnectorProvider());
		ConnectorProvider mockedConnectorProvider = Mockito.mock(ConnectorProvider.class);
		
     	factory.setConnectorProvider(mockedConnectorProvider);
		
     	Assert.assertEquals(mockedConnectorProvider, factory.getConnectorProvider());
	}

	@Test
	public void providersTest() {
		Assert.assertNotNull(factory.getProviders());
		Assert.assertEquals(0, factory.getProviders().size());
		ContextResolver<?> mockedProvider = Mockito.mock(ContextResolver.class);
		
     	factory.setProviders(Arrays.<Object>asList(mockedProvider));
		
     	Assert.assertEquals(1, factory.getProviders().size());
		Assert.assertEquals(mockedProvider, factory.getProviders().get(0));
	}


	@Test
	public void providerClassesTest() {
		Assert.assertNotNull(factory.getProviderClasses());
		Assert.assertEquals(0, factory.getProviderClasses().size());
		List<Class<?>> providerClasses = Arrays.<Class<?>>asList(ContextResolver.class);
	     
     	factory.setProviderClasses(providerClasses);
		
     	Assert.assertEquals(1, factory.getProviderClasses().size());
		Assert.assertEquals(ContextResolver.class, factory.getProviderClasses().get(0));
	}
  
  	@Test
  	public void propertiesTest(){
		Assert.assertNotNull(factory.getProperties());
		Assert.assertEquals(0, factory.getProperties().size());
     	
     	Map<String,Object> properties = new HashMap<String, Object>();
		properties.put("TEST", this.getClass());     	
     
     	factory.setProperties(properties);
		
     	Assert.assertEquals(1, factory.getProperties().size());
		Assert.assertEquals(this.getClass(), factory.getProperties().get("TEST"));     	
   }
  
	@Test
	public void newClientTest() {
		ConnectorProvider mockedConnectorProvider = Mockito.mock(ConnectorProvider.class);
		factory.setConnectorProvider(mockedConnectorProvider);

		ContextResolver<?> mockedProvider = Mockito.mock(ContextResolver.class);
		factory.getProviders().add(mockedProvider);

     	factory.getProviderClasses().add(AddETagResponseFilter.class);
     
     	factory.getProperties().put("TEST", "VALUE");
     
		Client client = factory.newClient();
		
     	Assert.assertEquals(JerseyClient.class, client.getClass());
		JerseyClient jc = (JerseyClient) client;
     
		Assert.assertEquals(mockedConnectorProvider, jc.getConfiguration().getConnectorProvider());
		Assert.assertEquals(ClientConfig.class, client.getConfiguration().getClass());
		Assert.assertTrue(jc.getConfiguration().isRegistered(mockedProvider));
     	Assert.assertTrue(jc.getConfiguration().isRegistered(AddETagResponseFilter.class));
     	Assert.assertEquals("VALUE", jc.getConfiguration().getProperty("TEST"));
	}

}
