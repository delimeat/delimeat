package io.delimeat.util.jaxb;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.util.jaxb.LocalDateAdapter;

public class LocalDateAdapterTest {

	private LocalDateAdapter adapter;

	@Before
	public void setUp() {
		adapter = new LocalDateAdapter();
	}

	@Test
	public void marshalTest() throws Exception {
		Assert.assertEquals("2016-01-03" ,adapter.marshal(LocalDate.parse("2016-01-03")));
	}
	@Test
	public void marshalNullTest() throws Exception {
		Assert.assertNull(adapter.marshal(null));
	}

	@Test
	public void setValidDateTest() throws Exception {
		Assert.assertEquals(LocalDate.parse("2012-02-04"), adapter.unmarshal("2012-02-04"));
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
