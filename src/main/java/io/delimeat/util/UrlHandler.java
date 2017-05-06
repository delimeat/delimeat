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
package io.delimeat.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public interface UrlHandler {

	/**
	 * Open an input stream from a url
	 * 
	 * @param url
	 * @return input stream
	 * @throws IOException
	 */
	public InputStream openInput(URL url) throws IOException;

	/**
	 * Open an input stream from a url
	 * 
	 * @param url
	 * @param headerProps
	 * @return input stream
	 * @throws IOException
	 */
	public InputStream openInput(URL url, Map<String, String> headerProps) throws IOException;
  
  	/**
  	 * Open an input stream from a URL Connection
  	 * @param connection
  	 * @return input stream
  	 * @throws IOException
  	 */
  	public InputStream openInput(URLConnection connection) throws IOException;

	/**
	 * Open an output stream from a url
	 * 
	 * @param url
	 * @return output stream
	 * @throws IOException
	 */
	public OutputStream openOutput(URL url) throws IOException;

	/**
	 * Open a URL Connection from a url
	 * 
	 * @param url
	 * @return url connection
	 * @throws IOException
	 */
	public URLConnection openUrlConnection(URL url) throws IOException;

	/**
	 * Open a URL connection from a url
	 * 
	 * @param url
	 * @param headerProps
	 * @return url connection
	 * @throws IOException
	 */
	public URLConnection openUrlConnection(URL url, Map<String, String> headerProps) throws IOException;

}
