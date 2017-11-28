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

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.delimeat.api.util.JsonUtils;
import io.delimeat.api.util.SparkController;
import io.delimeat.show.EpisodeService;
import io.delimeat.show.ShowService;
import io.delimeat.show.entity.Show;
import io.delimeat.show.exception.ShowConcurrencyException;
import io.delimeat.show.exception.ShowNotFoundException;
import spark.Request;
import spark.Response;
import spark.Spark;

@Named
public class ShowController implements SparkController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShowController.class);

	@Inject
	private ShowService showService;

	@Inject
	private EpisodeService episodeService;
	
	/**
	 * @return the showService
	 */
	public ShowService getShowService() {
		return showService;
	}

	/**
	 * @param showService the showService to set
	 */
	public void setShowService(ShowService showService) {
		this.showService = showService;
	}

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
	
	@Override
	public void init() throws Exception{
		LOGGER.trace("Entering init");

		Spark.path("/api/show", () -> {
			Spark.get("", (Request request, Response response)-> {
				return showService.readAll();
			}, JsonUtils::toJson);
	
			Spark.post("", (Request request, Response response)-> {
				Show show = JsonUtils.fromJson(request.bodyAsBytes(), Show.class);
				showService.create(show);
				return show;
			}, JsonUtils::toJson);
			
			Spark.get("/:id", (Request request, Response response) -> {
				Long showId = Long.valueOf(request.params(":id"));
				return showService.read(showId);
			}, JsonUtils::toJson);
	
			Spark.put("/:id", (Request request, Response response) -> {
				Show show = JsonUtils.fromJson(request.bodyAsBytes(), Show.class);
				return showService.update(show);
			}, JsonUtils::toJson);
	
			Spark.delete("/:id", (Request request, Response response) -> {
				Long showId = Long.valueOf(request.params(":id"));
				showService.delete(showId);
				return "";
			});
	
			Spark.get("/:id/episode", (Request request, Response response) -> {
				Long showId = Long.valueOf(request.params(":id"));
				return episodeService.findByShow(showId);
			}, JsonUtils::toJson);
		
		});
		
		Spark.exception(ShowConcurrencyException.class, (exception, request, response) -> {
		    response.body("{\"message\":\"You are trying to update a resource that has been modified\"}");
		    response.status(412);
		    response.type(JSON_CONTENT_TYPE);
		});
		
		Spark.exception(ShowNotFoundException.class, (exception, request, response) -> {
		    response.body("{\"message\":\"Unable to find requested resource\"}");
		    response.status(404);
		    response.type(JSON_CONTENT_TYPE);
		});
		
		LOGGER.trace("Leaving init");

	}
}
