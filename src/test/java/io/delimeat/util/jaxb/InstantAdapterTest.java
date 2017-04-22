package io.delimeat.util.jaxb;

import java.time.Instant;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.util.jaxb.InstantAdapter;

public class InstantAdapterTest {
	
	private InstantAdapter adapter;

	@Before
	public void setUp() {
		adapter = new InstantAdapter();
	}

	@Test
	public void marshalTest() throws Exception {
		Assert.assertEquals("2017-04-05T04:00:00Z" ,adapter.marshal(Instant.parse("2017-04-05T04:00:00Z")));
	}
	@Test
	public void marshalNullTest() throws Exception {
		Assert.assertNull(adapter.marshal(null));
	}

	@Test
	public void setValidDateTest() throws Exception {
		Assert.assertEquals(Instant.ofEpochMilli(1), adapter.unmarshal("1970-01-01T00:00:00.001Z"));
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
