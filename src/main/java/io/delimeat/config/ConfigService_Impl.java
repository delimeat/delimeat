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

import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import io.delimeat.config.domain.Config;
import io.delimeat.config.exception.ConfigConcurrencyException;
import io.delimeat.config.exception.ConfigException;

@Service
public class ConfigService_Impl implements ConfigService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigService_Impl.class);

	@Autowired
	private ConfigRepository configRepository;

	@Value("${io.delimeat.config.defaultOutputDir}")
	private String defaultOutputDir;

	/**
	 * @return the configRepository
	 */
	public ConfigRepository getConfigRepository() {
		return configRepository;
	}

	/**
	 * @param configRepository the configRepository to set
	 */
	public void setConfigRepository(ConfigRepository configRepository) {
		this.configRepository = configRepository;
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
			Config config = configRepository.findOne(1L);
			if (Optional.ofNullable(config).isPresent() == false) {
				Config defaultConfig = new Config();
				defaultConfig.setSearchInterval(4 * 60 * 60 * 1000);
				defaultConfig.setSearchDelay(60 * 60 * 1000);
				defaultConfig.setPreferFiles(true);
				defaultConfig.setIgnoreFolders(false);
				defaultConfig.setOutputDirectory(defaultOutputDir);
				LOGGER.trace(String.format("No config exists, creating default %s", defaultConfig));
				return update(defaultConfig);
			}
			return config;
		} catch (DataAccessException e) {
			throw new ConfigException(e);
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
			return configRepository.save(config);
		} catch (ConcurrencyFailureException e) {
			throw new ConfigConcurrencyException(e);
		} catch (DataAccessException e) {
			throw new ConfigException(e);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ConfigService_Impl [" + (configRepository != null ? "configRepository=" + configRepository + ", " : "")
				+ (defaultOutputDir != null ? "defaultOutputDir=" + defaultOutputDir : "") + "]";
	}
}
