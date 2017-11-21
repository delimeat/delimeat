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
package io.delimeat.config.entity;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConfigTest {

	private Config config;

	@BeforeEach
	public void setUp() {
		config = new Config();
	}

	@Test
	public void configIdTest() {
		Assertions.assertNull(config.getConfigId());
		config.setConfigId(1L);
		Assertions.assertEquals(1, config.getConfigId().longValue());

	}

	@Test
	public void searchIntervalTest() {
		Assertions.assertEquals(0, config.getSearchInterval());
		config.setSearchInterval(100);
		Assertions.assertEquals(100, config.getSearchInterval());
	}

	@Test
	public void searchDelayTest() {
		Assertions.assertEquals(0, config.getSearchDelay());
		config.setSearchDelay(100);
		Assertions.assertEquals(100, config.getSearchDelay());
	}

	@Test
	public void preferFilesTest() {
		Assertions.assertFalse(config.isPreferFiles());
		config.setPreferFiles(true);
		Assertions.assertTrue(config.isPreferFiles());
	}

	@Test
	public void ignoreFoldersTest() {
		Assertions.assertFalse(config.isIgnoreFolders());
		config.setIgnoreFolders(true);
		Assertions.assertTrue(config.isIgnoreFolders());
	}

	@Test
	public void ignoredFileTypesTest() {
		Assertions.assertEquals(0, config.getIgnoredFileTypes().size());
		config.setIgnoredFileTypes(Arrays.asList("AVI", "MKV"));
		Assertions.assertEquals(2, config.getIgnoredFileTypes().size());
		Assertions.assertEquals("AVI", config.getIgnoredFileTypes().get(0));
		Assertions.assertEquals("MKV", config.getIgnoredFileTypes().get(1));
	}

	@Test
	public void excludedKeywordsTest() {
		Assertions.assertNotNull(config.getExcludedKeywords());
		Assertions.assertEquals(0, config.getExcludedKeywords().size());
		config.setExcludedKeywords(Arrays.asList("265", "XVR"));
		Assertions.assertEquals(2, config.getExcludedKeywords().size());
		Assertions.assertEquals("265", config.getExcludedKeywords().get(0));
		Assertions.assertEquals("XVR", config.getExcludedKeywords().get(1));

	}

	@Test
	public void outputDirectoryTest() {
		Assertions.assertNull(config.getOutputDirectory());
		config.setOutputDirectory("OUTPUTDIR");
		Assertions.assertEquals("OUTPUTDIR", config.getOutputDirectory());
	}

	@Test
	public void hashCodeTest() {
		config.setConfigId(1L);
		config.setVersion(99L);
		Assertions.assertEquals(1091, config.hashCode());
	}

	@Test
	public void toStringTest() {
		Assertions.assertEquals(
				"Config [searchInterval=0, searchDelay=0, preferFiles=false, ignoreFolders=false, ignoredFileTypes=[], excludedKeywords=[], ]",
				config.toString());
	}

	@Test
	public void equalsNullTest() {
		Assertions.assertFalse(config.equals(null));
	}

	@Test
	public void equalsThisTest() {
		Assertions.assertTrue(config.equals(config));
	}

	@Test
	public void equalsOtherObjectTest() {
		Assertions.assertFalse(config.equals(new Object()));
	}

	@Test
	public void equalsTest() {
		config.setConfigId(1L);
		config.setVersion(99L);
		Config other = new Config();
		other.setConfigId(1L);
		other.setVersion(99L);
		Assertions.assertTrue(config.equals(other));
	}

	@Test
	public void equalsConfigIdNullTest() {
		config.setConfigId(null);
		config.setVersion(99L);
		Config other = new Config();
		other.setConfigId(1L);
		other.setVersion(99L);
		Assertions.assertFalse(config.equals(other));
	}

	@Test
	public void equalsVersionNullTest() {
		config.setConfigId(1L);
		config.setVersion(null);
		Config other = new Config();
		other.setConfigId(1L);
		other.setVersion(99L);
		Assertions.assertFalse(config.equals(other));
	}
	
	@Test
	public void equalsConfigIdTest() {
		config.setConfigId(2L);
		config.setVersion(99L);
		Config other = new Config();
		other.setConfigId(1L);
		other.setVersion(99L);
		Assertions.assertFalse(config.equals(other));
	}

	@Test
	public void equalsVersionTest() {
		config.setConfigId(1L);
		config.setVersion(100L);
		Config other = new Config();
		other.setConfigId(1L);
		other.setVersion(99L);
		Assertions.assertFalse(config.equals(other));
	}
}
