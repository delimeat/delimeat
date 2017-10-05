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
		StringBuilder reqStrBuilder = new StringBuilder();
		
		reqStrBuilder.append("<-- " + request.requestMethod() + ' ' + request.url()
		+ ' ' + request.protocol());
		
		boolean hasRequestBody = request.body() != null;
		int contentLength = request.contentLength();

		if (hasRequestBody) {

			if (request.contentType() != null) {
				reqStrBuilder.append("Content-Type: " + request.contentType() + "\n");
			}
			
			if (contentLength != -1) {
				reqStrBuilder.append("Content-Length: " + contentLength + "\n");
			}
		}

		Set<String> reqHeaders = request.headers();
		for(String name: reqHeaders){
			if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
				reqStrBuilder.append(name + ": " + request.headers(name) + "\n");
			}
		}

		if (hasRequestBody) {;
			reqStrBuilder.append("Body:\n"+ request.body() + "\n");
			reqStrBuilder.append("<-- END " + request.requestMethod() + " (" + contentLength + "-byte body)" + "\n");
		} else {
			reqStrBuilder.append("<-- END " + request.requestMethod() + "\n");
		}
		LOGGER.trace(reqStrBuilder.toString());
	}

}
