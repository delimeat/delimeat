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
		
		respStrBuilder.append("--> " + response.status() + ' '
						+ request.ip()+"\n");

		Collection<String> respHeaders = response.raw().getHeaderNames();
		for(String name: respHeaders){
			respStrBuilder.append(name + ": " + response.raw().getHeader(name) + "\n");
		}
		
		String responseBody = response.body();
		boolean hasResponseBody = responseBody != null;
		if (hasResponseBody) {			
			respStrBuilder.append("Body:\n"+ responseBody + "\n");
			respStrBuilder.append("--> END HTTP (" + responseBody.getBytes().length + "-byte body)");
		} else{
			respStrBuilder.append("--> END HTTP\n");
		}
		
		LOGGER.trace(respStrBuilder.toString());
	}

}
