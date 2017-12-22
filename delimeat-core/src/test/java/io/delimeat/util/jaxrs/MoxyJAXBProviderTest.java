package io.delimeat.util.jaxrs;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MoxyJAXBProviderTest {

	private MoxyJAXBProvider provider;
	
	@BeforeEach
	public void setUp() {
		provider = new MoxyJAXBProvider();
	}
	
	@Test
	public void isReadableTest() {
		Assertions.assertTrue(provider.isReadable(null, null, null, MediaType.APPLICATION_JSON_TYPE));
		Assertions.assertTrue(provider.isReadable(null, null, null, MediaType.APPLICATION_XML_TYPE));
		Assertions.assertFalse(provider.isReadable(null, null, null, MediaType.TEXT_HTML_TYPE));
	}
	
	@Test
	public void getSizeTest() {
		Assertions.assertEquals(-1, provider.getSize(null, null, null, null, null));
	}
	
	@Test
	public void isWriteableTest() {
		Assertions.assertTrue(provider.isWriteable(null, null, null, MediaType.APPLICATION_JSON_TYPE));
		Assertions.assertTrue(provider.isWriteable(null, null, null, MediaType.APPLICATION_XML_TYPE));
		Assertions.assertFalse(provider.isWriteable(null, null, null, MediaType.TEXT_HTML_TYPE));
	}
}
