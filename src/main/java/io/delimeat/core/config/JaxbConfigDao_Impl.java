package io.delimeat.core.config;

import io.delimeat.util.jaxb.AbstractJaxbHelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class JaxbConfigDao_Impl extends AbstractJaxbHelper implements ConfigDao {

	private String defaultOutputDir;
	private URL url;

	@Override
	public Config read() throws Exception, IOException {
		try {
			InputStream input = getUrlHandler().openInput(getUrl());
			Config config = unmarshal(input, Config.class);
			return config;
		} catch (FileNotFoundException e) {
			Config config = new Config();
			config.setOutputDirectory(getDefaultOutputDir());
			update(config);
			return config;
		}
	}

	@Override
	public void update(Config config) throws Exception, IOException {
		OutputStream output = getUrlHandler().openOutput(getUrl());
		marshal(output, config);
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getDefaultOutputDir() {
		return defaultOutputDir;
	}

	public void setDefaultOutputDir(String defaultOutputDir) {
		this.defaultOutputDir = defaultOutputDir;
	}

}
