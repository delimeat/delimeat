package io.delimeat.core;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.bridge.SLF4JBridgeHandler;

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

	//private static Log LOG = LogFactory.getLog(ApplicationStartupListener.class);
  	private static Logger LOGGER = Logger.getLogger(ApplicationStartupListener.class.getSimpleName());

	private String appPath;

	public ApplicationStartupListener() {
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		LogManager.getLogManager().reset();
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
		Logger.getLogger("global").setLevel(Level.FINEST);
		
		//LOG.debug("Entering contextInitialized");
      LOGGER.log(Level.FINE, "Entering contextInitialized");

		if (System.getProperty(CONFIG_FILE_PROPERTY) == null) {
			System.setProperty(CONFIG_FILE_PROPERTY, CONFIG_FILE_URL.replace("APP_DIR", getAppPath()));
		}
		//LOG.debug(CONFIG_FILE_PROPERTY + " = " + System.getProperty(CONFIG_FILE_PROPERTY));
      LOGGER.log(Level.FINE, CONFIG_FILE_PROPERTY + " = " + System.getProperty(CONFIG_FILE_PROPERTY));

		if (System.getProperty(SHOW_DB_PROPERTY) == null) {
			System.setProperty(SHOW_DB_PROPERTY, SHOW_DB_URL.replace("APP_DIR", getAppPath()));
		}
		//LOG.debug(SHOW_DB_PROPERTY + " = " + System.getProperty(SHOW_DB_PROPERTY));
      LOGGER.log(Level.FINE, SHOW_DB_PROPERTY + " = " + System.getProperty(SHOW_DB_PROPERTY));

		if (System.getProperty(SHOW_DB_DRIVER_PROPERTY) == null) {
			System.setProperty(SHOW_DB_DRIVER_PROPERTY, SHOW_DB_DRIVER);
		}
		//LOG.debug(SHOW_DB_DRIVER_PROPERTY + " = " + System.getProperty(SHOW_DB_DRIVER_PROPERTY));
      LOGGER.log(Level.FINE, SHOW_DB_DRIVER_PROPERTY + " = " + System.getProperty(SHOW_DB_DRIVER_PROPERTY));


		//LOG.debug("Exiting contextInitialized");
     	LOGGER.log(Level.FINE,"Exiting contextInitialized");

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
