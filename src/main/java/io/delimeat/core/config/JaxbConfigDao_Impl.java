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
	private String defaultOutputDir;

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
  
	@Override
	public Config read() throws ConfigException {
        try {
          
          	InputStream input;
        		try{
              input = getUrlHandler().openInput(getUrl());
            }catch(FileNotFoundException ex){
              Config config = new Config();
              config.setOutputDirectory(defaultOutputDir);
              createOrUpdate(config);
              return config;
            }
            return unmarshal(input, Config.class);

        } catch (IOException ex) {
            throw new ConfigException(ex);
        } catch (JAXBException ex) {
            throw new ConfigException(ex);
        }
	}

	@Override
	public void createOrUpdate(Config config) throws ConfigException {
        try {
          
            OutputStream output = getUrlHandler().openOutput(getUrl());
            marshal(output, config);
          
        } catch (IOException ex) {
            throw new ConfigException(ex);
        } catch (JAXBException ex) {
            throw new ConfigException(ex);
        }
	}
	
}
