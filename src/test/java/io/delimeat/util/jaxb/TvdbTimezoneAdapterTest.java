package io.delimeat.util.jaxb;

import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
		Assert.assertEquals("NZ", adapter.unmarshal("TVNZ"));
	}
	
	@Test
	public void testNotExistsUnmarshal() throws Exception {
		Assert.assertEquals("EST", adapter.unmarshal("RANDOM"));
	}
	
	@Test
	public void testNullUnmarshal() throws Exception {
		Assert.assertNull(adapter.unmarshal(null));
	}
	
	@Test
	public void testEmptyUnmarshal() throws Exception {
		Assert.assertNull(adapter.unmarshal(""));
	}
	@Test
	public void test(){
		for(String id: TimeZone.getAvailableIDs()){
			System.out.println(id + " " +TimeZone.getTimeZone(id).getDisplayName());
			
		}
	}

}
