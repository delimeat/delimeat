package io.delimeat.util.jaxrs;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class ClientFactory_Impl implements ClientFactory{

	private List<Object> providers = new ArrayList<Object>();
	
	/**
	 * @return the providers
	 */
	public List<Object> getProviders() {
		return providers;
	}

	/**
	 * @param providers the providers to set
	 */
	public void setProviders(List<Object> providers) {
		this.providers = providers;
	}

	@Override
	public Client build() {
		Client client = ClientBuilder.newClient();
		providers.forEach(p->client.register(p));
		return client;
	}

}
