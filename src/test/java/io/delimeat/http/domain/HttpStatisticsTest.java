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
package io.delimeat.http.domain;

import java.time.Instant;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.http.domain.HttpStatistics;

public class HttpStatisticsTest {

	private HttpStatistics stats;
	
	@Before
	public void setUp(){
		stats = new HttpStatistics("host");
	}
	@Test
	public void hostTest(){
		Assert.assertEquals("host",stats.getHost());
	}
	
	@Test
	public void lastSuccessTest(){
		Assert.assertNull(stats.getLastSuccess());

		stats.setLastSuccess(Instant.EPOCH);
		
		Assert.assertEquals(Instant.EPOCH, stats.getLastSuccess());
	}
	
	@Test
	public void lastFailureTest(){
		Assert.assertNull(stats.getLastFailure());

		stats.setLastFailure(Instant.EPOCH);
		
		Assert.assertEquals(Instant.EPOCH, stats.getLastFailure());
	}
	
	@Test
	public void responseCountsTest(){
		Assert.assertNotNull(stats.getResponseCounts());
		Assert.assertEquals(0, stats.getResponseCounts().size());
		
		stats.getResponseCounts().put(1, 2);
		
		Assert.assertEquals(1, stats.getResponseCounts().size());
		Assert.assertEquals(2, stats.getResponseCounts().get(1).intValue());
	}
	
	@Test
	public void toStringTest(){
		Assert.assertEquals("HttpStatistics [host=host, responseCounts={}, ]", new HttpStatistics("host").toString());
	}
	
}
