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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.delimeat.config.domain.Config;
import io.delimeat.config.exception.ConfigConcurrencyException;
import io.delimeat.util.JsonUtils;
import io.delimeat.util.spark.SparkController;
import spark.Request;
import spark.Response;
import spark.Spark;

@Component
public class ConfigController implements SparkController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigController.class);

	@Autowired
	ConfigService configService;
	
	/**
	 * @return the configService
	 */
	public ConfigService getConfigService() {
		return configService;
	}

	/**
	 * @param configService the configService to set
	 */
	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.util.spark.SparkController#init()
	 */
	@Override
	public void init() throws Exception{
		LOGGER.trace("Entering init");
	
		Spark.path("/api/config", () -> {
			Spark.get("",(Request request, Response response) -> {
				return configService.read();
			}, JsonUtils::toJson);
	
			Spark.put("",(Request request, Response response) -> {
				Config config = JsonUtils.fromJson(request.bodyAsBytes(), Config.class);
				return configService.update(config);
			}, JsonUtils::toJson);
		});
		
		Spark.exception(ConfigConcurrencyException.class, (exception, request, response) -> {
		    response.body("{\"message\":\"You are trying to update a resource that has been modified\"}");
		    response.status(412);
		    response.type(JSON_CONTENT_TYPE);
		});

		LOGGER.trace("Leaving init");

	}	
}