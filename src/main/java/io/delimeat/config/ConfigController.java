/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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