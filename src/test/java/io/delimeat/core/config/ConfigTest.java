package io.delimeat.core.config;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class ConfigTest {
	
	private Config config;

	@Before
	public void setUp() {
		config = new Config();
	}

	@Test
	public void setSearchInterval() {
		Assert.assertEquals(60 * 60 * 1000, config.getSearchInterval());
		config.setSearchInterval(100);
		Assert.assertEquals(100, config.getSearchInterval());
	}

	@Test
	public void setPreferFiles() {
		Assert.assertTrue(config.isPreferFiles());
		config.setPreferFiles(false);
		Assert.assertFalse(config.isPreferFiles());
	}

	@Test
	public void setIgnoreFolders() {
		Assert.assertFalse(config.isIgnoreFolders());
		Assert.assertTrue(config.isPreferFiles());
		config.setPreferFiles(false);
		Assert.assertFalse(config.isPreferFiles());
		config.setIgnoreFolders(true);
		Assert.assertTrue(config.isIgnoreFolders());
		Assert.assertTrue(config.isPreferFiles());
	}

	@Test
	public void setIgnoredFileTypes() {
		Assert.assertEquals(0, config.getIgnoredFileTypes().size());
     	config.setIgnoredFileTypes(Arrays.asList("AVI","MKV"));
		Assert.assertEquals(2, config.getIgnoredFileTypes().size());
		Assert.assertEquals("AVI", config.getIgnoredFileTypes().get(0));
		Assert.assertEquals("MKV", config.getIgnoredFileTypes().get(1));
	}

	@Test
	public void setOutputDirectory() {
		Assert.assertNull(config.getOutputDirectory());
		config.setOutputDirectory("OUTPUTDIR");
		Assert.assertEquals("OUTPUTDIR", config.getOutputDirectory());
	}
  
  	@Test
  	public void hashCodeTest(){
     	config.setSearchInterval(100);
		config.setPreferFiles(false);
		config.setIgnoreFolders(true);
		config.getIgnoredFileTypes().add("MKV");
		config.setOutputDirectory("OUTPUTDIR");
		Assert.assertEquals(993502270, config.hashCode());
   }
  
  	@Test
  	public void toStringTest(){
  		Assert.assertEquals("Config{outputDirectory=null, searchInterval=3600000, preferFiles=true, ignoreFolders=false, ignoredFileTypes=[]}",config.toString());
  	}
  
  	@Test
  	public void equalsNullTest(){
     	Assert.assertFalse(config.equals(null));
   }
  
  	@Test
  	public void equalsThisTest(){
     	Assert.assertTrue(config.equals(config));
   }
  
  	@Test
  	public void equalsOtherObjectTest(){
     	Assert.assertFalse(config.equals(new Object()));
   }
  
  	@Test
  	public void equalsTest(){
     	Config other = new Config();
     	Assert.assertTrue(config.equals(other));
   }
}
