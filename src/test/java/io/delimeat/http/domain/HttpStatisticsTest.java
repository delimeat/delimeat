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
	
  	@Test
  	public void hashCodeTest(){
		stats.setLastSuccess(Instant.EPOCH);
		stats.setLastFailure(Instant.EPOCH);

		Assert.assertEquals(-1211457529,stats.hashCode());
   }
  	
	@Test
	public void equalsTest() {
		stats.setLastSuccess(Instant.EPOCH);
		stats.setLastFailure(Instant.EPOCH);
		stats.getResponseCounts().put(201, Integer.MAX_VALUE);
		HttpStatistics other = new HttpStatistics();
		other.setHost("host");
		other.setLastSuccess(Instant.EPOCH);
		other.setLastFailure(Instant.EPOCH);
		other.getResponseCounts().put(201, Integer.MAX_VALUE);

		Assert.assertTrue(stats.equals(other));
	}

	@Test
	public void equalsSelfTest() {
		Assert.assertTrue(stats.equals(stats));
	}

	@Test
	public void equalsNullTest() {
		Assert.assertFalse(stats.equals(null));
	}

	@Test
	public void equalsOtherClassTest() {
		Assert.assertFalse(stats.equals("STRING"));
	}
	
	@Test
	public void equalsLastSuccessNullTest() {
		stats.setLastSuccess(null);
		stats.setLastFailure(Instant.EPOCH);
		stats.getResponseCounts().put(201, Integer.MAX_VALUE);
		HttpStatistics other = new HttpStatistics();
		other.setHost("host");
		other.setLastSuccess(Instant.EPOCH);
		other.setLastFailure(Instant.EPOCH);
		other.getResponseCounts().put(201, Integer.MAX_VALUE);

		Assert.assertFalse(stats.equals(other));
	}
	
	@Test
	public void equalsLastFailureNullTest() {
		stats.setLastSuccess(Instant.EPOCH);
		stats.setLastFailure(null);
		stats.getResponseCounts().put(201, Integer.MAX_VALUE);
		HttpStatistics other = new HttpStatistics();
		other.setHost("host");
		other.setLastSuccess(Instant.EPOCH);
		other.setLastFailure(Instant.EPOCH);
		other.getResponseCounts().put(201, Integer.MAX_VALUE);

		Assert.assertFalse(stats.equals(other));
	}
	
	@Test
	public void equalsHostNullTest() {
		stats.setLastSuccess(Instant.EPOCH);
		stats.setLastFailure(Instant.EPOCH);
		stats.getResponseCounts().put(201, Integer.MAX_VALUE);
		HttpStatistics other = new HttpStatistics();
		other.setHost(null);
		other.setLastSuccess(Instant.EPOCH);
		other.setLastFailure(Instant.EPOCH);
		other.getResponseCounts().put(201, Integer.MAX_VALUE);

		Assert.assertFalse(stats.equals(other));
	}
	
	@Test
	public void equalsLastSuccessTest() {
		stats.setLastSuccess(Instant.now());
		stats.setLastFailure(Instant.EPOCH);
		stats.getResponseCounts().put(201, Integer.MAX_VALUE);
		HttpStatistics other = new HttpStatistics();
		other.setHost("host");
		other.setLastSuccess(Instant.EPOCH);
		other.setLastFailure(Instant.EPOCH);
		other.getResponseCounts().put(201, Integer.MAX_VALUE);

		Assert.assertFalse(stats.equals(other));
	}
	
	@Test
	public void equalsLastFailureTest() {
		stats.setLastSuccess(Instant.EPOCH);
		stats.setLastFailure(Instant.now());
		stats.getResponseCounts().put(201, Integer.MAX_VALUE);
		HttpStatistics other = new HttpStatistics();
		other.setHost("host");
		other.setLastSuccess(Instant.EPOCH);
		other.setLastFailure(Instant.EPOCH);
		other.getResponseCounts().put(201, Integer.MAX_VALUE);

		Assert.assertFalse(stats.equals(other));
	}
	
	@Test
	public void equalsHostTest() {
		stats.setLastSuccess(Instant.EPOCH);
		stats.setLastFailure(Instant.EPOCH);
		stats.getResponseCounts().put(201, Integer.MAX_VALUE);
		HttpStatistics other = new HttpStatistics();
		other.setHost("other host");
		other.setLastSuccess(Instant.EPOCH);
		other.setLastFailure(Instant.EPOCH);
		other.getResponseCounts().put(201, Integer.MAX_VALUE);

		Assert.assertFalse(stats.equals(other));
	}
	
}
