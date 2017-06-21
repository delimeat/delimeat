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
package io.delimeat.torrent.domain;

import org.junit.Assert;
import org.junit.Test;

import io.delimeat.torrent.domain.ScrapeResult;

public class ScrapeResultTest {

	private ScrapeResult result;
	
	@Test
	public void createResult(){
		result = new ScrapeResult(Long.MIN_VALUE,Long.MAX_VALUE);
		Assert.assertEquals(Long.MIN_VALUE, result.getSeeders());
		Assert.assertEquals(Long.MAX_VALUE, result.getLeechers());
	}
  
  	@Test
  	public void toStringTest(){
 		result = new ScrapeResult(Long.MIN_VALUE,Long.MAX_VALUE);
		Assert.assertEquals("ScrapeResult(seeders=-9223372036854775808, leechers=9223372036854775807)", result.toString());    	
   }
}
