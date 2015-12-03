package io.delimeat.util.jaxrs;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.spi.ConnectorProvider;

public class JerseyClientFactory {

	private ConnectorProvider connectorProvider;
	private List<Object> providers = new ArrayList<Object>();

	/**
	 * @return the connectorProvider
	 */
	public ConnectorProvider getConnectorProvider() {
		return connectorProvider;
	}

	/**
	 * @param connectorProvider
	 *            the connectorProvider to set
	 */
	public void setConnectorProvider(ConnectorProvider connectorProvider) {
		this.connectorProvider = connectorProvider;
	}

	/**
	 * @return the providers
	 */
	public List<Object> getProviders() {
		return providers;
	}

	/**
	 * @param providers
	 *            the providers to set
	 */
	public void setProviders(List<Object> providers) {
		this.providers = providers;
	}

	public Client newClient() {
		ClientConfig configuration = new ClientConfig();
		for (Object provider : providers) {
			configuration.register(provider);
		}
		if (connectorProvider != null) {
			configuration.connectorProvider(connectorProvider);
		}
		return JerseyClientBuilder.createClient(configuration);
	}

}
