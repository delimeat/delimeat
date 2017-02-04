package io.delimeat.util.jaxb;

import io.delimeat.util.jaxb.TvdbDateAdapter;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TvdbDateAdapterTest {
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
    static {
    	SDF.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    
	private TvdbDateAdapter adapter;

	@Before
	public void setUp() {
		adapter = new TvdbDateAdapter();
	}

	@Test
	public void marshalTest() throws Exception {
		Assert.assertEquals("2016-01-03" ,adapter.marshal(SDF.parse("2016-01-03")));
	}
	@Test
	public void marshalNullTest() throws Exception {
		Assert.assertNull(adapter.marshal(null));
	}

	@Test
	public void setValidDateTest() throws Exception {
		Assert.assertEquals(SDF.parse("2012-02-04"), adapter.unmarshal("2012-02-04"));
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
