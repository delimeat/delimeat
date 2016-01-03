package io.delimeat.util.jaxb;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AirTimeAdapterTest {

	private AirTimeAdapter adapter;
	@Before
	public void setUp() throws Exception {
		adapter = new AirTimeAdapter();
	}

	@Test
	public void testMarshal() throws Exception {
		String value = adapter.marshal(45900000);
		Assert.assertEquals("12:45 PM", value);
	}

	@Test
	public void testUnmarshal() throws Exception {
		Integer value = adapter.unmarshal("12:45 pm");
		Assert.assertEquals(new Integer(45900000), value);
	}

}
