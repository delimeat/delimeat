package io.delimeat.util.jaxrs;

import io.delimeat.util.jaxrs.JerseyClientFactory;

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
		factory.getProviders().add(mockedProvider);
		Assert.assertEquals(1, factory.getProviders().size());
		Assert.assertEquals(mockedProvider, factory.getProviders().get(0));
	}

	@Test
	public void newClientTest() {
		ConnectorProvider mockedConnectorProvider = Mockito.mock(ConnectorProvider.class);
		factory.setConnectorProvider(mockedConnectorProvider);

		ContextResolver<?> mockedProvider = Mockito.mock(ContextResolver.class);
		factory.getProviders().add(mockedProvider);

		Client client = factory.newClient();
		Assert.assertEquals(JerseyClient.class, client.getClass());
		JerseyClient jc = (JerseyClient) client;
		Assert.assertEquals(mockedConnectorProvider, jc.getConfiguration().getConnectorProvider());

		Assert.assertEquals(ClientConfig.class, client.getConfiguration().getClass());
		Assert.assertEquals(true, jc.getConfiguration().isRegistered(mockedProvider));
	}

}
