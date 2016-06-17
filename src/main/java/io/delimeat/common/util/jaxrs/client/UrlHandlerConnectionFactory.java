package io.delimeat.common.util.jaxrs.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.client.HttpUrlConnectorProvider.ConnectionFactory;

import io.delimeat.util.UrlHandler;

public class UrlHandlerConnectionFactory implements ConnectionFactory {

	private UrlHandler urlHandler;

	@Override
	public HttpURLConnection getConnection(URL url) throws IOException {
		return (HttpURLConnection) urlHandler.openUrlConnection(url);
	}

	/**
	 * @return the urlhandler
	 */
	public UrlHandler getUrlHandler() {
		return urlHandler;
	}

	/**
	 * @param urlhandler
	 *            the urlhandler to set
	 */
	public void setUrlHandler(UrlHandler urlHandler) {
		this.urlHandler = urlHandler;
	}

	public HttpUrlConnectorProvider createConnectionFactory() {
		final HttpUrlConnectorProvider provider = new HttpUrlConnectorProvider();
		provider.connectionFactory(this);
		return provider;
	}

}
