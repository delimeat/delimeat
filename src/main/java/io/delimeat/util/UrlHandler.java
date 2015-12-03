package io.delimeat.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public interface UrlHandler {

	public InputStream openInput(URL url) throws IOException;

	public InputStream openInput(URL url, Map<String, String> headerProps) throws IOException;

	public OutputStream openOutput(URL url) throws IOException;

	public URLConnection openUrlConnection(URL url) throws IOException;

	public URLConnection openUrlConnection(URL url, Map<String, String> headerProps) throws IOException;

}
