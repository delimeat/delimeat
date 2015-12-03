package io.delimeat.util.jaxb;

import io.delimeat.core.guide.GuideIdentifier;
import io.delimeat.core.guide.GuideSource;
import io.delimeat.util.jaxb.IMDBIdentifierAdapter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IMDBIdentifierAdapterTest {

	private IMDBIdentifierAdapter adapter;

	@Before
	public void setUp() {
		adapter = new IMDBIdentifierAdapter();
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
		GuideIdentifier id = adapter.unmarshal("VALUE");
		Assert.assertNotNull(id);
		Assert.assertEquals(GuideSource.IMDB, id.getSource());
		Assert.assertEquals("VALUE", id.getValue());
	}
}
