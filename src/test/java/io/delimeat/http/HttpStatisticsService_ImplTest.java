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
package io.delimeat.http;

import java.net.URL;
import java.time.Instant;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HttpStatisticsService_ImplTest {

	private HttpStatisticsService_Impl service;
	
	@Before
	public void setUp(){
		service = new HttpStatisticsService_Impl();
	}
	
	@Test
	public void toStringTest(){
		Assert.assertEquals("HttpStatisticsService_Impl [stats={}]", service.toString());
	}
	
	@Test
	public void addResponseTest() throws Exception{
		Instant start = Instant.now();
		Thread.sleep(1000);
		service.addResponse(new URL("https://test.com"), 200);
		service.addResponse(new URL("http://test.com"), 200);

		service.addResponse(new URL("https://alternate.io/dogs"), 500);
		service.addResponse(new URL("http://alternate.io/stuff"), 500);
		
		Assert.assertEquals(2, service.getStatistics().size());
		Assert.assertEquals("test.com", service.getStatistics().get(0).getHost());
		Assert.assertEquals(1, service.getStatistics().get(0).getResponseCounts().size());
		Assert.assertEquals(2, service.getStatistics().get(0).getResponseCounts().get(200).intValue());
		Assert.assertNotNull(service.getStatistics().get(0).getLastSuccess());
		Assert.assertTrue(service.getStatistics().get(0).getLastSuccess().isAfter(start));
		Assert.assertNull( service.getStatistics().get(0).getLastFailure());

		Instant beforeFailures = Instant.now();
		Thread.sleep(1000);
		service.addResponse(new URL("https://test.com"), 404);
		service.addResponse(new URL("http://test.com"), 503);
		
		Assert.assertEquals(3, service.getStatistics().get(0).getResponseCounts().size());
		Assert.assertEquals(2, service.getStatistics().get(0).getResponseCounts().get(200).intValue());
		Assert.assertEquals(1, service.getStatistics().get(0).getResponseCounts().get(404).intValue());
		Assert.assertEquals(1, service.getStatistics().get(0).getResponseCounts().get(503).intValue());
		Assert.assertNotNull(service.getStatistics().get(0).getLastSuccess());
		Assert.assertTrue(service.getStatistics().get(0).getLastSuccess().isAfter(start));
		Assert.assertNotNull( service.getStatistics().get(0).getLastFailure());
		Assert.assertTrue(service.getStatistics().get(0).getLastFailure().isAfter(beforeFailures));
		
		Assert.assertEquals("alternate.io", service.getStatistics().get(1).getHost());
		Assert.assertEquals(1, service.getStatistics().get(1).getResponseCounts().size());
		Assert.assertEquals(2, service.getStatistics().get(1).getResponseCounts().get(500).intValue());	
		
	}
}
