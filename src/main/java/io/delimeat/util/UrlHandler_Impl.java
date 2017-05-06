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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipException;

@Component("urlHandlerId")
public class UrlHandler_Impl implements UrlHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(UrlHandler_Impl.class);
	public static final String DEFAULT_USER_AGENT = "delimeat.io " + System.getProperty("os.name");
	
	@Value("${io.delimeat.util.urlhandler.timeout}")
	private int timeout;

	/* (non-Javadoc)
	 * @see io.delimeat.common.util.UrlHandler#openInput(java.net.URL, java.util.Map)
	 */
	@Override
	public InputStream openInput(URL url, Map<String, String> headerProps) throws IOException {
		URLConnection conn = openUrlConnection(url, headerProps);
		return openInput(conn);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.util.UrlHandler#openInput(java.net.URL)
	 */
	@Override
	public InputStream openInput(URL url) throws IOException {
		return openInput(url, null);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.util.UrlHandler#openOutput(java.net.URL)
	 */
	@Override
	public OutputStream openOutput(URL url) throws IOException {
		OutputStream os;
		if ("file".equalsIgnoreCase(url.getProtocol())) {
			File file = new File(url.getFile());
			if (file.exists() == false) {
				if (file.getParent() != null) {
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
			os = new FileOutputStream(file);

		} else {
			URLConnection conn = openUrlConnection(url);
			os = conn.getOutputStream();
		}
		BufferedOutputStream bos = new BufferedOutputStream(os);
		return bos;
	}

	/**
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout
	 *            the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.util.UrlHandler#openUrlConnection(java.net.URL, java.util.Map)
	 */
	@Override
	public URLConnection openUrlConnection(URL url, Map<String, String> headerProps) throws IOException {
		URLConnection conn = url.openConnection();
		conn.setConnectTimeout(getTimeout());

		// create header props if not provided
		if (headerProps == null) {
			headerProps = new HashMap<String, String>();
		}

		// add user-agent header if not provided
		if (headerProps.containsKey("user-agent") == false) {
			conn.setRequestProperty("user-agent", DEFAULT_USER_AGENT);
		}

		// add header props to connection
		for (String key : headerProps.keySet()) {
			String value = headerProps.get(key);
			conn.setRequestProperty(key, value);
		}

		// if http follow redirects
		if (HttpURLConnection.class.isAssignableFrom(conn.getClass()) == true) {
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			// set follow redirects false because using own implementation
			httpConn.setInstanceFollowRedirects(false);
			switch (httpConn.getResponseCode()) {
			case HttpURLConnection.HTTP_MOVED_PERM:
			case HttpURLConnection.HTTP_MOVED_TEMP:
				String location = conn.getHeaderField("Location");
				URL redirectUrl = new URL(url, location);
				conn = openUrlConnection(redirectUrl, headerProps);
			}
		}
		return conn;
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.util.UrlHandler#openUrlConnection(java.net.URL)
	 */
	@Override
	public URLConnection openUrlConnection(URL url) throws IOException {
		return openUrlConnection(url, null);
	}

	/* (non-Javadoc)
	 * @see io.delimeat.common.util.UrlHandler#openInput(java.net.URLConnection)
	 */
	@Override
	public InputStream openInput(URLConnection connection) throws IOException {
		final String encoding = connection.getContentEncoding();
		if ("gzip".equalsIgnoreCase(encoding) == true) {
			try {
				return new GZIPInputStream(connection.getInputStream());
			} catch (ZipException ex) {
				// do nothing, the raw input stream will be returned
				LOGGER.warn(String.format("Input stream for %s said it was gzip'd but it wasnt", connection.getURL()),
						ex);
			}
		} else if ("deflate".equalsIgnoreCase(encoding) == true) {
			return new InflaterInputStream(connection.getInputStream());
		}
		return connection.getInputStream();
	}

}
