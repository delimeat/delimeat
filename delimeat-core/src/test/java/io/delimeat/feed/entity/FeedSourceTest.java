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

import org.junit.Assert;
import org.junit.Test;

public class FeedSourceTest {

	@Test
	public void katTest() {
		Assert.assertEquals(0, FeedSource.KAT.getValue());
	}

	@Test
	public void torrentProjectTest() {
		Assert.assertEquals(1, FeedSource.TORRENTPROJECT.getValue());
	}

	@Test
	public void limeTorrentsTest() {
		Assert.assertEquals(2, FeedSource.LIMETORRENTS.getValue());
	}

	@Test
	public void extraTorrentTest() {
		Assert.assertEquals(3, FeedSource.EXTRATORRENT.getValue());
	}

	@Test
	public void bitSnoopTest() {
		Assert.assertEquals(4, FeedSource.BITSNOOP.getValue());
	}

	@Test
	public void torrentDownloadsTest() {
		Assert.assertEquals(5, FeedSource.TORRENTDOWNLOADS.getValue());
	}

	@Test
	public void zooqleTest() {
		Assert.assertEquals(6, FeedSource.ZOOQLE.getValue());
	}
	
	@Test
	public void priateBayTest() {
		Assert.assertEquals(7, FeedSource.PIRATEBAY.getValue());
	}
	
	@Test
	public void skytorrentsTest() {
		Assert.assertEquals(8, FeedSource.SKYTORRENTS.getValue());
	}
}
