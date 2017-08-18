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
package io.delimeat.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.delimeat.util.JsonUtils;
import io.delimeat.util.spark.SparkController;
import spark.Request;
import spark.Response;
import spark.Spark;

public class HttpStatisticsController  implements SparkController  {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpStatisticsController.class);
	
	@Autowired
	HttpStatisticsService httpStatsService;

	/**
	 * @return the httpStatsService
	 */
	public HttpStatisticsService getHttpStatsService() {
		return httpStatsService;
	}

	/**
	 * @param httpStatsService the httpStatsService to set
	 */
	public void setHttpStatsService(HttpStatisticsService httpStatsService) {
		this.httpStatsService = httpStatsService;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.util.spark.SparkController#init()
	 */
	@Override
	public void init() throws Exception {
		LOGGER.trace("Entering init");
		
		Spark.path("/api/stats", () -> {
			Spark.get("",(Request request, Response response) -> {
				return httpStatsService.getStatistics();
			}, JsonUtils::toJson);

		});

		LOGGER.trace("Leaving init");
		
	}
}
