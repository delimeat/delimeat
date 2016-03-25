package io.delimeat.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ApplicationStartupListenerTest {

	private ApplicationStartupListener listener;

	@Before
	public void setUp() {
		listener = new ApplicationStartupListener();
	}

	@After
	public void after() {
		System.clearProperty("user.home");
		System.clearProperty("os.name");
	}
	
	@Test
	public void contextInitializedTest(){
		listener.contextInitialized(null);
		Assert.assertEquals(Level.FINEST,Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).getLevel());
	}

}
