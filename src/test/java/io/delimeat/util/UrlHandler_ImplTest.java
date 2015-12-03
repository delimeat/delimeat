package io.delimeat.util;

import io.delimeat.util.UrlHandler_Impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

//TODO add additional tests
public class UrlHandler_ImplTest {

	private UrlHandler_Impl handler;

	@Before
	public void setUp() {
		handler = new UrlHandler_Impl();
	}

	@Test
	public void timeoutTest() {
		Assert.assertEquals(0, handler.getTimeout());
		handler.setTimeout(Integer.MAX_VALUE);
		Assert.assertEquals(Integer.MAX_VALUE, handler.getTimeout());
	}
}
