/*
 * Copyright 2013-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.delimeat.config.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.config.domain.Config;

import java.util.Arrays;

public class ConfigTest {
	
	private Config config;

	@Before
	public void setUp() {
		config = new Config();
	}

	@Test
	public void searchIntervalTest() {
		Assert.assertEquals(0, config.getSearchInterval());
		config.setSearchInterval(100);
		Assert.assertEquals(100, config.getSearchInterval());
	}
	
	@Test
	public void searchDelayTest() {
		Assert.assertEquals(0, config.getSearchDelay());
		config.setSearchDelay(100);
		Assert.assertEquals(100, config.getSearchDelay());
	}

	@Test
	public void preferFilesTest() {
		Assert.assertFalse(config.isPreferFiles());
		config.setPreferFiles(true);
		Assert.assertTrue(config.isPreferFiles());
	}

	@Test
	public void ignoreFoldersTest() {
		Assert.assertFalse(config.isIgnoreFolders());
		config.setIgnoreFolders(true);
		Assert.assertTrue(config.isIgnoreFolders());
	}

	@Test
	public void ignoredFileTypesTest() {
		Assert.assertEquals(0, config.getIgnoredFileTypes().size());
     	config.setIgnoredFileTypes(Arrays.asList("AVI","MKV"));
		Assert.assertEquals(2, config.getIgnoredFileTypes().size());
		Assert.assertEquals("AVI", config.getIgnoredFileTypes().get(0));
		Assert.assertEquals("MKV", config.getIgnoredFileTypes().get(1));
	}
  
  	@Test
  	public void excludedKeywordsTest(){
     	Assert.assertNotNull(config.getExcludedKeywords());
     	Assert.assertEquals(0, config.getExcludedKeywords().size());
     	config.setExcludedKeywords(Arrays.asList("265","XVR"));
     	Assert.assertEquals(2, config.getExcludedKeywords().size());
     	Assert.assertEquals("265", config.getExcludedKeywords().get(0));
     	Assert.assertEquals("XVR", config.getExcludedKeywords().get(1));
     
   }

	@Test
	public void outputDirectoryTest() {
		Assert.assertNull(config.getOutputDirectory());
		config.setOutputDirectory("OUTPUTDIR");
		Assert.assertEquals("OUTPUTDIR", config.getOutputDirectory());
	}
  
  	@Test
  	public void hashCodeTest(){
  		config.setConfigId(1L);
  		config.setVersion(99L);
		Assert.assertEquals(1091, config.hashCode());
   }
  
  	@Test
  	public void toStringTest(){
  		Assert.assertEquals("Config{searchInterval=0, searchDelay=0, preferFiles=false, ignoreFolders=false, ignoredFileTypes=[], excludedKeywords=[]}",config.toString());
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
