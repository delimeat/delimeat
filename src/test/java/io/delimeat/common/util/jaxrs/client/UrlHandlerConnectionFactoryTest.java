package io.delimeat.common.util.jaxrs.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.delimeat.util.UrlHandler;

public class UrlHandlerConnectionFactoryTest {

	UrlHandlerConnectionFactory factory;

	@Before
	public void setUp() throws Exception {
		factory = new UrlHandlerConnectionFactory();
	}

	@Test
	public void urlHandlerTest() {
		Assert.assertNull(factory.getUrlHandler());
		UrlHandler mockedUrlHandler = Mockito.mock(UrlHandler.class);
		factory.setUrlHandler(mockedUrlHandler);
		Assert.assertEquals(mockedUrlHandler, factory.getUrlHandler());
	}

	@Test
	public void urlConnectionTest() throws IOException {
		UrlHandler mockedUrlHandler = Mockito.mock(UrlHandler.class);
		HttpURLConnection mockedHttpConnection = Mockito.mock(HttpURLConnection.class);
		Mockito.when(mockedUrlHandler.openUrlConnection(Mockito.any(URL.class))).thenReturn(mockedHttpConnection);
		factory.setUrlHandler(mockedUrlHandler);
		Assert.assertEquals(mockedHttpConnection, factory.getConnection(new URL("http://test.com")));
	}

	@Test // TODO more meaningful test?
	public void connectorFactoryTest() {
		Assert.assertNotNull(factory.createConnectionFactory());
	}

}
