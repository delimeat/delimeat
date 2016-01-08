package io.delimeat.core.service;

import io.delimeat.core.config.Config;
import io.delimeat.core.config.ConfigDao;
import io.delimeat.core.config.ConfigException;
import io.delimeat.core.config.ConfigNotFoundException;

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
        Config config = null;
        try {
            config = dao.read();
        } catch (ConfigNotFoundException ex) {
            config = new Config();
            dao.create(config);
        }
        return config;
    }
  
	 @Override
    public void update(Config config) throws ConfigException {
        try {
            dao.update(config);
        } catch (ConfigNotFoundException ex) {
            dao.create(config);
        }
    }
}
