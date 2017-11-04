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
package io.delimeat.show.entity;

import org.junit.Assert;
import org.junit.Test;

public class EpisodeStatusTest {

	@Test
	public void pendingTest(){
		Assert.assertEquals(0, EpisodeStatus.PENDING.getValue());
	}
	
	@Test
	public void foundTest(){
		Assert.assertEquals(1, EpisodeStatus.FOUND.getValue());
	}
	
	@Test
	public void skippedTest(){
		Assert.assertEquals(2, EpisodeStatus.SKIPPED.getValue());
	}
}
