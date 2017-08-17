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
package io.delimeat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.delimeat.util.spark.SparkController;
import spark.Request;
import spark.Response;
import spark.Spark;

@Component
public class ApplicationController implements SparkController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);

	@Value("${io.delimeat.port}")
	private int port = 8080;
	
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

	/* (non-Javadoc)
	 * @see io.delimeat.util.spark.SparkController#init()
	 */
	@Override
	public void init() throws Exception {
		LOGGER.trace("Entering init");
		Spark.port(port);

		//TODO enable after moving to v2.6.0 of spark (conflict with wiremock)
		/*
		Spark.initExceptionHandler((e)->{
			LOGGER.error("ignite failed", e);
		    System.exit(100);
		});
		*/
		
		Spark.staticFiles.location("/static");

		
		Spark.notFound((request, response) -> {
			response.type(JSON_CONTENT_TYPE);
			response.header("Content-Encoding", "gzip");
		    return "{\"message\":\"resource not found\"}";
		});
		
		
		Spark.internalServerError((request, response) -> {
			response.type(JSON_CONTENT_TYPE);
			response.header("Content-Encoding", "gzip");
		    return "{\"message\":\"internal server error\"}";		    
		});
		
		Spark.exception(NumberFormatException.class, (exception, request, response) -> {
			response.type(JSON_CONTENT_TYPE);
		    response.body("{\"message\":\"invalid request format\"}");
		    response.status(400);
		    response.header("Content-Encoding", "gzip");
		});
		
		Spark.before((request, response) -> {
			StringBuilder sb = new StringBuilder();
			sb.append("HTTP REQUEST \n");
		    sb.append(request.requestMethod());
		    sb.append(" " + request.url());
		    sb.append(" " + request.ip());
		    sb.append("\n");
		    for(String key: request.headers()){
		    	sb.append(key + ": " + request.headers(key) + "\n");
		    }
		    sb.append("Body: " + (request.body().isEmpty() ? "empty" : request.body()));
		    LOGGER.trace(sb.toString());
		});
		
		Spark.after("/api/*",(Request request, Response response) -> {
			response.type(JSON_CONTENT_TYPE);
			response.header("Content-Encoding", "gzip");
		});
		
		
		Spark.afterAfter((request, response) -> {
			StringBuilder sb = new StringBuilder();
			sb.append("HTTP RESPONSE \n");
		    sb.append(response.status());
		    sb.append(" " + response.type());
		    sb.append("\n");		    
		    sb.append(request.url());
		    sb.append(" " + request.ip());
		    sb.append("\n");		    
		    sb.append("Body: " + (response.body() == null || response.body().isEmpty() ? "empty" : response.body()));
		    LOGGER.trace(sb.toString());
		});
		
		LOGGER.trace("Leaving init");
	}

}
