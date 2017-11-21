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
package io.delimeat.feed.entity;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FeedSearchTest {

	private FeedSearch search;

	@BeforeEach
	public void setUp() throws Exception {
		search = new FeedSearch();
	}

	@Test
	public void resultsTest() {
		Assertions.assertTrue(search.getResults().isEmpty());
		FeedResult result = new FeedResult();
		search.setResults(Arrays.asList(result));
		Assertions.assertFalse(search.getResults().isEmpty());
		Assertions.assertEquals(1, search.getResults().size());
		Assertions.assertEquals(result, search.getResults().get(0));
	}

	@Test
	public void hashCodeTest() {
		search.getResults().add(new FeedResult());
		Assertions.assertEquals(887503743, search.hashCode());
	}

	@Test
	public void toStringTest() {
		search.getResults().add(new FeedResult());
		Assertions.assertEquals("FeedSearch [results=[FeedResult [contentLength=0, seeders=0, leechers=0]]]",
				search.toString());
	}

	@Test
	public void equalsTest() {
		search.setResults(Arrays.asList(new FeedResult()));
		FeedSearch other = new FeedSearch();
		other.setResults(Arrays.asList(new FeedResult()));
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
	public void equalsResultsNullTest() {
		search.setResults(null);
		FeedSearch other = new FeedSearch();
		other.setResults(null);
		Assertions.assertTrue(search.equals(other));
	}

	@Test
	public void equalsNullOtherNotNullTest() {
		search.setResults(null);
		FeedSearch other = new FeedSearch();
		other.setResults(Arrays.asList(new FeedResult()));
		Assertions.assertFalse(search.equals(other));
	}

	@Test
	public void equalsNotNullOtherNullTest() {
		search.setResults(Arrays.asList(new FeedResult()));
		FeedSearch other = new FeedSearch();
		other.setResults(null);

		Assertions.assertFalse(search.equals(other));
	}

}
