package io.delimeat.util.jaxb;

import java.text.SimpleDateFormat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TvdbLastUpdatedAdapterTest {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

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
		Assert.assertEquals("2016-02-03",SDF.format(adapter.unmarshal(new Long(1454486401))));
		
	}

}
