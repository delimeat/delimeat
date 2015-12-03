package io.delimeat.core.guide;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TvdbTokenTest {

	private TvdbToken token;

	@Before
	public void setUp() {
		token = new TvdbToken();
	}

	@Test
	public void valueTest() {
		Assert.assertNull(token.getValue());
		Assert.assertNotEquals(0, token.getTime());
		token.setValue("VALUE");
		Assert.assertEquals("VALUE", token.getValue());
		Assert.assertNotEquals(0, token.getTime());
	}
}
