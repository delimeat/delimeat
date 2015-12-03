package io.delimeat.util.jaxrs;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public abstract class AbstractJaxrsClientHelper {

	protected static final String ENCODING = "UTF-8";

	private URI baseUri;
	private MediaType mediaType;
	private Client client;
	private WebTarget target;
	
	/**
	 * @param client
	 *            the client to set
	 */
	public void setClient(Client client) {
		this.client = client;
	}
	/**
	 * @return the client
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * @return the target
	 */
	public WebTarget getTarget() {
		if (target == null) {
			target = getClient().target(baseUri);
		}
		return target;
	}

	/**
	 * @return the baseUri
	 */
	public URI getBaseUri() {
		return baseUri;
	}

	/**
	 * @param baseUri
	 *            the baseUri to set
	 */
	public void setBaseUri(URI baseUri) {
		this.baseUri = baseUri;
	}

	/**
	 * @return the mediaType
	 */
	public MediaType getMediaType() {
		return mediaType;
	}

	/**
	 * @param mediaType
	 *            the mediaType to set
	 */
	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

}
