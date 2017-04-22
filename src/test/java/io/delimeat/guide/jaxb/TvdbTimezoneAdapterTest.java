package io.delimeat.guide.jaxb;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.guide.jaxb.TvdbTimezoneAdapter;

public class TvdbTimezoneAdapterTest {

	private TvdbTimezoneAdapter adapter;
	
	@Before
	public void setUp() throws Exception {
		adapter = new TvdbTimezoneAdapter();
	}

	@Test
	public void marshalTest() throws Exception {
		Assert.assertNull(adapter.marshal(null));
	}

	@Test
	public void testExistsUnmarshal() throws Exception {
		Assert.assertEquals("Etc/GMT+12", adapter.unmarshal("TVNZ"));
	}
	
	@Test
	public void testNotExistsUnmarshal() throws Exception {
		Assert.assertEquals("US/Pacific", adapter.unmarshal("RANDOM"));
	}
	
	@Test
	public void testNullUnmarshal() throws Exception {
		Assert.assertNull(adapter.unmarshal(null));
	}
	
	@Test
	public void testEmptyUnmarshal() throws Exception {
		Assert.assertNull(adapter.unmarshal(""));
	}

}
