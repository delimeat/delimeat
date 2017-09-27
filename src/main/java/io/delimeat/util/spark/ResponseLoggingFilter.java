package io.delimeat.util.spark;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Filter;
import spark.Request;
import spark.Response;

public class ResponseLoggingFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResponseLoggingFilter.class);
	
			
	@Override
	public void handle(Request request, Response response) throws Exception {
		StringBuilder respStrBuilder = new StringBuilder();
		String responseBody = response.body();
		boolean hasResponseBody = responseBody != null;
		long contentLength = -1;
		if(hasResponseBody){
			contentLength = responseBody.getBytes().length;
		}
		
		respStrBuilder
				.append("--> " + response.status() + ' '
						+ request.ip() + " (" + 0 + "ms )\n");
		
		if (hasResponseBody) {
			if (response.type() != null) {
				respStrBuilder.append("Content-Type: " + response.type() + "\n");
			}
			if (contentLength != -1) {
				respStrBuilder.append("Content-Length: " + contentLength + "\n");
			}
		}


		Collection<String> respHeaders = response.raw().getHeaderNames();
		for(String name: respHeaders){
			if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
				respStrBuilder.append(name + ": " + response.raw().getHeader(name) + "\n");
			}
		}
		

		if (!hasResponseBody) {
			respStrBuilder.append("--> END HTTP\n");
		} else {			
			if (contentLength > 0) {
				respStrBuilder.append("Body:\n"+responseBody+"\n");
			}

			respStrBuilder.append("--> END HTTP (" +contentLength + "-byte body)");
		}
		LOGGER.trace(respStrBuilder.toString());
	}

}
