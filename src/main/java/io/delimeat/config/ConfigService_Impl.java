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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import io.delimeat.config.domain.Config;
import io.delimeat.config.exception.ConfigConcurrencyException;
import io.delimeat.config.exception.ConfigException;
import lombok.Data;

@Service
@Data
public class ConfigService_Impl implements ConfigService {

	@Autowired
	private ConfigRepository configRepository;

	@Value("${io.delimeat.config.defaultOutputDir}")
	private String defaultOutputDir;

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
				config = update(defaultConfig);
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
		} catch (ObjectOptimisticLockingFailureException e) {
			throw new ConfigConcurrencyException(e);
		} catch (DataAccessException e) {
			throw new ConfigException(e);
		}
	}
}
