package io.delimeat.core.service;

import io.delimeat.core.config.Config;
import io.delimeat.core.config.ConfigDao;
import io.delimeat.core.config.ConfigException;
import io.delimeat.core.config.ConfigNotFoundException;

public class ConfigService_Impl implements ConfigService {

	private ConfigDao dao;
	private String defaultOutputDir;

	public void setConfigDao(ConfigDao dao) {
		this.dao = dao;
	}

	public ConfigDao getConfigDao() {
		return dao;
	}

	public String getDefaultOutputDir() {
		return defaultOutputDir;
	}

	public void setDefaultOutputDir(String defaultOutputDir) {
		this.defaultOutputDir = defaultOutputDir;
	}

	@Override
	public Config read() throws ConfigException {
		Config config = null;
		try {
			config = dao.read();
		} catch (ConfigNotFoundException ex) {
			config = new Config();
			config.setOutputDirectory(getDefaultOutputDir());
			dao.createOrUpdate(config);
		}
		return config;
	}

	@Override
	public Config update(Config config) throws ConfigException {
		dao.createOrUpdate(config);
		return config;
	}
}
