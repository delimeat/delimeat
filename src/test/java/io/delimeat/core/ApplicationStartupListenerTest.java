package io.delimeat.core;

import static org.mockito.Mockito.mock;

import javax.servlet.ServletContextEvent;

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
		System.clearProperty(ApplicationStartupListener.CONFIG_FILE_PROPERTY);
		System.clearProperty(ApplicationStartupListener.SHOW_DB_DRIVER_PROPERTY);
		System.clearProperty(ApplicationStartupListener.SHOW_DB_PROPERTY);
	}

	@Test
	public void appPathLinuxTest() {
		System.setProperty("user.home", "HOME_DIR");
		System.setProperty("os.name", "LINUX");
		String result = listener.getAppPath();
		Assert.assertEquals("HOME_DIR/delimeat", result);
	}

	@Test
	public void appPathWindowsTest() {
		System.setProperty("user.home", "HOME_DIR");
		System.setProperty("os.name", "WINDOWS");
		String result = listener.getAppPath();
		Assert.assertEquals("HOME_DIR/Application Data/delimeat", result);
	}

	@Test
	public void appPathMacTest() {
		System.setProperty("user.home", "HOME_DIR");
		System.setProperty("os.name", "MAC");
		String result = listener.getAppPath();
		Assert.assertEquals("HOME_DIR/Library/Application Support/delimeat", result);
	}

	@Test
	public void contextInitializedNullValues() {
		System.setProperty("user.home", "HOME_DIR");
		System.setProperty("os.name", "LINUX");
		ServletContextEvent mockedServletContextEvent = mock(ServletContextEvent.class);
		listener.contextInitialized(mockedServletContextEvent);
		Assert.assertEquals("file:HOME_DIR/delimeat/config.xml",
				System.getProperty(ApplicationStartupListener.CONFIG_FILE_PROPERTY));
		Assert.assertEquals("org.apache.derby.jdbc.EmbeddedDriver",
				System.getProperty(ApplicationStartupListener.SHOW_DB_DRIVER_PROPERTY));
		Assert.assertEquals("jdbc:derby:HOME_DIR/delimeat/db;create=true",
				System.getProperty(ApplicationStartupListener.SHOW_DB_PROPERTY));
	}

	@Test
	public void contextInitializedSetValues() {
		System.setProperty(ApplicationStartupListener.CONFIG_FILE_PROPERTY, "CONFIG_FILE_LOC");
		System.setProperty(ApplicationStartupListener.SHOW_DB_DRIVER_PROPERTY, "DB_DRIVER");
		System.setProperty(ApplicationStartupListener.SHOW_DB_PROPERTY, "DB_TYPE");

		Assert.assertEquals("CONFIG_FILE_LOC", System.getProperty(ApplicationStartupListener.CONFIG_FILE_PROPERTY));
		Assert.assertEquals("DB_DRIVER", System.getProperty(ApplicationStartupListener.SHOW_DB_DRIVER_PROPERTY));
		Assert.assertEquals("DB_TYPE", System.getProperty(ApplicationStartupListener.SHOW_DB_PROPERTY));
	}
}
