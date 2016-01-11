package io.delimeat.core.config;

import io.delimeat.util.jaxb.AbstractJaxbHelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.xml.bind.JAXBException;

public class JaxbConfigDao_Impl extends AbstractJaxbHelper implements ConfigDao {

	private URL url;
  
	@Override
	public Config read() throws ConfigNotFoundException, ConfigException {
        try {
            InputStream input = getUrlHandler().openInput(getUrl());
            Config config = unmarshal(input, Config.class);
            return config;
        } catch (FileNotFoundException ex) {
            throw new ConfigNotFoundException(ex);
        } catch (IOException ex) {
            throw new ConfigException(ex);
        } catch (JAXBException ex) {
            throw new ConfigException(ex);
        }
	}

	@Override
	public void createOrUpdate(Config config) throws ConfigNotFoundException, ConfigException {
        try {
            OutputStream output = getUrlHandler().openOutput(getUrl());
            marshal(output, config);
        } catch (IOException ex) {
            throw new ConfigException(ex);
        } catch (JAXBException ex) {
            throw new ConfigException(ex);
        }
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}
	
}
