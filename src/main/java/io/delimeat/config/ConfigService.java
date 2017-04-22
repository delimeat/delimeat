package io.delimeat.config;

import io.delimeat.config.domain.Config;

public interface ConfigService {

    public Config read();

    public Config update(Config config);
}
