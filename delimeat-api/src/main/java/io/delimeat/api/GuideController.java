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
package io.delimeat.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.delimeat.api.util.JsonUtils;
import io.delimeat.api.util.SparkController;
import io.delimeat.guide.GuideService;
import spark.Request;
import spark.Response;
import spark.Spark;

@Component
public class GuideController implements SparkController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShowController.class);

	@Autowired
	private GuideService guideService;

	/**
	 * @return the guideService
	 */
	public GuideService getGuideService() {
		return guideService;
	}

	/**
	 * @param guideService the guideService to set
	 */
	public void setGuideService(GuideService guideService) {
		this.guideService = guideService;
	}

	@Override
	public void init() throws Exception{
		LOGGER.trace("Entering init");
		
		Spark.path("/api/guide", () -> {
	
			Spark.get("/search/:title", (Request request, Response response)-> {
				return guideService.readLike(request.params(":title"));
			}, JsonUtils::toJson);

			Spark.get("/info/:id", (Request request, Response response) -> {
				return guideService.read(request.params(":id"));
			}, JsonUtils::toJson);
			
			Spark.get("/info/:id/episodes", (Request request, Response response) -> {
				return guideService.readEpisodes(request.params(":id"));
			}, JsonUtils::toJson);
		});
			
		LOGGER.trace("Leaving init");
			
	}
}
