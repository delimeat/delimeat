package io.delimeat.core.service;

import io.delimeat.core.config.Config;
import io.delimeat.core.config.ConfigDao;
import io.delimeat.core.config.ConfigException;

public class ConfigService_Impl implements ConfigService {

	private ConfigDao dao;

	public void setConfigDao(ConfigDao dao) {
		this.dao = dao;
	}

	public ConfigDao getConfigDao() {
		return dao;
	}

	@Override
	public Config read() throws ConfigException {
		return dao.read();
	}

	@Override
	public Config update(Config config) throws ConfigException {
		dao.createOrUpdate(config);
		return config;
	}
}
