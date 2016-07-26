package io.delimeat.util.jaxb;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TorrentProjectTorrentUrlAdapterTest {

	private TorrentProjectTorrentUrlAdapter adapter;
	
	@Before
	public void setUp() throws Exception {
		adapter = new TorrentProjectTorrentUrlAdapter();
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
	public void unmarshallHttpTest() throws Exception{
		Assert.assertEquals("https://test.com", adapter.unmarshal("http://test.com"));
	}
	
	@Test
	public void unmarshallHttpsTest() throws Exception{
		Assert.assertEquals("https://test.com", adapter.unmarshal("https://test.com"));
	}
	
	@Test
	public void unmarshallOtherSchemeTest() throws Exception{
		Assert.assertEquals("udp://test.com", adapter.unmarshal("udp://test.com"));
	}
}
