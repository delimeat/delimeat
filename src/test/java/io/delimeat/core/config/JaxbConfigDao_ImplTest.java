package io.delimeat.core.config;

import io.delimeat.util.UrlHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class JaxbConfigDao_ImplTest {

	private class EntityGenerator {

		private StringBuffer entity;

		public EntityGenerator(int searchInterval, boolean preferFiles, boolean ignoreFolders,
				List<String> ignoredFileTypes, String outputDir, int searchDelay, List<String> excludedKeywords) {
			entity = new StringBuffer();
			entity.append("{");
			entity.append("\"excludedKeywords\":[");
			boolean firstKeyword = true;
			for (String keyword : excludedKeywords) {
				if (!firstKeyword) {
					entity.append(",");
				}
				entity.append("\"" + keyword + "\"");
				firstKeyword = false;
			}
			entity.append("],");
        	// ignored folders stuff
			entity.append("\"ignoreFolders\":" + ignoreFolders + ",");
			entity.append("\"ignoredFileTypes\":[");
			boolean firstIgnored = true;
			for (String fileType : ignoredFileTypes) {
				if (!firstIgnored) {
					entity.append(",");
				}
				entity.append("\"" + fileType + "\"");
				firstIgnored = false;
			}
			entity.append("],");
			entity.append("\"outputDirectory\":\"" + outputDir + "\",");
			entity.append("\"preferFiles\":" + preferFiles + ",");
        	entity.append("\"searchDelay\":" + searchDelay + ",");
			entity.append("\"searchInterval\":" + searchInterval + "");
		}

		public String toString() {
        return entity.toString() + "}";
		}

		public InputStream generate() throws Exception {
			return new ByteArrayInputStream(this.toString().getBytes("UTF-8"));
		}
	}
  
	private static final String METADATA = "META-INF/oxm/config-oxm.xml";

	private JaxbConfigDao_Impl dao;

  	public JAXBContext getJAXBContext() throws Exception{
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, METADATA);
     	properties.put(JAXBContextProperties.MEDIA_TYPE,"application/json");
     	properties.put(MarshallerProperties.JSON_INCLUDE_ROOT,false);
     	properties.put(Marshaller.JAXB_FORMATTED_OUTPUT, true);
     	return JAXBContext.newInstance(new Class[] { Config.class}, properties);
   }
  
	@Before
	public void setUp() {
		dao = new JaxbConfigDao_Impl();
	}

	@Test
	public void unmarshallerTest() {
		Assert.assertNull(dao.getUnmarshaller());
		Unmarshaller mockedUnmarshaller = Mockito.mock(Unmarshaller.class);
		dao.setUnmarshaller(mockedUnmarshaller);
		Assert.assertEquals(mockedUnmarshaller, dao.getUnmarshaller());
	}

	@Test
	public void marshallerTest() {
		Assert.assertNull(dao.getMarshaller());
		Marshaller mockedMarshaller = Mockito.mock(Marshaller.class);
		dao.setMarshaller(mockedMarshaller);
		Assert.assertEquals(mockedMarshaller, dao.getMarshaller());
	}
	
	@Test
	public void defaultOutputDirectoryTest() {
		Assert.assertNull(dao.getDefaultOutputDir());
		dao.setDefaultOutputDir("DEFAULT_OUTPUT_DIR");
		Assert.assertEquals("DEFAULT_OUTPUT_DIR", dao.getDefaultOutputDir());
	}

	@Test
	public void urlTest() throws MalformedURLException {
		Assert.assertNull(dao.getUrl());
		URL url = new URL("https://www.test.com");
		dao.setUrl(url);
		Assert.assertEquals(url, dao.getUrl());
	}

	@Test
	public void urlHandlerTest() {
		Assert.assertNull(dao.getUrlHandler());
		UrlHandler mockedUrlHandler = Mockito.mock(UrlHandler.class);
		dao.setUrlHandler(mockedUrlHandler);
		Assert.assertEquals(mockedUrlHandler, dao.getUrlHandler());
	}

	@Test
	public void readTest() throws Exception {
		EntityGenerator generator = new EntityGenerator(100, true, false, Arrays.asList("AVI","MOV"), "outputDir",200,Arrays.asList("256","XRV"));
		UrlHandler mockedHandler = Mockito.mock(UrlHandler.class);
		Mockito.when(mockedHandler.openInput(Mockito.any(URL.class))).thenReturn(generator.generate());
		dao.setUrlHandler(mockedHandler);

     	JAXBContext jc = getJAXBContext();
		dao.setUnmarshaller(jc.createUnmarshaller());

		dao.setUrl(new URL("http://test.com"));
		
		Config config = dao.read();
		Assert.assertNotNull(config);

		Assert.assertEquals("outputDir", config.getOutputDirectory());
		Assert.assertEquals(100, config.getSearchInterval());
		Assert.assertEquals(200, config.getSearchDelay());
		Assert.assertEquals(true, config.isPreferFiles());
		Assert.assertEquals(false, config.isIgnoreFolders());
		Assert.assertEquals(2, config.getIgnoredFileTypes().size());
		Assert.assertEquals("AVI", config.getIgnoredFileTypes().get(0));
		Assert.assertEquals("MOV", config.getIgnoredFileTypes().get(1));
     	Assert.assertEquals(2, config.getExcludedKeywords().size());
     	Assert.assertEquals("256", config.getExcludedKeywords().get(0));
     	Assert.assertEquals("XRV", config.getExcludedKeywords().get(1));
     
     	Mockito.verify(mockedHandler).openInput(Mockito.any(URL.class));
     	Mockito.verifyNoMoreInteractions(mockedHandler);
	}

	@Test
	public void readFileNotFoundExceptionTest() throws Exception {
		UrlHandler mockedHandler = Mockito.mock(UrlHandler.class);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Mockito.when(mockedHandler.openOutput(Mockito.any(URL.class))).thenReturn(bos);
		Mockito.when(mockedHandler.openInput(Mockito.any(URL.class))).thenThrow(FileNotFoundException.class);
		dao.setUrlHandler(mockedHandler);

     	dao.setDefaultOutputDir("defaultOutputDir");
     
		JAXBContext jc = getJAXBContext();
		dao.setUnmarshaller(jc.createUnmarshaller());
		dao.setMarshaller(jc.createMarshaller());
     
		dao.setUrl(new URL("http://test.com"));
		
		Config config = dao.read();
     
		Assert.assertNotNull(config);
		Assert.assertEquals("defaultOutputDir", config.getOutputDirectory());
		Assert.assertEquals(14400000, config.getSearchInterval());
		Assert.assertEquals(3600000, config.getSearchDelay());
		Assert.assertEquals(true, config.isPreferFiles());
		Assert.assertEquals(false, config.isIgnoreFolders());
		Assert.assertEquals(0, config.getIgnoredFileTypes().size());
     	Assert.assertEquals(0, config.getExcludedKeywords().size());
     
		EntityGenerator generator = new EntityGenerator(14400000, true, false, Collections.<String>emptyList(), "defaultOutputDir",3600000,Collections.<String>emptyList());
		Assert.assertEquals(generator.toString(), bos.toString());
     
     	Mockito.verify(mockedHandler).openInput(Mockito.any(URL.class));
     	Mockito.verify(mockedHandler).openOutput(Mockito.any(URL.class));
     	Mockito.verifyNoMoreInteractions(mockedHandler);
	}
  
	@Test(expected=ConfigException.class)
	public void readIOExceptionTest() throws Exception {
		UrlHandler mockedHandler = Mockito.mock(UrlHandler.class);
		Mockito.when(mockedHandler.openInput(Mockito.any(URL.class))).thenThrow(IOException.class);
		dao.setUrlHandler(mockedHandler);

		dao.setUrl(new URL("http://test.com"));
		
     	dao.read();
  	}
  
  
	@Test(expected=ConfigException.class)
	public void readJAXBExceptionTest() throws Exception {
     	InputStream inputStream = Mockito.mock(InputStream.class);
		UrlHandler mockedHandler = Mockito.mock(UrlHandler.class);
		Mockito.when(mockedHandler.openInput(Mockito.any(URL.class))).thenReturn(inputStream);	
     	dao.setUrlHandler(mockedHandler);

     	Unmarshaller unmarshaller = Mockito.mock(Unmarshaller.class);
     	Mockito.when(unmarshaller.unmarshal(Mockito.any(StreamSource.class),Mockito.eq(Config.class))).thenThrow(JAXBException.class);
     	dao.setUnmarshaller(unmarshaller);
     
     	dao.read();
	}
  
	@Test(expected=ConfigException.class)
	public void readFinallyCloseTest() throws Exception {
     	InputStream inputStream = Mockito.mock(InputStream.class);
     	Mockito.doThrow(IOException.class).when(inputStream).close();
		UrlHandler mockedHandler = Mockito.mock(UrlHandler.class);
		Mockito.when(mockedHandler.openInput(Mockito.any(URL.class))).thenReturn(inputStream);	
     	dao.setUrlHandler(mockedHandler);

     	Unmarshaller unmarshaller = Mockito.mock(Unmarshaller.class);
     	JAXBElement<Config> configElement = new JAXBElement<Config>(new QName(""),Config.class,new Config());
     	Mockito.when(unmarshaller.unmarshal(Mockito.any(StreamSource.class),Mockito.eq(Config.class))).thenReturn(configElement);
     	dao.setUnmarshaller(unmarshaller);
     
     	dao.setUrl(new URL("http://test.com"));
     	
     	dao.read();
	}
  
	@Test
	public void createOrUpdateTest() throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		UrlHandler mockedHandler = Mockito.mock(UrlHandler.class);
		Mockito.when(mockedHandler.openOutput(Mockito.any(URL.class))).thenReturn(bos);
		dao.setUrlHandler(mockedHandler);

		JAXBContext jc = getJAXBContext();
		dao.setMarshaller(jc.createMarshaller());

		Config config = new Config();
		config.setOutputDirectory("outputDir");
		config.setSearchInterval(100);
		config.setSearchDelay(200);
		config.setPreferFiles(true);
		config.setIgnoreFolders(false);
		config.getIgnoredFileTypes().add("AVI");
		config.getIgnoredFileTypes().add("MOV");
     	config.getExcludedKeywords().add("256");
     	config.getExcludedKeywords().add("XRV");

     	dao.setUrl(new URL("http://test.com"));
     	
		dao.createOrUpdate(config);

		EntityGenerator generator = new EntityGenerator(100, true, false, Arrays.asList("AVI","MOV"), "outputDir",200, Arrays.asList("256","XRV"));
		Assert.assertEquals(generator.toString(), bos.toString());
     
     	Mockito.verify(mockedHandler).openOutput(Mockito.any(URL.class));
     	Mockito.verifyNoMoreInteractions(mockedHandler);

	}
  
	@Test(expected=ConfigException.class)
	public void createOrUpdateIOExceptionTest() throws Exception {
		UrlHandler mockedHandler = Mockito.mock(UrlHandler.class);
		Mockito.when(mockedHandler.openOutput(Mockito.any(URL.class))).thenThrow(new IOException());
		dao.setUrlHandler(mockedHandler);

		dao.setUrl(new URL("http://test.com"));
		
     	dao.createOrUpdate(new Config());
  	}
  
  
	@Test(expected=ConfigException.class)
	public void createOrUpdateJAXBExceptionTest() throws Exception {
     	OutputStream outputStream = Mockito.mock(OutputStream.class);
		UrlHandler mockedHandler = Mockito.mock(UrlHandler.class);
		Mockito.when(mockedHandler.openOutput(Mockito.any(URL.class))).thenReturn(outputStream);
     	dao.setUrlHandler(mockedHandler);

     	Marshaller marshaller = Mockito.mock(Marshaller.class);
     	Mockito.doThrow(JAXBException.class).when(marshaller).marshal(Mockito.any(),Mockito.any(StreamResult.class));
     	dao.setMarshaller(marshaller);
     
     	dao.createOrUpdate(new Config());
	}

	@Test(expected=ConfigException.class)
	public void createOrUpdateFinallyCloseTest() throws Exception {
     	OutputStream outputStream = Mockito.mock(OutputStream.class);
     	Mockito.doThrow(IOException.class).when(outputStream).close();

		UrlHandler mockedHandler = Mockito.mock(UrlHandler.class);
		Mockito.when(mockedHandler.openOutput(Mockito.any(URL.class))).thenReturn(outputStream);
     	dao.setUrlHandler(mockedHandler);

     	Marshaller marshaller = Mockito.mock(Marshaller.class);
     	dao.setMarshaller(marshaller);
     	
     	dao.setUrl(new URL("http://test.com"));
     
     	dao.createOrUpdate(new Config());
	}
}
