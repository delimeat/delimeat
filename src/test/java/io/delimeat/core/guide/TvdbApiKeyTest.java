package io.delimeat.core.guide;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TvdbApiKeyTest {

	private TvdbApiKey apiKey;

	@Before
	public void setUp() {
		apiKey = new TvdbApiKey();
	}

	@Test
	public void valueTest() {
		Assert.assertNull(apiKey.getValue());
		apiKey.setValue("VALUE");
		Assert.assertEquals("VALUE", apiKey.getValue());
	}
}
