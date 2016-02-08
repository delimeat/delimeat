package io.delimeat.core.config;

public interface ConfigDao {
  
	public Config read() throws ConfigException;

	public void createOrUpdate(Config config) throws ConfigException;
}
