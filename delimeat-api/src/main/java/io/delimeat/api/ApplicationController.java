package io.delimeat.api;
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


import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import io.delimeat.api.util.RequestLoggingFilter;
import io.delimeat.api.util.ResponseLoggingFilter;
import io.delimeat.api.util.SparkController;
import spark.Request;
import spark.Response;
import spark.Spark;

@Named
public class ApplicationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);
	private static final String JSON_CONTENT_TYPE = "application/json; charset=UTF-8";
	
	@Value("${io.delimeat.port}")
	private int port = 8080;
		
	@Inject
	private List<SparkController> controllers = new ArrayList<SparkController>();
	
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the controllers
	 */
	public List<SparkController> getControllers() {
		return controllers;
	}

	/**
	 * @param controllers the controllers to set
	 */
	public void setControllers(List<SparkController> controllers) {
		this.controllers = controllers;
	}

	@PostConstruct
	public void init() throws Exception {
		LOGGER.trace("Entering init");
		Spark.port(port);

		Spark.initExceptionHandler((e)->{
			LOGGER.error("failed to ignite", e);
		    System.exit(100);
		});
		
		Spark.staticFiles.location("/static");

		
		Spark.notFound((request, response) -> {
			response.type(JSON_CONTENT_TYPE);
			//response.header("Content-Encoding", "gzip");
		    return "{\"message\":\"resource not found\"}";
		});
		
		
		Spark.internalServerError((request, response) -> {
			response.type(JSON_CONTENT_TYPE);
			//response.header("Content-Encoding", "gzip");
		    return "{\"message\":\"internal server error\"}";		    
		});
		
		Spark.exception(NumberFormatException.class, (exception, request, response) -> {
			response.type(JSON_CONTENT_TYPE);
		    response.body("{\"message\":\"invalid request format\"}");
		    response.status(400);
		    //response.header("Content-Encoding", "gzip");
		});

		Spark.before(new RequestLoggingFilter());
		
		Spark.after("/api/*",(Request request, Response response) -> {
			if(response.body() != null){
				response.type(JSON_CONTENT_TYPE);
				//response.header("Content-Encoding", "gzip");
				//response.header("Content-Length", Integer.toString(response.body().getBytes().length));
			}
		});

		Spark.afterAfter(new ResponseLoggingFilter());
		
		LOGGER.trace("Starting child controllers");
		for(SparkController controller: controllers){
			controller.init();
		}
		LOGGER.trace("Started child controllers");

		LOGGER.trace("Leaving init");
	}

}
