package io.delimeat.util.jaxrs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.spi.ConnectorProvider;
import org.glassfish.jersey.filter.LoggingFilter;

public class JerseyClientFactory {

	private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());

	private ConnectorProvider connectorProvider;
	private List<Object> providers = new ArrayList<Object>();
	private List<Class<?>> providerClasses = new ArrayList<Class<?>>();
	private Map<String,Object> properties = new HashMap<String,Object>();

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
	public void setProviderClasses(List<Class<?>> providerClasses) {
		this.providerClasses = providerClasses;
	}
 
	/**
	 * @return the providers
	 */
	public List<Class<?>> getProviderClasses() {
		return providerClasses;
	}

	/**
	 * @param providers
	 *            the providers to set
	 */
	public void setProviders(List<Object> providers) {
		this.providers = providers;
	}
  
	/**
	 * @return the properties
	 */
	public Map<String,Object> getProperties() {
		return properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(Map<String,Object> properties) {
		this.properties = properties;
	}

	public Client newClient() {
		final ClientConfig configuration = new ClientConfig();

		for (Object provider : providers) {
			configuration.register(provider);
		}

		for (Class<?> provider : providerClasses) {
			configuration.register(provider);
		}

		for (String key : properties.keySet()) {
			configuration.property(key, properties.get(key));
		}

		LoggingFilter loggingFilter = new LoggingFilter(LOGGER, true);
		configuration.register(loggingFilter);

		if (connectorProvider != null) {
			configuration.connectorProvider(connectorProvider);
		}

		return JerseyClientBuilder.createClient(configuration);
	}

}
