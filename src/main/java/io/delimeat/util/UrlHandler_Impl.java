package io.delimeat.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class UrlHandler_Impl implements UrlHandler {

	private static final String DEFAULT_USER_AGENT = "delimeat.io " + System.getProperty("os.name");
	private int timeout;

	@Override
	public InputStream openInput(URL url, Map<String, String> headerProps) throws IOException {
		URLConnection conn = openUrlConnection(url, headerProps);
		InputStream input = conn.getInputStream();
		if ("gzip".equals(conn.getContentEncoding())) {
			input = new GZIPInputStream(input);
		}
		return input;
	}

	@Override
	public InputStream openInput(URL url) throws IOException {
		return openInput(url, null);
	}

	@Override
	public OutputStream openOutput(URL url) throws IOException {
		OutputStream os;
		if (url.getProtocol().equalsIgnoreCase("file")) {
			File file = new File(url.getFile());
			os = new FileOutputStream(file);

		} else {
			URLConnection conn = openUrlConnection(url);
			conn.setConnectTimeout(getTimeout());
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

	@Override
	public URLConnection openUrlConnection(URL url, Map<String, String> headerProps) throws IOException {
		URLConnection conn = url.openConnection();
		conn.setConnectTimeout(getTimeout());
		if (headerProps != null) {
			for (String key : headerProps.keySet()) {
				String value = headerProps.get(key);
				conn.setRequestProperty(key, value);
			}
		}
		if (headerProps == null || !headerProps.containsKey("user-agent")) {
			conn.setRequestProperty("user-agent", DEFAULT_USER_AGENT);
		}
		return conn;
	}

	@Override
	public URLConnection openUrlConnection(URL url) throws IOException {
		return openUrlConnection(url, null);
	}

}
