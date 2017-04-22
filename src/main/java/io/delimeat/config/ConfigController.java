package io.delimeat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.delimeat.config.domain.Config;

@RestController
@RequestMapping(path = "/api")
public class ConfigController {

	@Autowired
	ConfigService configService;
	
	@RequestMapping(value = "/config", method = RequestMethod.GET, produces = "application/json")
    public Config read() {
        return configService.read();
    }
	
	
	@RequestMapping(value = "/config", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public Config update(@RequestBody Config config) {
		return configService.update(config);
	}
		
}