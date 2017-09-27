package io.delimeat.util.spark;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Filter;
import spark.Request;
import spark.Response;

public class SparkLoggingFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SparkLoggingFilter.class);
	
	private final boolean forRequests;
	
	public SparkLoggingFilter(boolean forRequests){
		this.forRequests = forRequests;
	}
	@Override
	public void handle(Request request, Response response) throws Exception {
		if(forRequests){
			handleRequest(request);
		}else{
			handleResponse(response, request.ip());
		}

	}
	
	public void handleRequest(Request request){

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
		} else if (bodyEncoded(request.headers("Content-Encoding"))) {
			reqStrBuilder.append("<-- END " + request.requestMethod() + " (encoded body omitted)" + "\n");
		} else {

			reqStrBuilder.append("\n");
			if (isPlaintext(ByteBuffer.wrap(request.bodyAsBytes()))) {
				reqStrBuilder.append(request.body() + "\n");
				reqStrBuilder.append("<-- END " + request.requestMethod() 
					+ " (" + request.contentLength() 
					+ "-byte body)" + "\n");
			} else {
				reqStrBuilder.append("<-- END " + request.requestMethod() 
					+ " (binary " + request.contentLength() 
					+ "-byte body omitted)" + "\n");
			}
		}
		LOGGER.trace(reqStrBuilder.toString());
	}
	
	public void handleResponse(Response response, String url ){
		StringBuilder respStrBuilder = new StringBuilder();
		String responseBody = response.body();
		boolean hasResponseBody = responseBody != null;
		long contentLength = -1;
		if(hasResponseBody){
			contentLength = responseBody.getBytes().length;
		}
		
		respStrBuilder
				.append("--> " + response.status() + ' '
						+ url + " (" + 0 + "ms )\n");
		
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
	
	public boolean bodyEncoded(String contentEncoding) {
		return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
	}
	
	public boolean isPlaintext(ByteBuffer buffer) {
		for (int i = 0; i < 16; i++) {
			if (buffer.hasRemaining() == false) {
				break;
			}
			int codePoint = buffer.get();
			if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
				return false;
			}
		}
		return true;
	}

}
