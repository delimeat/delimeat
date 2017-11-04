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
import org.junit.Before;
import org.junit.Test;

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
	
	@Test
	public void equalsTest() {
		torrentFile.setLength(Long.MAX_VALUE);
		torrentFile.setName("NAME");
		TorrentFile other = new TorrentFile();
		other.setLength(Long.MAX_VALUE);
		other.setName("NAME");

		Assert.assertTrue(torrentFile.equals(other));
	}

	@Test
	public void equalsSelfTest() {
		Assert.assertTrue(torrentFile.equals(torrentFile));
	}

	@Test
	public void equalsNullTest() {
		Assert.assertFalse(torrentFile.equals(null));
	}

	@Test
	public void equalsOtherClassTest() {
		Assert.assertFalse(torrentFile.equals("STRING"));
	}
	
	@Test
	public void equalsNameNullTest() {
		torrentFile.setLength(Long.MAX_VALUE);
		torrentFile.setName(null);
		TorrentFile other = new TorrentFile();
		other.setLength(Long.MAX_VALUE);
		other.setName("NAME");

		Assert.assertFalse(torrentFile.equals(other));
	}
	
	@Test
	public void equalsLengthTest() {
		torrentFile.setLength(Long.MAX_VALUE);
		torrentFile.setName("NAME");
		TorrentFile other = new TorrentFile();
		other.setLength(Long.MIN_VALUE);
		other.setName("NAME");

		Assert.assertFalse(torrentFile.equals(other));
	}
	
	@Test
	public void equalsNameTest() {
		torrentFile.setLength(Long.MAX_VALUE);
		torrentFile.setName("NAME_OTHER");
		TorrentFile other = new TorrentFile();
		other.setLength(Long.MAX_VALUE);
		other.setName("NAME");

		Assert.assertFalse(torrentFile.equals(other));
	}
	
}
