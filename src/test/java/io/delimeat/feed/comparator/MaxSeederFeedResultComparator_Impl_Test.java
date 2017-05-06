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
package io.delimeat.feed.comparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.feed.comparator.MaxSeederFeedResultComparator_Impl;
import io.delimeat.feed.domain.FeedResult;

public class MaxSeederFeedResultComparator_Impl_Test {

	private MaxSeederFeedResultComparator_Impl comparator;
	private List<FeedResult> results;
	
	@Before
	public void setUp(){
		comparator = new MaxSeederFeedResultComparator_Impl();
		results = new ArrayList<FeedResult>();
	}
	
	@Test
	public void selectSortedTest() throws Exception{
		FeedResult result1 = new FeedResult();
		result1.setSeeders(100);
		results.add(result1);
		FeedResult result2 = new FeedResult();
		results.add(result2);
		result2.setSeeders(20);
		FeedResult result3 = new FeedResult();
		result3.setSeeders(200);
		results.add(result3);
		
		Collections.sort(results, comparator);
		Assert.assertEquals("0",result3, results.get(0));
		Assert.assertEquals("1",result1, results.get(1));
		Assert.assertEquals("2", result2, results.get(2));
	}
}
