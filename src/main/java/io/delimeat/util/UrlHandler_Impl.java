package io.delimeat.util;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

public class UrlHandler_Impl implements UrlHandler {

	private static final String DEFAULT_USER_AGENT = "delimeat.io " + System.getProperty("os.name");
	private int timeout;

	@Override
	public InputStream openInput(URL url, Map<String, String> headerProps) throws IOException {
		URLConnection conn = openUrlConnection(url, headerProps);
		return openInput(conn);
	}

	@Override
	public InputStream openInput(URL url) throws IOException {
		return openInput(url, null);
	}

	@Override
	public OutputStream openOutput(URL url) throws IOException {
		OutputStream os;
		if ("file".equalsIgnoreCase(url.getProtocol())) {
			os = new FileOutputStream(url.getFile());

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
  
	@Override  
  	public InputStream openInput(URLConnection connection) throws IOException{
     final String contentType = connection.getContentType();
     final InputStream input;
     if("gzip".equalsIgnoreCase(contentType) == true){
       input = new GZIPInputStream(connection.getInputStream());
     }else if("deflate".equalsIgnoreCase(contentType) == true){
       input = new InflaterInputStream(connection.getInputStream());
     }else{
       input = connection.getInputStream();
     }
     return input;
   }

}
