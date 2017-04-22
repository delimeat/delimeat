package io.delimeat.guide.jaxb;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.guide.jaxb.TvMazeStringAdapter;

public class TvMazeStringAdapterTest {

	private TvMazeStringAdapter adapter;

	@Before
	public void setUp() {
		adapter = new TvMazeStringAdapter();
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
	public void unmarshalValueTest() throws Exception {
		Assert.assertEquals("TEXT", adapter.unmarshal("<html>TEXT</html>"));
	}
}
