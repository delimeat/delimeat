package io.delimeat.core.config;

import java.io.IOException;

public interface ConfigDao {

	public Config read() throws IOException, Exception;

	public void update(Config config) throws IOException, Exception;
}
