package io.delimeat.util.spark;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Filter;
import spark.Request;
import spark.Response;

public class RequestLoggingFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class);
	
			
	@Override
	public void handle(Request request, Response response) throws Exception {
		boolean hasRequestBody = request.body() != null;

		StringBuilder reqStrBuilder = new StringBuilder();

		reqStrBuilder.append("<-- " + request.requestMethod() + ' ' + request.url()
				+ request.protocol());
		if (hasRequestBody) {
			reqStrBuilder.append(" (" + request.contentLength() + "-byte body)");
		}
		reqStrBuilder.append("\n");

		if (hasRequestBody) {
			if (request.contentType() != null) {
				reqStrBuilder.append("Content-Type: " + request.contentType() + "\n");
			}
			if (request.contentLength() != -1) {
				reqStrBuilder.append("Content-Length: " + request.contentLength() + "\n");
			}
		}

		Set<String> reqHeaders = request.headers();
		for(String name: reqHeaders){
			if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
				reqStrBuilder.append(name + ": " + request.headers(name) + "\n");
			}
		}

		if (request.contentLength() == -1) {
			reqStrBuilder.append("<-- END " + request.requestMethod() + "\n");
		} else {

			reqStrBuilder.append("\n");
			reqStrBuilder.append(request.body() + "\n");
			reqStrBuilder.append("<-- END " + request.requestMethod() 
				+ " (" + request.contentLength() 
				+ "-byte body)" + "\n");
		}
		LOGGER.trace(reqStrBuilder.toString());
	}

}
