package io.delimeat.core.service;

import io.delimeat.core.config.Config;
import io.delimeat.core.config.ConfigException;

public interface ConfigService {

    public Config read() throws ConfigException;

    public Config update(Config config) throws ConfigException;
}
