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
package io.delimeat.util.http.exception;

import java.net.URL;

public class HttpResponseException extends HttpException{

	private static final long serialVersionUID = 1L;

	private final String message;
	private final int code;
	private final URL url;
	
	
	public HttpResponseException(URL url, int code, String message){
		this.message = message;
		this.code = code;
		this.url = url;
	}
	
	@Override
	public String getMessage(){
		return String.format("HTTP response code %s with message \"%s\" for url %s",code, message, url);
	}
	
	public int getCode(){
		return code;
	}
	
	public URL getUrl(){
		return url;
	}
}
