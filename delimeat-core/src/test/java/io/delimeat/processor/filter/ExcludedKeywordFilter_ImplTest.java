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
package io.delimeat.processor.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.delimeat.config.entity.Config;
import io.delimeat.feed.entity.FeedResult;
import io.delimeat.show.entity.Episode;

public class ExcludedKeywordFilter_ImplTest {

	private ExcludedKeywordFilter_Impl filter;

	@BeforeEach
	public void setUp() {
		filter = new ExcludedKeywordFilter_Impl();
	}

	@Test
	public void nullExcludedKeywordsTest() throws Exception {
		Config config = new Config();
		FeedResult result = new FeedResult();
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, new Episode(), config);
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals(result, results.get(0));
	}

	@Test
	public void emptyExcludedKeywordsTest() throws Exception {
		Config config = new Config();
		config.setExcludedKeywords(Collections.<String>emptyList());

		FeedResult result = new FeedResult();
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, new Episode(), config);
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals(result, results.get(0));
	}

	@Test
	public void foundTest() throws Exception {
		Config config = new Config();
		config.setExcludedKeywords(Arrays.asList("FIND_ME", "TEXT2"));

		FeedResult result = new FeedResult();
		result.setTitle("BLAH_BLAHFIND_ME_BLAH");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, new Episode(), config);
		Assertions.assertEquals(0, results.size());
	}

	@Test
	public void notFoundTest() throws Exception {
		Config config = new Config();
		config.setExcludedKeywords(Arrays.asList("FIND_ME", "TEXT2"));

		FeedResult result = new FeedResult();
		result.setTitle("BLAH_BLAH_BLAH");
		List<FeedResult> results = new ArrayList<>(Arrays.asList(result));

		filter.filter(results, new Episode(), config);
		Assertions.assertEquals(1, results.size());
		Assertions.assertEquals(result, results.get(0));
	}
	
}
