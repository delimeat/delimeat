package io.delimeat.core.service;

import io.delimeat.core.config.Config;
import io.delimeat.core.config.ConfigDao;
import io.delimeat.core.config.ConfigException;
import io.delimeat.core.config.ConfigNotFoundException;

public interface ConfigService {

    public Config read() throws ConfigException;

    public void update(Config config) throws ConfigException;
}
