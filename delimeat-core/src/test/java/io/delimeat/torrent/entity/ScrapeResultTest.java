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
package io.delimeat.torrent.entity;

import org.junit.Assert;
import org.junit.Test;

public class ScrapeResultTest {

	private ScrapeResult result;

	@Test
	public void createResult() {
		result = new ScrapeResult(Long.MIN_VALUE, Long.MAX_VALUE);
		Assert.assertEquals(Long.MIN_VALUE, result.getSeeders());
		Assert.assertEquals(Long.MAX_VALUE, result.getLeechers());
	}

	@Test
	public void toStringTest() {
		result = new ScrapeResult(Long.MIN_VALUE, Long.MAX_VALUE);
		Assert.assertEquals("ScrapeResult [seeders=-9223372036854775808, leechers=9223372036854775807]",
				result.toString());
	}

	@Test
	public void hashCodeTest() {
		result = new ScrapeResult(Long.MIN_VALUE, Long.MAX_VALUE);
		Assert.assertEquals(961, result.hashCode());
	}

	@Test
	public void equalsTest() {
		result = new ScrapeResult(Long.MIN_VALUE, Long.MAX_VALUE);
		ScrapeResult other = new ScrapeResult(Long.MIN_VALUE, Long.MAX_VALUE);
		Assert.assertTrue(result.equals(other));
	}

	@Test
	public void equalsSelfTest() {
		result = new ScrapeResult(Long.MIN_VALUE, Long.MAX_VALUE);
		Assert.assertTrue(result.equals(result));
	}

	@Test
	public void equalsNullTest() {
		result = new ScrapeResult(Long.MIN_VALUE, Long.MAX_VALUE);
		Assert.assertFalse(result.equals(null));
	}

	@Test
	public void equalsOtherClassTest() {
		result = new ScrapeResult(Long.MIN_VALUE, Long.MAX_VALUE);
		Assert.assertFalse(result.equals("STRING"));
	}

	@Test
	public void equalsSeedersNotLeechersTest() {
		result = new ScrapeResult(Long.MIN_VALUE, Long.MAX_VALUE);
		ScrapeResult other = new ScrapeResult(Long.MIN_VALUE, Long.MIN_VALUE);
		Assert.assertFalse(result.equals(other));
	}

	@Test
	public void equalsLeechersNotSeedersTest() {
		result = new ScrapeResult(Long.MIN_VALUE, Long.MAX_VALUE);
		ScrapeResult other = new ScrapeResult(Long.MAX_VALUE, Long.MAX_VALUE);
		Assert.assertFalse(result.equals(other));
	}
}
