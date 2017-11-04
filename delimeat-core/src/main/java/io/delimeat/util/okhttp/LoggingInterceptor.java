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
package io.delimeat.util.okhttp;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

public class LoggingInterceptor implements Interceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingInterceptor.class);
	private static final Charset UTF8 = Charset.forName("UTF-8");
	
	/* (non-Javadoc)
	 * @see okhttp3.Interceptor#intercept(okhttp3.Interceptor.Chain)
	 */
	@Override
	public Response intercept(Chain chain) throws IOException {

		Request request = chain.request();

		RequestBody requestBody = request.body();
		boolean hasRequestBody = requestBody != null;

		StringBuilder reqStrBuilder = new StringBuilder();
		Connection connection = chain.connection();

		reqStrBuilder.append("--> " + request.method() + ' ' + request.url()
				+ (connection != null ? " " + connection.protocol() : ""));
		if (hasRequestBody) {
			reqStrBuilder.append(" (" + requestBody.contentLength() + "-byte body)");
		}
		reqStrBuilder.append("\n");

		if (hasRequestBody) {
			// Request body headers are only present when installed as a network
			// interceptor. Force
			// them to be included (when available) so there values are known.
			if (requestBody.contentType() != null) {
				reqStrBuilder.append("Content-Type: " + requestBody.contentType() + "\n");
			}
			if (requestBody.contentLength() != -1) {
				reqStrBuilder.append("Content-Length: " + requestBody.contentLength() + "\n");
			}
		}

		Headers reqHeaders = request.headers();
		for (int i = 0, count = reqHeaders.size(); i < count; i++) {
			String name = reqHeaders.name(i);
			// Skip headers from the request body as they are explicitly logged
			// above.
			if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
				reqStrBuilder.append(name + ": " + reqHeaders.value(i) + "\n");
			}
		}

		if (!hasRequestBody) {
			reqStrBuilder.append("--> END " + request.method() + "\n");
		} else if (bodyEncoded(request.headers())) {
			reqStrBuilder.append("--> END " + request.method() + " (encoded body omitted)" + "\n");
		} else {
			Buffer buffer = new Buffer();
			requestBody.writeTo(buffer);

			Charset charset = UTF8;
			MediaType contentType = requestBody.contentType();
			if (contentType != null) {
				charset = contentType.charset(UTF8);
			}

			reqStrBuilder.append("\n");
			if (isPlaintext(buffer)) {
				reqStrBuilder.append(buffer.readString(charset) + "\n");
				reqStrBuilder.append("--> END " + request.method() 
					+ " (" + requestBody.contentLength() 
					+ "-byte body)" + "\n");
			} else {
				reqStrBuilder.append("--> END " + request.method() 
					+ " (binary " + requestBody.contentLength() 
					+ "-byte body omitted)" + "\n");
			}
		}
		LOGGER.trace(reqStrBuilder.toString());

		long startNs = System.nanoTime();
		Response response;
		try {
			response = chain.proceed(request);
		} catch (Exception e) {
			throw e;
		}
		long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

		StringBuilder respStrBuilder = new StringBuilder();
		ResponseBody responseBody = response.body();
		long contentLength = responseBody.contentLength();
		respStrBuilder
				.append("<-- " + response.code() + (response.message().isEmpty() ? "" : ' ' + response.message()) + ' '
						+ response.request().url() + " (" + tookMs + "ms )\n");

		Headers respHeaders = response.headers();
		for (int i = 0, count = respHeaders.size(); i < count; i++) {
			respStrBuilder.append(respHeaders.name(i) + ": " + respHeaders.value(i) + "\n");
		}

		if (!HttpHeaders.hasBody(response)) {
			respStrBuilder.append("<-- END HTTP\n");
		} else if (bodyEncoded(response.headers())) {
			respStrBuilder.append("<-- END HTTP (encoded body omitted)\n");
		} else {
			BufferedSource source = responseBody.source();
			source.request(Long.MAX_VALUE); // Buffer the entire body.
			Buffer buffer = source.buffer();

			Charset charset = UTF8;
			MediaType contentType = responseBody.contentType();
			if (contentType != null) {
				charset = contentType.charset(UTF8);
			}

			
			if (!isPlaintext(buffer) || "application/x-bittorrent".equals(response.headers().get("Content-Type"))) {
				respStrBuilder.append("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)\n");
				return response;
			}
			
			if (contentLength > 0) {
				respStrBuilder.append("Body:\n"+buffer.clone().readString(charset)+"\n");
			}

			respStrBuilder.append("<-- END HTTP (" + buffer.size() + "-byte body)");
		}
		LOGGER.trace(respStrBuilder.toString());
	    return response;
	}

	/**
	 * Returns true if the body in question probably contains human readable
	 * text. Uses a small sample of code points to detect unicode control
	 * characters commonly used in binary file signatures.
	 */
	static boolean isPlaintext(Buffer buffer) {
		try {
			Buffer prefix = new Buffer();
			long byteCount = buffer.size() < 64 ? buffer.size() : 64;
			buffer.copyTo(prefix, 0, byteCount);
			for (int i = 0; i < 16; i++) {
				if (prefix.exhausted()) {
					break;
				}
				int codePoint = prefix.readUtf8CodePoint();
				if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
					return false;
				}
			}
			return true;
		} catch (EOFException e) {
			return false; // Truncated UTF-8 sequence.
		}
	}

	private boolean bodyEncoded(Headers headers) {
		String contentEncoding = headers.get("Content-Encoding");
		return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
	}

}
