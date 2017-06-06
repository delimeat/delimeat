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

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import io.delimeat.http.HttpStatisticsService_Impl;

public class HttpStatisticsService_ImplTest {

	private HttpStatisticsService_Impl service;
	
	@Before
	public void setUp(){
		service = new HttpStatisticsService_Impl();
	}
	
	@Test
	public void addResponseTest() throws URISyntaxException{
		service.addResponse(new URI("https://test.com"), 200);
		service.addResponse(new URI("http://test.com"), 200);
		service.addResponse(new URI("https://test.com"), 404);
		service.addResponse(new URI("http://test.com"), 503);
		
		service.addResponse(new URI("https://alternate.io/dogs"), 500);
		service.addResponse(new URI("http://alternate.io/stuff"), 500);
		
		Assert.assertEquals(2, service.getStatistics().size());
		Assert.assertEquals("test.com", service.getStatistics().get(0).getHost());
		Assert.assertEquals(3, service.getStatistics().get(0).getResponseCounts().size());
		Assert.assertEquals(2, service.getStatistics().get(0).getResponseCounts().get(200).intValue());
		Assert.assertEquals(1, service.getStatistics().get(0).getResponseCounts().get(404).intValue());
		Assert.assertEquals(1, service.getStatistics().get(0).getResponseCounts().get(503).intValue());
		
		Assert.assertEquals("alternate.io", service.getStatistics().get(1).getHost());
		Assert.assertEquals(1, service.getStatistics().get(1).getResponseCounts().size());
		Assert.assertEquals(2, service.getStatistics().get(1).getResponseCounts().get(500).intValue());	
		
		System.out.println(service.getStatistics());
	}
}
