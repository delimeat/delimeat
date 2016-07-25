package io.delimeat.util.jaxb;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExtraTorrentIntegerAdapterTest {

	private ExtraTorrentIntegerAdapter adapter;
	
	@Before
	public void setUp() throws Exception {
		adapter = new ExtraTorrentIntegerAdapter();
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
	public void unmarshallNonNumberTest() throws Exception{
		Assert.assertEquals(0, adapter.unmarshal("---").longValue());
	}
	
	@Test
	public void unmarshallNumberTest() throws Exception{
		Assert.assertEquals(123456789, adapter.unmarshal("123456789").longValue());
	}
}
