package io.delimeat.core.config;

public interface ConfigDao {
  
	public Config read() throws ConfigNotFoundException, ConfigException;

	public void createOrUpdate(Config config) throws ConfigNotFoundException, ConfigException;
}
