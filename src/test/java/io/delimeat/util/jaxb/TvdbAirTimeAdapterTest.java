package io.delimeat.util.jaxb;

import io.delimeat.util.jaxb.TvdbAirTimeAdapter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TvdbAirTimeAdapterTest {
	
	private TvdbAirTimeAdapter adapter;

	@Before
	public void setUp() {
		adapter = new TvdbAirTimeAdapter();
	}

	@Test
	public void marshalTest() throws Exception {
		Assert.assertNull(adapter.marshal(98765432));
	}

	@Test
	public void airTime12HourMinutesTest() throws Exception {
		Assert.assertEquals(72000000, adapter.unmarshal("8:00 PM").intValue());
	}

	@Test
	public void airTime12HourNoMinutesTest() throws Exception {
		Assert.assertEquals(72000000, adapter.unmarshal("8 PM").intValue());
	}

	@Test
	public void airTime24HourTest() throws Exception {
		Assert.assertEquals(72000000, adapter.unmarshal("20:00").intValue());
	}

	@Test
	public void airTimeUnknownTest() throws Exception {
		Assert.assertEquals(0, adapter.unmarshal("20:00 PM").intValue());
	}

	@Test
	public void unmarshalNullTest() throws Exception {
		Assert.assertNull(adapter.unmarshal(null));
	}

	@Test
	public void unmarshalEmptyStringTest() throws Exception {
		Assert.assertNull(adapter.unmarshal(""));
	}
}
