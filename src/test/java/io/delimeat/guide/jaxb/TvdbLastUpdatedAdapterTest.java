package io.delimeat.guide.jaxb;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.guide.jaxb.TvdbLastUpdatedAdapter;

public class TvdbLastUpdatedAdapterTest {

	private TvdbLastUpdatedAdapter adapter;
	
	@Before
	public void setUp() throws Exception {
		adapter = new TvdbLastUpdatedAdapter();
	}

	@Test
	public void marshalTest() throws Exception {
		Assert.assertNull(adapter.marshal(null));
	}
	

	@Test
	public void testNullUnmarshal() throws Exception {
		Assert.assertNull(adapter.unmarshal(null));
	}
	
	@Test
	public void testUnmarshal() throws Exception{
		Assert.assertEquals(LocalDate.parse("2016-02-03"),adapter.unmarshal(new Long(1454486401)));
		
	}

}
