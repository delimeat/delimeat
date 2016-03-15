package io.delimeat.core;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

@WebListener
public class ApplicationStartupListener implements ServletContextListener {

	// supported os types
	private static final String WINDOWS = "windows";
	private static final String MAC = "mac";
	private static final String LINUX = "linux";
	// app dir stuff
	private static final String DELIMEAT_DIR = "delimeat";
	private static final String WINDOWS_DIR = "/Application Data";
	private static final String MAC_DIR = "/Library/Application Support";
	// config stuff
	public static final String CONFIG_FILE_PROPERTY = "io.delimeat.core.config.url";
	private static final String CONFIG_FILE_URL = "file:APP_DIR/config.xml";
	// show db stuff
	public static final String SHOW_DB_PROPERTY = "io.delimeat.core.show.url";
	private static final String SHOW_DB_URL = "jdbc:derby:APP_DIR/db;create=true";

	public static final String SHOW_DB_DRIVER_PROPERTY = "io.delimeat.core.show.driver";
	private static final String SHOW_DB_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

  	private static Logger LOGGER = LoggerFactory.getLogger(ApplicationStartupListener.class);

	private String appPath;

	public ApplicationStartupListener() {
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		java.util.logging.LogManager.getLogManager().reset();
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		java.util.logging.Logger.getLogger("global").setLevel(java.util.logging.Level.FINEST);
		
		LOGGER.debug("Entering contextInitialized");

		if (System.getProperty(CONFIG_FILE_PROPERTY) == null) {
			System.setProperty(CONFIG_FILE_PROPERTY, CONFIG_FILE_URL.replace("APP_DIR", getAppPath()));
		}
		LOGGER.debug(CONFIG_FILE_PROPERTY + " = " + System.getProperty(CONFIG_FILE_PROPERTY));

		if (System.getProperty(SHOW_DB_PROPERTY) == null) {
			System.setProperty(SHOW_DB_PROPERTY, SHOW_DB_URL.replace("APP_DIR", getAppPath()));
		}
		LOGGER.debug( SHOW_DB_PROPERTY + " = " + System.getProperty(SHOW_DB_PROPERTY));

		if (System.getProperty(SHOW_DB_DRIVER_PROPERTY) == null) {
			System.setProperty(SHOW_DB_DRIVER_PROPERTY, SHOW_DB_DRIVER);
		}
		LOGGER.debug( SHOW_DB_DRIVER_PROPERTY + " = " + System.getProperty(SHOW_DB_DRIVER_PROPERTY));
		
		
		LOGGER.debug("Exiting contextInitialized");

	}

	public String getAppPath() {
		if (appPath == null) {
			String userDir = System.getProperty("user.home");
			String osName = System.getProperty("os.name").toLowerCase();
			if (osName.startsWith(WINDOWS)) {
				appPath = userDir + WINDOWS_DIR + "/" + DELIMEAT_DIR;
			} else if (osName.startsWith(MAC)) {
				appPath = userDir + MAC_DIR + "/" + DELIMEAT_DIR;
			} else if (osName.startsWith(LINUX)) {
				appPath = userDir + "/" + DELIMEAT_DIR;
			} else {
				appPath = "";
			}
			File dir = new File(appPath);
			if (!dir.exists()) {
				// TODO is this actually needed?
				// dir.mkdirs();
			}
		}
		return appPath;
	}

}
