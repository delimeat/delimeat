package io.delimeat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.delimeat.config.domain.Config;

@Service("configServiceId")
public class ConfigService_Impl implements ConfigService {

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

	/* (non-Javadoc)
	 * @see io.delimeat.config.ConfigService#read()
	 */
	@Override
	public Config read() {
		Config config = configRepository.findOne(1L);
		if(config == null){
			Config defaultConfig = new Config();
			defaultConfig.setSearchInterval(4* 60 * 60 * 1000);
			defaultConfig.setSearchDelay(60 * 60 * 1000);
			defaultConfig.setPreferFiles(true);
			defaultConfig.setIgnoreFolders(false);
			defaultConfig.setOutputDirectory(defaultOutputDir);
			config = update(defaultConfig);
		}
		
		return config;
		
	}

	/* (non-Javadoc)
	 * @see io.delimeat.config.ConfigService#update(io.delimeat.config.domain.Config)
	 */
	@Override
	public Config update(Config config) {
		config.setConfigId(1L);
		if(config.isIgnoreFolders()){
			config.setPreferFiles(true);
		}
		return configRepository.save(config);
	}
}
