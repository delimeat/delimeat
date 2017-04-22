package io.delimeat.guide.jaxb;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.guide.jaxb.TvdbGenreAdapter;

public class TvdbGenreAdapterTest {

	private TvdbGenreAdapter adapter;

	@Before
	public void setUp() {
		adapter = new TvdbGenreAdapter();
	}

	@Test
	public void marshalTest() throws Exception {
		Assert.assertNull(adapter.marshal(null));
	}

	@Test
	public void unmarshalListTest() throws Exception {
		List<?> list = (List<?>) adapter.unmarshal("GENRE1|GENRE2|GENRE3");
		Assert.assertEquals(3, list.size());
		Assert.assertEquals("GENRE1", list.get(0));
		Assert.assertEquals("GENRE2", list.get(1));
		Assert.assertEquals("GENRE3", list.get(2));
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
