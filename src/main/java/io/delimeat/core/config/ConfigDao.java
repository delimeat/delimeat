package io.delimeat.core.config;

public interface ConfigDao {
  
	public Config read() throws ConfigNotFoundException, ConfigException;

	public void update(Config config) throws ConfigNotFoundException, ConfigException;
}
