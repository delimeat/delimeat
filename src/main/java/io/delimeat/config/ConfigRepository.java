package io.delimeat.config;

import org.springframework.data.repository.Repository;

import io.delimeat.config.domain.Config;

public interface ConfigRepository extends Repository<Config, Long> {
	
	public Config findOne(Long configId);
	
	public Config save(Config config);

}
