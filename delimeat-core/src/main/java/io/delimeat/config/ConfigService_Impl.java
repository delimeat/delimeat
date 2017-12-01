/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.delimeat.config;

import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.delimeat.config.entity.Config;
import io.delimeat.config.exception.ConfigConcurrencyException;
import io.delimeat.config.exception.ConfigException;

@Service
public class ConfigService_Impl implements ConfigService {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private ConfigDao configDao;

	@Value("${io.delimeat.config.defaultOutputDir}")
	private String defaultOutputDir;

	/**
	 * @return the configDao
	 */
	public ConfigDao getConfigDao() {
		return configDao;
	}

	/**
	 * @param configDao the configDao to set
	 */
	public void setConfigDao(ConfigDao configDao) {
		this.configDao = configDao;
	}

	/**
	 * @return the defaultOutputDir
	 */
	public String getDefaultOutputDir() {
		return defaultOutputDir;
	}

	/**
	 * @param defaultOutputDir the defaultOutputDir to set
	 */
	public void setDefaultOutputDir(String defaultOutputDir) {
		this.defaultOutputDir = defaultOutputDir;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.delimeat.config.ConfigService#read()
	 */
	@Override
	@Transactional
	public Config read() throws ConfigException {
		try {
			try {
				return configDao.read(1L);
			}catch(EntityNotFoundException ex) {
				Config defaultConfig = new Config();
				defaultConfig.setSearchInterval(4 * 60 * 60 * 1000);
				defaultConfig.setSearchDelay(60 * 60 * 1000);
				defaultConfig.setPreferFiles(true);
				defaultConfig.setIgnoreFolders(false);
				defaultConfig.setOutputDirectory(defaultOutputDir);
				LOGGER.trace("No config exists, creating default {}", defaultConfig);
				return update(defaultConfig);
			}
		}catch(PersistenceException ex) {
			throw new ConfigException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.delimeat.config.ConfigService#update(io.delimeat.config.domain.Config)
	 */
	@Override
	@Transactional
	public Config update(Config config) throws ConfigConcurrencyException, ConfigException {
		try {
			config.setConfigId(1L);
			if (config.isIgnoreFolders()) {
				config.setPreferFiles(true);
			}
			return configDao.update(config);
		} catch(OptimisticLockException ex) {
			throw new ConfigConcurrencyException(ex);
		} catch(PersistenceException ex) {
			throw new ConfigException(ex);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ConfigService_Impl [" + (configDao != null ? "configRepository=" + configDao + ", " : "")
				+ (defaultOutputDir != null ? "defaultOutputDir=" + defaultOutputDir : "") + "]";
	}
}
