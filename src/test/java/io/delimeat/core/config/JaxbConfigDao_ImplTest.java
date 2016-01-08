package io.delimeat.core.config;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.mockito.Mockito;

import io.delimeat.util.UrlHandler;

public class JaxbConfigDao_ImplTest {

	private class XMLGenerator {

		private StringBuffer xml;

		public XMLGenerator(int checkInterval, boolean preferFiles, boolean ignoreFolders,
				List<String> ignoredFileTypes, String outputDir) {
			xml = new StringBuffer();
			xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			xml.append("<config>");
			xml.append("<outputDirectory>" + outputDir + "</outputDirectory>");
			xml.append("<preferFiles>" + preferFiles + "</preferFiles>");
			xml.append("<ignoreFolders>" + ignoreFolders + "</ignoreFolders>");
			xml.append("<ignoredFileTypes>");
			for (String fileType : ignoredFileTypes) {
				xml.append("<fileType>" + fileType + "</fileType>");
			}
			xml.append("</ignoredFileTypes>");
			xml.append("<searchInterval>" + checkInterval + "</searchInterval>");

		}

		public String toString() {
			return xml.toString() + "</config>";
		}

		public InputStream generate() throws Exception {
			return new ByteArrayInputStream(this.toString().getBytes("UTF-8"));
		}
	}
	
	private JaxbConfigDao_Impl dao;

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
		List<String> ignoredFiles = new ArrayList<String>();
		ignoredFiles.add("AVI");
		ignoredFiles.add("MOV");

		XMLGenerator generator = new XMLGenerator(100, true, false, ignoredFiles, "outputDir");
		UrlHandler mockedHandler = Mockito.mock(UrlHandler.class);
		Mockito.when(mockedHandler.openInput(Mockito.any(URL.class))).thenReturn(generator.generate());
		dao.setUrlHandler(mockedHandler);

		JAXBContext jc = JAXBContext.newInstance(Config.class);
		dao.setUnmarshaller(jc.createUnmarshaller());

		Config config = dao.read();
		Assert.assertNotNull(config);

		Assert.assertEquals("outputDir", config.getOutputDirectory());
		Assert.assertEquals(100, config.getSearchInterval());
		Assert.assertEquals(true, config.isPreferFiles());
		Assert.assertEquals(false, config.isIgnoreFolders());
		Assert.assertEquals(2, config.getIgnoredFileTypes().size());
		Assert.assertEquals("AVI", config.getIgnoredFileTypes().get(0));
		Assert.assertEquals("MOV", config.getIgnoredFileTypes().get(1));
	}

	@Test
	public void updateTest() throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		UrlHandler mockedHandler = Mockito.mock(UrlHandler.class);
		Mockito.when(mockedHandler.openOutput(Mockito.any(URL.class))).thenReturn(bos);
		dao.setUrlHandler(mockedHandler);

		JAXBContext jc = JAXBContext.newInstance(Config.class);
		dao.setMarshaller(jc.createMarshaller());

		List<String> ignoredFiles = new ArrayList<String>();
		ignoredFiles.add("AVI");
		ignoredFiles.add("MOV");

		Config config = new Config();
		config.setOutputDirectory("outputDir");
		config.setSearchInterval(100);
		config.setPreferFiles(true);
		config.setIgnoreFolders(false);
		config.getIgnoredFileTypes().add("AVI");
		config.getIgnoredFileTypes().add("MOV");

		dao.update(config);

		XMLGenerator generator = new XMLGenerator(100, true, false, ignoredFiles, "outputDir");

		Assert.assertEquals(generator.toString(), bos.toString());
	}
  
   @Ignore("need to figure out how to mock files")
   @Test
  	public void createTest() throws Exception{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		UrlHandler mockedHandler = Mockito.mock(UrlHandler.class);
		Mockito.when(mockedHandler.openOutput(Mockito.any(URL.class))).thenReturn(bos);
		dao.setUrlHandler(mockedHandler);
     
		//URL mockedUrl = Mockito.mock(URL.class);
		//Mockito.when(mockedUrl.getFile()).thenReturn("FILE");

		JAXBContext jc = JAXBContext.newInstance(Config.class);
		dao.setMarshaller(jc.createMarshaller());

		List<String> ignoredFiles = new ArrayList<String>();
		ignoredFiles.add("AVI");
		ignoredFiles.add("MOV");

		Config config = new Config();
		config.setOutputDirectory("outputDir");
		config.setSearchInterval(100);
		config.setPreferFiles(true);
		config.setIgnoreFolders(false);
		config.getIgnoredFileTypes().add("AVI");
		config.getIgnoredFileTypes().add("MOV");

		dao.create(config);

		XMLGenerator generator = new XMLGenerator(100, true, false, ignoredFiles, "outputDir");

		Assert.assertEquals(generator.toString(), bos.toString());    
   }

}
