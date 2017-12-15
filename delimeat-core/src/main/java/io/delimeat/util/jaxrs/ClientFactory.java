package io.delimeat.util.jaxrs;

import javax.ws.rs.client.Client;

public interface ClientFactory {

	public Client build(); 
}
