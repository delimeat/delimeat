package io.delimeat.util.jaxrs;

import java.net.URI;

import javax.ws.rs.core.MediaType;

public abstract class AbstractJaxrsClient {
	
  	protected String encoding = "UTF-8";
  	protected URI baseUri;
  	protected MediaType mediaType;
  	protected ClientFactory clientFactory;
	
	/**
	 * @return the client factory
	 */
	public ClientFactory getClientFactory() {
		return clientFactory;
	}
	
	/**
	 * @param clientFactory the clientFactory to set
	 */
	public void setClientFactory(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
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

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding
	 *            the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}
