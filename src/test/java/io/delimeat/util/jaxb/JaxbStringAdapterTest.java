package io.delimeat.util.jaxb;

import io.delimeat.util.jaxb.JaxbStringAdapter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JaxbStringAdapterTest {

	private JaxbStringAdapter adapter;

	@Before
	public void setUp() {
		adapter = new JaxbStringAdapter();
	}

	@Test
	public void marshalTest() throws Exception {
		Assert.assertNull(adapter.marshal(null));
	}

	@Test
	public void unmarshalNullTest() throws Exception {
		Assert.assertNull(adapter.unmarshal(null));
	}

	@Test
	public void unmarshalEmptyStringTest() throws Exception {
		Assert.assertNull(adapter.unmarshal(""));
	}

	@Test
	public void unmarshalValueTest() throws Exception {
		Assert.assertEquals("TEXT", adapter.unmarshal("<html>TEXT</html>"));
	}
}
