package io.delimeat.core.guide;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GuideIdentifierTest {

	private GuideIdentifier id;

	@Before
	public void setUp() {
		id = new GuideIdentifier();
	}

	@Test
	public void sourceTest() {
		Assert.assertNull(id.getSource());
		id.setSource(GuideSource.TVDB);
		Assert.assertEquals(GuideSource.TVDB, id.getSource());
	}

	@Test
	public void valueTest() {
		Assert.assertNull(id.getValue());
		id.setValue("VALUE");
		Assert.assertEquals("VALUE", id.getValue());
	}
}
