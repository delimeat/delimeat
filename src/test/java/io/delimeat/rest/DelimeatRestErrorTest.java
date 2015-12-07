package io.delimeat.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DelimeatRestErrorTest {

	private DelimeatRestError error;
	
	@Before
	public void setUp() throws Exception {
		error = new DelimeatRestError();
	}
	
	@Test
	public void messageTest(){
		Assert.assertNull(error.getMessage());
		error.setMessage("MESSAGE");
		Assert.assertEquals("MESSAGE", error.getMessage());
	}

}
