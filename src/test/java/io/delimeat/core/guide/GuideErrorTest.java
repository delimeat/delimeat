package io.delimeat.core.guide;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GuideErrorTest {
  
	private GuideError error;

	@Before
	public void setUp() {
		error = new GuideError();
	}

	@Test
	public void messageTest() {
		Assert.assertNull(error.getMessage());
		error.setMessage("MESSAGE");
		Assert.assertEquals("MESSAGE", error.getMessage());
	}
}
