package io.delimeat.core.networks;

import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.util.UrlHandler;

public class JaxbNetworksDao_ImplTest {

	private class XMLGenerator {
		private StringBuffer xml;

		public XMLGenerator() {
			xml = new StringBuffer();
			xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			xml.append("<networks>");
		}

		public void addNetwork(String name, String timezone) {
			xml.append("  <network>\n");
			xml.append("    <name>" + name + "</name>");
			xml.append("    <timezone>" + timezone + "</timezone>");
			xml.append("  </network>\n");
		}

		public String toString() {
			return xml.toString() + "</networks>";
		}

		public InputStream generate() throws Exception {
			return new ByteArrayInputStream(this.toString().getBytes("UTF-8"));
		}
	}

	private JaxbNetworksDao_Impl dao;

	@Before
	public void setUp() {
		dao = new JaxbNetworksDao_Impl();
	}

	@Test
	public void unmarshallerTest() {
		Assert.assertNull(dao.getUnmarshaller());
		Unmarshaller mockedUnmarshaller = Mockito.mock(Unmarshaller.class);
		dao.setUnmarshaller(mockedUnmarshaller);
		Assert.assertEquals(mockedUnmarshaller, dao.getUnmarshaller());
	}

	@Test
	public void urlHandlerTest() {
		Assert.assertNull(dao.getUrlHandler());
		UrlHandler mockedUrlHandler = Mockito.mock(UrlHandler.class);
		dao.setUrlHandler(mockedUrlHandler);
		Assert.assertEquals(mockedUrlHandler, dao.getUrlHandler());
	}

	@Test
	public void readFoundTest() throws IOException, Exception {
		XMLGenerator generator = new XMLGenerator();
		generator.addNetwork("NETWORK_NAME_ONE", "TIMEZONE_ONE");
		generator.addNetwork("NETWORK_NAME_TWO", "TIMEZONE_TWO");
		UrlHandler mockedHandler = Mockito.mock(UrlHandler.class);
		Mockito.when(mockedHandler.openInput(Mockito.any(URL.class))).thenReturn(generator.generate());
		dao.setUrlHandler(mockedHandler);

		JAXBContext jc = JAXBContext.newInstance(new Class[] { Network.class, Networks.class });
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		dao.setUnmarshaller(unmarshaller);

		dao.setUri(new URI("http://test.com"));

		Network network = dao.read("NETWORK_NAME_ONE");
		Assert.assertNotNull(network);

		Assert.assertEquals("NETWORK_NAME_ONE", network.getName());
		Assert.assertEquals("TIMEZONE_ONE", network.getTimezone());

	}

	@Test
	public void readNotFoundTest() throws IOException, Exception {
		XMLGenerator generator = new XMLGenerator();
		generator.addNetwork("NETWORK_NAME_ONE", "TIMEZONE_ONE");
		generator.addNetwork("NETWORK_NAME_TWO", "TIMEZONE_TWO");
		UrlHandler mockedHandler = Mockito.mock(UrlHandler.class);
		Mockito.when(mockedHandler.openInput(Mockito.any(URL.class))).thenReturn(generator.generate());
		dao.setUrlHandler(mockedHandler);

		JAXBContext jc = JAXBContext.newInstance(new Class[] { Network.class, Networks.class });
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		dao.setUnmarshaller(unmarshaller);

		dao.setUri(new URI("http://test.com"));

		Assert.assertNull(dao.read("JIBERISH"));

	}
}
