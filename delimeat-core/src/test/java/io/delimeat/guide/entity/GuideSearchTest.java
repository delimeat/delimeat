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
package io.delimeat.guide.entity;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.delimeat.guide.entity.GuideSearch;
import io.delimeat.guide.entity.GuideSearchResult;

public class GuideSearchTest {

	private GuideSearch search;

	@BeforeEach
	public void setUp() throws Exception {
		search = new GuideSearch();
	}

	@Test
	public void resultsTest() {
		Assertions.assertNotNull(search.getResults());
		Assertions.assertEquals(0, search.getResults().size());
		GuideSearchResult result = new GuideSearchResult();
		search.setResults(Arrays.asList(result));
		Assertions.assertEquals(1, search.getResults().size());
		Assertions.assertEquals(result, search.getResults().get(0));
	}

	@Test
	public void hashCodeTest() {
		search.getResults().add(new GuideSearchResult());
		Assertions.assertEquals(923583, search.hashCode());
	}

	@Test
	public void toStringTest() {
		search.getResults().add(new GuideSearchResult());
		Assertions.assertEquals("GuideSearch [results=[GuideSearchResult []]]", search.toString());
	}

	@Test
	public void equalsTest() {
		search.setResults(Arrays.asList(new GuideSearchResult()));
		GuideSearch other = new GuideSearch();
		other.setResults(Arrays.asList(new GuideSearchResult()));
		Assertions.assertTrue(search.equals(other));
	}

	@Test
	public void equalsSelfTest() {
		Assertions.assertTrue(search.equals(search));
	}

	@Test
	public void equalsNullTest() {
		Assertions.assertFalse(search.equals(null));
	}

	@Test
	public void equalsOtherClassTest() {
		Assertions.assertFalse(search.equals(new Object()));
	}

	@Test
	public void equalsNullOtherNullTest() {
		GuideSearch other = new GuideSearch();
		Assertions.assertTrue(search.equals(other));
	}

	@Test
	public void equalsResultsNullTest() {
		search.setResults(null);

		GuideSearch other = new GuideSearch();
		other.setResults(Arrays.asList(new GuideSearchResult()));
		Assertions.assertFalse(search.equals(other));
	}

	@Test
	public void equalsNotNullOtherNullTest() {
		search.setResults(Arrays.asList(new GuideSearchResult()));

		GuideSearch other = new GuideSearch();
		other.setResults(null);
		Assertions.assertFalse(search.equals(other));
	}
}
