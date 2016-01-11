package io.delimeat.util.jaxb;

import io.delimeat.util.jaxb.TvdbAiringStatusAdapter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TvdbAiringStatusAdapterTest {

	private TvdbAiringStatusAdapter adapter;

	@Before
	public void setUp() {
		adapter = new TvdbAiringStatusAdapter();
	}

	@Test
	public void marshalTest() throws Exception {
		Assert.assertNull(adapter.marshal(true));
	}

	@Test
	public void airingUnmarshalTest() throws Exception {
		Assert.assertTrue(adapter.unmarshal("Continuing"));
	}

	@Test
	public void endedUnmarshalTest() throws Exception {
		Assert.assertFalse(adapter.unmarshal("Ended"));
	}

	@Test
	public void unknownUnmarshalTest() throws Exception {
		Assert.assertFalse(adapter.unmarshal("GIBERISH"));
	}

	@Test
	public void unmarshalNullTest() throws Exception {
		Assert.assertFalse(adapter.unmarshal(null));
	}

	@Test
	public void unmarshalEmptyStringTest() throws Exception {
		Assert.assertFalse(adapter.unmarshal(""));

	}
}
