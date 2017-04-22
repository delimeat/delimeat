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
