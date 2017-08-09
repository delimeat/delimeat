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
import org.junit.Before;
import org.junit.Test;

import io.delimeat.torrent.domain.TorrentFile;

public class TorrentFileTest {

	private TorrentFile torrentFile;

	@Before
	public void setUp() {
		torrentFile = new TorrentFile();
	}

	@Test
	public void lengthTest() {
		Assert.assertEquals(0, torrentFile.getLength());
		torrentFile.setLength(Long.MAX_VALUE);
		Assert.assertEquals(Long.MAX_VALUE, torrentFile.getLength());
	}

	@Test
	public void nameTest() {
		Assert.assertNull(torrentFile.getName());
		torrentFile.setName("NAME");
		Assert.assertEquals("NAME", torrentFile.getName());
	}

	@Test
	public void toStringTest() {
		Assert.assertEquals("TorrentFile [length=0, ]", torrentFile.toString());
	}
  	
	@Test
  	public void hashCodeTest(){
		torrentFile.setName("NAME");
		torrentFile.setLength(Long.MAX_VALUE);
     	Assert.assertEquals(-2145094068, torrentFile.hashCode());
   }
}
