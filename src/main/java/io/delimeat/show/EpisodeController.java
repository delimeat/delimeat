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
package io.delimeat.show;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.delimeat.show.domain.Episode;
import io.delimeat.util.JsonUtils;
import io.delimeat.util.spark.SparkController;
import spark.Request;
import spark.Response;
import spark.Spark;

@Component
public class EpisodeController implements SparkController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShowController.class);

	@Autowired
	private EpisodeService episodeService;
	
	/**
	 * @return the episodeService
	 */
	public EpisodeService getEpisodeService() {
		return episodeService;
	}

	/**
	 * @param episodeService the episodeService to set
	 */
	public void setEpisodeService(EpisodeService episodeService) {
		this.episodeService = episodeService;
	}
	
	/* (non-Javadoc)
	 * @see io.delimeat.util.spark.SparkController#init()
	 */
	@Override
	public void init() throws Exception {
		LOGGER.trace("Entering init");

		Spark.path("/api/episode", () -> {
			Spark.get("", (Request request, Response response)-> {
				return episodeService.findAllPending();
			}, JsonUtils::toJson);
			
			Spark.put("/:id", (Request request, Response response)-> {
				Episode episode = JsonUtils.fromJson(request.bodyAsBytes(), Episode.class);
				episodeService.update(episode);
				return episode;
			}, JsonUtils::toJson);
			
		});
		
		LOGGER.trace("Leaving init");

	}

}
