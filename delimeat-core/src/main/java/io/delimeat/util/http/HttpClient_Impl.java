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
package io.delimeat.util.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.delimeat.util.http.exception.HttpBodyException;
import io.delimeat.util.http.exception.HttpException;
import io.delimeat.util.http.exception.HttpResponseException;
import io.delimeat.util.http.exception.HttpTimeoutException;
import io.delimeat.util.okhttp.LoggingInterceptor;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpClient_Impl implements HttpClient {

	/* (non-Javadoc)
	 * @see io.delimeat.util.rest.RestClient#get(java.net.URL, java.util.Map, java.lang.Class)
	 */
	@Override
	public <T> T get(URL url, Map<String, String> headers, Class<T> responseType, BodyUnmarshaller<T> unmarshaller) throws HttpTimeoutException, HttpResponseException, HttpBodyException, HttpException{
		return execute(new Request.Builder()
				.url(url)
				.headers(Headers.of(headers))
				.build(),responseType, unmarshaller);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.util.rest.RestClient#post(java.net.URL, java.util.Map, java.lang.Class, java.lang.Object)
	 */
	@Override
	public <T, S> T post(URL url, Map<String,String> headers, Class<T> responseType, S requestObject, BodyMarshaller marshaller, BodyUnmarshaller<T> unmarshaller) throws HttpTimeoutException, HttpResponseException, HttpBodyException, HttpException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try{
			marshaller.marshall(output, requestObject);
		}catch(Exception ex){
			throw new HttpBodyException(requestObject.getClass(), ex);
		}
		
		if(headers.get("Content-Type") == null || headers.get("Content-Type") == null){
			headers.put("Content-Type", "application/json");
		}
		
		return execute(new Request.Builder()
				.url(url)
				.headers(Headers.of(headers))
				.post(RequestBody.create(MediaType.parse(headers.get("Content-Type")), output.toByteArray()))
				.build(),responseType, unmarshaller);
	}
	
	public OkHttpClient getClient(){
		return new OkHttpClient().newBuilder()
				.connectTimeout(2, TimeUnit.SECONDS)
				.readTimeout(2, TimeUnit.SECONDS)
				.writeTimeout(2, TimeUnit.SECONDS)
				.addInterceptor(new LoggingInterceptor())
				.build();
	}
	
	private <T> T execute(Request request, Class<T> responseType, BodyUnmarshaller<T> unmarshaller) throws HttpTimeoutException, HttpResponseException, HttpBodyException, HttpException {
		
		try(Response response = getClient().newCall(request).execute()){
			
			if (response.isSuccessful() == false) {
				throw new HttpResponseException(request.url().url(), response.code(), response.message() );
			}
			
			byte[] responseBytes = response.body().bytes();
			return unmarshaller.unmarshall(new ByteArrayInputStream(responseBytes), responseType);	
			
		}catch(SocketTimeoutException ex){
			throw new HttpTimeoutException(request.url().url());
		}catch(IOException ex){
			throw new HttpException(ex);
		}catch(Exception ex){
			throw new HttpBodyException(responseType, ex);
		}
	}

}
