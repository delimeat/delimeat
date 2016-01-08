package io.delimeat.core.config;

public interface ConfigDao {

   public void create(Config config) throws ConfigException;
  
	public Config read() throws ConfigNotFoundException, ConfigException;

	public void update(Config config) throws ConfigNotFoundException, ConfigException;
}
