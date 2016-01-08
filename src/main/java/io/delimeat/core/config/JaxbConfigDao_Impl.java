package io.delimeat.core.config;

import io.delimeat.util.jaxb.AbstractJaxbHelper;
import io.delimeat.util.DelimeatUtils;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.xml.bind.JAXBException;

public class JaxbConfigDao_Impl extends AbstractJaxbHelper implements ConfigDao {

	private String defaultOutputDir;
	private URL url;

	@Override
   public void create(Config config) throws ConfigException{
        try {
            File file = new File(getUrl().getFile());
	         file.createNewFile();
            if(DelimeatUtils.isEmpty(config.getOutputDirectory())){
              config.setOutputDirectory(getDefaultOutputDir());
            }
          	update(config);
        } catch (IOException ex) {
            throw new ConfigException(ex);
        }
   }
  
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
	public void update(Config config) throws ConfigNotFoundException, ConfigException {
        try {
            OutputStream output = getUrlHandler().openOutput(getUrl());
            marshal(output, config);
        } catch (FileNotFoundException ex) {
            throw new ConfigNotFoundException(ex);
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

	public String getDefaultOutputDir() {
		return defaultOutputDir;
	}

	public void setDefaultOutputDir(String defaultOutputDir) {
		this.defaultOutputDir = defaultOutputDir;
	}

}
